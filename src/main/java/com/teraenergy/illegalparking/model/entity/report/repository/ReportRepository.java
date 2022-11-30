package com.teraenergy.illegalparking.model.entity.report.repository;

import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Date : 2022-09-24
 * Author : young
 * Project : illegalParking
 * Description :
 */
public interface ReportRepository extends JpaRepository<Report, Integer> {

    Report findByReportSeq(Integer reportSeq);

//    Report findByReceiptUserSeq(Integer receiptUserSeq);


}
