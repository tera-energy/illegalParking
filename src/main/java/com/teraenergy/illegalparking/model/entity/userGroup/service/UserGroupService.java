package com.teraenergy.illegalparking.model.entity.userGroup.service;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.userGroup.domain.UserGroup;

import java.util.List;

/**
 * Date : 2022-10-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */


public interface UserGroupService {

    UserGroup get(Integer userGroupSeq);

    List<UserGroup> gets();

    List<UserGroup> getsByUser(Integer userSeq);

    int getCountByUser(Integer userSeq);

    UserGroup set(UserGroup userGroup);

    boolean isExist(Integer userSeq, Integer groupSeq);

    void remove(Integer userGroupSeq) throws TeraException;

}
