package com.teraenergy.illegalparking.model.entity.lawdong.repository;

import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface LawDongRepository extends JpaRepository<LawDong, Integer> {
    LawDong findByCodeAndIsDel(String code, Boolean isDel);

    LawDong findByNameAndIsDel(String name, Boolean isDel);
}
