package com.teraenergy.illegalparking.model.entity.userGroup.repository;

import com.teraenergy.illegalparking.model.entity.userGroup.domain.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-10-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface UserGroupRepository extends JpaRepository<UserGroup, Integer> {
}
