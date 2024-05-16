package com.github.guocay.hj212.business.service;

import com.github.guocay.hj212.business.po.MonitorFactorPo;
import com.github.guocay.hj212.core.ProtocolMapper;
import com.github.guocay.hj212.model.CpData;
import com.github.guocay.hj212.model.Data;
import com.github.guocay.hj212.model.DataFlag;
import com.github.guocay.hj212.business.core.annotaion.MonitorServiceListen;
import com.github.guocay.hj212.business.core.util.Constant;
import com.github.guocay.hj212.business.mapper.MonitorFactorMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_PROTOTYPE;

/**
 * 用于接收hj212协议内容
 * @author GuoKai
 * @since 2019/11/22
 */
@Service
@Scope(SCOPE_PROTOTYPE)
public class MonitorService {

	private static final Logger log = LoggerFactory.getLogger(MonitorService.class);

    private final MonitorFactorMapping factorMapper;

    private final ProtocolMapper t212Mapper = ProtocolMapper.getInstance()
            .enableDefaultParserFeatures().enableDefaultVerifyFeatures();

    public MonitorService(MonitorFactorMapping factorMapper) {
        this.factorMapper = factorMapper;
    }

	/**
     * 接收hj212协议内容
     * @param monitorData hj212协议内容
     * @param ipAddress ip地址
     * @return hj212协议响应内容
     * @throws Exception 抛出异常
     */
    @MonitorServiceListen("hj212协议接收服务")
    public String monitor(String monitorData,String ipAddress) throws Exception{
        String response = null;
		Data data = t212Mapper.readData(monitorData);
		data.getCp().getPollution().forEach((key, value) -> {
			MonitorFactorPo monitorFactorPo = MonitorFactorPo.builder().dataStatus(Constant.EFFECTIVE)
				.mncode(data.getMn())
				.dataTime(data.getCp().getDataTime())
				.factor(key)
				.value(value.getRtd().toString())
				.time(LocalDateTime.now(Clock.systemDefaultZone()))
				.build();
			factorMapper.insert(monitorFactorPo);
		});
		//如果数据需要响应，返回响应值
		if (DataFlag.A.isMarked(data.getDataFlag())) {
			Data builder = new Data();
			builder.setQn(data.getQn()).setSt(data.getSt()).setCn(Constant.DATA_RESPONSE);
			builder.setPw(data.getPw()).setMn(data.getMn()).setCp(new CpData());
			builder.setDataFlag(Collections.singletonList(DataFlag.V0));
			response = t212Mapper.writeDataAsString(builder);
		}
        return response;
    }
}
