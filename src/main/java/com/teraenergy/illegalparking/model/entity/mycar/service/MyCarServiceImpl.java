package com.teraenergy.illegalparking.model.entity.mycar.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.model.entity.mycar.domain.MyCar;
import com.teraenergy.illegalparking.model.entity.mycar.domain.QMyCar;
import com.teraenergy.illegalparking.model.entity.mycar.repository.MyCarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Date : 2022-10-18
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class MyCarServiceImpl implements MyCarService{

    private final JPAQueryFactory jpaQueryFactory;

    private final MyCarRepository myCarRepository;


    @Override
    public MyCar getByAlarm(String carNum) {
        JPAQuery query = jpaQueryFactory.selectFrom(QMyCar.myCar);
        query.where(QMyCar.myCar.carNum.eq(carNum));
        query.where(QMyCar.myCar.isAlarm.isTrue());
        if ( query.fetchOne() != null) {
            return (MyCar) query.fetchOne();
        } else {
            return null;
        }
    }

    @Override
    public MyCar get(Integer userSeq, String carNum) {
        JPAQuery query = jpaQueryFactory.selectFrom(QMyCar.myCar);
        query.where(QMyCar.myCar.userSeq.eq(userSeq));
        query.where(QMyCar.myCar.carNum.eq(carNum));
        if ( query.fetchOne() != null) {
            return (MyCar) query.fetchOne();
        } else {
            return null;
        }
    }

    @Override
    public List<MyCar> gets(Integer userSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QMyCar.myCar);
        query.where(QMyCar.myCar.userSeq.eq(userSeq));
        return query.fetch();
    }

    @Override
    public boolean isExist(String carNum) {
        JPAQuery query = jpaQueryFactory.selectFrom(QMyCar.myCar);
        query.where(QMyCar.myCar.carNum.eq(carNum));
        if ( query.fetch().size() > 0) {
            return true;
        }
        return false;
    }

    @Transactional
    @Override
    public MyCar set(MyCar myCar) {
//        JPAUpdateClause query = jpaQueryFactory.update(QMyCar.myCar);
//        query.where(QMyCar.myCar.carSeq.eq(myCar.getCarSeq()));
//        query.set(QMyCar.myCar.isAlarm, myCar.isAlarm());
//        query.execute();
        return myCarRepository.save(myCar);
    }

    @Override
    public MyCar modify(MyCar myCar) {
        return myCarRepository.save(myCar);
    }

}
