package com.teraenergy.illegalparking.controller.user;

import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.lib.strategy.page.PageStrategy;
import com.teraenergy.illegalparking.model.dto.user.domain.UserGovernmentDto;
import com.teraenergy.illegalparking.model.dto.user.enums.UserGovernmentFilterColumn;
import com.teraenergy.illegalparking.model.dto.user.service.UserGovernmentDtoService;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.CHashMap;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2022-10-06
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Controller
public class UserController extends ExtendsController {

    private final UserService userService;

    private final UserGovernmentDtoService userGovernmentDtoService;

    private final PageStrategy pageStrategy;

    private String subTitle = "사용자";

    @GetMapping("/user/userList")
    public String user(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap paramMap = requestUtil.getParameterMap();

        ReceiptStateType receiptStateType = null;

        String filterColumnStr = paramMap.getAsString("filterColumn");
        UserGovernmentFilterColumn filterColumn;
        if (filterColumnStr == null) {
            filterColumn = UserGovernmentFilterColumn.OFFICE_NAME;
        } else {
            filterColumn = UserGovernmentFilterColumn.valueOf(filterColumnStr);
        }

        Integer pageNumber = paramMap.getAsInt("pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
        }

        Integer pageSize = paramMap.getAsInt("pageSize");
        if (pageSize == null) {
            pageSize = 10;
        }

        String search = "";
        switch (filterColumn) {
            case LOCATION:
                search = paramMap.getAsString("searchStr2");
                break;
            case OFFICE_NAME:
                String searchStr = paramMap.getAsString("searchStr");
                if ( searchStr != null) search = searchStr;
                break;
        }

        Page<UserGovernmentDto> pages = userGovernmentDtoService.gets(pageNumber, pageSize, filterColumn, search);

        int totalPages = pages.getTotalPages();
        pageStrategy.setModelForPageTag(totalPages, pageNumber, pageSize, model);

        model.addAttribute("userGovernmentDtos", pages.getContent());
        model.addAttribute("subTitle", subTitle);
        return getPath("/userList");
    }



}
