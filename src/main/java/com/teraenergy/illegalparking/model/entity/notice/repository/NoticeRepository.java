package com.teraenergy.illegalparking.model.entity.notice.repository;

import com.teraenergy.illegalparking.model.entity.notice.domain.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-10-17
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface NoticeRepository extends JpaRepository<Notice, Integer> {
}
