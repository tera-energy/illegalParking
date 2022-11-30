package com.teraenergy.illegalparking.model.entity.product.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.model.entity.product.domain.Product;
import com.teraenergy.illegalparking.model.entity.product.domain.QProduct;
import com.teraenergy.illegalparking.model.entity.product.enums.Brand;
import com.teraenergy.illegalparking.model.entity.product.enums.ProductFilterColumn;
import com.teraenergy.illegalparking.model.entity.product.enums.ProductOrderColumn;
import com.teraenergy.illegalparking.model.entity.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final JPAQueryFactory jpaQueryFactory;

    private final ProductRepository productRepository;

    @Override
    public Product get(Integer productSeq) {
        Optional<Product> optional = productRepository.findById(productSeq);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<Product> gets() {
        return productRepository.findAll();
    }

    @Override
    public List<Product> gets(List<Integer> productSeqs) {
        JPAQuery query = jpaQueryFactory.selectFrom(QProduct.product).where(QProduct.product.productSeq.in(productSeqs));
        return query.fetch();
    }

    @Override
    public Page<Product> gets(int pageNumber, int pageSize, ProductFilterColumn filterColumn, String search) {
        JPAQuery query = jpaQueryFactory.selectFrom(QProduct.product);

        if (search != null && search.length() > 0) {
            switch (filterColumn) {
                case name:
                    query.where(QProduct.product.name.contains(search));
                    break;
                case brand:
                    query.where(QProduct.product.brand.eq(Brand.valueOf(search)));
                    break;
                case point:

                    break;
            }
        }

        query.where(QProduct.product.isDel.isFalse());

        int total = query.fetch().size();

        pageNumber = pageNumber - 1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Product> products = query.fetch();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Product> page = new PageImpl<Product>(products, pageRequest, total);
        return page;
    }

    @Override
    public Product set(Product product) {
        return productRepository.save(product);
    }

    @Override
    public List<Product> sets(List<Product> products) {
        return productRepository.saveAll(products);
    }

    @Override
    public long remove(Integer integer) {
        JPAUpdateClause query = jpaQueryFactory.update(QProduct.product).set(QProduct.product.isDel, true);
        return query.execute();
    }
}
