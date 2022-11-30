package com.teraenergy.illegalparking.model.entity.lawdong.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.QLawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.repository.LawDongRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class LawDongServiceImpl implements LawDongService{

    private final JPAQueryFactory jpaQueryFactory;
    private final LawDongRepository lawDongRepository;

    @Override
    public LawDong get(String code) {
        return lawDongRepository.findByCodeAndIsDel(code, false);
    }

    @Override
    public LawDong getFromLnmadr(String lnmadr) {
        return lawDongRepository.findByNameAndIsDel(lnmadr, false);
    }

    @Override
    public List<LawDong> gets() {
        return lawDongRepository.findAll();
    }

    @Override
    public void set(LawDong lawDong) {
        lawDongRepository.save(lawDong);
    }

    @Override
    public void sets(List<LawDong> lawDongs) {
        lawDongRepository.saveAll(lawDongs);
    }

    @Override
    public void modify(LawDong lawDong) {
        lawDongRepository.save(lawDong);
    }

    @Override
    public void remove(LawDong lawDong) {
        JPAUpdateClause query = jpaQueryFactory.update(QLawDong.lawDong);
        query.set(QLawDong.lawDong.isDel, true);
        query.where(QLawDong.lawDong.dongSeq.eq(lawDong.getDongSeq()));
        query.execute();
    }
}

