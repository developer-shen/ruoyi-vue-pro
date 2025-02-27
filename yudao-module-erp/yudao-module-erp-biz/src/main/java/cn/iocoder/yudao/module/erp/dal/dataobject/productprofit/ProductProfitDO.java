package cn.iocoder.yudao.module.erp.dal.dataobject.productprofit;

import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 产品利润 DO
 *
 * @author 沈飞宇
 */
@TableName("erp_product_profit")
@KeySequence("erp_product_profit_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductProfitDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 产品编号
     */
    private Long productId;
    /**
     * 产品利润
     */
    private BigDecimal profit;
    /**
     * 采购价格
     */
    private BigDecimal purchasePrice;
    /**
     * 销售价格
     */
    private BigDecimal salePrice;
    /**
     * 头程价格
     */
    private BigDecimal firstLegPrice;
    /**
     * 尾程价格
     */
    private BigDecimal lastMilePrice;
    /**
     * 退货率
     */
    private BigDecimal refundRate;
    /**
     * 退货运费
     */
    private BigDecimal refundFreight;
    /**
     * 广告费用
     */
    private BigDecimal adPrice;
    /**
     * 其他费用
     */
    private BigDecimal otherPrice;
    /**
     * 其他费用详情
     */
    private String otherDetail;
    /**
     * 备注
     */
    private String remark;

}