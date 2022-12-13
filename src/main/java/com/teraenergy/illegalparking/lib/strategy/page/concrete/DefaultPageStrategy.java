package com.teraenergy.illegalparking.lib.strategy.page.concrete;

import com.teraenergy.illegalparking.lib.strategy.page.PageStrategy;
import org.springframework.ui.Model;

/**
 * Date : 2022-12-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public class DefaultPageStrategy implements PageStrategy {

    @Override
    public void setModelForPageTag(int totalPages, int pageNumber, int pageSize, Model model) {
        boolean isBeginOver = false;
        boolean isEndOver = false;

        int offsetPage = pageNumber - 1;

        if (offsetPage >= (totalPages-2)) {
            offsetPage = totalPages-2;
        } else {
            if (totalPages > 3) isEndOver = true;
        }

        if ( offsetPage < 1) {
            offsetPage = 1;
        } else {
            if (offsetPage > 1 && totalPages > 3) isBeginOver = true;
        }

        model.addAttribute("totalPages", totalPages);
        model.addAttribute("offsetPage", offsetPage);
        model.addAttribute("isBeginOver", isBeginOver);
        model.addAttribute("isEndOver", isEndOver);
        model.addAttribute("pageNumber", pageNumber);
        model.addAttribute("pageSize", pageSize);
    }

}
