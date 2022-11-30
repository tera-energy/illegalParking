package com.teraenergy.illegalparking.model.dto.report.service;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.dto.report.domain.ReceiptDetailDto;
import com.teraenergy.illegalparking.model.dto.report.domain.ReceiptDto;
import com.teraenergy.illegalparking.model.dto.report.domain.ReportDetailDto;
import com.teraenergy.illegalparking.model.dto.report.domain.ReportDto;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.illegalGroup.service.IllegalGroupServcie;
import com.teraenergy.illegalparking.model.entity.mycar.service.MyCarService;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptFilterColumn;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.service.ReceiptService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Date : 2022-09-28
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class ReportDtoServiceImpl implements ReportDtoService {

    private final ReportService reportService;

    private final ReceiptService receiptService;

    private final UserService userService;

    private final CommentService commentService;

    private final IllegalGroupServcie illegalGroupServcie;

    private final PointService pointService;

    private final MyCarService myCarService;

    @Override
    public ReportDto get(Report report) {
        ReportDto reportDto = new ReportDto();
        Receipt receipt = report.getReceipt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm");
        return reportDto;
    }

    @Override
    public List<ReportDto> gets(List<Report> reports) {
        return null;
    }

    @Override
    public Page<ReceiptDto> getsFromReceipt(int pageNumber, int pageSize, ReceiptStateType receiptStateType, ReceiptFilterColumn filterColumn, String search) {
        Page<Receipt> receiptPage = receiptService.gets(pageNumber, pageSize, receiptStateType, filterColumn, search);

        List<ReceiptDto> receiptDtos = Lists.newArrayList();
        for (Receipt receipt : receiptPage.getContent()) {
            ReceiptDto receiptDto = new ReceiptDto();
            receiptDto.setReceiptSeq(receipt.getReceiptSeq());
            receiptDto.setAddr(receipt.getAddr());

            if ( myCarService.get(receipt.getUser().getUserSeq(), receipt.getCarNum()) != null ){
                receiptDto.setCarNum(receipt.getCarNum() + "(등록)");
            } else {
                receiptDto.setCarNum(receipt.getCarNum());
            }

            receiptDto.setName(receipt.getUser().getName());
            receiptDto.setOverlapCount(receiptService.getsOverlabCount(receipt.getUser().getUserSeq(), receipt.getCarNum(), receipt.getRegDt()));
            receiptDto.setRegDt(receipt.getRegDt());
            receiptDto.setReceiptStateType(receipt.getReceiptStateType());

            receiptDtos.add(receiptDto);
        }

        pageNumber = pageNumber -1;  // 이유 : offset 시작 값이 0부터 이므로
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ReceiptDto> page = new PageImpl<ReceiptDto>(receiptDtos, pageRequest, receiptPage.getTotalElements());
        return page;
    }


    @Override
    public ReceiptDetailDto getFromReceiptDetailDto(Integer receiptSeq) throws TeraException {

        Receipt receipt = receiptService.get(receiptSeq);
        ReceiptDetailDto receiptDetailDto = new ReceiptDetailDto();
        receiptDetailDto.setReceiptSeq(receipt.getReceiptSeq());
        receiptDetailDto.setName(receipt.getUser().getName());
        receiptDetailDto.setAddr(receipt.getAddr());

        if ( myCarService.get(receipt.getUser().getUserSeq(), receipt.getCarNum()) != null ){
            receiptDetailDto.setCarNum(receipt.getCarNum() + "(등록)");
        } else {
            receiptDetailDto.setCarNum(receipt.getCarNum());
        }

        receiptDetailDto.setOverlapCount(receiptService.getsOverlabCount(receipt.getUser().getUserSeq(), receipt.getCarNum(), receipt.getRegDt())); // 중복 횟수
        receiptDetailDto.setRegDt(receipt.getRegDt());
        receiptDetailDto.setReceiptStateType(receipt.getReceiptStateType());

        List<String> comments = Lists.newArrayList();
        List<Integer> receiptSeqs = Lists.newArrayList();

        receiptDetailDto.setFirstFileName(receipt.getFileName());
        receiptDetailDto.setFirstRegDt(receipt.getRegDt());
        receiptDetailDto.setFirstAddr(receipt.getAddr());

        // 불법 주정차 구역이 아닌 지역에서 신고한 경우 illegalzone 이 없음.
        if ( receipt.getIllegalZone() != null) {
            receiptDetailDto.setFirstIllegalType(receipt.getIllegalZone().getIllegalEvent().getIllegalType());
        }

        if ( receipt.getSecondRegDt() != null ) {
            receiptDetailDto.setSecondFileName(receipt.getSecondFileName());
            receiptDetailDto.setSecondRegDt(receipt.getSecondRegDt());
            receiptDetailDto.setSecondAddr(receipt.getAddr());
            receiptDetailDto.setSecondIllegalType(receipt.getIllegalZone().getIllegalEvent().getIllegalType());
        }

        receiptSeqs.add(receipt.getReceiptSeq());

        List<Comment> receiptComments = commentService.gets(receiptSeqs);
        for (Comment receiptComment : receiptComments) {
            comments.add(receiptComment.getContent());
        }

        receiptDetailDto.setComments(comments);
        return receiptDetailDto;
    }

    @Override
    public Page<ReportDto> getsFromReport(int pageNumber, int pageSize, ReportStateType reportStateType, ReportFilterColumn filterColumn, String search, List<Integer> zoneSeqs) throws TeraException {
        Page<Report> reportPage = reportService.gets(pageNumber, pageSize, reportStateType, filterColumn, search, zoneSeqs);

        List<ReportDto> reportDtos = Lists.newArrayList();
        for (Report report : reportPage.getContent()) {
            ReportDto reportDto = new ReportDto();
            reportDto.setReportSeq(report.getReportSeq());
            reportDto.setAddr(report.getReceipt().getAddr());
            reportDto.setCarNum(report.getReceipt().getCarNum());
            reportDto.setName(report.getReceipt().getUser().getName());
            reportDto.setRegDt(report.getRegDt());
            reportDto.setReportStateType(report.getReportStateType());

            reportDto.setOverlapCount(reportService.getsOverlabCount(report.getReceipt().getCarNum(), report.getReceipt().getRegDt()));
            if (report.getReportUserSeq() != null) {
                User user = userService.get(report.getReportUserSeq());
                reportDto.setGovernmentName(user.getGovernMentOffice().getName());

                Point point = pointService.get(report.getReceipt().getIllegalZone().getIllegalEvent().getGroupSeq());
                String mark = "-";
                switch (point.getPointType()) {
                    case PLUS:
                        mark = "+";
                        break;
                    case MINUS:
                        mark = "-";
                        break;
                }
                reportDto.setEvent(mark + point.getValue().toString());
            }
            reportDtos.add(reportDto);
        }

        pageNumber = pageNumber -1;  // 이유 : offset 시작 값이 0부터 이므로
        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<ReportDto> page = new PageImpl<ReportDto>(reportDtos, pageRequest, reportPage.getTotalElements());
        return page;
    }

    @Override
    public ReportDetailDto getFromReportDetailDto(int reportSeq) throws TeraException {
        Report report = reportService.get(reportSeq);

        ReportDetailDto reportDetailDto = new ReportDetailDto();
        reportDetailDto.setReportSeq(report.getReportSeq());
        reportDetailDto.setReportStateType(report.getReportStateType());    // 신고기관

        Receipt receipt = report.getReceipt();
        reportDetailDto.setOverlapCount(receiptService.getsOverlabCount(receipt.getUser().getUserSeq(), receipt.getCarNum(), receipt.getRegDt())); // 중복 횟수
        reportDetailDto.setRegDt(receipt.getSecondRegDt());     // 접수시간 ( 접수시간 : 두번째 신고가 발생 시간 )
        reportDetailDto.setCarNum(receipt.getCarNum());         // 차량번호
        reportDetailDto.setAddr(receipt.getAddr());             // 위치
        reportDetailDto.setName(receipt.getUser().getName());   // 신고자

        if (report.getReportUserSeq() != null) {
            User governmentUser = userService.get(report.getReportUserSeq());
            reportDetailDto.setGovernmentOfficeName(governmentUser.getGovernMentOffice().getName());
        }

        // 내용
        String note = "";
        switch (report.getReportStateType()) {
            case EXCEPTION:
                note += report.getNote();
                if ( report.getNote() != null && report.getNote().length() > 0) {
                    note += "<br>";
                }
                note += StringUtil.convertDatetimeToString(report.getRegDt(), "yyyy-MM-dd HH:mm");
                note += "에 '신고 제외' 처리되었습니다. <br>";
                note += "신고제외 되었습니다.";
                reportDetailDto.setNote(note);                          // 내용
                break;
            case PENALTY:
                note += report.getNote();
                if ( report.getNote() != null && report.getNote().length() > 0) {
                    note += "<br>";
                }
                note += StringUtil.convertDatetimeToString(report.getRegDt(), "yyyy-MM-dd HH:mm");
                note += "에 '과태료 대상' 처리되었습니다. <br>";
                note += "주차하신 위치는 불법주정차 집중단속구역입니다. <br>";
                note += "과태료 대상입니다.";
                reportDetailDto.setNote(note);                          // 내용
                break;
        }

        List<Integer> receiptSeqs = Lists.newArrayList();

        reportDetailDto.setFirstFileName(receipt.getFileName());    // 첫번째 파일 이름
        reportDetailDto.setFirstRegDt(receipt.getRegDt());          // 첫번째 등록 일자
        reportDetailDto.setFirstAddr(receipt.getAddr());            // 첫번째 등록 주소
        reportDetailDto.setFirstIllegalType(receipt.getIllegalZone().getIllegalEvent().getIllegalType());            // 첫번째 등록 주소

        reportDetailDto.setSecondFileName(receipt.getSecondFileName());     // 두번째 파일 이름
        reportDetailDto.setSecondRegDt(receipt.getSecondRegDt());           // 두번째 등록 일자
        reportDetailDto.setSecondAddr(receipt.getAddr());                   // 두번째 등록 주소
        reportDetailDto.setSecondIllegalType(receipt.getIllegalZone().getIllegalEvent().getIllegalType());

        receiptSeqs.add(receipt.getReceiptSeq());

        List<Comment> receiptComments = commentService.gets(receiptSeqs);
        List<String> comments = Lists.newArrayList();
        for (Comment receiptComment : receiptComments) {
            comments.add(receiptComment.getContent());
        }

        reportDetailDto.setComments(comments);
        return reportDetailDto;
    }
}
