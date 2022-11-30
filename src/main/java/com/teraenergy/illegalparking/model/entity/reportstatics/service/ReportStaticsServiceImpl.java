package com.teraenergy.illegalparking.model.entity.reportstatics.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.reportstatics.domain.QReportStatics;
import com.teraenergy.illegalparking.model.entity.reportstatics.domain.ReportStatics;
import com.teraenergy.illegalparking.model.entity.reportstatics.repository.ReportStaticsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-10-20
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class ReportStaticsServiceImpl implements ReportStaticsService{

    private final JPAQueryFactory jpaQueryFactory;

    private final ReportStaticsRepository reportStaticsRepository;

    @Override
    public List<ReportStatics> gets() {
        return reportStaticsRepository.findAll();
    }

    @Override
    public List<ReportStatics> gets(String code) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReportStatics.reportStatics);
        query.where(QReportStatics.reportStatics.code.eq(code));
        return query.fetch();

    }

    @Override
    public ReportStatics gets(Integer zoneSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReportStatics.reportStatics);
        query.where(QReportStatics.reportStatics.zoneSeq.eq(zoneSeq));
        return (ReportStatics) query.fetchFirst();
    }

    @Override
    public List<ReportStatics> gets(List<String> codes) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReportStatics.reportStatics);
        query.where(QReportStatics.reportStatics.code.in(codes));
        return query.fetch();
    }

    @Override
    public ReportStatics set(ReportStatics reportStatics) {
        return reportStaticsRepository.save(reportStatics);
    }

    @Override
    public List<ReportStatics> sets(List<ReportStatics> reportStaticsList) {
        return reportStaticsRepository.saveAll(reportStaticsList);
    }
}
