package cn.iocoder.yudao.module.erp.controller.admin.statistics.vo.sale;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Schema(description = "管理后台 - ERP 销售某个时间段的统计 Response VO")
@Data
public class ErpSaleTimeSummaryRespVO {

    @Schema(description = "时间", requiredMode = Schema.RequiredMode.REQUIRED, example = "2022-03")
    private String time;

    @Schema(description = "动态 key-value 存储不同 name 和对应数据", requiredMode = Schema.RequiredMode.REQUIRED, example = "{'tiktok':1000}")
    private Map<String, Object> data;
}
