package com.teraenergy.illegalparking.model.entity.illegalzone.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.QIllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.repository.IllegalZoneRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;

/**
 * Date : 2022-09-21
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class IllegalZoneServiceImpl implements IllegalZoneService {

    private final IllegalZoneRepository illegalZoneRepository;

    private final JPAQueryFactory jpaQueryFactory;

    private final EntityManager entityManager;

    private final ObjectMapper objectMapper;

    @Override
    public List<IllegalZone> gets( ) {
        return illegalZoneRepository.findAll();
    }

    @Override
    public List<IllegalZone> gets(List<Integer> groupSeqs) {
        JPAQuery query = jpaQueryFactory.selectFrom(QIllegalZone.illegalZone);
        query.where(QIllegalZone.illegalZone.illegalEvent.groupSeq.in(groupSeqs));
        return query.fetch();
    }

    @Override
    public IllegalZone get(Integer zoneSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QIllegalZone.illegalZone);
        query.where(QIllegalZone.illegalZone.zoneSeq.eq(zoneSeq));
        query.where(QIllegalZone.illegalZone.isDel.isFalse());
        return (IllegalZone) query.fetchOne();
    }

    @Override
    public IllegalZone set(IllegalZone illegalZone) {
        String query = "";
        entityManager.createQuery(query);
        return illegalZoneRepository.save(illegalZone);
    }

    @Override
    public List<IllegalZone> sets(List<IllegalZone> illegalZone) {
        return illegalZoneRepository.saveAll(illegalZone);
    }

    @Override
    public IllegalZone modify(IllegalZone illegalZone) {
        return illegalZoneRepository.save(illegalZone);
    }

    @Override
    public List<IllegalZone> modifies(List<IllegalZone> illegalZones) {
        return illegalZoneRepository.saveAll(illegalZones);
    }

    @Override
    public long remove(Integer illegalZoneSeq) {
        JPAUpdateClause query = jpaQueryFactory.update(QIllegalZone.illegalZone);
        query.set(QIllegalZone.illegalZone.isDel, true);
        query.where(QIllegalZone.illegalZone.zoneSeq.eq(illegalZoneSeq));
        return query.execute();
    }


    @Override
    public long removes(List<Integer> illegalZoneSeqs) {
        JPAUpdateClause query = jpaQueryFactory.update(QIllegalZone.illegalZone);
        query.set(QIllegalZone.illegalZone.isDel, true);
        query.where(QIllegalZone.illegalZone.zoneSeq.in(illegalZoneSeqs));
        return query.execute();
    }
}
