package com.teraenergy.illegalparking.model.entity.report.service;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.illegalEvent.enums.IllegalType;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportFilterColumn;
import com.teraenergy.illegalparking.model.entity.report.enums.ReportStateType;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface ReportService {


    boolean isExist(String carNum, IllegalType illegalType);

    boolean isExistByReceipt(String carNum, LocalDateTime regDt, IllegalType illegalType);

    Report get(Integer reportSeq);

    List<Report> gets();

    Page<Report> gets(int pageNumber, int pageSize, ReportStateType reportStateType, ReportFilterColumn filterColumn, String search);

    int getSizeForReport(List<IllegalZone> illegalZones);

    int getSizeForException(List<IllegalZone> illegalZones);

    int getSizeForPenalty(List<IllegalZone> illegalZones);

    int getSizeForCOMPLETE(List<IllegalZone> illegalZones);

    int getSizeForPenalty(IllegalZone illegalZone);

    Report set(Report report);

    List<Report> sets(List<Report> reports);

    Report modifyByGovernmentOffice(Integer reportSeq, Integer userSeq, ReportStateType reportStateType, String note) throws TeraException;

    Report modify(Report report);

    long remove(Integer reportSeq);

    long removes(List<Integer> reportSeqs);
}
