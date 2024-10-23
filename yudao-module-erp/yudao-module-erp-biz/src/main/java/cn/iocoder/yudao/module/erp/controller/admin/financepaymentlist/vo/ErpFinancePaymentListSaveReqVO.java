package cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - ERP 付款清单新增/修改 Request VO")
@Data
public class ErpFinancePaymentListSaveReqVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "20475")
    private Long id;

    @Schema(description = "财务人员", example = "4904")
    private Long financeUserId;

    @Schema(description = "付款时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "付款时间不能为空")
    private LocalDateTime paymentTime;

    @Schema(description = "实付金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "24767")
    @NotNull(message = "实付金额不能为空")
    private BigDecimal paymentPrice;

    @Schema(description = "付款方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "付款方式不能为空")
    private String paymentWay;

    @Schema(description = "付款用途")
    private String paymentPurpose;

    @Schema(description = "附件", example = "https://www.iocoder.cn")
    private String fileUrl;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}