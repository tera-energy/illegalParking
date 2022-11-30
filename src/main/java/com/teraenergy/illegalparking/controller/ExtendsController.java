/*
  Author: young
  Date: 2022-02-17
  Description: Controller 의 Root 경로 가져오기
*/
package com.teraenergy.illegalparking.controller;

import lombok.extern.slf4j.Slf4j;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Slf4j
public class ExtendsController {

    protected String getPath(String path) {
        String rootPath = this.getClass().getSimpleName().split("Controller")[0].toLowerCase();
        return "/normal/controller/" + rootPath + path;
    }

    protected String getMobilePath(String path) {
        String rootPath = this.getClass().getSimpleName().split("Controller")[0].toLowerCase();
        return "/mobile/controller/" + rootPath + path;
    }

}
