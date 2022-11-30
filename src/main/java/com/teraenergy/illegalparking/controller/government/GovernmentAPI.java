package com.teraenergy.illegalparking.controller.government;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.JsonUtil;
import com.teraenergy.illegalparking.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Date : 2022-11-02
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class GovernmentAPI {

    private final UserService userService;
    private final ReportService reportService;

    /**
     * 공공 기관의 신고 정보 요청 데이터 ~~~~
     * 언제 부터 (startDate) ~ 언제 까지 (stopDate)
     * id :
     * pw :
     * sTime :
     * eTime :
     */
    @PostMapping("/api/report/gets")
    @ResponseBody
    public Object getReports(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String userName = jsonNode.get("id").asText();
        String password = jsonNode.get("password").asText();

        LocalDateTime startDateTime = StringUtil.convertStringToDateTime(jsonNode.get("sTime").asText(), "yyyy-MM-dd HH:mm");
        LocalDateTime endDateTime = StringUtil.convertStringToDateTime(jsonNode.get("eTime").asText(), "yyyy-MM-dd HH:mm");

        User user = userService.getByGovernmentOffice(userName, password);

        List<Map<String, Object>> resultMap = Lists.newArrayList();

        if (user == null) {
            throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST);
        }

        List<Report> reports = reportService.getByGovernmentOffice(user.getUserSeq(), startDateTime, endDateTime);

        for (Report report : reports) {
            Map<String, Object> map = Maps.newHashMap();
            Receipt receipt = report.getReceipt();
            map.put("userName", receipt.getUser().getName());
            map.put("carNum", receipt.getCarNum());
            map.put("firstFileName", receipt.getFileName());
            map.put("secondFileName", receipt.getSecondFileName());
            map.put("regDt", report.getRegDt());
            map.put("secondRegDt", receipt.getSecondRegDt());
            map.put("firstRegDt", receipt.getRegDt());
            map.put("addr", receipt.getAddr());
            map.put("illegalType", receipt.getIllegalZone().getIllegalEvent().getIllegalType().getValue());
            map.put("reportState", report.getReportStateType());

            resultMap.add(map);
        }

        if ( resultMap.size() == 0 ) {

        }

        return resultMap;
    }
}
