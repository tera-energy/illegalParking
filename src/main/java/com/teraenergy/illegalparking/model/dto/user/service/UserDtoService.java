package com.teraenergy.illegalparking.model.dto.user.service;

import com.teraenergy.illegalparking.model.dto.user.domain.UserDto;
import com.teraenergy.illegalparking.model.entity.user.domain.User;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface UserDtoService {

    UserDto get(User user);

}
