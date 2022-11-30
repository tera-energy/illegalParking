package com.teraenergy.illegalparking.controller.myinfo;

import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.user.service.UserDtoService;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2022-10-02
 * Author : zilet
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Controller
public class MyInfoController extends ExtendsController {

    private final UserService userService;
    private final UserDtoService userDtoService;

    private String subTitle = "내정보";
    
    @GetMapping("/myInfo")
    public String myinfo(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);

        model.addAttribute("subTitle", subTitle);
        return getPath("/myInfo");
    }
}
