package com.teraenergy.illegalparking.model.entity.calculate.service;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calculate.domain.QCalculate;
import com.teraenergy.illegalparking.model.entity.calculate.enums.CalculateFilterColumn;
import com.teraenergy.illegalparking.model.entity.calculate.enums.CalculateOrderColumn;
import com.teraenergy.illegalparking.model.entity.calculate.repository.CalculateRepository;
import com.teraenergy.illegalparking.model.entity.user.domain.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class CalculateServiceImpl implements CalculateService{

    private final JPAQueryFactory jpaQueryFactory;

    private final CalculateRepository calculateRepository;

    @Override
    public Calculate get(Integer calculateSeq) {
        Optional<Calculate> optional = calculateRepository.findById(calculateSeq);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public Calculate getAtLast(Integer userSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QCalculate.calculate)
                .where(QCalculate.calculate.userSeq.eq(userSeq))
                .orderBy(QCalculate.calculate.calculateSeq.desc())
                .limit(1);
        return (Calculate) query.fetchOne();
    }

    @Override
    public List<Calculate> gets() {
        return calculateRepository.findAll();
    }

    @Override
    public List<Calculate> getsByUser(Integer userSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QCalculate.calculate)
                .where(QCalculate.calculate.userSeq.eq(userSeq))
                .orderBy(QCalculate.calculate.calculateSeq.desc());
        return query.fetch();
    }

    @Override
    public Page<Calculate> gets(int pageNumber, int pageSize, CalculateFilterColumn filterColumn, String search) {
        JPAQuery query = jpaQueryFactory.selectFrom(QCalculate.calculate);
        query.where(QCalculate.calculate.userSeq.in(
                JPAExpressions.select(QUser.user.userSeq)
                        .from(QUser.user)
                        .where(QUser.user.isDel.isFalse())
        ));

        if ( search != null && search.length() > 0) {
            switch (filterColumn) {
                case user:
                    break;
                case product:
                    break;
            }
        }

        int total = query.fetch().size();

        pageNumber = pageNumber -1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Calculate> calculates = query.fetch();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Calculate> page = new PageImpl<Calculate>(calculates, pageRequest, total);
        return page;
    }

    @Override
    public Calculate set(Calculate calculate) {
        return calculateRepository.save(calculate);
    }

    @Override
    public List<Calculate> sets(List<Calculate> calculates) {
        return calculateRepository.saveAll(calculates);
    }

    @Override
    public Calculate modify(Calculate calculate) {
        return calculateRepository.save(calculate);
    }

    @Override
    public Calculate remove(Integer calculateSeq) {

        return null;
    }
}
