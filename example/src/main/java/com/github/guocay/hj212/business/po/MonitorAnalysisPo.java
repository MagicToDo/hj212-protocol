package com.github.guocay.hj212.business.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.github.guocay.hj212.model.CpData;
import com.github.guocay.hj212.model.DataFlag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author liuyong
 * @date 2024/5/16 13:04
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_monitor_analysis")
public class MonitorAnalysisPo {
	/**
	 * 主键
	 */
	@TableId(value="id",type= IdType.AUTO)
	private Long id;

	/**
	 * 请求编号
	 */
	@TableField("qn_code")
	private String qnCode;

	/**
	 * 总包号
	 */
	@TableField("pnum_code")
	private Integer pNumCode;

	/**
	 * 包号
	 */
	@TableField("pno_code")
	private Integer pNoCode;

	/**
	 * 系统编号
	 */
	@TableField("st_code")
	private String stCode;

	/**
	 * 命令编号
	 */
	@TableField("cn_code")
	private String cnCode;

	/**
	 * 访问密码
	 */
	@TableField("pw_code")
	private String pwCode;

	/**
	 * 设备唯一标识
	 */
	@TableField("mn_code")
	private String mnCode;

	@TableField("time")
	private LocalDateTime time;

}
