package com.teraenergy.illegalparking.model.entity.calculate.repository;

import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-09-26
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface CalculateRepository extends JpaRepository<Calculate, Integer> {
}
