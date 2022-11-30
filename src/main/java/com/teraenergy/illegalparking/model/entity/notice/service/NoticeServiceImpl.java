package com.teraenergy.illegalparking.model.entity.notice.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.notice.domain.Notice;
import com.teraenergy.illegalparking.model.entity.notice.domain.QNotice;
import com.teraenergy.illegalparking.model.entity.notice.enums.NoticeFilterColumn;
import com.teraenergy.illegalparking.model.entity.notice.enums.NoticeType;
import com.teraenergy.illegalparking.model.entity.notice.repository.NoticeRepository;
import com.teraenergy.illegalparking.model.entity.parking.domain.QParking;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-10-17
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class NoticeServiceImpl implements NoticeService{

    private final NoticeRepository noticeRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Notice> gets() {
        return noticeRepository.findAll();
    }

    @Override
    public Page<Notice> gets(int pageNumber, int pageSize, NoticeFilterColumn filterColumn, String search) {
        JPAQuery query = jpaQueryFactory.selectFrom(QNotice.notice);

        switch (filterColumn) {
            case CONTENT:
                query.where(QNotice.notice.content.contains(search));
                break;
            case SUBJECT:
                query.where(QNotice.notice.subject.contains(search));
                break;
            case NOTICETYPE:
                query.where(QNotice.notice.noticeType.eq(NoticeType.valueOf(search)));
                break;
        }

        query.where(QNotice.notice.isDel.isFalse());

        int total = query.fetch().size();

        query.orderBy(QNotice.notice.noticeType.desc());
        query.orderBy(QNotice.notice.noticeSeq.desc());

        pageNumber = pageNumber -1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Notice> notices = query.fetch();
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Notice> page = new PageImpl<Notice>(notices, pageRequest, total);
        return page;
    }

    @Override
    public Notice get(Integer noticeSeq) {
        Optional<Notice> optional =noticeRepository.findById(noticeSeq);
        if (optional.isEmpty()) {
            return null;
        }
        return optional.get();
    }

    @Override
    public Notice set(Notice notice) {
        return noticeRepository.save(notice);
    }

    @Override
    public List<Notice> sets(List<Notice> notices) {
        return noticeRepository.saveAll(notices);
    }

    @Override
    public List<Notice> getsAtFive() {
        JPAQuery query = jpaQueryFactory.selectFrom(QNotice.notice);
        query.where(QNotice.notice.isDel.isFalse());
        query.orderBy(QNotice.notice.noticeType.desc());
        query.orderBy(QNotice.notice.noticeSeq.desc());
        query.limit(5);
        return query.fetch();
    }

    @Override
    public List<Notice> getsAtFive(int offset, int limit) {
        JPAQuery query = jpaQueryFactory.selectFrom(QNotice.notice);
        query.where(QNotice.notice.isDel.isFalse());
        query.orderBy(QNotice.notice.noticeType.desc());
        query.orderBy(QNotice.notice.noticeSeq.desc());
        query.offset(offset);
        query.limit(5);
        return query.fetch();
    }
}
