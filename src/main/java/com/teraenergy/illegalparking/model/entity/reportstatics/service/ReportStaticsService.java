package com.teraenergy.illegalparking.model.entity.reportstatics.service;

import com.teraenergy.illegalparking.model.entity.reportstatics.domain.ReportStatics;

import java.util.List;

/**
 * Date : 2022-10-20
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface ReportStaticsService {

    List<ReportStatics> gets();

    List<ReportStatics> gets(String code);

    ReportStatics gets(Integer zoneSeq);

	List<ReportStatics> gets(List<String> codes);

    ReportStatics set(ReportStatics reportStatics);

    List<ReportStatics> sets(List<ReportStatics> reportStaticsList);

}
