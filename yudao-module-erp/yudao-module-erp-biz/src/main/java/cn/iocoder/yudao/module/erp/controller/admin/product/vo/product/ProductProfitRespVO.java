package cn.iocoder.yudao.module.erp.controller.admin.product.vo.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - ERP 产品利润 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ProductProfitRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "10553")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "14915")
    @ExcelProperty("产品编号")
    private Long productId;

    @Schema(description = "产品利润")
    @ExcelProperty("产品利润")
    private BigDecimal profit;

    @Schema(description = "采购价格", example = "10397")
    @ExcelProperty("采购价格")
    private BigDecimal purchasePrice;

    @Schema(description = "销售价格", example = "24634")
    @ExcelProperty("销售价格")
    private BigDecimal salePrice;

    @Schema(description = "头程价格", example = "14615")
    @ExcelProperty("头程价格")
    private BigDecimal firstLegPrice;

    @Schema(description = "尾程价格", example = "1996")
    @ExcelProperty("尾程价格")
    private BigDecimal lastMilePrice;

    @Schema(description = "退货率")
    @ExcelProperty("退货率")
    private BigDecimal refundRate;

    @Schema(description = "退货运费")
    @ExcelProperty("退货运费")
    private BigDecimal refundFreight;

    @Schema(description = "广告费用", example = "14237")
    @ExcelProperty("广告费用")
    private BigDecimal adPrice;

    @Schema(description = "其他费用", example = "14237")
    @ExcelProperty("其他费用")
    private BigDecimal otherPrice;

    @Schema(description = "其他费用详情")
    @ExcelProperty("其他费用详情")
    private String otherDetail;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "备注", example = "随便")
    @ExcelProperty("备注")
    private String remark;

}