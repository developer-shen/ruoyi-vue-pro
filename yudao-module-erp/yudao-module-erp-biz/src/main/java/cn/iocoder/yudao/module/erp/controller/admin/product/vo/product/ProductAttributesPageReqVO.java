package cn.iocoder.yudao.module.erp.controller.admin.product.vo.product;

import lombok.*;
import java.util.*;
import io.swagger.v3.oas.annotations.media.Schema;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND;

@Schema(description = "管理后台 - ERP 产品属性分页 Request VO")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ProductAttributesPageReqVO extends PageParam {

    @Schema(description = "产品编号", example = "6087")
    private Long productId;

    @Schema(description = "创建时间")
    @DateTimeFormat(pattern = FORMAT_YEAR_MONTH_DAY_HOUR_MINUTE_SECOND)
    private LocalDateTime[] createTime;

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