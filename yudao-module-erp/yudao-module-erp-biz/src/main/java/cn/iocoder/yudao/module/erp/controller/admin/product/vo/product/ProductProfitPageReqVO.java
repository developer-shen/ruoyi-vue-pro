package cn.iocoder.yudao.module.erp.controller.admin.product.vo.product;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 产品利润分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductProfitPageReqVO extends PageParam {

    @Schema(description = "产品编号", example = "14915")
    private Long productId;

    @Schema(description = "产品利润")
    private BigDecimal profit;

    @Schema(description = "采购价格", example = "10397")
    private BigDecimal purchasePrice;

    @Schema(description = "销售价格", example = "24634")
    private BigDecimal salePrice;

    @Schema(description = "头程价格", example = "14615")
    private BigDecimal firstLegPrice;

    @Schema(description = "尾程价格", example = "1996")
    private BigDecimal lastMilePrice;

    @Schema(description = "退货率")
    private BigDecimal refundRate;

    @Schema(description = "退货运费")
    private BigDecimal refundFreight;

    @Schema(description = "其他费用", example = "14237")
    private BigDecimal otherPrice;

    @Schema(description = "其他费用详情")
    private String otherDetail;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

    @Schema(description = "备注", example = "随便")
    private String remark;

}