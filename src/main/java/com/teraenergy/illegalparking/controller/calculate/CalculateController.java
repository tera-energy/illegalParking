package com.teraenergy.illegalparking.controller.calculate;

import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.product.domain.Product;
import com.teraenergy.illegalparking.model.entity.calculate.enums.CalculateFilterColumn;
import com.teraenergy.illegalparking.model.entity.calculate.enums.CalculateOrderColumn;
import com.teraenergy.illegalparking.model.entity.product.enums.ProductFilterColumn;
import com.teraenergy.illegalparking.model.entity.product.enums.ProductOrderColumn;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.product.service.ProductService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.CHashMap;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class CalculateController extends ExtendsController {

    private final CalculateService calculateService;

    private final ProductService productService;

    private final UserService userService;

    private String subTitle = "결재";
    private String subTitleByProduct = "상품";

    @RequestMapping("/calculate")
    public RedirectView calculate() {
        return new RedirectView("/calculate/calculateList");
    }

    @GetMapping("/calculate/calculateList")
    public String calculateList(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap parameterMap = requestUtil.getParameterMap();

        Integer pageNumber = parameterMap.getAsInt("pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
            model.addAttribute("pageNumber", pageNumber);
        }

        String filterColumnStr = parameterMap.getAsString("filterColumn");
        CalculateFilterColumn filterColumn;
        if (filterColumnStr == null) {
            filterColumn = CalculateFilterColumn.user;
        } else {
            filterColumn = CalculateFilterColumn.valueOf(filterColumnStr);
        }

        String searchStr = parameterMap.getAsString("searchStr");
        if (searchStr == null) {
            searchStr = "";
        }

        Integer pageSize = parameterMap.getAsInt("pageSize");
        if (pageSize == null) {
            pageSize = 10;
        }

        Page<Calculate> pages = calculateService.gets(pageNumber, pageSize, filterColumn, searchStr);

        List<HashMap<String, Object>> calculates = Lists.newArrayList();

        for(Calculate calculate : pages.getContent()) {
            HashMap<String, Object> map = Maps.newHashMap();

            User user = userService.get(calculate.getUserSeq());

            map.put("calculateSeq", calculate.getCalculateSeq());
            map.put("userName", user.getName());
            map.put("currentPointValue", calculate.getCurrentPointValue());
            map.put("eventPointValue", calculate.getEventPointValue());
            map.put("locationType", calculate.getLocationType());
            map.put("pointType", calculate.getPointType());
            map.put("productName", calculate.getProductName());
            map.put("regDt", calculate.getRegDt());

            calculates.add(map);
        }

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
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("isBeginOver", isBeginOver);
        model.addAttribute("isEndOver", isEndOver);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("calculates", calculates);
        model.addAttribute("subTitle", subTitle);
        return getPath("/calculateList");
    }

    @GetMapping("/calculate/productList")
    public String productList(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap parameterMap = requestUtil.getParameterMap();

        Integer pageNumber = parameterMap.getAsInt("pageNumber");
        if (pageNumber == null) {
            pageNumber = 1;
            model.addAttribute("pageNumber", pageNumber);
        }

        String filterColumnStr = parameterMap.getAsString("filterColumn");
        ProductFilterColumn filterColumn;
        if (filterColumnStr == null) {
            filterColumn = ProductFilterColumn.name;
        } else {
            filterColumn = ProductFilterColumn.valueOf(filterColumnStr);
        }

        String searchStr = parameterMap.getAsString("searchStr");
        String searchStr2 = parameterMap.getAsString("searchStr2");
        String search;
        if (filterColumn.equals(ProductFilterColumn.brand)) {
            search = searchStr2;
        } else {
            if (searchStr == null ) {
                search = "";
            } else {
                search = searchStr.trim();
            }
        }

        Integer pageSize = parameterMap.getAsInt("pageSize");
        if (pageSize == null) {
            pageSize = 10;
        }

        Page<Product> pages = productService.gets(pageNumber, pageSize, filterColumn, search);

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
        model.addAttribute("products", pages.getContent());
        model.addAttribute("subTitle",subTitleByProduct);
        return getPath("/productList");
    }

    @GetMapping("/calculate/productAdd")
    public String productAdd(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);

        model.addAttribute("subTitle", subTitleByProduct);
        return getPath("/productAdd");
    }

}
