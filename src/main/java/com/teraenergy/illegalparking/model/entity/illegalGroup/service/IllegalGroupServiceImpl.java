package com.teraenergy.illegalparking.model.entity.illegalGroup.service;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.QIllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.enums.GroupFilterColumn;
import com.teraenergy.illegalparking.model.entity.illegalGroup.repository.IllegalGroupRepository;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class IllegalGroupServiceImpl implements IllegalGroupServcie{

    private final JPAQueryFactory jpaQueryFactory;

    private final IllegalGroupRepository illegalGroupRepository;

    @Override
    public IllegalGroup get(LocationType locationType, String name) {
        JPAQuery query = jpaQueryFactory.selectFrom(QIllegalGroup.illegalGroup);
        query.where(QIllegalGroup.illegalGroup.isDel.isFalse());
        query.where(QIllegalGroup.illegalGroup.locationType.eq(locationType));
        query.where(QIllegalGroup.illegalGroup.name.eq(name));
        return  (IllegalGroup) query.fetchOne();
    }

    @Override
    public IllegalGroup get(Integer groupSeq) {
        Optional<IllegalGroup> optional = illegalGroupRepository.findById(groupSeq);
        if (optional.isEmpty()) return null;
        return optional.get();
    }

    @Override
    public List<IllegalGroup> getsNameByIllegalEvent(LocationType locationType) {
        JPAQuery query = jpaQueryFactory.selectFrom(QIllegalGroup.illegalGroup);
        query.where(QIllegalGroup.illegalGroup.locationType.eq(locationType));
        return query.fetch();
    }

    @Override
    public List<String> getsNameByUserGroup(LocationType locationType) {
        JPAQuery query = jpaQueryFactory.selectFrom(QIllegalGroup.illegalGroup);
        query.where(QIllegalGroup.illegalGroup.locationType.eq(locationType));
        List<IllegalGroup> illegalGroups = query.fetch();
        if ( illegalGroups.size() == 0) {
            return Lists.newArrayList();
        }
        return illegalGroups.stream().map( illegalGroup -> illegalGroup.getName()).collect(Collectors.toList());
    }

    @Override
    public List<IllegalGroup> gets(List<Integer> groupSeqs) {
        JPAQuery query = jpaQueryFactory.selectFrom(QIllegalGroup.illegalGroup);
        query.where(QIllegalGroup.illegalGroup.groupSeq.in(groupSeqs));
        return query.fetch();
    }

    @Override
    public Page<IllegalGroup> get(Integer pageNumber, Integer pageSize, GroupFilterColumn filterColumn, String search) {
        JPAQuery query = jpaQueryFactory.selectFrom(QIllegalGroup.illegalGroup);

        switch (filterColumn) {
            case NAME:
                query.where(QIllegalGroup.illegalGroup.name.contains(search));
                break;
            case LOCATION:
                LocationType locationType = LocationType.valueOf(search);
                query.where(QIllegalGroup.illegalGroup.locationType.eq(locationType));
                break;
        }

        query.orderBy(QIllegalGroup.illegalGroup.groupSeq.desc());

        int total = query.fetch().size();
        pageNumber = pageNumber - 1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<IllegalGroup> illegalGroups = query.fetch();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<IllegalGroup> page = new PageImpl<IllegalGroup>(illegalGroups, pageRequest, total);
        return page;
    }

    @Override
    public IllegalGroup set(IllegalGroup illegalGroup) {
        return illegalGroupRepository.save(illegalGroup);
    }

}
