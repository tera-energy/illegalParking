package com.teraenergy.illegalparking.controller.report;

import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.report.domain.ReceiptDto;
import com.teraenergy.illegalparking.model.dto.report.domain.ReportDto;
import com.teraenergy.illegalparking.model.dto.report.service.ReportDtoService;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptFilterColumn;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.util.CHashMap;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
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
public class ReportController extends ExtendsController {

    private final ReportDtoService reportDtoService;

    private String subTitle = "신고";

    @GetMapping(value = "/report")
    public RedirectView report() {
        return new RedirectView("/report/reportList");
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
        ReceiptFilterColumn filterColumn;
        if (filterColumnStr == null) {
            filterColumn = ReceiptFilterColumn.ADDR;
        } else {
            filterColumn = ReceiptFilterColumn.valueOf(filterColumnStr);
        }

        String search = "";
        String searchStr = paramMap.getAsString("searchStr");
        if (searchStr == null) {
            searchStr = "";
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

        Page<ReceiptDto> pages = reportDtoService.getsFromReceipt(pageNumber, pageSize, receiptStateType, filterColumn, search);

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
            if (totalPages > 3) isBeginOver = true;
        }

        model.addAttribute("offsetPage", offsetPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("isBeginOver", isBeginOver);
        model.addAttribute("isEndOver", isEndOver);
        model.addAttribute("receipts", pages.getContent());
        model.addAttribute("subTitle", subTitle);
        return getPath("/receiptList");
    }

    @GetMapping(value = "/report/reportList")
    public String reportList(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        CHashMap paramMap = requestUtil.getParameterMap();

        ReportStateType reportStateType = null;
        String stateTypeStr = paramMap.getAsString("reportStateType");
        if ( stateTypeStr != null && stateTypeStr.trim().length() > 0) {
            reportStateType = ReportStateType.valueOf(stateTypeStr);
        }

        String filterColumnStr = paramMap.getAsString("filterColumn");
        ReportFilterColumn filterColumn;
        if (filterColumnStr == null) {
            filterColumn = ReportFilterColumn.ADDR;
        } else {
            filterColumn = ReportFilterColumn.valueOf(filterColumnStr);
        }

        String search = "";
        String searchStr = paramMap.getAsString("searchStr");
        if (searchStr == null) {
            searchStr = "";
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

        Page<ReportDto> pages = reportDtoService.getsFromReport(pageNumber, pageSize, reportStateType, filterColumn, search);

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
            if (totalPages > 3) isBeginOver = true;
        }

        model.addAttribute("offsetPage", offsetPage);
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("isBeginOver", isBeginOver);
        model.addAttribute("isEndOver", isEndOver);
        model.addAttribute("reports", pages.getContent());
        model.addAttribute("subTitle", subTitle);
        return getPath("/reportList");
    }

}
