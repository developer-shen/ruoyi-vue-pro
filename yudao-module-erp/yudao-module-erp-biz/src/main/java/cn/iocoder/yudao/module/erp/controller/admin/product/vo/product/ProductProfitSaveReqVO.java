package cn.iocoder.yudao.module.erp.controller.admin.product.vo.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP 产品利润新增/修改 Request VO")
@Data
public class ProductProfitSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10553")
    private Long id;

    @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "14915")
    @NotNull(message = "产品编号不能为空")
    private Long productId;

    @Schema(description = "产品利润")
    @NotNull(message = "产品利润不能为空")
    private BigDecimal profit;

    @Schema(description = "采购价格", example = "10397")
    @NotNull(message = "采购价格不能为空")
    private BigDecimal purchasePrice;

    @Schema(description = "销售价格", example = "24634")
    @NotNull(message = "销售价格不能为空")
    private BigDecimal salePrice;

    @Schema(description = "头程价格", example = "14615")
    @NotNull(message = "头程价格不能为空")
    private BigDecimal firstLegPrice;

    @Schema(description = "尾程价格", example = "1996")
    @NotNull(message = "尾程价格不能为空")
    private BigDecimal lastMilePrice;

    @Schema(description = "退货率")
    @NotNull(message = "退货率不能为空")
    private BigDecimal refundRate;

    @Schema(description = "退货运费")
    @NotNull(message = "退货运费不能为空")
    private BigDecimal refundFreight;

    @Schema(description = "其他费用", example = "14237")
    @NotNull(message = "其他费用不能为空")
    private BigDecimal otherPrice;

    @Schema(description = "其他费用详情")
    private String otherDetail;

    @Schema(description = "备注", example = "随便")
    private String remark;

}