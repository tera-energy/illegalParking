package com.teraenergy.illegalparking.model.entity.notice.domain;

import com.teraenergy.illegalparking.model.entity.notice.enums.NoticeType;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Date : 2022-10-17
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Setter
@Getter
@Entity (name = "notice")
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer noticeSeq;

    @Column
    String subject;

    @Column
    String content;

    @Column
    String html;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "UserSeq")
    User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    NoticeType noticeType;

    @Column
    LocalDateTime regDt = LocalDateTime.now();

    @Column
    boolean isDel = false;

    @Column
    LocalDateTime delDt;

}
