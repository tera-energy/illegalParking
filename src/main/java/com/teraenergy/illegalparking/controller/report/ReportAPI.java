package com.teraenergy.illegalparking.controller.report;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.report.service.ReportDtoService;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.point.enums.PointType;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReplyType;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.model.entity.userGroup.domain.UserGroup;
import com.teraenergy.illegalparking.model.entity.userGroup.service.UserGroupService;
import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class ReportAPI {

    private final IllegalZoneService illegalZoneService;
    private final UserGroupService userGroupService;
    private final ReportService reportService;
    private final ReportDtoService reportDtoService;

    // 신고 접수 정보 등록
    @PostMapping("/report/set")
    @ResponseBody
    public Object setReport(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer reportSeq = jsonNode.get("reportSeq").asInt();
            String note = jsonNode.get("note").asText();
            ReportStateType reportStateType = ReportStateType.valueOf(jsonNode.get("reportStateType").asText());
            Integer userSeq = jsonNode.get("userSeq").asInt();
            reportService.modifyByGovernmentOffice(reportSeq, userSeq, reportStateType, note);
            return "complete ... ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.REPORT_REGISTER_FAIL);
        }
    }

    // 신고 접수 정보 가져오기
    @PostMapping("/report/get")
    @ResponseBody
    public Object getReport(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer reportSeq = jsonNode.get("reportSeq").asInt();
        return reportDtoService.getFromReportDetailDto(reportSeq);
    }

    // 신고 등록 정보 가져오기
    @PostMapping("/report/receipt/get")
    @ResponseBody
    public Object getReceipt(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer receiptSeq = jsonNode.get("receiptSeq").asInt();
        return reportDtoService.getFromReceiptDetailDto(receiptSeq);
    }

    @PostMapping("/report/statics/get")
    @ResponseBody
    public Object getStatics(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer userSeq = jsonNode.get("userSeq").asInt();
        List<UserGroup> userGroups = userGroupService.getsByUser(userSeq);
        List<Integer> groupSeqs = userGroups.stream().map(userGroup -> userGroup.getGroupSeq()).collect(Collectors.toList());
        List<IllegalZone> illegalZones = illegalZoneService.gets(groupSeqs);

        Map<String, Integer> resultMap = Maps.newHashMap();
        resultMap.put("totalCount", reportService.getSizeForReport(illegalZones));    // total Size
        resultMap.put("completeCount", reportService.getSizeForCOMPLETE(illegalZones));  // complete Size
        resultMap.put("exceptionCount", reportService.getSizeForException(illegalZones)); //
        resultMap.put("penaltyCount", reportService.getSizeForPenalty(illegalZones));   // penalty Size
        return resultMap;
    }

}
