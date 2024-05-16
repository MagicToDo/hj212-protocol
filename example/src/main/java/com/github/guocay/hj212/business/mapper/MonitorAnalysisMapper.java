package com.github.guocay.hj212.business.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.github.guocay.hj212.business.po.MonitorAnalysisPo;

import java.util.Objects;

public interface MonitorAnalysisMapper extends BaseMapper<MonitorAnalysisPo> {
	default Integer countBymnCodeWithDataTime(String mnCode, String dataTime) {
		return selectCount(Wrappers.<MonitorAnalysisPo>lambdaQuery()
			.eq(Objects.nonNull(mnCode), MonitorAnalysisPo::getMnCode, mnCode)
			.eq(Objects.nonNull(dataTime), MonitorAnalysisPo::getDataTime, dataTime)
		);
	}
}
