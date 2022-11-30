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


    boolean isExist(String carNum, LocalDateTime regDt, IllegalType illegalType);

    Report get(Integer reportSeq);
    List<Report> getByGovernmentOffice(Integer reportSeq, LocalDateTime startDate, LocalDateTime endDate);

    List<Report> gets();

    Integer getsOverlabCount(String carNum, LocalDateTime regDt);

    Page<Report> gets(int pageNumber, int pageSize, ReportStateType reportStateType, ReportFilterColumn filterColumn, String search, List<Integer> zoneSeqs);

    int getSizeForReport(List<IllegalZone> illegalZones);

    int getSizeForException(List<IllegalZone> illegalZones);

    int getSizeForPenalty(List<IllegalZone> illegalZones);

    int getSizeForCOMPLETE(List<IllegalZone> illegalZones);

    int getSizeForPenalty(IllegalZone illegalZone);

    int getReportCountByMonth(int year, int month);



    Report set(Report report);

    List<Report> sets(List<Report> reports);

    Report modifyByGovernmentOffice(Integer reportSeq, Integer userSeq, ReportStateType reportStateType, String note) throws TeraException;

    Report modify(Report report);

    long remove(Integer reportSeq);

    long removes(List<Integer> reportSeqs);
}
