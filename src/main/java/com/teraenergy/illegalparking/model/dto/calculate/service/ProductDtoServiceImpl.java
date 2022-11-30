package com.teraenergy.illegalparking.model.dto.calculate.service;

import com.teraenergy.illegalparking.model.dto.calculate.domain.ProductDto;
import com.teraenergy.illegalparking.model.entity.product.domain.Product;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Service
public class ProductDtoServiceImpl implements ProductDtoService{

    @Override
    public ProductDto get(Product product) {
        ProductDto productDto = new ProductDto();
        productDto.setProductSeq(product.getProductSeq());
        productDto.setBrand(product.getBrand().name());
        productDto.setName(product.getName());
        productDto.setIsDel(product.getIsDel());
        productDto.setPointValue(product.getPointValue());

        return productDto;
    }

    @Override
    public List<ProductDto> gets(List<Product> products) {
        List<ProductDto> productDtos = Lists.newArrayList();
        for( Product product : products) {
            productDtos.add(get(product));
        }
        return productDtos;
    }

}
