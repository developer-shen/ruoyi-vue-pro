package cn.iocoder.yudao.module.erp.dal.dataobject.productattributes;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 产品属性 DO
 *
 * @author 沈飞宇
 */
@TableName("erp_product_attributes")
@KeySequence("erp_product_attributes_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductAttributesDO extends BaseDO {

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
     * 备注
     */
    private String remark;
    /**
     * 基础重量（g）
     */
    private BigDecimal weight;
    /**
     * 尺码范围
     */
    private String sizes;
    /**
     * 库存
     */
    private Integer stock;
    /**
     * 成分
     */
    private String composition;
    /**
     * 产品尺码表
     */
    private String productMeasurements;
    /**
     * 基码表
     */
    private String bodyMeasurements;
    /**
     * 护理说明
     */
    private String careInstructions;

}