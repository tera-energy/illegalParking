package com.teraenergy.illegalparking.model.dto.user.service;

import com.teraenergy.illegalparking.model.dto.user.domain.UserDto;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Service
public class UserDtoServiceImpl implements UserDtoService{

    @Override
    public UserDto get(User user) {
        UserDto userDto = new UserDto();
        userDto.setUserSeq(user.getUserSeq());
        userDto.setName(user.getName());
        userDto.setRole(user.getRole());
        userDto.setUserCode(user.getUserCode());
        userDto.setUserName(user.getUsername());
        userDto.setPhotoName(user.getPhotoName());
        userDto.setPhoneNumber(user.getPhoneNumber());
        return userDto;
    }

}
