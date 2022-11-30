package com.teraenergy.illegalparking.model.entity.governmentoffice.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.governmentoffice.domain.GovernmentOffice;
import com.teraenergy.illegalparking.model.entity.governmentoffice.domain.QGovernmentOffice;
import com.teraenergy.illegalparking.model.entity.governmentoffice.repository.GovernmentOfficeRepository;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.QIllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class GovernmentOfficeServiceImpl implements GovernmentOfficeService{

    private final GovernmentOfficeRepository governmentOfficeRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public GovernmentOffice get(Integer officeSeq) {
        Optional<GovernmentOffice> optional = governmentOfficeRepository.findById(officeSeq);
        if ( optional.isEmpty() ) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<GovernmentOffice> gets() {
        return governmentOfficeRepository.findAll();
    }

    @Override
    public boolean isExist(String name, LocationType locationType) {
        JPAQuery query = jpaQueryFactory.selectFrom(QGovernmentOffice.governmentOffice);
        query.where(QGovernmentOffice.governmentOffice.name.eq(name));
        query.where(QGovernmentOffice.governmentOffice.locationType.eq(locationType));
        if ( query.fetch().size() > 0 ) {
            return true;
        }
        return false;
    }

    @Override
    public GovernmentOffice set(GovernmentOffice governmentOffice) {
        return governmentOfficeRepository.save(governmentOffice);
    }
}
