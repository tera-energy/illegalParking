package com.teraenergy.illegalparking.model.entity.user.service;

import com.google.common.collect.Lists;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.encrypt.YoungEncoder;
import com.teraenergy.illegalparking.encrypt.YoungEncrypter;
import com.teraenergy.illegalparking.exception.EncryptedException;
import com.teraenergy.illegalparking.exception.enums.EncryptedExceptionCode;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.user.enums.UserGovernmentFilterColumn;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.user.domain.QUser;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import com.teraenergy.illegalparking.model.entity.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-09-20
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Slf4j
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public User get(Integer userSeq) throws TeraException {
        try {
            Optional<User> optional = userRepository.findById(userSeq);
            if ( optional.isEmpty()) {
                return null;
            }
            User user = optional.get();
            return user;
        } catch (EncryptedException e) {
            throw new TeraException(EncryptedExceptionCode.DECRYPT_FAILURE.getMessage(), e);
        }
    }

    @Override
    public User get(String userName) throws TeraException {
        try {
            User user = jpaQueryFactory.selectFrom(QUser.user)
                    .where(QUser.user.username.eq(userName))
                    .fetchOne();

            if (user == null) {
                return null;
            }
            return user;
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST, e);
        }
    }

    @Override
    public User getByGovernmentOffice(String userName, String password) throws TeraException {
        String encryptPassword = YoungEncoder.encrypt(password);
        JPAQuery query = jpaQueryFactory.selectFrom(QUser.user);
        query.where(QUser.user.username.eq(userName));
        query.where(QUser.user.password.eq(encryptPassword));
        if ( query.fetchOne() != null) {
            return (User) query.fetchOne();
        } else {
            return null;
        }
    }

    @Override
    public List<User> gets() throws TeraException {
        try {
            List<User> users = userRepository.findAll();
            if (users == null) {
                users = Lists.newArrayList();
            }
            return users;
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.UNKNOWN, e);
        }
    }

    @Override
    public Page<User> getsByGovernmentRole(int pageNumber, int pageSize, UserGovernmentFilterColumn userGovernmentFilterColumn, String search) throws TeraException {
        JPAQuery query = jpaQueryFactory.selectFrom(QUser.user);
        query.where(QUser.user.role.eq(Role.GOVERNMENT));

        switch (userGovernmentFilterColumn) {
            case LOCATION:
                query.where(QUser.user.governMentOffice.locationType.eq(LocationType.valueOf(search)));
                break;
            case OFFICE_NAME:
                query.where(QUser.user.governMentOffice.name.contains(search));
                break;
        }

        int total = query.fetch().size();

        pageNumber = pageNumber - 1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<User> users = query.fetch();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<User> page = new PageImpl<User>(users, pageRequest, total);
        return page;


    }

    @Override
    public boolean isUserByUserNameAndPassword(String userName, String password) throws TeraException {
        try {
            String _password = YoungEncoder.encrypt(password);
            if (jpaQueryFactory.selectFrom(QUser.user)
                    .where(QUser.user.username.eq(userName))
                    .where(QUser.user.password.eq(_password))
                    .where(QUser.user.role.ne(Role.USER))
                    .fetchOne() != null) {
                return true;
            }
        } catch (EncryptedException e) {
            throw new TeraException(EncryptedExceptionCode.ENCRYPT_FAILURE.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean isUserByUserNameAndPasswordMobile(String userName, String password) throws TeraException {
        try {
            String _password = YoungEncoder.encrypt(password);
            if (jpaQueryFactory.selectFrom(QUser.user)
                    .where(QUser.user.username.eq(userName))
                    .where(QUser.user.password.eq(_password))
                    .where(QUser.user.role.eq(Role.USER))
                    .where(QUser.user.isDel.isFalse())
                    .fetchOne() != null) {
                return true;
            }
        } catch (EncryptedException e) {
            throw new TeraException(EncryptedExceptionCode.ENCRYPT_FAILURE.getMessage(), e);
        }
        return false;
    }

    @Override
    public boolean isUser(String userName) throws TeraException {
        try {
            if (jpaQueryFactory.selectFrom(QUser.user)
                    .where(QUser.user.username.eq(userName))
                    .where(QUser.user.role.ne(Role.USER))
                    .fetchOne() != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST, e);
        }
    }

    @Override
    public boolean isUserByPhoneNumber(String phoneNumber) throws TeraException {
        try {
            if (jpaQueryFactory.selectFrom(QUser.user)
                    .where(QUser.user.phoneNumber.eq(phoneNumber))
                    .where(QUser.user.role.eq(Role.USER))
                    .fetch().size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST, e);
        }
    }

    @Override
    public boolean isUserByDuplicate(String userName) throws TeraException {
        try {
            if (jpaQueryFactory.selectFrom(QUser.user)
                    .where(QUser.user.username.eq(userName))
                    .where(QUser.user.role.eq(Role.USER))
                    .fetch().size() > 0) {
                return true;
            }
            return false;
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST, e);
        }
    }

    @Override
    public User set(User user) throws TeraException {
        try {
            user.setEncyptPassword();
            return userRepository.save(user);
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.USER_INSERT_FAIL, e);
        }
    }

    @Override
    public List<User> sets(List<User> users) throws TeraException {
        try {
            for (User user : users)
                user.setEncyptPassword();
            return userRepository.saveAll(users);
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.USER_INSERT_FAIL);
        }
    }

}
