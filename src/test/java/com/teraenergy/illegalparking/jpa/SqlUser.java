package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.governmentoffice.domain.GovernmentOffice;
import com.teraenergy.illegalparking.model.entity.pm.domain.Pm;
import com.teraenergy.illegalparking.model.entity.pm.enums.PmType;
import com.teraenergy.illegalparking.model.entity.pm.service.PmService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import com.teraenergy.illegalparking.model.entity.governmentoffice.service.GovernmentOfficeService;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Date : 2022-09-20
 * Author : young
 * Project : illegalParking
 * Description :
 */

@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
@Transactional
public class SqlUser {

    @Autowired
    UserService userService;

    @Autowired
    GovernmentOfficeService governmentOfficeService;

    @Test
    public void insert(){
        try {
            insertByAdminUser();
            insertByGovernmentOffice();
            insertByGeneralUser();
        } catch (TeraException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void insertByAdminUser() throws TeraException {
        List<User> users = Lists.newArrayList();

        User user = new User();
        user.setIsDel(false);
        user.setUsername("admin");

        user.setPassword("tera1234");
        user.setRole(Role.ADMIN);
        user.setUserCode(1234l);
        user.setName("관리자");
        user.setPhotoName("");
        user.setPhoneNumber("");
        users.add(user);

        userService.sets(users);
    }

    @Test
    public void insertByGovernmentUser() throws TeraException {
        List<User> users = Lists.newArrayList();

        GovernmentOffice governMentOffice = governmentOfficeService.get(1);

        User user3 = new User();
        user3.setUsername("naju");
        user3.setPassword("qwer1234");
        user3.setRole(Role.GOVERNMENT);
        user3.setUserCode(1234l);
        user3.setIsDel(false);
        user3.setName("양만춘");
        user3.setGovernMentOffice(governMentOffice);
        users.add(user3);

        userService.sets(users);
    }

    @Test
    public void insertByGeneralUser() throws TeraException {
        List<User> users = Lists.newArrayList();

        User user2 = new User();
        user2.setUsername("hong@gmail.com");
        user2.setPassword("qwer1234");
        user2.setRole(Role.USER);
        user2.setUserCode(1234l);
        user2.setIsDel(false);
        user2.setName("홍길동");
        user2.setPhotoName("sample2");
        user2.setPhoneNumber("010-1234-8901");
        users.add(user2);

        userService.sets(users);
    }

    @Test
    public void insertByGovernmentOffice(){
        GovernmentOffice governMentOffice = new GovernmentOffice();
        governMentOffice.setLocationType(LocationType.JEONNAM);
        governMentOffice.setName("나주시청 차량 민원과");

        governmentOfficeService.set(governMentOffice);
    }

    @Test
    public void authentication() throws TeraException {
        if ( userService.isUser("admin") ) {
            System.out.println("is ... ");
        } else  {
            System.out.println("is not .... ");
        }
    }

    @Test
    public void select() throws TeraException {
        User user = userService.get(1);
        user.setDecryptPassword();
        System.out.println(user.getPassword());
    }
}
