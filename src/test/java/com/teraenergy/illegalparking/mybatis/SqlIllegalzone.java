package com.teraenergy.illegalparking.mybatis;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import com.teraenergy.illegalparking.util.StringUtil;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
//@Transactional
public class SqlIllegalzone {

    @Autowired
    IllegalZoneMapperService illegalZoneMapperService;

    @Autowired
    IllegalZoneService illegalZoneService;

    @Autowired
    IllegalEventService illegalEventService;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    public void insert() {
        insertOne();
        insertList();
    }

    @Test
    public void insertOne() {
        IllegalZone illegalZone = new IllegalZone();
        illegalZone.setPolygon("POLYGON((126.567668343956 33.451276403135246,126.56935715259203 33.45123719996867,126.56834423197559 33.451621366446425,126.56966217559021 33.45045386564941,126.567668343956 33.451276403135246))");
        illegalZone.setCode("5013032000");
        illegalZone.setIsDel(false);
        illegalZoneMapperService.set(illegalZone);
    }

    @Test
    public void insertList() {
        List<IllegalZone> illegalZones = Lists.newArrayList();

        IllegalZone illegalZone = new IllegalZone();
        illegalZone.setPolygon("POLYGON((126.567668343956 33.451276403135246,126.56935715259203 33.45123719996867,126.56834423197559 33.451621366446425,126.56966217559021 33.45045386564941,126.567668343956 33.451276403135246))");
        illegalZone.setCode("1100000000");
        illegalZone.setIsDel(false);

        IllegalZone illegalZone2 = new IllegalZone();
        illegalZone2.setPolygon("POLYGON((126.567668343956 33.451276403135246,126.56935715259203 33.45123719996867,126.56834423197559 33.451621366446425,126.56966217559021 33.45045386564941,126.567668343956 33.451276403135246))");
        illegalZone2.setCode("5013032026");
        illegalZone2.setIsDel(false);

        illegalZones.add(illegalZone);
        illegalZones.add(illegalZone2);

        illegalZoneMapperService.sets(illegalZones);
    }

//    @Test
//    public void update() {
//        IllegalEvent illegalEvent = illegalEventService.get(1);
//        IllegalZone illegalZone = illegalZoneMapperService.get(1);
//
//        illegalZone.setCode("1111100000");
//        illegalZoneMapperService.modify(illegalZone);
//
//        illegalZone.setIllegalEvent(illegalEvent);
//        illegalZoneMapperService.modifyByEvent(illegalZone.getZoneSeq(), illegalEvent.getEventSeq());
//    }

    @Test
    public void delete() {
        illegalZoneMapperService.delete(1);
    }


    @Test
    void select() {
//        try {
//            List<IllegalZone> illegalZones = illegalZoneService.gets();
//            System.out.println(objectMapper.writeValueAsString(illegalZones));
//
////            List<String> codes = Lists.newArrayList();
////            codes.add("1111100000");
////            codes.add("5013032026");
////            List<IllegalZone> illegalZones = illegalZoneMapperService.getsByCode(codes);
////            System.out.println(objectMapper.writeValueAsString(illegalZones));
//
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }

        LocalDateTime startTime = LocalDateTime.now();


        List<IllegalZone> illegalZones = illegalZoneService.gets();

        if (!illegalZones.isEmpty()) {
            IllegalZone illegalZone = illegalZones.get(0);

//            GeometryFactory geometryFactory = new GeometryFactory();

//            Point polyInnerPoint = geometryFactory.createPoint(new Coordinate(126.7936133, 35.0191631));
//            Point polyInnerPoint2 = geometryFactory.createPoint(new Coordinate(126.7936284, 35.0196371));
//            Point polyInnerPoint3 = geometryFactory.createPoint(new Coordinate(126.7936222, 35.0197501));
//            Point polyInnerPoint4 = geometryFactory.createPoint(new Coordinate(126.7936031, 35.0199252));
//            Point polyInnerPoint5 = geometryFactory.createPoint(new Coordinate(126.7916735, 35.0205684));
//            Point polyInnerPoint6 = geometryFactory.createPoint(new Coordinate(126.7916886, 35.0206192));
//            Point polyInnerPoint7 = geometryFactory.createPoint(new Coordinate(126.7922889, 35.0206205));
//            Point polyInnerPoint8 = geometryFactory.createPoint(new Coordinate(126.7923496, 35.0206148));
//            Point polyInnerPoint9 = geometryFactory.createPoint(new Coordinate(126.792455, 35.0206217));
//            Point polyInnerPoint10 = geometryFactory.createPoint(new Coordinate(126.7927074, 35.0206219));

//            Polygon polygon;
//            try {
//                polygon = (Polygon) new WKTReader().read(String.format(illegalZone.getPolygon()));
//            } catch (ParseException e) {
//                throw new RuntimeException(e);
//            }

//            boolean innerWithin = polyInnerPoint.within(polygon);
//            boolean innerWithin2 = polyInnerPoint2.within(polygon);
//            boolean innerWithin3 = polyInnerPoint3.within(polygon);
//            boolean innerWithin4 = polyInnerPoint4.within(polygon);
//            boolean innerWithin5 = polyInnerPoint5.within(polygon);
//            boolean innerWithin6 = polyInnerPoint6.within(polygon);
//            boolean innerWithin7 = polyInnerPoint7.within(polygon);
//            boolean innerWithin8 = polyInnerPoint8.within(polygon);
//            boolean innerWithin9 = polyInnerPoint9.within(polygon);
//            boolean innerWithin10 = polyInnerPoint10.within(polygon);

//            System.out.println("innerWithin :"  + innerWithin);
//            System.out.println("innerWithin2 :" + innerWithin2);
//            System.out.println("innerWithin3 :" + innerWithin3);
//            System.out.println("innerWithin4 :" + innerWithin4);
//            System.out.println("innerWithin5 :" + innerWithin5);
//            System.out.println("innerWithin6 :" + innerWithin6);
//            System.out.println("innerWithin7 :" + innerWithin7);
//            System.out.println("innerWithin8 :" + innerWithin8);
//            System.out.println("innerWithin9 :" + innerWithin9);
//            System.out.println("innerWithin10 :" + innerWithin10);


            LocalDateTime endTime = startTime.plusNanos(843137163L);


            System.out.println("시작 시간 = " + StringUtil.convertDatetimeToString(startTime, "yyyy-MM-dd hh:mm:ss.SSS"));
            System.out.println("종료 시간 = " + StringUtil.convertDatetimeToString(endTime, "yyyy-MM-dd hh:mm:ss.SSS"));
            Duration duration = Duration.between(startTime, endTime);
            System.out.println("소요 시간 = 0." + duration.getNano());

        }

    }
}
