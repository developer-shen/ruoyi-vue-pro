package cn.iocoder.yudao.module.erp.dal.dataobject.financepaymentlist;

import lombok.*;
import java.util.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.*;
import cn.iocoder.yudao.framework.mybatis.core.dataobject.BaseDO;

/**
 * ERP 付款清单 DO
 *
 * @author 沈飞宇
 */
@TableName("erp_finance_payment_list")
@KeySequence("erp_finance_payment_list_seq") // 用于 Oracle、PostgreSQL、Kingbase、DB2、H2 数据库的主键自增。如果是 MySQL 等数据库，可不写。
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErpFinancePaymentListDO extends BaseDO {

    /**
     * 编号
     */
    @TableId
    private Long id;
    /**
     * 财务人员
     */
    private Long financeUserId;
    /**
     * 付款时间
     */
    private LocalDateTime paymentTime;
    /**
     * 实付金额
     */
    private BigDecimal paymentPrice;
    /**
     * 付款方式
     */
    private String paymentWay;
    /**
     * 付款用途
     */
    private String paymentPurpose;
    /**
     * 附件
     */
    private String fileUrl;
    /**
     * 备注
     */
    private String remark;

}