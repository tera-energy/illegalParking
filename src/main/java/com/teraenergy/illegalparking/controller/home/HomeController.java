package com.teraenergy.illegalparking.controller.home;

import com.teraenergy.illegalparking.controller.ExtendsController;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.illegalGroup.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.model.entity.userGroup.domain.UserGroup;
import com.teraenergy.illegalparking.model.entity.userGroup.service.UserGroupService;
import com.teraenergy.illegalparking.util.RequestUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.mobile.device.Device;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
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
public class HomeController extends ExtendsController {

    private final UserService userService;
    private String subTitle = "불법 주정차";

    private final ReportService reportService;

    private final ReceiptService receiptService;

    private final IllegalZoneService illegalZoneService;

    private final IllegalGroupServcie illegalGroupServcie;
    private final UserGroupService userGroupService;

    @RequestMapping("/")
    public RedirectView home_(Device device) throws TeraException {
        if (device.isMobile()) {
            return new RedirectView("/area/map");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.get(auth.getName());
        switch (user.getRole()) {
            case ADMIN:
                return new RedirectView("/notice/noticeList");
            case GOVERNMENT:
            default:
                return new RedirectView("/home");
        }
    }

    @RequestMapping("/home")
    public String home(Model model, HttpServletRequest request) throws TeraException {
        RequestUtil requestUtil = new RequestUtil(request);
        requestUtil.setParameterToModel(model);
        model.addAttribute("subTitle", subTitle);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.get(auth.getName());
        switch (user.getRole()) {
            case ADMIN:
                return "/";
            case GOVERNMENT:
            default:
                List<UserGroup> userGroups = userGroupService.getsByUser(user.getUserSeq());
                List<Integer> groupSeqs = userGroups.stream().map(userGroup -> userGroup.getGroupSeq()).collect(Collectors.toList());
                List<IllegalZone> illegalZones = illegalZoneService.gets(groupSeqs);

                int totalCount = reportService.getSizeForReport(illegalZones);
                int completeCount = reportService.getSizeForCOMPLETE(illegalZones);
                int exceptionCount = reportService.getSizeForException(illegalZones);
                int penaltyCount = reportService.getSizeForPenalty(illegalZones);

                List<Integer> reportCounts = Lists.newArrayList();
                List<Integer> receiptCounts = Lists.newArrayList();

                int year = LocalDateTime.now().getYear();
                for (int i=1; i<=12; i++) {
                    reportCounts.add(reportService.getReportCountByMonth(year, i));
                    receiptCounts.add(receiptService.getReceiptCountByMonth(year, i));
                }

                model.addAttribute("officeName", user.getGovernMentOffice().getName());
                model.addAttribute("totalCount", totalCount);// 년 전체 건수
                model.addAttribute("sendPenaltyCount", (penaltyCount + exceptionCount ));// 기관에 전송한 건수
                model.addAttribute("completeCount", completeCount); // 대기 건수
                model.addAttribute("exceptionCount", exceptionCount); // 미처리 건수
                model.addAttribute("penaltyCount", penaltyCount); // 처리 건수

                model.addAttribute("reportCounts", reportCounts);
                model.addAttribute("receiptCounts", receiptCounts);
                return getPath("/home");
        }
    }

}
