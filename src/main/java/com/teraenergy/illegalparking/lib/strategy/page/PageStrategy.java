package com.teraenergy.illegalparking.lib.strategy.page;

import org.springframework.ui.Model;

/**
 * Date : 2022-12-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface PageStrategy {

    /** page tag에 대한 데이터 model 데이터 설정 */
    void setModelForPageTag(int totalPages, int pageNumber, int pageSize, Model model);
}
