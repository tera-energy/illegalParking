package com.teraenergy.illegalparking.model.entity.mycar.repository;

import com.teraenergy.illegalparking.model.entity.mycar.domain.MyCar;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-10-18
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface MyCarRepository extends JpaRepository<MyCar, Integer> {
}
