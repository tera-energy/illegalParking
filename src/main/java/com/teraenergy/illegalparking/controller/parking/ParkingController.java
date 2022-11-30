package com.teraenergy.illegalparking.controller.parking;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.enums.ParkingFilterColumn;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import com.teraenergy.illegalparking.util.CHashMap;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Controller
public class ParkingController extends ExtendsController {

    private final ObjectMapper objectMapper;

    private final ParkingService parkingService;

    private String subTitle = "공영주차장";

    @Value("${addr-api.key}")
    String addrKey;


    @GetMapping("/parking")
    public RedirectView parking(){
        return new RedirectView("/parking/map");
    }

    @GetMapping("/parking/map")
    public ModelAndView map(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getPath("/map"));
        modelAndView.addObject("subTitle", subTitle);
        return modelAndView;
    }

    @GetMapping("/parking/parkingList")
    public String parkingList(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap paramMap = requestUtil.getParameterMap();

        Integer pageNumber = paramMap.getAsInt("pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
            model.addAttribute("pageNumber", pageNumber);
        }

        String filterColumnStr = paramMap.getAsString("filterColumn");
        ParkingFilterColumn filterColumn;
        if(filterColumnStr == null) {
            filterColumn = ParkingFilterColumn.parkingchrgeInfo;
        } else  {
            filterColumn = ParkingFilterColumn.valueOf(filterColumnStr);
        }

        String searchStr = paramMap.getAsString("searchStr");
        if (searchStr == null) {
            searchStr = "";
        }

        Integer pageSize = paramMap.getAsInt("pageSize");
        if ( pageSize == null) {
            pageSize = 10;
        }

        Page<Parking> pages = parkingService.gets(pageNumber, pageSize, filterColumn, searchStr);

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
        model.addAttribute("parkings", pages.getContent());
        model.addAttribute("subTitle", subTitle);

        return getPath("/parkingList");
    }

    @GetMapping("/parking/parkingAdd")
    public String parkingAdd(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);

        model.addAttribute("subTitle", subTitle);

        return getPath("/parkingAdd");
    }

    @GetMapping("/api/parking/jusoPopup")
    public String jusoPopup(Model model) {
        model.addAttribute("key", addrKey);
        return getPath("/jusoPopup");
    }

}
