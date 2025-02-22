package cn.iocoder.yudao.module.erp.controller.admin.product.vo.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP 产品属性新增/修改 Request VO")
@Data
public class ProductAttributesSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11178")
    private Long id;

    @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6087")
    @NotNull(message = "产品编号不能为空")
    private Long productId;

    @Schema(description = "备注", example = "你猜")
    private String remark;

    @Schema(description = "基础重量（g）")
    private BigDecimal weight;

    @Schema(description = "尺码范围")
    private String sizes;

    @Schema(description = "库存")
    private Integer stock;

    @Schema(description = "成分")
    private String composition;

    @Schema(description = "产品尺码表")
    private String productMeasurements;

    @Schema(description = "基码表")
    private String bodyMeasurements;

    @Schema(description = "护理说明")
    private String careInstructions;

}