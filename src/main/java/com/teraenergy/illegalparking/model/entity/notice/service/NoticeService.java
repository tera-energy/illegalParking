package com.teraenergy.illegalparking.model.entity.notice.service;

import com.teraenergy.illegalparking.model.entity.notice.domain.Notice;
import com.teraenergy.illegalparking.model.entity.notice.enums.NoticeFilterColumn;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Date : 2022-10-17
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface NoticeService {

    List<Notice> gets();

    Page<Notice> gets(int pageNumber, int pageSize, NoticeFilterColumn filterColumn, String search);

    Notice get(Integer noticeSeq);

    Notice set(Notice notice);

    List<Notice> sets(List<Notice> notices);

    List<Notice> getsAtFive();

    List<Notice> getsAtFive(int offset, int limit);
}
