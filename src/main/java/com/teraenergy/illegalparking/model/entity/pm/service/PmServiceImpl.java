package com.teraenergy.illegalparking.model.entity.pm.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.pm.domain.Pm;
import com.teraenergy.illegalparking.model.entity.pm.domain.QPm;
import com.teraenergy.illegalparking.model.entity.pm.repository.PmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-11-03
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class PmServiceImpl implements PmService{

    private final PmRepository pmRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Pm get(Integer pmSeq) {
        Optional<Pm>  optional = pmRepository.findById(pmSeq);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<Pm> gets() {
        return pmRepository.findAll();
    }

    @Override
    public List<Pm> gets(List<String> codes) {
        JPAQuery query = jpaQueryFactory.selectFrom(QPm.pm);
        query.where(QPm.pm.code.in(codes));
        query.where(QPm.pm.isDel.isFalse());
        return query.fetch();
    }

    @Override
    public Pm set(Pm pm) {
        return pmRepository.save(pm);
    }

    @Override
    public List<Pm> sets(List<Pm> pms) {
        return pmRepository.saveAll(pms);
    }

}
