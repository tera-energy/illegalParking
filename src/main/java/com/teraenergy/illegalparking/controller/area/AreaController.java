package com.teraenergy.illegalparking.controller.area;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.illegalzone.domain.IllegalGroupDto;
import com.teraenergy.illegalparking.model.dto.illegalzone.service.IllegalGroupDtoService;
import com.teraenergy.illegalparking.model.entity.illegalGroup.enums.GroupFilterColumn;
import com.teraenergy.illegalparking.util.CHashMap;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.io.ParseException;
import org.springframework.data.domain.Page;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2022-03-02
 * Author : young
 * Project : sbAdmin
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class AreaController extends ExtendsController {

    private final IllegalGroupDtoService illegalGroupDtoService;

    private final ObjectMapper objectMapper;

    private String subTitle = "불법주정차 구역";

    @GetMapping("/area")
    public RedirectView area(){
        return new RedirectView("/area/map");
    }

    @GetMapping("/area/mapSet")
    public String mapSet(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        model.addAttribute("subTitle", subTitle);
        return getPath("/mapSet");
    }

    @GetMapping("/area/groupList")
    public String groupList(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap paramMap = requestUtil.getParameterMap();

        String filterColumnStr = paramMap.getAsString("filterColumn");
        GroupFilterColumn filterColumn;
        if (filterColumnStr == null) {
            filterColumn = GroupFilterColumn.NAME;
        } else {
            filterColumn = GroupFilterColumn.valueOf(filterColumnStr);
        }

        String search = "";
        switch (filterColumn) {
            case LOCATION:
                search = paramMap.getAsString("searchStr2");
                break;
            case NAME:
                search = paramMap.getAsString("searchStr");
                if (search == null) {
                    search = "";
                }
                break;
        }

        Integer pageNumber = paramMap.getAsInt("pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
            model.addAttribute("pageNumber", pageNumber);
        }

        Integer pageSize = paramMap.getAsInt("pageSize");
        if (pageSize == null) {
            pageSize = 10;
            model.addAttribute("pageSize", pageSize);
        }

        Page<IllegalGroupDto> pages = illegalGroupDtoService.gets(pageNumber, pageSize, filterColumn, search);

        boolean isBeginOver = false;
        boolean isEndOver = false;

        int totalPages = pages.getTotalPages();

        int offsetPage = pageNumber - 1;

        if (offsetPage >= (totalPages-2)) {
            offsetPage = totalPages-2;
        } else {
            if (totalPages > 3) isEndOver = true;
        }

        if ( offsetPage < 1) {
            offsetPage = 1;
        } else {
            if (offsetPage > 1 && totalPages > 3) isBeginOver = true;
        }

        model.addAttribute("offsetPage", offsetPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("isBeginOver", isBeginOver);
        model.addAttribute("isEndOver", isEndOver);
        model.addAttribute("illegalGroupDtos", pages.getContent());
        model.addAttribute("subTitle", subTitle);
        return getPath("/groupList");
    }



    @GetMapping("/area/map")
    public ModelAndView mapByDesktop(Device device) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("subTitle", subTitle);
        if ( device.isMobile() || device.isTablet()) {
            return null;
        } else {
            modelAndView.setViewName(getPath("/map"));
        }
        return modelAndView;
    }

    @GetMapping("/api/area/map")
    public ModelAndView mapByMobile(Device device) throws ParseException {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("subTitle", subTitle);
        if ( device.isMobile() || device.isTablet()) {
            modelAndView.setViewName(getMobilePath("/map"));
        } else {
            modelAndView.setViewName(getPath("/map"));
        }
        return modelAndView;
    }

}
