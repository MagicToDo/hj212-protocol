package com.github.guocay.hj212.business.service;

import com.github.guocay.hj212.business.mapper.MonitorAnalysisMapper;
import com.github.guocay.hj212.business.po.MonitorAnalysisPo;
import com.github.guocay.hj212.business.po.MonitorFactorPo;
import com.github.guocay.hj212.core.ProtocolMapper;
import com.github.guocay.hj212.model.CpData;
import com.github.guocay.hj212.model.Data;
import com.github.guocay.hj212.model.DataFlag;
import com.github.guocay.hj212.business.core.annotaion.MonitorServiceListen;
import com.github.guocay.hj212.business.core.util.Constant;
import com.github.guocay.hj212.business.mapper.MonitorFactorMapper;
import com.github.guocay.hj212.model.Pollution;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 用于接收hj212协议内容
 * @author GuoKai
 * @since 2019/11/22
 */
@Service
@Scope(SCOPE_PROTOTYPE)
@Slf4j
public class MonitorService {

    private final MonitorFactorMapper factorMapper;

	public MonitorService(MonitorFactorMapper factorMapper) {
		this.factorMapper = factorMapper;
	}

    private final ProtocolMapper t212Mapper = ProtocolMapper.getInstance()
            .enableDefaultParserFeatures().enableDefaultVerifyFeatures();

	@Autowired
	private  MonitorAnalysisMapper analysisMapper;



	/**
     * 接收hj212协议内容
     * @param monitorData hj212协议内容
     * @param ipAddress ip地址
     * @return hj212协议响应内容
     * @throws Exception 抛出异常
     */
    @MonitorServiceListen("hj212协议接收服务")
	@Transactional(rollbackFor = Exception.class)
    public String monitor(String monitorData,String ipAddress) throws Exception{
        String response = null;
		Data data = t212Mapper.readData(monitorData);
		MonitorAnalysisPo monitorAnalysisPo = MonitorAnalysisPo.builder()
			.qnCode(data.getQn())
			.pNoCode(data.getPNo())
			.pNumCode(data.getPNum())
			.mnCode(data.getMn())
			.cnCode(data.getCn())
			.pwCode(data.getPw())
			.stCode(data.getSt())
			.time(LocalDateTime.now(Clock.systemDefaultZone()))
			.build();
		int analysisId = analysisMapper.insert(monitorAnalysisPo);

		for (Map.Entry<String, Pollution> entry : data.getCp().getPollution().entrySet()) {
			String key = entry.getKey();
			Pollution value = entry.getValue();
			MonitorFactorPo monitorFactorPo = MonitorFactorPo.builder().dataStatus(Constant.EFFECTIVE)
				.analysisId((long) analysisId)
				.mnCode(data.getMn())
				.dataTime(data.getCp().getDataTime())
				.factor(key)
				.value(value.getRtd().toString())
				.time(LocalDateTime.now(Clock.systemDefaultZone()))
				.build();
			factorMapper.insert(monitorFactorPo);
		}
		//如果数据需要响应，返回响应值
		if (DataFlag.A.isMarked(data.getDataFlag())) {
			Data build = Data.builder().qn(data.getQn())
				.st(data.getSt())
				.cn(Constant.DATA_RESPONSE)
				.pw(data.getPw())
				.mn(data.getMn())
				.cp(new CpData())
				.dataFlag(Collections.singletonList(DataFlag.V0)).build();
			response = t212Mapper.writeDataAsString(build);
		}
        return response;
    }
}
