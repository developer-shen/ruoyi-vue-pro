package cn.iocoder.yudao.module.erp.service.productattributes;

import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductAttributesPageReqVO;
import cn.iocoder.yudao.module.erp.controller.admin.product.vo.product.ProductAttributesSaveReqVO;
import cn.iocoder.yudao.module.erp.service.product.ProductAttributesServiceImpl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

import cn.iocoder.yudao.framework.test.core.ut.BaseDbUnitTest;

import cn.iocoder.yudao.module.erp.dal.dataobject.productattributes.ProductAttributesDO;
import cn.iocoder.yudao.module.erp.dal.mysql.product.ProductAttributesMapper;
import cn.iocoder.yudao.framework.common.pojo.PageResult;

import org.springframework.context.annotation.Import;

import static cn.iocoder.yudao.module.erp.enums.ErrorCodeConstants.*;
import static cn.iocoder.yudao.framework.test.core.util.AssertUtils.*;
import static cn.iocoder.yudao.framework.test.core.util.RandomUtils.*;
import static cn.iocoder.yudao.framework.common.util.date.LocalDateTimeUtils.*;
import static cn.iocoder.yudao.framework.common.util.object.ObjectUtils.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link ProductAttributesServiceImpl} 的单元测试类
 *
 * @author 沈飞宇
 */
@Import(ProductAttributesServiceImpl.class)
public class ProductAttributesServiceImplTest extends BaseDbUnitTest {

    @Resource
    private ProductAttributesServiceImpl productAttributesService;

    @Resource
    private ProductAttributesMapper productAttributesMapper;

    @Test
    public void testCreateProductAttributes_success() {
        // 准备参数
        ProductAttributesSaveReqVO createReqVO = randomPojo(ProductAttributesSaveReqVO.class).setId(null);

        // 调用
        Long productAttributesId = productAttributesService.createProductAttributes(createReqVO);
        // 断言
        assertNotNull(productAttributesId);
        // 校验记录的属性是否正确
        ProductAttributesDO productAttributes = productAttributesMapper.selectById(productAttributesId);
        assertPojoEquals(createReqVO, productAttributes, "id");
    }

    @Test
    public void testUpdateProductAttributes_success() {
        // mock 数据
        ProductAttributesDO dbProductAttributes = randomPojo(ProductAttributesDO.class);
        productAttributesMapper.insert(dbProductAttributes);// @Sql: 先插入出一条存在的数据
        // 准备参数
        ProductAttributesSaveReqVO updateReqVO = randomPojo(ProductAttributesSaveReqVO.class, o -> {
            o.setId(dbProductAttributes.getId()); // 设置更新的 ID
        });

        // 调用
        productAttributesService.updateProductAttributes(updateReqVO);
        // 校验是否更新正确
        ProductAttributesDO productAttributes = productAttributesMapper.selectById(updateReqVO.getId()); // 获取最新的
        assertPojoEquals(updateReqVO, productAttributes);
    }

    @Test
    public void testUpdateProductAttributes_notExists() {
        // 准备参数
        ProductAttributesSaveReqVO updateReqVO = randomPojo(ProductAttributesSaveReqVO.class);

        // 调用, 并断言异常
        assertServiceException(() -> productAttributesService.updateProductAttributes(updateReqVO), PRODUCT_ATTRIBUTES_NOT_EXISTS);
    }

    @Test
    public void testDeleteProductAttributes_success() {
        // mock 数据
        ProductAttributesDO dbProductAttributes = randomPojo(ProductAttributesDO.class);
        productAttributesMapper.insert(dbProductAttributes);// @Sql: 先插入出一条存在的数据
        // 准备参数
        Long id = dbProductAttributes.getId();

        // 调用
        productAttributesService.deleteProductAttributes(id);
       // 校验数据不存在了
       assertNull(productAttributesMapper.selectById(id));
    }

    @Test
    public void testDeleteProductAttributes_notExists() {
        // 准备参数
        Long id = randomLongId();

        // 调用, 并断言异常
        assertServiceException(() -> productAttributesService.deleteProductAttributes(id), PRODUCT_ATTRIBUTES_NOT_EXISTS);
    }

    @Test
    @Disabled  // 请修改 null 为需要的值，然后删除 @Disabled 注解
    public void testGetProductAttributesPage() {
       // mock 数据
       ProductAttributesDO dbProductAttributes = randomPojo(ProductAttributesDO.class, o -> { // 等会查询到
           o.setProductId(null);
           o.setCreateTime(null);
           o.setRemark(null);
           o.setWeight(null);
           o.setSizes(null);
           o.setStock(null);
           o.setComposition(null);
           o.setProductMeasurements(null);
           o.setBodyMeasurements(null);
           o.setCareInstructions(null);
       });
       productAttributesMapper.insert(dbProductAttributes);
       // 测试 productId 不匹配
       productAttributesMapper.insert(cloneIgnoreId(dbProductAttributes, o -> o.setProductId(null)));
       // 测试 createTime 不匹配
       productAttributesMapper.insert(cloneIgnoreId(dbProductAttributes, o -> o.setCreateTime(null)));
       // 测试 remark 不匹配
       productAttributesMapper.insert(cloneIgnoreId(dbProductAttributes, o -> o.setRemark(null)));
       // 测试 weight 不匹配
       productAttributesMapper.insert(cloneIgnoreId(dbProductAttributes, o -> o.setWeight(null)));
       // 测试 sizes 不匹配
       productAttributesMapper.insert(cloneIgnoreId(dbProductAttributes, o -> o.setSizes(null)));
       // 测试 stock 不匹配
       productAttributesMapper.insert(cloneIgnoreId(dbProductAttributes, o -> o.setStock(null)));
       // 测试 composition 不匹配
       productAttributesMapper.insert(cloneIgnoreId(dbProductAttributes, o -> o.setComposition(null)));
       // 测试 productMeasurements 不匹配
       productAttributesMapper.insert(cloneIgnoreId(dbProductAttributes, o -> o.setProductMeasurements(null)));
       // 测试 bodyMeasurements 不匹配
       productAttributesMapper.insert(cloneIgnoreId(dbProductAttributes, o -> o.setBodyMeasurements(null)));
       // 测试 careInstructions 不匹配
       productAttributesMapper.insert(cloneIgnoreId(dbProductAttributes, o -> o.setCareInstructions(null)));
       // 准备参数
       ProductAttributesPageReqVO reqVO = new ProductAttributesPageReqVO();
       reqVO.setProductId(null);
       reqVO.setCreateTime(buildBetweenTime(2023, 2, 1, 2023, 2, 28));
       reqVO.setRemark(null);
       reqVO.setWeight(null);
       reqVO.setSizes(null);
       reqVO.setStock(null);
       reqVO.setComposition(null);
       reqVO.setProductMeasurements(null);
       reqVO.setBodyMeasurements(null);
       reqVO.setCareInstructions(null);

       // 调用
       PageResult<ProductAttributesDO> pageResult = productAttributesService.getProductAttributesPage(reqVO);
       // 断言
       assertEquals(1, pageResult.getTotal());
       assertEquals(1, pageResult.getList().size());
       assertPojoEquals(dbProductAttributes, pageResult.getList().get(0));
    }

}