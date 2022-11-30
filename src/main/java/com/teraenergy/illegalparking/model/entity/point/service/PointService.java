package com.teraenergy.illegalparking.model.entity.point.service;

import com.teraenergy.illegalparking.model.entity.point.domain.Point;

import java.util.List;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface PointService {

    Point get(Integer pointSeq);

    List<Point> gets();

    Point getInGroup(Integer groupSeq);

    List<Point> getsAllInGroup(Integer groupSeq);

    List<Point> getsInGroup(Integer groupSeq);

    Point set(Point point);

    List<Point> sets(List<Point> points);

}
