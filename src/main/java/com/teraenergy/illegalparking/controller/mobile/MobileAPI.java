package com.teraenergy.illegalparking.controller.mobile;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.user.domain.UserDto;
import com.teraenergy.illegalparking.model.dto.user.service.UserDtoService;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import com.teraenergy.illegalparking.model.entity.mycar.domain.MyCar;
import com.teraenergy.illegalparking.model.entity.mycar.service.MyCarService;
import com.teraenergy.illegalparking.model.entity.notice.domain.Notice;
import com.teraenergy.illegalparking.model.entity.notice.service.NoticeService;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import com.teraenergy.illegalparking.model.entity.pm.service.PmService;
import com.teraenergy.illegalparking.model.entity.point.enums.PointType;
import com.teraenergy.illegalparking.model.entity.product.domain.Product;
import com.teraenergy.illegalparking.model.entity.product.service.ProductService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.enums.Role;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.sms.Sms;
import com.teraenergy.illegalparking.lib.strategy.receipt.concrete.ProcessReceiptStrategy;
import com.teraenergy.illegalparking.lib.strategy.receipt.ReceiptStrategy;
import com.teraenergy.illegalparking.util.JsonUtil;
import com.teraenergy.illegalparking.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.mobile.device.Device;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Date : 2022-10-17
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class MobileAPI {

    private final UserService userService;
    private final UserDtoService userDtoService;
    private final ReceiptService receiptService;
    private final CalculateService calculateService;
    private final NoticeService noticeService;
    private final ProductService productService;
    private final IllegalEventService illegalEventService;
    private final ReportService reportService;
    private final LawDongService lawDongService;
    private final IllegalZoneMapperService illegalZoneMapperService;
    private final CommentService commentService;
    private final MyCarService myCarService;
    private final ParkingService parkingService;
    private final PmService pmService;

    /**
     * 사용자 로그인 하기
     */
    @PostMapping("/api/login")
    @ResponseBody
    public Object login(@RequestBody String body) throws TeraException {
        boolean result = false;
        UserDto userDto = null;
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String username = jsonNode.get("userName").asText();
        String password = jsonNode.get("password").asText();

        result = userService.isUserByUserNameAndPasswordMobile(username, password);

        if (result) {
            User user = userService.get(username);
            userDto = userDtoService.get(user);
        } else {
            throw new TeraException(TeraExceptionCode.USER_PASSWORD_IS_NOT_EXIST);
        }

        return userDto;
    }

    /**
     * 사용자 등록 하기
     */
    @PostMapping("/api/user/register")
    @ResponseBody
    public Object register(@RequestBody String body) throws TeraException {

        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String userName = jsonNode.get("userName").asText();
        String password = jsonNode.get("password").asText();
        String name = jsonNode.get("name").asText();
        String phoneNumber = jsonNode.get("phoneNumber").asText();
        String photoName = jsonNode.get("photoName").asText();

        User user = new User();
        user.setUsername(userName);
        user.setPassword(password);
        user.setName(name);
        user.setRole(Role.USER);
        user.setUserCode(1L);
        user.setPhoneNumber(phoneNumber.trim());
        user.setPhotoName(photoName);

        try {
            // 핸드폰 번호 체크
            if (userService.isUserByPhoneNumber(user.getPhoneNumber())) {
                throw new TeraException(TeraExceptionCode.USER_IS_EXIST);
            }

            // 사용자 아이디 체크
            if (userService.isUserByDuplicate(user.getUsername())) {
                throw new TeraException(TeraExceptionCode.USER_IS_EXIST);
            }

            userService.set(user);
            return "";
        } catch (TeraException e) {
            e.printStackTrace();
            if (e.getCode() == TeraExceptionCode.USER_IS_EXIST.getCode()) {
                throw new TeraException(TeraExceptionCode.USER_IS_EXIST);
            } else {
                throw new TeraException(TeraExceptionCode.USER_FAIL_RESiSTER);
            }
        }
    }

    /**
     * 사용자 존재 여부 확인하기
     */
    @PostMapping("/api/user/isExist")
    @ResponseBody
    public Object isExist(@RequestBody String body) throws TeraException {

        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        String userName = jsonNode.get("userName").asText();

        HashMap<String, Object> resultMap = Maps.newHashMap();

        try {
            if (userService.isUserByDuplicate(userName)) {
                resultMap.put("isExist", true);
                resultMap.put("msg", TeraExceptionCode.USER_IS_NOT_EXIST.getMessage());
            } else {
                resultMap.put("isExist", false);
            }
            return resultMap;
        } catch (TeraException e) {
            throw new TeraException(TeraExceptionCode.UNKNOWN);
        }
    }

    /**
     * 프로필 변경 하기
     */
    @PostMapping("/api/user/profile/change")
    @ResponseBody
    public Object changeProfile(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String photoName = jsonNode.get("photoName").asText();

            User user = userService.get(userSeq);
            user.setDecryptPassword();
            user.setPhotoName(photoName);

            userService.set(user);
            return "complete ... ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.USER_INSERT_FAIL);
        }
    }

    /**
     * 패스워드 체크하기
     */
    @PostMapping("/api/user/password/check")
    @ResponseBody
    public Object checkPassword(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String password = jsonNode.get("password").asText();

            User user = userService.get(userSeq);
            user.setDecryptPassword();
            if (user.getPassword().equals(password)) {
                return "complete .. ";
            } else {
                throw new TeraException(TeraExceptionCode.USER_FAIL_PASSWORD);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.USER_FAIL_PASSWORD);
        }
    }

    /**
     * 패스워드 변경 하기
     */
    @PostMapping("/api/user/password/change")
    @ResponseBody
    public Object changePassword(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String password = jsonNode.get("password").asText();

            User user = userService.get(userSeq);
            user.setPassword(password);
            userService.set(user);

            return "complete .. ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.USER_INSERT_FAIL);
        }

    }

    /**
     * 내 페이지 정보 가져오기
     */
    @PostMapping("/api/myPage/get")
    @ResponseBody
    public Object getMyPage(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();

            HashMap<String, Object> resultMap = Maps.newHashMap();

            List<MyCar> myCars = myCarService.gets(userSeq);
            if (myCars.size() > 0) {
                resultMap.put("carNum", myCars.get(0).getCarNum());
                resultMap.put("carLevel", myCars.get(0).getCarGrade());
                resultMap.put("carName", myCars.get(0).getCarName());
                resultMap.put("isAlarm", myCars.get(0).isAlarm());
            } else {
                resultMap.put("carNum", "");
                resultMap.put("carLevel", "");
                resultMap.put("carName", "");
                resultMap.put("isAlarm", false);
            }

            List<Receipt> receipts = receiptService.gets(userSeq);
            Calculate calculate = calculateService.getAtLast(userSeq);
            List<Notice> notices = noticeService.getsAtFive();

            List<Map<String, Object>> noticeMap = Lists.newArrayList();
            for (Notice notice : notices) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("noticeType", notice.getNoticeType().getValue());
                map.put("subject", notice.getSubject());
                map.put("content", notice.getContent());
                map.put("regDt", StringUtil.convertDatetimeToString(notice.getRegDt(), "yyyy-MM-dd HH:mm"));

                noticeMap.add(map);
            }

            resultMap.put("reportCount", receipts == null ? 0 : receipts.size());
            resultMap.put("currentPoint", calculate == null ? 0 : calculate.getCurrentPointValue());
            resultMap.put("notices", noticeMap);

            return resultMap;
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.MYPAGE_GET_FAIL);
        }
    }

    /**
     * 공지 사항 정보 리스트 가져오기
     */
    @PostMapping("/api/notice/gets")
    @ResponseBody
    public Object getsNotice(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            Integer offset = jsonNode.get("offset").asInt();
            Integer count = jsonNode.get("count").asInt();

            User user = userService.get(userSeq);

            if (user == null) {
                throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST);
            }

            List<Notice> notices = noticeService.getsAtFive(offset, count);

            List<Map<String, Object>> noticeMap = Lists.newArrayList();
            for (Notice notice : notices) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("noticeType", notice.getNoticeType().getValue());
                map.put("subject", notice.getSubject());
                map.put("content", notice.getContent());
                map.put("regDt", StringUtil.convertDatetimeToString(notice.getRegDt(), "yyyy-MM-dd HH:mm"));
                noticeMap.add(map);
            }
            return noticeMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.NOTICE_GET_FAIL);
        }
    }

    /**
     * 포인트 리스트 정보 가져오기
     */
    @PostMapping("/api/point/gets")
    @ResponseBody
    public Object getsPoint(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            List<Calculate> calculates = calculateService.getsByUser(userSeq);
            List<HashMap<String, Object>> resultMap = Lists.newArrayList();
            for (Calculate calculate : calculates) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("value", calculate.getEventPointValue());
                map.put("locationType", calculate.getLocationType() != null ? calculate.getLocationType().getValue() : null);
                map.put("productName", calculate.getProductName());
                map.put("pointType", calculate.getPointType());
                map.put("regDt", StringUtil.convertDatetimeToString(calculate.getRegDt(), "yyyy-MM-dd HH:mm"));
                resultMap.add(map);
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.POINT_GET_FAIL);
        }
    }

    /**
     * 제품 리스트 정보
     */
    @PostMapping("/api/product/gets")
    @ResponseBody
    public Object getsProduct(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();

            User user = userService.get(userSeq);
            if (user == null) {
                throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST);
            }

            List<Product> products = productService.gets();
            List<HashMap<String, Object>> resultMap = Lists.newArrayList();
            for (Product product : products) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("productSeq", product.getProductSeq());
                map.put("brandType", product.getBrand().getValue());
                map.put("productName", product.getName());
                map.put("pointValue", product.getPointValue());
                map.put("thumbnail", product.getThumbnail());

                resultMap.add(map);
            }
            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.POINT_GET_FAIL);
        }
    }

    /**
     * 제품 구매 신청 등록
     */
    @PostMapping("/api/calculate/set")
    @ResponseBody
    public Object setCalculate(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();

            User user = userService.get(userSeq);
            if (user == null) {
                throw new TeraException(TeraExceptionCode.USER_IS_NOT_EXIST);
            }

            Calculate calculate = new Calculate();
            Integer productSeq = jsonNode.get("productSeq").asInt();

            // 잔액 포인트 = 현재 포인트 - 사용 포인트
            long balancePointValue = jsonNode.get("balancePointValue").asLong();

            // 제품 가격 및 제품 명
            Product product = productService.get(productSeq);
            calculate.setEventPointValue(product.getPointValue());
            calculate.setProductName(product.getName());

            // 현재 가격
            calculate.setCurrentPointValue(balancePointValue);
            calculate.setRegDt(LocalDateTime.now());
            calculate.setUserSeq(userSeq);
            calculate.setPointType(PointType.MINUS);

            calculateService.set(calculate);
            return "complete ... ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.POINT_GET_FAIL);
        }
    }


    /**
     * 차량 알림 이력 정보
     */
    @PostMapping("/api/car/alarmHistory")
    @ResponseBody
    public Object alarmHistoryCar(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String carNum = jsonNode.get("carNum").asText();
            List<Receipt> receipts = receiptService.gets(carNum);
            List<HashMap<String, Object>> resultMap = Lists.newArrayList();

            for (Receipt receipt : receipts) {
                HashMap<String, Object> map = Maps.newHashMap();
                map.put("addr", receipt.getAddr());
                String timePattern = "yyyy-MM-dd HH:mm";
                if (receipt.getSecondRegDt() == null) {
                    map.put("regDt", StringUtil.convertDatetimeToString(receipt.getRegDt(), timePattern));
                } else {
                    map.put("regDt", StringUtil.convertDatetimeToString(receipt.getSecondRegDt(), timePattern));
                }
                map.put("stateType", receipt.getReceiptStateType().getValue());
                map.put("fileName", receipt.getFileName());
                resultMap.add(map);
            }

            return resultMap;
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.CAR_ALARM_HISTORY_GET_FAIL);
        }
    }

    /**
     * 차량 등록
     */
    @PostMapping("/api/car/set")
    @ResponseBody
    public Object setCar(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String carNum = jsonNode.get("carNum").asText();
            String carName = jsonNode.get("carName").asText();
            String carGrade = jsonNode.get("carGrade").asText();

            MyCar mycar = new MyCar();
            mycar.setUserSeq(userSeq);
            mycar.setCarName(carName);
            mycar.setCarNum(carNum);
            mycar.setCarGrade(carGrade);

            if (myCarService.isExist(carNum)) {
                throw new TeraException(TeraExceptionCode.CAR_EXIST);
            }

            mycar = myCarService.set(mycar);

            return "complete ... ";
        } catch (TeraException e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.valueOf(e.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.CAR_SET_FAIL);
        }

    }


    /**
     * 차량 알림 서비스 변경 등록
     */
    @PostMapping("/api/car/modify")
    @ResponseBody
    public Object modifyCar(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer userSeq = jsonNode.get("userSeq").asInt();
            String carNum = jsonNode.get("carNum").asText();
            boolean isAlarm = jsonNode.get("isAlarm").booleanValue();

            MyCar myCar = myCarService.get(userSeq, carNum);
            myCar.setIsAlarm(isAlarm);
            myCarService.set(myCar);
            return "complete ... ";
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.CAR_SET_FAIL);
        }
    }

    /**
     * 신고 접수
     */
    @PostMapping("/api/receipt/set")
    @ResponseBody
    public Object setReceipt(@RequestBody String body) throws TeraException {
        try {
            if ( body.length() == 0) {
                throw new TeraException(TeraExceptionCode.UNKNOWN);
            }

            ReceiptStrategy receiptStrategy = new ProcessReceiptStrategy(reportService, receiptService, commentService);

            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            double latitude = jsonNode.get("latitude").asDouble();      // 위도
            double longitude = jsonNode.get("longitude").asDouble();    // 경도
            String addr = jsonNode.get("addr").asText();
            String temp[] = addr.split(" ");

            String carNum = jsonNode.get("carNum").asText();    // 차량 번호
            String regDtStr = jsonNode.get("regDt").asText();   // 신고 시간 (문자)
            LocalDateTime regDt = StringUtil.convertStringToDateTime(regDtStr, "yyyy-MM-dd HH:mm:ss"); // 신고 시간 (LocalDateTime 변환)
            String fileName = jsonNode.get("fileName").asText();

            // 동 코드
            LawDong lawDong = lawDongService.getFromLnmadr(temp[0] + " " + temp[1] + " " + temp[2]);

            // 사용자
            User user = userService.get(jsonNode.get("userSeq").asInt());

            Receipt receipt;

            // 0. fileName 이 존재 하지 않음.
            if (fileName == null || fileName.length() == 0 ) {
                throw new TeraException(TeraExceptionCode.FILE_NOT_FOUND);
            }

            // 1. lawdong 코드 신고
            receiptStrategy.receiptNotCode(addr, carNum, fileName, regDt,  lawDong,  user);

            String code = lawDong.getCode();

            /** 불법 주정차 구역 ( mybatis 로 가져오기 때문에 illegal_event 데이터는 따로 요청 해야함) */
            IllegalZone illegalZone = illegalZoneMapperService.get(code, latitude, longitude);

            // 2. 불법 주정자 구역 신고 ( 불법 주정차 지역 인가? )
            receiptStrategy.receiptNotIllegalZone(addr,carNum, fileName, regDt, code, user, illegalZone);

            /** 불법 주정차 구역의 Event 설정 */
            IllegalEvent illegalEvent = illegalEventService.get(illegalZone.getEventSeq());
            illegalZone.setIllegalEvent(illegalEvent);

            // 3. 불법 주정차 허용 시간에 신고 - 첫번째 허용 시간 & 두번째 허용 시간
            receiptStrategy.receiptWithinAllowTime(addr, carNum, fileName, regDt, regDtStr, code, user, illegalZone);

            // 4. 중복 신고
            receiptStrategy.receiptDuplicate(addr, carNum, fileName, regDt, code, user, illegalZone, illegalEvent);

            String message;
            String state;

            // 신고 일 때 기본의 접수 확인 의 방식
            // * 기존 방식 : 신고 접수 일때 현재 시간 기준으로 11분 전까지 (또는 16분 전까지)
            // * TODO : 사장님 요청에 의한 변경 .... ( 확인 후 적용 )
            if ( !receiptService.isExist(user.getUserSeq(), carNum, regDt, code, illegalEvent.getIllegalType()) ) {
                // 5. 1차 신고

                receipt = new Receipt();
                receipt.setAddr(addr);
                receipt.setCarNum(carNum);
                receipt.setFileName(fileName);
                receipt.setRegDt(regDt);
                receipt.setCode(code);
                receipt.setUser(user);
                receipt.setIllegalZone(illegalZone);

                // 5.1. 신고 등록
                receiptStrategy.receiptForOCCUR(receipt);

                // 5.2 1차 신고 (신고등록) 결과 메시지 설정
                state = ReceiptStateType.OCCUR.getValue();
                message = "불법주정차 위반에 신고 되었습니다.\n 1분내로 차를 이동하여 주차하시길 바랍니다. ";
            } else {
                // 6. 2차 신고

                // 6.1 최초 신고 1분 (또는 5분) 후에 추가 신고가 된 경우 - 신고 시간이 지난 후 신고
                receiptStrategy.receiptPastTime(addr, carNum, fileName, regDt, code, user, illegalZone);

                // 6.2 최초 신고 1분 (또는 5분) 후에 추가 신고가 된 경우 - 최초신고 이후 1분 이후 10분이내 (또는 5분 이후 16분 이내) 신고
                receiptStrategy.receiptAddWhenOneAndEleven(addr, carNum, fileName, regDt, code, user, illegalZone);

                /** 11분 (15분)전 사이의 해당 차량 번호로 신고등록 정보 가져오기 */
                receipt = receiptService.getByCarNumAndBetweenNow(user.getUserSeq(), carNum, LocalDateTime.now(), illegalEvent);
                receipt.setSecondFileName(fileName);
                receipt.setSecondRegDt(regDt);

                // 6.3 허용 시간 1분 ( 또는 5분 ) 전 에 두분째 신고
                receiptStrategy.receiptBeforeIllegalZoneTypeOfAllowTime(carNum, regDt, code, receipt, user, illegalEvent);

                // 6.4 신고 접수
                receiptStrategy.receiptForReport(carNum, regDt, code, receipt, user, illegalEvent);

                // 6.5 2차 신고 (신고접수) 결과 설정
                state = ReceiptStateType.REPORT.getValue();
                message = "불법주정차 과태료 대상 접수되어 해당부서에서 검토중입니다. ";
            }

            // 7. 문자 전송
            MyCar car = myCarService.getByAlarm(carNum);
            if (car != null) {
                User carUser = userService.get(car.getUserSeq());
                String phoneNumber = carUser.getPhoneNumber();
                Sms.sendSMS(phoneNumber, message);
            }

            return state + "가(이) 등록 되었습니다.";
        } catch (TeraException e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.valueOf(e.getCode()));
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.UNKNOWN);
        }
    }

    /**
     * 신고 이력 정보 가져오기
     */
    @PostMapping("/api/receipt/gets")
    @ResponseBody
    public Object getsReceipt(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer userSeq = jsonNode.get("userSeq").asInt();
        List<Receipt> receipts = receiptService.gets(userSeq);

        List<HashMap<String, Object>> resutMap = Lists.newArrayList();
        String timePattern = "yyyy-MM-dd HH:mm";
        for (Receipt receipt : receipts) {
            HashMap<String, Object> map = Maps.newHashMap();
            map.put("carNum", receipt.getCarNum());
            map.put("addr", receipt.getAddr());
            map.put("firstRegDt", StringUtil.convertDatetimeToString(receipt.getRegDt(), timePattern));
            if (receipt.getSecondRegDt() != null) {
                map.put("secondRegDt", StringUtil.convertDatetimeToString(receipt.getSecondRegDt(), timePattern));
            }
            map.put("reportState", receipt.getReceiptStateType().getValue());
            map.put("firstFileName", receipt.getFileName());
            if (receipt.getSecondFileName() != null) {
                map.put("secondFileName", receipt.getSecondFileName());
            }
            List<Comment> comments = commentService.gets(receipt.getReceiptSeq());
            List<String> commentStrs = comments.stream().map(comment -> comment.getContent()).collect(Collectors.toList());
            map.put("comments", commentStrs);
            resutMap.add(map);
        }
        return resutMap;
    }

    @PostMapping("/api/parking/gets")
    @ResponseBody
    public Object getsParking(Device device, @RequestBody String body) throws TeraException {
        try {
            if (device.isMobile()) {
                JsonNode jsonNode = JsonUtil.toJsonNode(body);
                List<String> codes = com.google.common.collect.Lists.newArrayList();
                JsonNode codesArrNode = jsonNode.get("codes");
                if (codesArrNode.isArray()) {
                    for (JsonNode obj : codesArrNode) {
                        codes.add(obj.asText());
                    }
                }
                return parkingService.gets(codes);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.UNKNOWN);
        }
    }

    @PostMapping("/api/pm/gets")
    @ResponseBody
    public Object getsPm(Device device, @RequestBody String body) throws TeraException {
        try {
            if (device.isMobile()) {
                JsonNode jsonNode = JsonUtil.toJsonNode(body);
                List<String> codes = com.google.common.collect.Lists.newArrayList();
                JsonNode codesArrNode = jsonNode.get("codes");
                if (codesArrNode.isArray()) {
                    for (JsonNode obj : codesArrNode) {
                        codes.add(obj.asText());
                    }
                }
                return pmService.gets(codes);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new TeraException(TeraExceptionCode.UNKNOWN);
        }
    }

}
