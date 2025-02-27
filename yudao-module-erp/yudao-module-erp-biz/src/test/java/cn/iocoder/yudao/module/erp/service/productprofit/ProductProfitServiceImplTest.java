package cn.iocoder.yudao.module.erp.service.productprofit;

import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductProfitPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductProfitSaveReqVO;
import cn.iocoder.yudao.module.erp.service.product.ProductProfitServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.erp.dal.dataobject.productprofit.ProductProfitDO;
import cn.iocoder.yudao.module.erp.dal.mysql.product.ProductProfitMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ProductProfitServiceImpl} 的单元测试类
 *
 * @author 沈飞宇
 */
@Import(ProductProfitServiceImpl.class)
public class ProductProfitServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProductProfitServiceImpl productProfitService;

    @Resource
    private ProductProfitMapper productProfitMapper;

    @Test
    public void testCreateProductProfit_success() {
        // 准备参数
        ProductProfitSaveReqVO createReqVO = randomPojo(ProductProfitSaveReqVO.class).setId(null);

        // 调用
        Long productProfitId = productProfitService.createProductProfit(createReqVO);
        // 断言
        assertNotNull(productProfitId);
        // 校验记录的属性是否正确
        ProductProfitDO productProfit = productProfitMapper.selectById(productProfitId);
        assertPojoEquals(createReqVO, productProfit, "id");
    }

    @Test
    public void testUpdateProductProfit_success() {
        // mock 数据
        ProductProfitDO dbProductProfit = randomPojo(ProductProfitDO.class);
        productProfitMapper.insert(dbProductProfit);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProductProfitSaveReqVO updateReqVO = randomPojo(ProductProfitSaveReqVO.class, o -> {
            o.setId(dbProductProfit.getId()); // 设置更新的 ID
        });

        // 调用
        productProfitService.updateProductProfit(updateReqVO);
        // 校验是否更新正确
        ProductProfitDO productProfit = productProfitMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, productProfit);
    }

    @Test
    public void testUpdateProductProfit_notExists() {
        // 准备参数
        ProductProfitSaveReqVO updateReqVO = randomPojo(ProductProfitSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> productProfitService.updateProductProfit(updateReqVO), PRODUCT_PROFIT_NOT_EXISTS);
    }

    @Test
    public void testDeleteProductProfit_success() {
        // mock 数据
        ProductProfitDO dbProductProfit = randomPojo(ProductProfitDO.class);
        productProfitMapper.insert(dbProductProfit);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProductProfit.getId();

        // 调用
        productProfitService.deleteProductProfit(id);
       // 校验数据不存在了
       assertNull(productProfitMapper.selectById(id));
    }

    @Test
    public void testDeleteProductProfit_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> productProfitService.deleteProductProfit(id), PRODUCT_PROFIT_NOT_EXISTS);
    }

    @Test
    @Disabled  //  请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProductProfitPage() {
       // mock 数据
       ProductProfitDO dbProductProfit = randomPojo(ProductProfitDO.class, o -> { // 等会查询到
           o.setProductId(null);
           o.setProfit(null);
           o.setPurchasePrice(null);
           o.setSalePrice(null);
           o.setFirstLegPrice(null);
           o.setLastMilePrice(null);
           o.setRefundRate(null);
           o.setRefundFreight(null);
           o.setAdPrice(null);
           o.setOtherPrice(null);
           o.setOtherDetail(null);
           o.setCreateTime(null);
           o.setRemark(null);
       });
       productProfitMapper.insert(dbProductProfit);
       // 测试 productId 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setProductId(null)));
       // 测试 profit 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setProfit(null)));
       // 测试 purchasePrice 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setPurchasePrice(null)));
       // 测试 salePrice 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setSalePrice(null)));
       // 测试 firstLegPrice 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setFirstLegPrice(null)));
       // 测试 lastMilePrice 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setLastMilePrice(null)));
       // 测试 refundRate 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setRefundRate(null)));
       // 测试 refundFreight 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setRefundFreight(null)));
       // 测试 otherPrice 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setOtherPrice(null)));
       // 测试 otherDetail 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setOtherDetail(null)));
       // 测试 createTime 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       productProfitMapper.insert(cloneIgnoreId(dbProductProfit, o -> o.setRemark(null)));
       // 准备参数
       ProductProfitPageReqVO reqVO = new ProductProfitPageReqVO();
       reqVO.setProductId(null);
       reqVO.setProfit(null);
       reqVO.setPurchasePrice(null);
       reqVO.setSalePrice(null);
       reqVO.setFirstLegPrice(null);
       reqVO.setLastMilePrice(null);
       reqVO.setRefundRate(null);
       reqVO.setRefundFreight(null);
       reqVO.setOtherPrice(null);
       reqVO.setOtherDetail(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setRemark(null);

       // 调用
       PageResult<ProductProfitDO> pageResult = productProfitService.getProductProfitPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProductProfit, pageResult.getList().get(0));
    }

}