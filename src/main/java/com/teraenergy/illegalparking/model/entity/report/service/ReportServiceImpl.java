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
            case FIVE_MINUTE:   // 5분 주정차
                startTime = now.minusMinutes(16);
                break;
            case ILLEGAL:       // 불법 주정차
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

        pageNumber = pageNumber - 1; // 이유 : offset 시작 값이 0부터 이므로
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

    // 신고제외(미처리) 처리 신고 건수

    @Override
    public int getSizeForException(List<IllegalZone> illegalZones) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.in(illegalZones));
        query.where(QReport.report.reportStateType.eq(ReportStateType.EXCEPTION));
        query.where(QReport.report.isDel.isFalse());
        return query.fetch().size();
    }

    // 과태료 처리 신고 건수
    @Override
    public int getSizeForPenalty(List<IllegalZone> illegalZones) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReport.report);
        query.where(QReport.report.receipt.illegalZone.in(illegalZones));
        query.where(QReport.report.reportStateType.eq(ReportStateType.PENALTY));
        query.where(QReport.report.isDel.isFalse());
        return query.fetch().size();
    }

    // 대기중인 신고 건수
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

    // 월말일자 구하기
    private int getLastDay(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }


    // 한달간의 신고 건수 가져오기
    @Override
    public int getReportCountByMonth(int year, int month) {
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
        // 신고 접수를 판단하는 사용자 (관공서)
        User user = userService.get(userSeq);

        Report report = get(reportSeq);
        report.setNote(note);
        report.setReportStateType(reportStateType);
        report.setReportUserSeq(userSeq);

        // 신고 등록 (Receipt) 에 대한 결과 정보 변경
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

                    // 포인트 제한 없음
                    if (updatePoint.getIsPointLimit()) {
                        // 날짜 제한 없음
                        if (!updatePoint.getIsTimeLimit()) {
                            // 기간 내부에 존재 여부 확인
                            if ( !(updatePoint.getStartDate().isBefore(LocalDate.now()) && updatePoint.getStopDate().isAfter(LocalDate.now())) ) {
                                break;
                            }
                        }
                        pointValue = updatePoint.getValue();
                        updatePoint.setUseValue(updatePoint.getUseValue() + pointValue);                // 누적 사용량
                        break;
                    }

                    // 날짜 제한 없음
                    if ( updatePoint.getIsTimeLimit()) {
                        if (point.getValue() < updatePoint.getResidualValue()) {
                            pointValue = updatePoint.getValue();
                            updatePoint.setResidualValue(updatePoint.getResidualValue() - pointValue);      // 남은 포인트
                            updatePoint.setUseValue(updatePoint.getUseValue() + pointValue);                // 누적 사용량
                        }
                    } else {
                        // 제한 시간에서 포인트 체크
                        if ( updatePoint.getStartDate().isBefore(LocalDate.now()) && updatePoint.getStopDate().isAfter(LocalDate.now()) ) {
                            if (updatePoint.getValue() < updatePoint.getResidualValue()) {
                                pointValue = updatePoint.getValue();
                                updatePoint.setResidualValue(updatePoint.getResidualValue() - pointValue);                                      // 남은 포인트
                                updatePoint.setUseValue( (updatePoint.getUseValue() == null ? pointValue : updatePoint.getUseValue()) + pointValue);    // 누적 사용량
                            }
                        }
                    }
                }

                List<Comment> commentList = Lists.newArrayList();

                // 댓글 1
                Comment firstComment = new Comment();
                firstComment.setReceiptSeq(receipt.getReceiptSeq());
                firstComment.setContent(ReplyType.REPORT_COMPLETE.getValue());

                // 댓글 2
                Comment secondComment = new Comment();
                secondComment.setReceiptSeq(receipt.getReceiptSeq());
                secondComment.setContent(ReplyType.GIVE_PENALTY.getValue());

                // 댓글 3 ( 관공서 내용 )
                Comment thirdComment = new Comment();
                thirdComment.setReceiptSeq(receipt.getReceiptSeq());
                thirdComment.setContent(note);

                // 댓글 4
                Comment forthComment = new Comment();
                forthComment.setReceiptSeq(receipt.getReceiptSeq());
                String pointContent = "";

                if (updatePoint != null) {
                    if ( pointValue == 0L ) {
                        pointContent = "포인트가 모두 소진되어 제공이 불가합니다.";
                    } else {

                        if (updatePoint.getValue() > updatePoint.getResidualValue()) {
                            updatePoint.setNote("포인트 소진으로 인한 종료");
                        }

                        pointService.set(updatePoint);
                        pointContent = user.getGovernMentOffice().getLocationType().getValue();
                        pointContent += ("(으)로 부터 포상금 " + pointValue);
                        pointContent += "포인트가 제공되었습니다.";

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
                    pointContent = "포인트가 모두 소진되어 제공이 불가합니다.";
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
