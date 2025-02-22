package cn.iocoder.yudao.module.erp.controller.admin.product.vo.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - ERP 产品属性 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductAttributesRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "11178")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6087")
    @ExcelProperty("产品编号")
    private Long productId;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "基础重量（g）")
    @ExcelProperty("基础重量（g）")
    private BigDecimal weight;

    @Schema(description = "尺码范围")
    @ExcelProperty("尺码范围")
    private String sizes;

    @Schema(description = "库存")
    @ExcelProperty("库存")
    private Integer stock;

    @Schema(description = "成分")
    @ExcelProperty("成分")
    private String composition;

    @Schema(description = "产品尺码表")
    @ExcelProperty("产品尺码表")
    private String productMeasurements;

    @Schema(description = "基码表")
    @ExcelProperty("基码表")
    private String bodyMeasurements;

    @Schema(description = "护理说明")
    @ExcelProperty("护理说明")
    private String careInstructions;

}