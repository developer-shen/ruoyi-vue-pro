package cn.iocoder.yudao.module.report.service.report;

import java.util.*;
import javax.validation.*;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.module.report.controller.admin.report.vo.ReportPageReqVO;
import cn.iocoder.yudao.module.report.controller.admin.report.vo.ReportSaveReqVO;
import cn.iocoder.yudao.module.report.dal.dataobject.report.ReportDO;

/**
 * 在线excel设计器 Service 接口
 *
 * @author 沈飞宇
 */
public interface ReportService {

    /**
     * 创建在线excel设计器
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createReport(@Valid ReportSaveReqVO createReqVO);

    /**
     * 更新在线excel设计器
     *
     * @param updateReqVO 更新信息
     */
    void updateReport(@Valid ReportSaveReqVO updateReqVO);

    /**
     * 删除在线excel设计器
     *
     * @param id 编号
     */
    void deleteReport(String id);

    /**
     * 获得在线excel设计器
     *
     * @param id 编号
     * @return 在线excel设计器
     */
    ReportDO getReport(String id);

    /**
     * 获得在线excel设计器分页
     *
     * @param pageReqVO 分页查询
     * @return 在线excel设计器分页
     */
    PageResult<ReportDO> getReportPage(ReportPageReqVO pageReqVO);

}