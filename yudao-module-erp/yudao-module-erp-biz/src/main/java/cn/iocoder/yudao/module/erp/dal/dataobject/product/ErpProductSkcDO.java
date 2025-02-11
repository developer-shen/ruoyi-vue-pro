package cn.iocoder.yudao.module.erp.dal.dataobject.product;

import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;
import com.baomidou.mybatisplus.annotation.KeySequence;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.math.BigDecimal;

/**
 * ERP 产品变种skc DO
 *
 * @author 芋道源码
 */
@TableName("erp_product_skc")
@KeySequence("erp_product_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpProductSkcDO extends BaseDO {

    /**
     * 产品变种编号
     */
    @TableId
    private Long id;
    /**
     * 产品变种名称
     */
    private String name;
    /**
     * 产品变种条码-skc货号
     */
    private String barCode;
    /**
     * 父级产品编号
     *
     * 关联 {@link ErpProductDO#getId()}
     */
    private Long productId;
    /**
     * 单位编号
     *
     * 关联 {@link ErpProductUnitDO#getId()}
     */
    private Long unitId;
    /**
     * 产品变种状态
     *
     * 枚举 {@link cn.iocoder.yudao.framework.common.enums.CommonStatusEnum}
     */
    private Integer status;
    /**
     * 产品变种规格
     */
    private String standard;
    /**
     * 产品变种备注
     */
    private String remark;
    /**
     * 保质期天数
     */
    private Integer expiryDay;
    /**
     * 基础重量（kg）
     */
    private BigDecimal weight;
    /**
     * 采购价格，单位：元
     */
    private BigDecimal purchasePrice;
    /**
     * 销售价格，单位：元
     */
    private BigDecimal salePrice;
    /**
     * 最低价格，单位：元
     */
    private BigDecimal minPrice;
    /**
     * 附件地址
     */
    private String fileUrl;
    /**
     * 颜色
     */
    private String color;

}