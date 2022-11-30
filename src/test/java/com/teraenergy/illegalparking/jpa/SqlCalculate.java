package com.teraenergy.illegalparking.jpa;

import com.teraenergy.illegalparking.ApplicationTests;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Calculate;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import com.teraenergy.illegalparking.model.entity.point.domain.Point;
import com.teraenergy.illegalparking.model.entity.product.domain.Product;
import com.teraenergy.illegalparking.model.entity.product.enums.Brand;
import com.teraenergy.illegalparking.model.entity.point.enums.PointType;
import com.teraenergy.illegalparking.model.entity.calculate.service.CalculateService;
import com.teraenergy.illegalparking.model.entity.point.service.PointService;
import com.teraenergy.illegalparking.model.entity.product.service.ProductService;
import com.teraenergy.illegalparking.model.entity.report.domain.Report;
import com.teraenergy.illegalparking.model.entity.report.service.ReportService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.parameters.P;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
@Transactional
public class SqlCalculate {

    @Autowired
    private CalculateService calculateService;

    @Autowired
    private PointService pointService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReportService reportService;

    @Test
    public void insert(){
        try {
            insertByProduct();
            insertPointByReport();
            insertByCalculate();
        } catch (TeraException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void insertByProduct() throws TeraException {
        List<Product> products = Lists.newArrayList();

        Product product = new Product();
        product.setUserSeq(1);
        product.setPointValue(500L);
        product.setName("아메리카노");
        product.setBrand(Brand.STARBUGS);
        product.setThumbnail("sample1");

        Product product2 = new Product();
        product2.setUserSeq(1);
        product2.setPointValue(1000L);
        product2.setName("아이스크림");
        product2.setBrand(Brand.BASKINROBBINS);
        product2.setThumbnail("sample2");;


        products.add(product);
        products.add(product2);
        productService.sets(products);
    }

    @Test
    public void insertPointByReport(){

        List<Point> points = Lists.newArrayList();

        Point point = new Point();
        point.setValue(1000L);
        point.setResidualValue(1000L);
        point.setUseValue(1000L);
        point.setPointType(PointType.PLUS);
        point.setNote("");
        point.setIsPointLimit(false);
        point.setIsTimeLimit(false);
        point.setStartDate(LocalDate.now().minusDays(10));
        point.setStopDate(LocalDate.now().plusDays(10));
        points.add(point);

        pointService.sets(points);
    }

    @Test
    public void insertByCalculate() throws TeraException {
        Product product = productService.get(1);

        Calculate calculate = new Calculate();
        calculate.setRegDt(LocalDateTime.now());
        calculate.setUserSeq(2);
        calculate.setPointType(PointType.MINUS);
        long eventPoint = product.getPointValue();
        long oldCurrentPointPoint = calculate.getCurrentPointValue() == null ? 0L : calculate.getCurrentPointValue();
        long newCurrentPointPoint = oldCurrentPointPoint + eventPoint;
        calculate.setCurrentPointValue(newCurrentPointPoint);
        calculate.setEventPointValue(eventPoint);
        calculate.setLocationType(LocationType.JEONNAM);
        calculateService.set(calculate);
    }

}
