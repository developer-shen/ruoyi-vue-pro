package cn.iocoder.yudao.module.report.service.report;

import cn.iocoder.yudao.module.report.controller.admin.report.vo.ReportPageReqVO;
import cn.iocoder.yudao.module.report.controller.admin.report.vo.ReportSaveReqVO;
import cn.iocoder.yudao.module.report.dal.dataobject.report.ReportDO;
import cn.iocoder.yudao.module.report.dal.mysql.report.ReportMapper;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;


import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 在线excel设计器 Service 实现类
 *
 * @author 沈飞宇
 */
@Service
@Validated
public class ReportServiceImpl implements ReportService {

    @Resource
    private ReportMapper reportMapper;

    @Override
    public String createReport(ReportSaveReqVO createReqVO) {
        // 插入
        ReportDO report = BeanUtils.toBean(createReqVO, ReportDO.class);
        reportMapper.insert(report);
        // 返回
        return report.getId();
    }

    @Override
    public void updateReport(ReportSaveReqVO updateReqVO) {
        // 校验存在
        validateReportExists(updateReqVO.getId());
        // 更新
        ReportDO updateObj = BeanUtils.toBean(updateReqVO, ReportDO.class);
        reportMapper.updateById(updateObj);
    }

    @Override
    public void deleteReport(String id) {
        // 校验存在
        validateReportExists(id);
        // 删除
        reportMapper.deleteById(id);
    }

    private void validateReportExists(String id) {

    }

    @Override
    public ReportDO getReport(String id) {
        return reportMapper.selectById(id);
    }

    @Override
    public PageResult<ReportDO> getReportPage(ReportPageReqVO pageReqVO) {
        return null;
    }

}