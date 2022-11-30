package com.teraenergy.illegalparking.controller.error;

import com.teraenergy.illegalparking.controller.ExtendsController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Controller
public class ErrorController extends ExtendsController {

    @RequestMapping("/401")
    public ModelAndView error401(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getPath("/401"));
        modelAndView.addObject("title", "401 Error");
        return modelAndView;
    }

    @RequestMapping("/404")
    public ModelAndView error404(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getPath("/404"));
        modelAndView.addObject("title", "404 Error");
        return modelAndView;
    }

    @RequestMapping("/500")
    public ModelAndView error500(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(getPath("/500"));
        modelAndView.addObject("title", "500 Error");
        return modelAndView;
    }

}
