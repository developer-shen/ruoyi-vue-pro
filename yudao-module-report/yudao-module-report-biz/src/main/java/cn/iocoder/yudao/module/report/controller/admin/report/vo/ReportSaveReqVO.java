package cn.iocoder.yudao.module.report.controller.admin.report.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 在线excel设计器新增/修改 Request VO")
@Data
public class ReportSaveReqVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "3018")
    private String id;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "名称", example = "张三")
    private String name;

    @Schema(description = "说明")
    private String note;

    @Schema(description = "状态", example = "1")
    private String status;

    @Schema(description = "类型", example = "1")
    private String type;

    @Schema(description = "json字符串")
    private String jsonStr;

    @Schema(description = "请求地址", example = "https://www.iocoder.cn")
    private String apiUrl;

    @Schema(description = "缩略图")
    private String thumb;

    @Schema(description = "创建人")
    private String createBy;

    @Schema(description = "修改人")
    private String updateBy;

    @Schema(description = "删除标识0-正常,1-已删除")
    private Boolean delFlag;

    @Schema(description = "请求方法0-get,1-post")
    private String apiMethod;

    @Schema(description = "请求编码")
    private String apiCode;

    @Schema(description = "是否是模板 0-是,1-不是")
    private Boolean template;

    @Schema(description = "浏览次数", example = "31214")
    private Long viewCount;

    @Schema(description = "css增强")
    private String cssStr;

    @Schema(description = "js增强")
    private String jsStr;

    @Schema(description = "py增强")
    private String pyStr;

    @Schema(description = "乐观锁版本", example = "21729")
    private Integer updateCount;

    @Schema(description = "是否填报报表 0不是,1是")
    private Boolean submitForm;

}