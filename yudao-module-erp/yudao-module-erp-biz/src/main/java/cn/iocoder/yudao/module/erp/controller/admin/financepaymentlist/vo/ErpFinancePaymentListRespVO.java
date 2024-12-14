package cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - ERP 付款清单 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ErpFinancePaymentListRespVO {

    @Schema(description = "编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "20475")
    @ExcelProperty("编号")
    private Long id;

    @Schema(description = "财务人员编号", example = "4904")
    private Long financeUserId;
    @Schema(description = "财务人员名称", example = "张三")
    @ExcelProperty("财务人员")
    private String financeUserName;

    @Schema(description = "付款时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("付款时间")
    private LocalDateTime paymentTime;

    @Schema(description = "实付金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "24767")
    @ExcelProperty("实付金额")
    private BigDecimal paymentPrice;

    @Schema(description = "付款方式", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("付款方式")
    private String paymentWay;

    @Schema(description = "付款用途")
    @ExcelProperty("付款用途")
    private String paymentPurpose;

    @Schema(description = "附件", example = "https://www.iocoder.cn")
    @ExcelProperty("附件")
    private String fileUrl;

    @Schema(description = "备注", example = "你猜")
    @ExcelProperty("备注")
    private String remark;

    @Schema(description = "创建人")
    private String creator;
    @Schema(description = "创建人名称")
    private String creatorName;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDateTime createTime;

}