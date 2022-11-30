package com.teraenergy.illegalparking.model.entity.userGroup.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.entity.userGroup.domain.QUserGroup;
import com.teraenergy.illegalparking.model.entity.userGroup.domain.UserGroup;
import com.teraenergy.illegalparking.model.entity.userGroup.repository.UserGroupRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-10-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class UserGroupServiceImpl implements UserGroupService {

    private final UserGroupRepository userGroupRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public UserGroup get(Integer userGroupSeq) {
        Optional<UserGroup> optional = userGroupRepository.findById(userGroupSeq);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<UserGroup> gets() {
        return userGroupRepository.findAll();
    }

    @Override
    public List<UserGroup> getsByUser(Integer userSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QUserGroup.userGroup);
        query.where(QUserGroup.userGroup.userSeq.eq(userSeq));
        return query.fetch();
    }

    @Override
    public int getCountByUser(Integer userSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QUserGroup.userGroup);
        query.where(QUserGroup.userGroup.userSeq.eq(userSeq));
        return query.fetch().size();
    }

    @Override
    public UserGroup set(UserGroup userGroup) {
        return userGroupRepository.save(userGroup);
    }

    @Override
    public boolean isExist(Integer userSeq, Integer groupSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QUserGroup.userGroup);
        query.where(QUserGroup.userGroup.userSeq.eq(userSeq));
        query.where(QUserGroup.userGroup.groupSeq.eq(groupSeq));
        if ( query.fetch().size() != 0) {
            return true;
        }
        return false;
    }

    @Override
    public void remove(Integer userGroupSeq) throws TeraException {
        try {
            userGroupRepository.deleteById(userGroupSeq);
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.UNKNOWN, e);
        }
    }


}
