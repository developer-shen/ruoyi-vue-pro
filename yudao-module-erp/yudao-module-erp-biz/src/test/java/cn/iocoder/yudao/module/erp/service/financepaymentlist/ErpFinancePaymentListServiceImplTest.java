package cn.iocoder.yudao.module.erp.service.financepaymentlist;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.erp.controller.admin.financepaymentlist.vo.*;
import cn.iocoder.yudao.module.erp.dal.dataobject.financepaymentlist.ErpFinancePaymentListDO;
import cn.iocoder.yudao.module.erp.dal.mysql.financepaymentlist.ErpFinancePaymentListMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import javax.annotation.Resource;
import org.springframework.context.annotation.Import;
import java.util.*;
import java.time.LocalDateTime;

import static cn.hutool.core.util.RandomUtil.*;
import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.DateUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * {@link ErpFinancePaymentListServiceImpl} 的单元测试类
 *
 * @author 沈飞宇
 */
@Import(ErpFinancePaymentListServiceImpl.class)
public class ErpFinancePaymentListServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ErpFinancePaymentListServiceImpl financePaymentListService;

    @Resource
    private ErpFinancePaymentListMapper financePaymentListMapper;

    @Test
    public void testCreateFinancePaymentList_success() {
        // 准备参数
        ErpFinancePaymentListSaveReqVO createReqVO = randomPojo(ErpFinancePaymentListSaveReqVO.class).setId(null);

        // 调用
        Long financePaymentListId = financePaymentListService.createFinancePaymentList(createReqVO);
        // 断言
        assertNotNull(financePaymentListId);
        // 校验记录的属性是否正确
        ErpFinancePaymentListDO financePaymentList = financePaymentListMapper.selectById(financePaymentListId);
        assertPojoEquals(createReqVO, financePaymentList, "id");
    }

    @Test
    public void testUpdateFinancePaymentList_success() {
        // mock 数据
        ErpFinancePaymentListDO dbFinancePaymentList = randomPojo(ErpFinancePaymentListDO.class);
        financePaymentListMapper.insert(dbFinancePaymentList);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ErpFinancePaymentListSaveReqVO updateReqVO = randomPojo(ErpFinancePaymentListSaveReqVO.class, o -> {
            o.setId(dbFinancePaymentList.getId()); // 设置更新的 ID
        });

        // 调用
        financePaymentListService.updateFinancePaymentList(updateReqVO);
        // 校验是否更新正确
        ErpFinancePaymentListDO financePaymentList = financePaymentListMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, financePaymentList);
    }

    @Test
    public void testUpdateFinancePaymentList_notExists() {
        // 准备参数
        ErpFinancePaymentListSaveReqVO updateReqVO = randomPojo(ErpFinancePaymentListSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> financePaymentListService.updateFinancePaymentList(updateReqVO), FINANCE_PAYMENT_LIST_NOT_EXISTS);
    }

    @Test
    public void testDeleteFinancePaymentList_success() {
        // mock 数据
        ErpFinancePaymentListDO dbFinancePaymentList = randomPojo(ErpFinancePaymentListDO.class);
        financePaymentListMapper.insert(dbFinancePaymentList);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbFinancePaymentList.getId();

        // 调用
        financePaymentListService.deleteFinancePaymentList(id);
       // 校验数据不存在了
       assertNull(financePaymentListMapper.selectById(id));
    }

    @Test
    public void testDeleteFinancePaymentList_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> financePaymentListService.deleteFinancePaymentList(id), FINANCE_PAYMENT_LIST_NOT_EXISTS);
    }

    @Test
    public void testGetFinancePaymentListPage() {
       // mock 数据 修改 null 为需要的值
       ErpFinancePaymentListDO dbFinancePaymentList = randomPojo(ErpFinancePaymentListDO.class, o -> { // 等会查询到
           o.setFinanceUserId(null);
           o.setPaymentTime(null);
           o.setPaymentPrice(null);
           o.setPaymentWay(null);
           o.setPaymentPurpose(null);
           o.setFileUrl(null);
           o.setRemark(null);
           o.setCreator(null);
           o.setCreateTime(null);
       });
       financePaymentListMapper.insert(dbFinancePaymentList);
       // 测试 financeUserId 不匹配
       financePaymentListMapper.insert(cloneIgnoreId(dbFinancePaymentList, o -> o.setFinanceUserId(null)));
       // 测试 paymentTime 不匹配
       financePaymentListMapper.insert(cloneIgnoreId(dbFinancePaymentList, o -> o.setPaymentTime(null)));
       // 测试 paymentPrice 不匹配
       financePaymentListMapper.insert(cloneIgnoreId(dbFinancePaymentList, o -> o.setPaymentPrice(null)));
       // 测试 paymentWay 不匹配
       financePaymentListMapper.insert(cloneIgnoreId(dbFinancePaymentList, o -> o.setPaymentWay(null)));
       // 测试 paymentPurpose 不匹配
       financePaymentListMapper.insert(cloneIgnoreId(dbFinancePaymentList, o -> o.setPaymentPurpose(null)));
       // 测试 fileUrl 不匹配
       financePaymentListMapper.insert(cloneIgnoreId(dbFinancePaymentList, o -> o.setFileUrl(null)));
       // 测试 remark 不匹配
       financePaymentListMapper.insert(cloneIgnoreId(dbFinancePaymentList, o -> o.setRemark(null)));
       // 测试 creator 不匹配
       financePaymentListMapper.insert(cloneIgnoreId(dbFinancePaymentList, o -> o.setCreator(null)));
       // 测试 createTime 不匹配
       financePaymentListMapper.insert(cloneIgnoreId(dbFinancePaymentList, o -> o.setCreateTime(null)));
       // 准备参数
       ErpFinancePaymentListPageReqVO reqVO = new ErpFinancePaymentListPageReqVO();
       reqVO.setFinanceUserId(null);
       reqVO.setPaymentTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setPaymentPrice(null);
       reqVO.setPaymentWay(null);
       reqVO.setPaymentPurpose(null);
       reqVO.setFileUrl(null);
       reqVO.setRemark(null);
       reqVO.setCreator(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));

       // 调用
       PageResult<ErpFinancePaymentListDO> pageResult = financePaymentListService.getFinancePaymentListPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbFinancePaymentList, pageResult.getList().get(0));
    }

}