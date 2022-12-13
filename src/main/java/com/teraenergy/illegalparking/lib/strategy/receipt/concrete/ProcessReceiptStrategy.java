package com.teraenergy.illegalparking.lib.strategy.receipt.concrete;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.lib.strategy.receipt.ReceiptStrategy;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.util.StringUtil;

import java.time.LocalDateTime;

/**
 * Date : 2022-12-06
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */


public class ProcessReceiptStrategy implements ReceiptStrategy {

    private ReportService reportService;
    private ReceiptService receiptService;
    private CommentService commentService;

    public ProcessReceiptStrategy(ReportService reportService, ReceiptService receiptService, CommentService commentService) {
        this.reportService = reportService;
        this.receiptService = receiptService;
        this.commentService = commentService;
    }

    private Receipt setReceipt(String addr, String carNum, String fileName, LocalDateTime regDt, String code, ReceiptStateType stateType, User user, IllegalZone illegalZone) {
        Receipt receipt = new Receipt();
        receipt.setAddr(addr);
        receipt.setCarNum(carNum);
        receipt.setFileName(fileName);
        receipt.setRegDt(regDt);
        receipt.setCode(code);
        receipt.setReceiptStateType(stateType);
        receipt.setUser(user);
        receipt.setIllegalZone(illegalZone);
        receiptService.set(receipt);

        return receipt;
    }

    private void setComment(Integer receiptSeq, String content) {
        Comment comment = new Comment();
        comment.setReceiptSeq(receiptSeq);
        comment.setRegDt(LocalDateTime.now());
        comment.setContent(content);
        commentService.set(comment);
    }

    public void removeComment(Integer receiptSeq) {
        Comment comment = commentService.getByOneMinute(receiptSeq);
        if (comment == null) {
            return;
        }
        comment.setDel(true);
        comment.setDelDt(LocalDateTime.now());
        commentService.set(comment);
    }

    @Override
    public void receiptNotCode(String addr, String carNum, String fileName, LocalDateTime regDt, LawDong lawDong, User user) throws TeraException {
        if (lawDong == null) {
            Receipt receipt = setReceipt(addr, carNum, fileName, regDt, "", ReceiptStateType.EXCEPTION, user, null);
            setComment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_NOT_AREA.getMessage());
            throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_NOT_AREA);
        }
    }

    @Override
    public void receiptNotIllegalZone(String addr, String carNum, String fileName, LocalDateTime regDt, String code, User user, IllegalZone illegalZone) throws TeraException {
        if ( illegalZone == null) {
            Receipt receipt = setReceipt(addr, carNum, fileName, regDt, code, ReceiptStateType.EXCEPTION, user, illegalZone);
            setComment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_NOT_AREA.getMessage());
            throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_NOT_AREA);
        }
    }

    @Override
    public void receiptPastTime(String addr, String carNum, String fileName, LocalDateTime regDt, String code, User user, IllegalZone illegalZone) throws TeraException {
        Receipt receipt = null;

        switch (illegalZone.getIllegalEvent().getIllegalType()) {
            case ILLEGAL:
                if (regDt.plusMinutes(11).isBefore(LocalDateTime.now())) {
                    receipt = setReceipt(addr, carNum, fileName, regDt,  code, ReceiptStateType.FORGET, user, illegalZone);
                    receiptService.set(receipt);
                    setComment(receipt.getReceiptSeq(), TeraExceptionCode.REPORT_OVER_TIME.getMessage());
                    throw new TeraException(TeraExceptionCode.REPORT_OVER_TIME);
                }
            case FIVE_MINUTE:
                if (regDt.plusMinutes(16).isBefore(LocalDateTime.now())) {
                    receipt = setReceipt(addr, carNum, fileName, regDt, code, ReceiptStateType.FORGET, user, illegalZone);
                    setComment(receipt.getReceiptSeq(), TeraExceptionCode.REPORT_OVER_TIME.getMessage());
                    throw new TeraException(TeraExceptionCode.REPORT_OVER_TIME);
                }
        }
    }

    @Override
    public void receiptAddWhenOneAndEleven(String addr, String carNum, String fileName, LocalDateTime regDt, String code, User user, IllegalZone illegalZone) throws TeraException {
        Receipt receipt = receiptService.getByLastOccur(user.getUserSeq(), carNum, regDt, illegalZone.getIllegalEvent().getIllegalType());
        if (receipt != null) {
            receipt.setReceiptStateType(ReceiptStateType.NOTHING);
            receiptService.set(receipt);
            setComment(receipt.getReceiptSeq(), TeraExceptionCode.REPORT_OVER_TIME.getMessage());
            throw new TeraException(TeraExceptionCode.REPORT_OVER_TIME);
        }
    }

    @Override
    public void receiptWithinAllowTime(String addr, String carNum, String fileName, LocalDateTime regDt, String regDtStr, String code, User user, IllegalZone illegalZone) throws TeraException {
        String dateStr = regDtStr.split(" ")[0];
        Receipt receipt;

        // 첫번째 시간 체크
        if (!illegalZone.getIllegalEvent().isUsedFirst()) {
            LocalDateTime fs = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getFirstStartTime(), "yyyy-MM-dd HH:mm");
            LocalDateTime fe = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getFirstEndTime(), "yyyy-MM-dd HH:mm");
            if (fs.isBefore(regDt) && fe.isAfter(regDt)) {
                receipt = setReceipt(addr, carNum, fileName, regDt, code, ReceiptStateType.EXCEPTION, user, illegalZone);
                receipt = receiptService.set(receipt);
                setComment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME.getMessage());
                throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME);
            }
        }

        // 두번째 시간 체크
        if (!illegalZone.getIllegalEvent().isUsedSecond()) {
            LocalDateTime ss = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getSecondStartTime(), "yyyy-MM-dd HH:mm");
            LocalDateTime se = StringUtil.convertStringToDateTime(dateStr + " " + illegalZone.getIllegalEvent().getSecondEndTime(), "yyyy-MM-dd HH:mm");

            if (ss.isBefore(regDt) && se.isAfter(regDt)) {
                receipt = setReceipt(addr, carNum, fileName, regDt, code, ReceiptStateType.EXCEPTION, user, illegalZone);
                receipt = receiptService.set(receipt);
                setComment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME.getMessage());
                throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_NOT_CRACKDOWN_TIME);
            }
        }
    }

    @Override
    public void receiptBeforeIllegalZoneTypeOfAllowTime(String carNum, LocalDateTime regDt, String code, Receipt receipt, User user, IllegalEvent illegalEvent) throws TeraException {
        if (receipt.getReceiptSeq() == null) {
            return;
        }

        if (receiptService.isExistByIllegalType(user.getUserSeq(), carNum, regDt, code, illegalEvent.getIllegalType())) {
            switch (illegalEvent.getIllegalType()) {
                case ILLEGAL:
                    setComment(receipt.getReceiptSeq(), TeraExceptionCode.REPORT_OCCUR_ONE.getMessage());
                    throw new TeraException(TeraExceptionCode.REPORT_OCCUR_ONE);
                case FIVE_MINUTE:
                    setComment(receipt.getReceiptSeq(), TeraExceptionCode.REPORT_OCCUR_FIVE.getMessage());
                    throw new TeraException(TeraExceptionCode.REPORT_OCCUR_FIVE);
            }
        }
    }

    @Override
    public void receiptDuplicate(String addr, String carNum, String fileName, LocalDateTime regDt, String code, User user, IllegalZone illegalZone, IllegalEvent illegalEvent) throws TeraException {
        if (reportService.isExist(carNum, regDt, illegalEvent.getIllegalType())) {
            // 신고 차량이 이미 신고가 완료 되었기 때문에 신고 불가 (NOTHING)
            Receipt receipt = setReceipt(addr, carNum, fileName, regDt, code, ReceiptStateType.NOTHING, user, illegalZone);
            setComment(receipt.getReceiptSeq(), TeraExceptionCode.ILLEGAL_PARKING_EXIST_REPORT_CAR_NUM.getMessage());
            throw new TeraException(TeraExceptionCode.ILLEGAL_PARKING_EXIST_REPORT_CAR_NUM);
        }
    }

    @Override
    public void receiptForOCCUR(Receipt receipt) throws TeraException{
        try {
            receipt.setReceiptStateType(ReceiptStateType.OCCUR);
            receiptService.set(receipt);
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.RECEIPT_REGISTER_FAIL, e);
        }
    }

    @Override
    public void receiptForReport(String carNum, LocalDateTime regDt, String code, Receipt receipt, User user, IllegalEvent illegalEvent) throws TeraException{
        try {
            if (receiptService.isExist(user.getUserSeq(), carNum, regDt, code, illegalEvent.getIllegalType())) {
                // 2회 신고 ( 1분 ~ 11분(또는 16분) 내에 두번째 신고 )
                receipt.setReceiptStateType(ReceiptStateType.REPORT);
                receipt = receiptService.set(receipt);

                // 신고 접수
                Report report = new Report();
                report.setReceipt(receipt);
                report.setReportStateType(ReportStateType.COMPLETE);
                reportService.set(report);

                // "1분 이후 접수가필요합니다" 주석 삭제
                removeComment(receipt.getReceiptSeq());
            }
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.REPORT_REGISTER_FAIL, e);
        }

    }
}
