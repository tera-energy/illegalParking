package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.governmentoffice.domain.GovernmentOffice;
import com.teraenergy.illegalparking.model.entity.governmentoffice.service.GovernmentOfficeService;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.notice.domain.Notice;
import com.teraenergy.illegalparking.model.entity.notice.enums.NoticeType;
import com.teraenergy.illegalparking.model.entity.notice.service.NoticeService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
public class SqlNotice {

    @Autowired
    UserService userService;

    @Autowired
    NoticeService noticeService;

    @Test
    public void insert() {
        try {
            List<Notice> notices = Lists.newArrayList();

            Notice notice = new Notice();
            Notice notice1 = new Notice();
            Notice notice2 = new Notice();
            Notice notice3 = new Notice();
            Notice notice4 = new Notice();
            Notice notice5 = new Notice();

            notice.setSubject("sample1");
            notice1.setSubject("sample2");
            notice2.setSubject("sample3");
            notice3.setSubject("sample4");
            notice4.setSubject("sample5");
            notice5.setSubject("sample6");

            notice.setContent("content ... 1");
            notice1.setContent("content ... 2");
            notice2.setContent("content ... 3");
            notice3.setContent("content ... 4");
            notice4.setContent("content ... 5");
            notice5.setContent("content ... 6");

            User user = userService.get(2);
            notice. setUser(user);
            notice1.setUser(user);
            notice2.setUser(user);
            notice3.setUser(user);
            notice4.setUser(user);
            notice5.setUser(user);

            notice.setRegDt(LocalDateTime.now());
            notice1.setRegDt(LocalDateTime.now());
            notice2.setRegDt(LocalDateTime.now());
            notice3.setRegDt(LocalDateTime.now());
            notice4.setRegDt(LocalDateTime.now());
            notice5.setRegDt(LocalDateTime.now());

            notice.setNoticeType(NoticeType.ANNOUNCEMENT);
            notice1.setNoticeType(NoticeType.DISTRIBUTION);
            notice2.setNoticeType(NoticeType.ANNOUNCEMENT);
            notice3.setNoticeType(NoticeType.ANNOUNCEMENT);
            notice4.setNoticeType(NoticeType.DISTRIBUTION);
            notice5.setNoticeType(NoticeType.ANNOUNCEMENT);

            notices.add(notice);
            notices.add(notice1);
            notices.add(notice2);
            notices.add(notice3);
            notices.add(notice4);
            notices.add(notice5);

            noticeService.sets(notices);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void update(){
        List<Notice> notices = noticeService.gets();

        for(Notice notice : notices) {
            String html = "<p>";
            html += notice.getContent();
            html.replaceAll("\n", "</p><p>");
            html += "</p>";
            System.out.println(html);
            notice.setHtml(html);
        }
        noticeService.sets(notices);
    }
}
