package com.teraenergy.illegalparking.model.entity.receipt.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType;
import com.teraenergy.illegalparking.model.entity.receipt.domain.QReceipt;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptFilterColumn;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.receipt.repository.ReceiptRepository;
import com.teraenergy.illegalparking.model.entity.report.domain.QReport;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.util.StringUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

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
public class ReceiptServiceImpl implements ReceiptService {

    private final JPAQueryFactory jpaQueryFactory;

    private final ReceiptRepository receiptRepository;

    @Override
    public Receipt get(Integer receiptSeq) {
        return receiptRepository.findByReceiptSeqAndIsDel(receiptSeq, false);
    }

    // 법정동 코드를 이용하여 신고자가 신고 차량번호를 등록했는지 여부 확인
    @Override
    public boolean isExist(Integer userSeq, String carNum, LocalDateTime regDt, String code, IllegalType illegalType) {
        LocalDateTime endTime = regDt;
        LocalDateTime startTime = null;
        switch (illegalType) {
            case ILLEGAL:
                startTime = endTime.minusMinutes(11);
                break;
            case FIVE_MINUTE:
                startTime = endTime.minusMinutes(16);
                break;
        }

        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.user.userSeq.eq(userSeq));
        query.where(QReceipt.receipt.carNum.eq(carNum));
        query.where(QReceipt.receipt.regDt.between(startTime, endTime));
        query.where(QReceipt.receipt.illegalZone.code.eq(code));
        query.where(QReceipt.receipt.receiptStateType.ne(ReceiptStateType.NOTHING));
        query.where(QReceipt.receipt.isDel.isFalse());
        if (query.fetch().size() > 0) {
            return true;
        }
        return false;
    }

    // 불법 주정차 그룹위치 정보에 의한 신고 정보가 있는지 확인
    @Override
    public boolean isExistByIllegalType(Integer userSeq, String carNum, LocalDateTime regDt, String code, IllegalType illegalType) {

        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.user.userSeq.eq(userSeq));
        query.where(QReceipt.receipt.carNum.eq(carNum));
        query.where(QReceipt.receipt.illegalZone.code.eq(code));
        query.where(QReceipt.receipt.receiptStateType.ne(ReceiptStateType.NOTHING));

        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        switch (illegalType) {
            case ILLEGAL:
                startTime = regDt.minusMinutes(1);
                endTime = regDt;
                query.where(QReceipt.receipt.regDt.between(startTime, endTime));
                break;
            case FIVE_MINUTE:
                startTime = regDt.minusMinutes(5);
                endTime = regDt;
                query.where(QReceipt.receipt.regDt.between(startTime, endTime));
                break;
        }

        if (query.fetch().size() > 0) {
            return true;
        }
        return false;
    }

    @Override
    public List<Receipt> gets() {
        return receiptRepository.findAllByIsDel(false);
    }

    @Override
    public List<Receipt> gets(Integer userSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.user.userSeq.eq(userSeq));
        query.where(QReceipt.receipt.isDel.isFalse());
        query.orderBy(QReceipt.receipt.receiptSeq.desc());
        return query.fetch();
    }

    public List<Receipt> gets(String carNum) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.carNum.eq(carNum));
        query.where(QReceipt.receipt.isDel.isFalse());
        query.orderBy(QReceipt.receipt.receiptSeq.desc());
        return query.fetch();
    }

    // 신고 목록에서 - 중복 회수
    @Override
    public int getsOverlabCount(Integer user, String carNum, LocalDateTime regDt) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.user.userSeq.eq(user));
        query.where(QReceipt.receipt.carNum.eq(carNum));
        query.where(QReceipt.receipt.regDt.before(regDt));
        return query.fetch().size();
    }

    @Override
    public List<Receipt> gets(LocalDateTime startTime, LocalDateTime endTime, ReceiptStateType receiptStateType, IllegalType illegalType) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.regDt.between(startTime, endTime));
        query.where(QReceipt.receipt.receiptStateType.eq(receiptStateType));
        query.where(QReceipt.receipt.illegalZone.illegalEvent.illegalType.eq(illegalType));
        query.where(QReceipt.receipt.isDel.isFalse());

        return query.fetch();
    }

    @Override
    public Page<Receipt> gets(int pageNumber, int pageSize, ReceiptStateType receiptStateType, ReceiptFilterColumn filterColumn, String search) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);

        if (search != null && search.length() > 0) {
            switch (filterColumn) {
                case CAR_NUM:
                    query.where(QReceipt.receipt.carNum.contains(search));
                    break;
                case ADDR:
                    query.where(QReceipt.receipt.addr.contains(search));
                    break;
                case USER:
                    query.where(QReceipt.receipt.user.name.contains(search));
                    break;
            }
        }

        query.where(QReceipt.receipt.isDel.isFalse());

        query.where(QReceipt.receipt.receiptStateType.ne(ReceiptStateType.REPORT));     // 사고접수 (처리완료)
        query.where(QReceipt.receipt.receiptStateType.ne(ReceiptStateType.PENALTY));    // 과태료대상 (처리완료)
        query.where(QReceipt.receipt.receiptStateType.ne(ReceiptStateType.NOTHING));    // 신고불가
        query.orderBy(QReceipt.receipt.receiptSeq.desc());
        int total = query.fetch().size();

        if (receiptStateType != null) {
            query.where(QReceipt.receipt.receiptStateType.eq(receiptStateType));
        }

        pageNumber = pageNumber - 1; // 이유 : offset 시작 값이 0부터 이므로
        query.limit(pageSize).offset(pageNumber * pageSize);
        List<Receipt> receipts = query.fetch();

        PageRequest pageRequest = PageRequest.of(pageNumber, pageSize);
        Page<Receipt> page = new PageImpl<Receipt>(receipts, pageRequest, total);
        return page;
    }


    // 월말일자 구하기
    private int getLastDay(int year, int month) {
        Calendar c = Calendar.getInstance();
        c.set(year, month, 1);
        return c.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    // 한달간의 처리 및 미처리 건수 가져오기
    @Override
    public int getReceiptCountByMonth(int year, int month) {
        int lastDay = getLastDay(year, month);
        String lastDayStr = String.valueOf(lastDay);
        String yearStr = String.valueOf(year);
        String monthStr = String.valueOf(month);
        if ( month < 10) {
            monthStr = "0" + monthStr;
        }
        LocalDateTime startTime = StringUtil.convertStringToDateTime( (yearStr + monthStr + "010000"),  "yyyyMMddHHmm" );
        LocalDateTime endTime =  StringUtil.convertStringToDateTime( (yearStr + monthStr + lastDayStr +"2359"),  "yyyyMMddHHmm" );
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.regDt.between(startTime, endTime));
        query.where(QReceipt.receipt.receiptStateType.ne(ReceiptStateType.NOTHING));
        query.where(QReceipt.receipt.receiptStateType.ne(ReceiptStateType.FORGET));
        return query.fetch().size();
    }

    // 가장 최근에 신고 발생한 신고등록 정보 가져오기
    @Override
    public Receipt getByLastOccur(Integer userSeq, String carNum, LocalDateTime regDt, IllegalType illegalType) {
        LocalDateTime endTime = regDt;
        LocalDateTime diffTime = null;
        switch (illegalType) {
            case ILLEGAL:
                diffTime = endTime.minusMinutes(11);
                break;
            case FIVE_MINUTE:
                diffTime = endTime.minusMinutes(16);
                break;
        }

        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.user.userSeq.eq(userSeq));
        query.where(QReceipt.receipt.carNum.eq(carNum));
        query.where(QReceipt.receipt.receiptStateType.eq(ReceiptStateType.OCCUR));
        query.where(QReceipt.receipt.regDt.before(diffTime));
        query.where(QReceipt.receipt.isDel.isFalse());
        query.orderBy(QReceipt.receipt.receiptSeq.desc());
        query.limit(1);
        if ( query.fetchOne() != null) {
            return (Receipt) query.fetchOne();
        }
        return null;
    }

    @Override
    public Receipt set(Receipt receipt) {
        return receiptRepository.save(receipt);
    }

    @Override
    public List<Receipt> sets(List<Receipt> receipts) {
        return receiptRepository.saveAll(receipts);
    }

    @Override
    public Receipt modify(Receipt receipt) {
        return set(receipt);
    }

    @Override
    public long remove(Integer receiptSeq) {
        JPAUpdateClause query = jpaQueryFactory.update(QReceipt.receipt);
        query.set(QReceipt.receipt.isDel, true);
        query.where(QReceipt.receipt.receiptSeq.eq(receiptSeq));
        return query.execute();
    }

    @Override
    public long removes(List<Integer> receiptSeqs) {
        JPAUpdateClause query = jpaQueryFactory.update(QReceipt.receipt);
        query.set(QReceipt.receipt.isDel, true);
        query.where(QReceipt.receipt.receiptSeq.in(receiptSeqs));
        return query.execute();
    }

    // 현재 기준에서 11분전 사이의 해당 차량 번호로 신고등록 정보 가져오기
    @Override
    public Receipt getByCarNumAndBetweenNow(Integer userSeq, String carNum, LocalDateTime regDt) {
        JPAQuery query = jpaQueryFactory.selectFrom(QReceipt.receipt);
        query.where(QReceipt.receipt.user.userSeq.eq(userSeq));
        query.where(QReceipt.receipt.carNum.eq(carNum));
        query.where(QReceipt.receipt.regDt.between(regDt.minusMinutes(11), regDt));
        query.where(QReceipt.receipt.receiptStateType.ne(ReceiptStateType.NOTHING));
        query.where(QReceipt.receipt.isDel.isFalse());
        query.limit(1);
        return (Receipt) query.fetchOne();
    }

}
