package com.github.guocay.hj212.business.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("t_monitor_error")
public class MonitorErrorPo {

    @TableId(value="id",type= IdType.AUTO)
    private Long id;

    @TableField("info_id")
    private Long infoId;

    @TableField("error")
    private String error;

    @TableField("time")
    private LocalDateTime time;

}
