package cn.iocoder.yudao.module.erp.controller.admin.product.vo.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Schema(description = "管理后台 - ERP 产品新增/修改 Request VO")
@Data
public class ProductSaveReqVO {

    @Schema(description = "产品编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "15672")
    private Long id;

    @Schema(description = "skc货号", requiredMode = Schema.RequiredMode.REQUIRED, example = "李四")
    @NotEmpty(message = "skc货号不能为空")
    private String name;

    @Schema(description = "产品条码", example = "X110")
    private String barCode;

    @Schema(description = "产品分类编号", example = "11161")
    private Long categoryId;

    @Schema(description = "单位编号", example = "8869")
    private Long unitId;

    @Schema(description = "产品状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "产品状态不能为空")
    private Integer status;

    @Schema(description = "产品规格", example = "红色")
    private String standard;

    @Schema(description = "产品备注", example = "你猜")
    private String remark;

    @Schema(description = "保质期天数", example = "10")
    private Integer expiryDay;

    @Schema(description = "基础重量（kg）", example = "1.00")
    private BigDecimal weight;

    @Schema(description = "采购价格，单位：元", example = "10.30")
    private BigDecimal purchasePrice;

    @Schema(description = "销售价格，单位：元", example = "74.32")
    private BigDecimal salePrice;

    @Schema(description = "最低价格，单位：元", example = "161.87")
    private BigDecimal minPrice;

    @Schema(description = "附件地址", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "spu商品货号", example = "A01")
    private String spuCode;
}