package com.teraenergy.illegalparking.model.entity.receipt.service;

import com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType;
import com.teraenergy.illegalparking.model.entity.receipt.domain.Receipt;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptFilterColumn;
import com.teraenergy.illegalparking.model.entity.receipt.enums.ReceiptStateType;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface ReceiptService {

    Receipt get(Integer receiptSeq);

    boolean isExist(Integer userSeq, String carNum, LocalDateTime regDt, String code, IllegalType illegalType);

    boolean isExistByIllegalType(Integer userSeq, String carNum, LocalDateTime regDt, String code, IllegalType illegalType);

    List<Receipt> gets();

    List<Receipt> gets(Integer userSeq);

    List<Receipt> gets(String carNum);

    int getsOverlabCount(Integer user, String carNum, LocalDateTime regDt);

    List<Receipt> gets(LocalDateTime now, LocalDateTime old, ReceiptStateType receiptStateType, IllegalType illegalType);

    Page<Receipt> gets(int pageNumber, int pageSize, ReceiptStateType receiptStateType, ReceiptFilterColumn filterColumn, String search);

    int getReceiptCountByMonth(int year, int month);

    Receipt getByLastOccur(Integer userSeq, String carNum, LocalDateTime regDt, IllegalType illegalType);

    Receipt set(Receipt receipt);

    List<Receipt> sets(List<Receipt> receipts);

    Receipt modify(Receipt receipt);

    long remove(Integer receiptSeq);

    long removes(List<Integer> receiptSeqs);

    Receipt getByCarNumAndBetweenNow(Integer userSeq, String carNum, LocalDateTime regDt);

}
