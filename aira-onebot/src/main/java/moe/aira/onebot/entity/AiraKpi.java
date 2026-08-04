package moe.aira.onebot.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Accessors(chain = true)
public class AiraKpi {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Date kpiDate;
    private Integer kpiType;
    private BigDecimal kpi;
}
