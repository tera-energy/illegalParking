package com.teraenergy.illegalparking.model.entity.pm.repository;

import com.teraenergy.illegalparking.model.entity.pm.domain.Pm;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-11-03
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface PmRepository extends JpaRepository<Pm, Integer> {
}
