package com.teraenergy.illegalparking.model.dto.illegalzone.service;

import com.teraenergy.illegalparking.model.dto.illegalzone.domain.IllegalGroupDto;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.enums.GroupFilterColumn;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Date : 2022-10-09
 * Author : zilet
 * Project : illegalParking
 * Description :
 */

public interface IllegalGroupDtoService {

    List<IllegalGroupDto> gets(List<IllegalGroup> illegalGroups);

    Page<IllegalGroupDto> gets(Integer pageNumber, Integer pageSize, GroupFilterColumn filterColumn, String search);

}
