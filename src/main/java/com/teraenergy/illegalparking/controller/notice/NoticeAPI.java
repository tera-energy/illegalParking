package com.teraenergy.illegalparking.controller.notice;

import com.fasterxml.jackson.databind.JsonNode;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.entity.notice.domain.Notice;
import com.teraenergy.illegalparking.model.entity.notice.enums.NoticeType;
import com.teraenergy.illegalparking.model.entity.notice.service.NoticeService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;

/**
 * Date : 2022-10-17
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class NoticeAPI {

    private final UserService userService;
    private final NoticeService noticeService;

    @PostMapping("/notice/get")
    @ResponseBody
    public Object get(@RequestBody String body) throws TeraException {

        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer noticeSeq = jsonNode.get("noticeSeq").asInt();
            return noticeService.get(noticeSeq);
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.NOTICE_GET_FAIL);
        }
    }

    @PostMapping("/notice/set")
    @ResponseBody
    public Object set(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);

            Integer userSeq = jsonNode.get("userSeq").asInt();
            User user = userService.get(userSeq);

            Integer noticeSeq = jsonNode.get("noticeSeq").asInt();

            Notice notice = new Notice();
            if ( noticeSeq != null) notice.setNoticeSeq(noticeSeq);
            notice.setUser(user);
            notice.setNoticeType(NoticeType.valueOf(jsonNode.get("noticeType").asText()));
            notice.setSubject(jsonNode.get("subject").asText());
            notice.setContent(jsonNode.get("content").asText());
            notice.setHtml(jsonNode.get("html").asText());
            notice.setRegDt(LocalDateTime.now());

            notice = noticeService.set(notice);
            return "complete ... ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.NOTICE_SET_FAIL);
        }

    }

    @PostMapping("/notice/remove")
    @ResponseBody
    public Object remove(@RequestBody String body) throws  TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer noticeSeq = jsonNode.get("noticeSeq").asInt();
            Notice notice = noticeService.get(noticeSeq);
            notice.setDel(true);
            notice.setDelDt(LocalDateTime.now());
            return noticeService.set(notice);
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.NOTICE_REMOVE_FAIL, e);
        }
    }
}
