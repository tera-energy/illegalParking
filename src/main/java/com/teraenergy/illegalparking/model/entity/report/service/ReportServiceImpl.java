package com.teraenergy.illegalparking.model.entity.report.service;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.comment.service.CommentService;
import com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType;
import com.teraenergy.illegalparking.model.entity.illegalGroup.domain.QIllegalGroup;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReplyType;
import com.teraenergy.illegalparking.model.entity.report.domain.QReport;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.report.repository.ReportRepository;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.apache.commons.compress.utils.Lists;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Service
public class ReportServiceImpl implements ReportService {

    private final JPAQueryFactory jpaQueryFactory;

    private final ReportRepository reportRepository;

    private final CommentService commentService;

    private final UserService userService;

    private final PointService pointService;

    private final CalculateService calculateService;

    @Override
    public boolean isExist(String carNum, LocalDateTime regDt, IllegalType illegalType) {
        LocalDateTime now = regDt;
        LocalDateTime startTime = null;
        switch (illegalType) {
            case FIVE_MINUTE:   // 5??? ?????????
                startTime = now.minusMinutes(16);
                break;
            case ILLEGAL:       // ?????? ?????????
                startTime = now.minusMinutes(11);
                break;
        }
        LocalDateTime endTime = now;

        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.carNum.eq(carNum));
        query.where(QReport.report.receipt.receiptStateType.eq(ReceiptStateType.REPORT));
        query.where(QReport.report.receipt.regDt.between(startTime, endTime));
        query.where(QReport.report.isDel.isFalse());
        if (query.fetchOne() == null) {
            return false;
        }
        return true;
    }

    @Override
    public Report get(Integer reportSeq) {
        return reportRepository.findByReportSeq(reportSeq);
    }

    @Override
    public List<Report> getByGovernmentOffice(Integer reportSeq, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.regDt.between(startDateTime, endDateTime));
        query.where(QReport.report.isDel.isFalse());
        return query.fetch();
    }

    @Override
    public List<Report> gets() {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.isDel.isFalse());
        return query.fetch();
    }

    @Override
    public Integer getsOverlabCount(String carNum, LocalDateTime regDt) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.carNum.eq(carNum));
        query.where(QReport.report.receipt.regDt.before(regDt));
        query.where(QReport.report.isDel.isFalse());
        return query.fetch().size();
    }

    @Override
    public Page<Report> gets(int pageNumber, int pageSize, ReportStateType reportStateType, ReportFilterColumn filterColumn, String search, List<Integer> zoneSeqs) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);

        if (search != null && search.length() > 0) {
            switch (filterColumn) {
                case CAR_NUM:
                    query.where(QReport.report.receipt.carNum.contains(search));
                    break;
                case ADDR:
                    query.where(QReport.report.receipt.addr.contains(search));
                    break;
                case USER:
                    query.where(QReport.report.receipt.user.name.contains(search));
                    break;
            }
        }

        if (zoneSeqs != null) {
            query.where(QReport.report.receipt.illegalZone.zoneSeq.in(zoneSeqs));
        }

        query.where(QReport.report.isDel.isFalse());
        query.orderBy(QReport.report.reportSeq.desc());

        if (reportStateType != null) {
            query.where(QReport.report.reportStateType.eq(reportStateType));
        }

        int total = query.fetch().size();

        pageNumber = pageNumber - 1; // ?????? : offset ?????? ?????? 0?????? ?????????
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Report> reports = query.fetch();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Report> page = new PageImpl<Report>(reports, pageRequest, total);
        return page;
    }

    @Override
    public int getSizeForReport(List<IllegalZone> illegalZones) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.in(illegalZones));
        query.where(QReport.report.isDel.isFalse());
        return query.fetch().size();
    }

    // ????????????(?????????) ?????? ?????? ??????

    @Override
    public int getSizeForException(List<IllegalZone> illegalZones) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.in(illegalZones));
        query.where(QReport.report.reportStateType.eq(ReportStateType.EXCEPTION));
        query.where(QReport.report.isDel.isFalse());
        return query.fetch().size();
    }

    // ????????? ?????? ?????? ??????
    @Override
    public int getSizeForPenalty(List<IllegalZone> illegalZones) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.in(illegalZones));
        query.where(QReport.report.reportStateType.eq(ReportStateType.PENALTY));
        query.where(QReport.report.isDel.isFalse());
        return query.fetch().size();
    }

    // ???????????? ?????? ??????
    @Override
    public int getSizeForCOMPLETE(List<IllegalZone> illegalZones) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.in(illegalZones));
        query.where(QReport.report.reportStateType.eq(ReportStateType.COMPLETE));
        query.where(QReport.report.isDel.isFalse());
        return query.fetch().size();
    }

    @Override
    public int getSizeForPenalty(IllegalZone illegalZone) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.eq(illegalZone));
        query.where(QReport.report.reportStateType.eq(ReportStateType.PENALTY));
        query.where(QReport.report.isDel.isFalse());
        return query.fetch().size();
    }

    // ???????????? ?????????
    private int getLastDay(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    // ???????????? ?????? ?????? ????????????
    @Override
    public int getReportCountByMonth(int year, int month, List<IllegalZone> illegalZones) {
        int lastDay = getLastDay(year, month);
        String lastDayStr = String.valueOf(lastDay);
        String yearStr = String.valueOf(year);
        String monthStr = String.valueOf(month);
        if ( month < 10) {
            monthStr = "0" + monthStr;
        }
        LocalDateTime startTime = StringUtil.convertStringToDateTime( (yearStr + monthStr + "010000"),  "yyyyMMddHHmm" );
        LocalDateTime endTime =  StringUtil.convertStringToDateTime( (yearStr + monthStr + lastDayStr +"2359"),  "yyyyMMddHHmm" );
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.regDt.between(startTime, endTime));
        query.where(QReport.report.receipt.illegalZone.in(illegalZones));
        query.where(QReport.report.isDel.isFalse());
        return query.fetch().size();
    }

    @Override
    public Report set(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public List<Report> sets(List<Report> reports) {
        return reportRepository.saveAll(reports);
    }

    @Transactional
    @Override
    public Report modifyByGovernmentOffice(Integer reportSeq, Integer userSeq, ReportStateType reportStateType, String note) throws TeraException {
        // ?????? ????????? ???????????? ????????? (?????????)
        User user = userService.get(userSeq);

        Report report = get(reportSeq);
        report.setNote(note);
        report.setReportStateType(reportStateType);
        report.setReportUserSeq(userSeq);

        // ?????? ?????? (Receipt) ??? ?????? ?????? ?????? ??????
        Receipt receipt = report.getReceipt();

        switch (reportStateType) {
            case PENALTY:
                receipt.setReceiptStateType(ReceiptStateType.PENALTY);

                Integer groupSeq = receipt.getIllegalZone().getIllegalEvent().getGroupSeq();
                List<Point> points = pointService.getsInGroup(groupSeq);

                long pointValue = 0L;
                Point updatePoint = null;
                for ( Point point : points) {

                    updatePoint = point;

                    // ????????? ?????? ??????
                    if (updatePoint.getIsPointLimit()) {
                        // ?????? ?????? ??????
                        if (!updatePoint.getIsTimeLimit()) {
                            // ?????? ????????? ?????? ?????? ??????
                            if ( !(updatePoint.getStartDate().isBefore(LocalDate.now()) && updatePoint.getStopDate().isAfter(LocalDate.now())) ) {
                                break;
                            }
                        }
                        pointValue = updatePoint.getValue();
                        updatePoint.setUseValue(updatePoint.getUseValue() + pointValue);                // ?????? ?????????
                        break;
                    }

                    // ?????? ?????? ??????
                    if ( updatePoint.getIsTimeLimit()) {
                        if (point.getValue() < updatePoint.getResidualValue()) {
                            pointValue = updatePoint.getValue();
                            updatePoint.setResidualValue(updatePoint.getResidualValue() - pointValue);      // ?????? ?????????
                            updatePoint.setUseValue(updatePoint.getUseValue() + pointValue);                // ?????? ?????????
                        }
                    } else {
                        // ?????? ???????????? ????????? ??????
                        if ( updatePoint.getStartDate().isBefore(LocalDate.now()) && updatePoint.getStopDate().isAfter(LocalDate.now()) ) {
                            if (updatePoint.getValue() < updatePoint.getResidualValue()) {
                                pointValue = updatePoint.getValue();
                                updatePoint.setResidualValue(updatePoint.getResidualValue() - pointValue);                                      // ?????? ?????????
                                updatePoint.setUseValue( (updatePoint.getUseValue() == null ? pointValue : updatePoint.getUseValue()) + pointValue);    // ?????? ?????????
                            }
                        }
                    }
                }

                List<Comment> commentList = Lists.newArrayList();

                // ?????? 1
                Comment firstComment = new Comment();
                firstComment.setReceiptSeq(receipt.getReceiptSeq());
                firstComment.setContent(ReplyType.REPORT_COMPLETE.getValue());

                // ?????? 2
                Comment secondComment = new Comment();
                secondComment.setReceiptSeq(receipt.getReceiptSeq());
                secondComment.setContent(ReplyType.GIVE_PENALTY.getValue());

                // ?????? 3 ( ????????? ?????? )
                Comment thirdComment = new Comment();
                thirdComment.setReceiptSeq(receipt.getReceiptSeq());
                thirdComment.setContent(note);

                // ?????? 4
                Comment forthComment = new Comment();
                forthComment.setReceiptSeq(receipt.getReceiptSeq());
                String pointContent = "";

                if (updatePoint != null) {
                    if ( pointValue == 0L ) {
                        pointContent = "???????????? ?????? ???????????? ????????? ???????????????.";
                    } else {

                        if (updatePoint.getValue() > updatePoint.getResidualValue()) {
                            updatePoint.setNote("????????? ???????????? ?????? ??????");
                        }

                        pointService.set(updatePoint);
                        pointContent = user.getGovernMentOffice().getLocationType().getValue();
                        pointContent += ("(???)??? ?????? ????????? " + pointValue);
                        pointContent += "???????????? ?????????????????????.";

                        Calculate oldCalculate = calculateService.getAtLast(receipt.getUser().getUserSeq());
                        Calculate newCalculate = new Calculate();
                        if (oldCalculate == null) {
                            newCalculate.setCurrentPointValue(pointValue);
                        } else {
                            newCalculate.setCurrentPointValue((oldCalculate.getCurrentPointValue() == null ? 0 : oldCalculate.getCurrentPointValue()) + pointValue);
                        }

                        newCalculate.setUserSeq(receipt.getUser().getUserSeq());
                        newCalculate.setPointType(updatePoint.getPointType());
                        newCalculate.setEventPointValue(pointValue);
                        newCalculate.setLocationType(user.getGovernMentOffice().getLocationType());
                        calculateService.set(newCalculate);
                    }
                } else {
                    pointContent = "???????????? ?????? ???????????? ????????? ???????????????.";
                }

                forthComment.setContent(pointContent);

                commentList.add(firstComment);
                commentList.add(secondComment);
                commentList.add(thirdComment);
                commentList.add(forthComment);

                commentService.sets(commentList);
                break;
            case EXCEPTION:
                Comment comment = new Comment();
                receipt.setReceiptStateType(ReceiptStateType.EXCEPTION);
                comment.setContent(ReplyType.REPORT_EXCEPTION.getValue());
                comment.setReceiptSeq(receipt.getReceiptSeq());
                commentService.set(comment);
                break;
        }

        report.setReceipt(receipt);
        return set(report);
    }

    @Override
    public Report modify(Report report) {
        return reportRepository.save(report);
    }

    @Override
    public long remove(Integer reportSeq) {
        JPAUpdateClause query = jpaQueryFactory.update(QReport.report);
        query.set(QReport.report.isDel, true);
        query.where(QReport.report.reportSeq.eq(reportSeq));
        return query.execute();
    }

    @Override
    public long removes(List<Integer> reportSeqs) {
        JPAUpdateClause query = jpaQueryFactory.update(QReport.report);
        query.set(QReport.report.isDel, true);
        query.where(QReport.report.reportSeq.in(reportSeqs));
        return query.execute();
    }
}
