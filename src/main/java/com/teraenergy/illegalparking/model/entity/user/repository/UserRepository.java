package com.teraenergy.illegalparking.model.entity.user.repository;

import com.teraenergy.illegalparking.model.entity.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-09-20
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface UserRepository extends JpaRepository<User, Integer> {
}
