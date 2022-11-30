package com.teraenergy.illegalparking.model.entity.user.service;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.user.enums.UserGovernmentFilterColumn;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Date : 2022-09-20
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface UserService {

    User get(Integer userSeq) throws TeraException;

    User get(String userName) throws TeraException;

    User getByGovernmentOffice(String userName, String password) throws TeraException;

    List<User> gets() throws TeraException;

    Page<User> getsByGovernmentRole(int pageNumber, int pageSize, UserGovernmentFilterColumn userGovernmentFilterColumn, String search) throws TeraException;

    boolean isUserByUserNameAndPassword(String userName, String password) throws TeraException;

    boolean isUserByUserNameAndPasswordMobile(String userName, String password) throws TeraException;

    boolean isUser(String userName) throws TeraException;

    boolean isUserByPhoneNumber(String phoneNumber) throws TeraException;

    boolean isUserByDuplicate(String userName) throws TeraException;

    User set(User user) throws TeraException;

    List<User> sets(List<User> users) throws TeraException;



}
