package com.teraenergy.illegalparking.controller.report;

import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.lib.strategy.page.PageStrategy;
import com.teraenergy.illegalparking.model.dto.report.domain.ReceiptDto;
import com.teraenergy.illegalparking.model.dto.report.domain.ReportDto;
import com.teraenergy.illegalparking.model.dto.report.service.ReportDtoService;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.IllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalGroup.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptFilterColumn;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.model.entity.userGroup.domain.UserGroup;
import com.teraenergy.illegalparking.model.entity.userGroup.service.UserGroupService;
import com.teraenergy.illegalparking.util.CHashMap;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class ReportController extends ExtendsController {

    private final ReportDtoService reportDtoService;

    private final UserGroupService userGroupService;

    private final IllegalZoneService illegalZoneService;

    private final UserService userService;
    private final PageStrategy pageStrategy;

    private String subTitle = "신고";

    @GetMapping(value = "/report")
    public RedirectView report() {
        return new RedirectView("/report/reportList");
    }

    @GetMapping(value = "/reportByGovernment")
    public RedirectView reportByGovernment() {
        return new RedirectView("/report/reportListByGovernment");
    }

    @GetMapping(value = "/report/receiptList")
    public String receiptList(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap paramMap = requestUtil.getParameterMap();

        ReceiptStateType receiptStateType = null;
        String stateTypeStr = paramMap.getAsString("receiptStateType");
        if ( stateTypeStr != null && stateTypeStr.trim().length() > 0) {
            receiptStateType = ReceiptStateType.valueOf(stateTypeStr);
        }

        String filterColumnStr = paramMap.getAsString("filterColumn");
        ReceiptFilterColumn filterColumn = (filterColumnStr == null ? ReceiptFilterColumn.ADDR : ReceiptFilterColumn.valueOf(filterColumnStr));

        String search = paramMap.getAsString("searchStr");
        search = ( search == null ? "" : search);

        Integer pageNumber = paramMap.getAsInt("pageNumber");
        pageNumber = ( pageNumber == null ? 1 : pageNumber );

        Integer pageSize = paramMap.getAsInt("pageSize");
        pageSize = ( pageSize == null ? 10 : pageSize );

        Page<ReceiptDto> pages = reportDtoService.getsFromReceipt(pageNumber, pageSize, receiptStateType, filterColumn, search);
        int totalPages = pages.getTotalPages();
        pageStrategy.setModelForPageTag(totalPages, pageNumber, pageSize, model);

        model.addAttribute("receipts", pages.getContent());
        model.addAttribute("subTitle", subTitle);
        return getPath("/receiptList");
    }

    @GetMapping(value = "/report/reportList")
    public String reportList(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap paramMap = requestUtil.getParameterMap();

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.get(auth.getName());

        List<Integer> zoneSeqs = null;
        if ( user.getRole() != Role.ADMIN) {
            List<UserGroup> userGroups = userGroupService.getsByUser(user.getUserSeq());
            List<Integer> groupSeqs = userGroups.stream().map(userGroup -> userGroup.getGroupSeq()).collect(Collectors.toList());

            List<IllegalZone> illegalZones = illegalZoneService.gets(groupSeqs);
            zoneSeqs = illegalZones.stream().map(illegalZone -> illegalZone.getZoneSeq()).collect(Collectors.toList());
        }

        ReportStateType reportStateType = null;
        String stateTypeStr = paramMap.getAsString("reportStateType");
        if ( stateTypeStr != null && stateTypeStr.trim().length() > 0) {
            reportStateType = ReportStateType.valueOf(stateTypeStr);
        }

        String filterColumnStr = paramMap.getAsString("filterColumn");
        ReportFilterColumn filterColumn = ( filterColumnStr == null ? ReportFilterColumn.ADDR : ReportFilterColumn.valueOf(filterColumnStr) );

        String search = paramMap.getAsString("searchStr");
        search = ( search == null ? "" : search);

        Integer pageNumber = paramMap.getAsInt("pageNumber");
        pageNumber = ( pageNumber == null ? 1 : pageNumber );

        Integer pageSize = paramMap.getAsInt("pageSize");
        pageSize = ( pageSize == null ? 10 : pageSize );

        Page<ReportDto> pages = reportDtoService.getsFromReport(pageNumber, pageSize, reportStateType, filterColumn, search, zoneSeqs);
        int totalPages = pages.getTotalPages();
        pageStrategy.setModelForPageTag(totalPages, pageNumber, pageSize, model);

        model.addAttribute("reports", pages.getContent());
        model.addAttribute("subTitle", subTitle);
        return getPath("/reportList");
    }

    @GetMapping(value = "/report/reportListByGovernment")
    public String reportListByGovernment(Model model, HttpServletRequest request) throws TeraException{
        reportList(model, request);
        return getPath("/reportListByGovernment");
    }
}
