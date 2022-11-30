package com.teraenergy.illegalparking.model.entity.parking.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.domain.QParking;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingFilterColumn;
import com.teraenergy.illegalparking.model.entity.parking.repository.ParkingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
public class ParkingServiceImpl implements ParkingService{

    private final JPAQueryFactory jpaQueryFactory;

    private final ParkingRepository parkingRepository;

    @Override
    public Parking get(Integer parkingSeq) {
        return parkingRepository.findByParkingSeq(parkingSeq);
    }

    @Override
    public List<Parking> gets() {
        return parkingRepository.findByIsDel(false);
    }

    @Override
    public List<Parking> gets(List<String> codes) {
        JPAQuery query = jpaQueryFactory.selectFrom(QParking.parking);
        query.where(QParking.parking.code.in(codes));
        query.where(QParking.parking.isDel.isFalse());
        return query.fetch();
    }

    @Override
    public Page<Parking> gets(int pageNumber, int pageSize, ParkingFilterColumn filterColumn, String search ) {
        JPAQuery query = jpaQueryFactory.selectFrom(QParking.parking);

        if ( search != null && search.length() > 0) {
            switch (filterColumn) {
                case parkingchrgeInfo:
                    query.where(QParking.parking.parkingchrgeInfo.like("%" + search + "%"));
                    break;
                case prkplceNm:
                    query.where(QParking.parking.prkplceNm.like("%" + search + "%"));
                    break;
            }
        }

        query.where(QParking.parking.isDel.isFalse());
        query.orderBy(QParking.parking.parkingSeq.desc());

        int total = query.fetch().size();

        pageNumber = pageNumber -1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Parking> parkings = query.fetch();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Parking> page = new PageImpl<Parking>(parkings, pageRequest, total);
        return page;
    }

    @Override
    public List<Parking> sets(List<Parking> parkings) {
        return parkingRepository.saveAll(parkings);
    }

    @Override
    public Parking set(Parking parking) {
        return parkingRepository.save(parking);
    }

    @Override
    public Parking modify(Parking parking) {
        return parkingRepository.save(parking);
    }

    @Override
    public long remove(Parking parking) {
        JPAUpdateClause query = jpaQueryFactory.update(QParking.parking);
        query.set(QParking.parking.isDel, true);
        query.where(QParking.parking.parkingSeq.eq(parking.getParkingSeq()));
        return query.execute();
    }
}
