
package com.teraenergy.illegalparking.jpa;

import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.model.entity.lawdong.domain.LawDong;
import com.teraenergy.illegalparking.model.entity.lawdong.service.LawDongService;
import com.teraenergy.illegalparking.model.entity.pm.domain.Pm;
import com.teraenergy.illegalparking.model.entity.pm.enums.PmType;
import com.teraenergy.illegalparking.model.entity.pm.service.PmService;
import org.apache.commons.compress.utils.Lists;
import org.junit.jupiter.api.Test;
import com.teraenergy.illegalparking.ApplicationTests;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.transaction.Transactional;
import java.util.*;

/**
 * Date : 2022-11-03
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@ActiveProfiles(value = "debug")
@SpringBootTest(classes = ApplicationTests.class)
@RunWith(SpringRunner.class)
//@Transactional
public class SqlPm {

    @Autowired
    PmService pmService;

    @Autowired
    LawDongService lawDongService;

    @Test
    public void insert() {
        List<Pm> pms = Lists.newArrayList();

        // X :127.695368, Y :34.9421689
//        X :127.695806, Y :34.9421754
        // X :126.793837, Y :35.0203561

        Pm kick = new Pm();
        kick.setPmId("SWING-10A-BAC-ADFAEDF");
        kick.setPmName("킥보드");
        kick.setPmPrice(700);
        kick.setLatitude(34.9421689);
        kick.setLongitude(127.695368);
        kick.setPmOperOpenHhmm("12:00");
        kick.setPmOperCloseHhmm("18:00");
        kick.setCode("4623010600");
        kick.setPmType(PmType.KICK);
        pms.add(kick);

        Pm bike = new Pm();
        bike.setPmId("GER-202305-AA");
        bike.setPmName("자전거");
        bike.setPmPrice(0);
        bike.setLatitude(34.9421689);
        bike.setLongitude(127.695806);
        bike.setPmOperOpenHhmm("00:00");
        bike.setPmOperCloseHhmm("23:00");
        bike.setCode("4623010600");
        bike.setPmType(PmType.BIKE);
        pms.add(bike);

        Pm najuKick = new Pm();
        najuKick.setPmId("SWING-10A-BAC-ADFAEDF1");
        najuKick.setPmName("킥보드2");
        najuKick.setPmPrice(700);
        najuKick.setLatitude(35.0203561);
        najuKick.setLongitude(126.793837);
        najuKick.setPmOperOpenHhmm("12:00");
        najuKick.setPmOperCloseHhmm("18:00");
        najuKick.setCode("4617013400");
        najuKick.setPmType(PmType.KICK);
        pms.add(najuKick);

        Pm najuBike = new Pm();
        najuBike.setPmId("SWING-10A-BAC-ADFAEDF1");
        najuBike.setPmName("킥보드2");
        najuBike.setPmPrice(700);
        najuBike.setLatitude(35.0198785);
        najuBike.setLongitude(126.793926);
        najuBike.setPmOperOpenHhmm("12:00");
        najuBike.setPmOperCloseHhmm("18:00");
        najuBike.setCode("4617013400");
        najuBike.setPmType(PmType.BIKE);
        pms.add(najuBike);

        pmService.sets(pms);
    }

    @Test
    public void kwangyangInsert() {
        List<HashMap<String, Object>> points = Lists.newArrayList();

        HashMap<String, Object> point1 = Maps.newHashMap();  point1.put("lat",34.9411498);	point1.put("long", 127.6952444); point1.put("addr", "전라남도 광양시 중동");points.add(point1);
        HashMap<String, Object> point2 = Maps.newHashMap();  point2.put("lat",34.9424866);	point2.put("long", 127.6961242); point2.put("addr", "전라남도 광양시 중동");points.add(point2);
        HashMap<String, Object> point3 = Maps.newHashMap();  point3.put("lat",34.9397338);	point3.put("long", 127.6943218); point3.put("addr", "전라남도 광양시 중동");points.add(point3);
        HashMap<String, Object> point4 = Maps.newHashMap();  point4.put("lat",34.9396986);	point4.put("long", 127.6981841); point4.put("addr", "전라남도 광양시 중동");points.add(point4);
        HashMap<String, Object> point5 = Maps.newHashMap();  point5.put("lat",34.9411498);	point5.put("long", 127.6986884); point5.put("addr", "전라남도 광양시 중동");points.add(point5);
        HashMap<String, Object> point6 = Maps.newHashMap();  point6.put("lat",34.941141);	point6.put("long", 127.6967679); point6.put("addr", "전라남도 광양시 중동");points.add(point6);
        HashMap<String, Object> point7 = Maps.newHashMap();  point7.put("lat",34.9386168);	point7.put("long", 127.6964461); point7.put("addr", "전라남도 광양시 중동");points.add(point7);
        HashMap<String, Object> point8 = Maps.newHashMap();  point8.put("lat",34.9403319);	point8.put("long", 127.6982378); point8.put("addr", "전라남도 광양시 중동");points.add(point8);
        HashMap<String, Object> point9 = Maps.newHashMap();  point9.put("lat",34.93947);	point9.put("long", 127.6894938); point9.put("addr", "전라남도 광양시 중동");points.add(point9);
        HashMap<String, Object> point10 = Maps.newHashMap(); point10.put("lat",34.9445533);	point10.put("long", 127.6982807); point10.put("addr", "전라남도 광양시 중동");points.add(point10);
        HashMap<String, Object> point11 = Maps.newHashMap(); point11.put("lat",34.9413873);	point11.put("long", 127.6967143); point11.put("addr", "전라남도 광양시 중동");points.add(point11);
        HashMap<String, Object> point12 = Maps.newHashMap(); point12.put("lat",34.9432605);	point12.put("long", 127.6892792); point12.put("addr", "전라남도 광양시 중동");points.add(point12);
        HashMap<String, Object> point13 = Maps.newHashMap(); point13.put("lat",34.9411674);	point13.put("long", 127.6887535); point13.put("addr", "전라남도 광양시 중동");points.add(point13);
        HashMap<String, Object> point14 = Maps.newHashMap(); point14.put("lat",34.9378326);	point14.put("long", 127.699849); point14.put("addr", "전라남도 광양시 중동");points.add(point14);
        HashMap<String, Object> point15 = Maps.newHashMap(); point15.put("lat",34.9442103);	point15.put("long", 127.7036773); point15.put("addr", "전라남도 광양시 중동");points.add(point15);
        HashMap<String, Object> point16 = Maps.newHashMap(); point16.put("lat",34.9500612);	point16.put("long", 127.6883389); point16.put("addr", "전라남도 광양시 중동");points.add(point16);
        HashMap<String, Object> point17 = Maps.newHashMap(); point17.put("lat",34.9500814);	point17.put("long", 127.6979695); point17.put("addr", "전라남도 광양시 중동");points.add(point17);
        HashMap<String, Object> point18 = Maps.newHashMap(); point18.put("lat",34.9447996);	point18.put("long", 127.6950299); point18.put("addr", "전라남도 광양시 중동");points.add(point18);
        HashMap<String, Object> point19 = Maps.newHashMap(); point19.put("lat",34.941524);	point19.put("long", 127.7080973); point19.put("addr", "전라남도 광양시 마동");points.add(point19);
        HashMap<String, Object> point20 = Maps.newHashMap(); point20.put("lat",34.9454592);	point20.put("long", 127.69459); point20.put("addr", "전라남도 광양시 중동 ");points.add(point20);
        HashMap<String, Object> point21 = Maps.newHashMap(); point21.put("lat",34.9434452);	point21.put("long", 127.699536); point21.put("addr", "전라남도 광양시 중동");points.add(point21);
        HashMap<String, Object> point22 = Maps.newHashMap(); point22.put("lat",34.9500673);	point22.put("long", 127.6980017); point22.put("addr", "전라남도 광양시 중동");points.add(point22);
        HashMap<String, Object> point23 = Maps.newHashMap(); point23.put("lat",34.9483322);	point23.put("long", 127.6898505); point23.put("addr", "전라남도 광양시 중동");points.add(point23);
        HashMap<String, Object> point24 = Maps.newHashMap(); point24.put("lat",34.9330642);	point24.put("long", 127.70101); point24.put("addr", "전라남도 광양시 중동");points.add(point24);
        HashMap<String, Object> point25 = Maps.newHashMap(); point25.put("lat",34.941783);	point25.put("long", 127.6952337); point25.put("addr", "전라남도 광양시 중동");points.add(point25);
        HashMap<String, Object> point26 = Maps.newHashMap(); point26.put("lat",34.9430143);	point26.put("long", 127.6936673); point26.put("addr", "전라남도 광양시 중동");points.add(point26);
        HashMap<String, Object> point27 = Maps.newHashMap(); point27.put("lat",34.9433485);	point27.put("long", 127.6979481); point27.put("addr", "전라남도 광양시 중동");points.add(point27);
        HashMap<String, Object> point28 = Maps.newHashMap(); point28.put("lat",34.9421876);	point28.put("long", 127.6927553); point28.put("addr", "전라남도 광양시 중동");points.add(point28);
        HashMap<String, Object> point29 = Maps.newHashMap(); point29.put("lat",34.9409035);	point29.put("long", 127.69253); point29.put("addr", "전라남도 광양시 중동");points.add(point29);
        HashMap<String, Object> point30 = Maps.newHashMap(); point30.put("lat",34.9409123);	point30.put("long", 127.6936029); point30.put("addr", "전라남도 광양시 중동");points.add(point30);
        HashMap<String, Object> point31 = Maps.newHashMap(); point31.put("lat",34.9397426);	point31.put("long", 127.6958453); point31.put("addr", "전라남도 광양시 중동");points.add(point31);
        HashMap<String, Object> point32 = Maps.newHashMap(); point32.put("lat",34.9450018);	point32.put("long", 127.697991); point32.put("addr", "전라남도 광양시 중동");points.add(point32);
        HashMap<String, Object> point33 = Maps.newHashMap(); point33.put("lat",34.9403495);	point33.put("long", 127.6958667); point33.put("addr", "전라남도 광양시 중동");points.add(point33);
        HashMap<String, Object> point34 = Maps.newHashMap(); point34.put("lat",34.9409651);	point34.put("long", 127.6973258); point34.put("addr", "전라남도 광양시 중동");points.add(point34);
        HashMap<String, Object> point35 = Maps.newHashMap(); point35.put("lat",34.9413697);	point35.put("long", 127.6957702); point35.put("addr", "전라남도 광양시 중동");points.add(point35);
        HashMap<String, Object> point36 = Maps.newHashMap(); point36.put("lat",34.9478659);	point36.put("long", 127.7038248); point36.put("addr", "전라남도 광양시 마동");points.add(point36);
        HashMap<String, Object> point37 = Maps.newHashMap(); point37.put("lat",34.9463914);	point37.put("long", 127.6913284); point37.put("addr", "전라남도 광양시 중동");points.add(point37);
        HashMap<String, Object> point38 = Maps.newHashMap(); point38.put("lat",34.9444742);	point38.put("long", 127.6862322); point38.put("addr", "전라남도 광양시 중동");points.add(point38);
        HashMap<String, Object> point39 = Maps.newHashMap(); point39.put("lat",34.936212);	point39.put("long", 127.6820624); point39.put("addr", "전라남도 광양시 도이동");points.add(point39);
        HashMap<String, Object> point40 = Maps.newHashMap(); point40.put("lat",34.9434452);	point40.put("long", 127.6941072); point40.put("addr", "전라남도 광양시 중동");points.add(point40);
        HashMap<String, Object> point41 = Maps.newHashMap(); point41.put("lat",34.9445357);	point41.put("long", 127.6917254); point41.put("addr", "전라남도 광양시 중동");points.add(point41);
        HashMap<String, Object> point42 = Maps.newHashMap(); point42.put("lat",34.9454504);	point42.put("long", 127.6936458); point42.put("addr", "전라남도 광양시 중동");points.add(point42);
        HashMap<String, Object> point43 = Maps.newHashMap(); point43.put("lat",34.9406749);	point43.put("long", 127.6940643); point43.put("addr", "전라남도 광양시 중동");points.add(point43);
        HashMap<String, Object> point44 = Maps.newHashMap(); point44.put("lat",34.9397602);	point44.put("long", 127.6927339); point44.put("addr", "전라남도 광양시 중동");points.add(point44);
        HashMap<String, Object> point45 = Maps.newHashMap(); point45.put("lat",34.9389159);	point45.put("long", 127.6923798); point45.put("addr", "전라남도 광양시 중동");points.add(point45);
        HashMap<String, Object> point46 = Maps.newHashMap(); point46.put("lat",34.9406661);	point46.put("long", 127.7001797); point46.put("addr", "전라남도 광양시 중동");points.add(point46);
        HashMap<String, Object> point47 = Maps.newHashMap(); point47.put("lat",34.9393556);	point47.put("long", 127.6960491); point47.put("addr", "전라남도 광양시 중동");points.add(point47);
        HashMap<String, Object> point48 = Maps.newHashMap(); point48.put("lat",34.9424338);	point48.put("long", 127.6992141); point48.put("addr", "전라남도 광양시 중동");points.add(point48);

        List<Pm> pms = Lists.newArrayList();

        int len = 0;


        for( HashMap<String, Object> point : points) {
            Pm pm = new Pm();

            if (len % 2 == 1 ) {
                pm.setPmPrice(0);
                pm.setPmType(PmType.BIKE);
                pm.setPmName("따릉이");
                pm.setPmId("Bike-"+ UUID.randomUUID().toString().substring(19).toUpperCase(Locale.ROOT));
            } else {
                pm.setPmPrice(700);
                pm.setPmType(PmType.KICK);
                pm.setPmName("SWING");
                pm.setPmId("SWING-"+ UUID.randomUUID().toString());
            }

            LawDong lawDong = lawDongService.getFromLnmadr((String) point.get("addr"));
            pm.setCode(lawDong.getCode());
            pm.setPmOperOpenHhmm("00:00");
            pm.setPmOperCloseHhmm("23:59");
            pm.setLatitude((Double) point.get("lat"));
            pm.setLongitude((Double) point.get("long"));
            pms.add(pm);

            len ++;
        }

        pmService.sets(pms);
    }

    @Test
    public void kwangyangInsert2() {
        List<HashMap<String, Object>> points = Lists.newArrayList();

        HashMap<String, Object> point1 = Maps.newHashMap();  point1.put("lat", 34.9343181);	point1.put("long",  127.6994121); point1.put("addr",  "전라남도 광양시 중동");points.add(point1);
        HashMap<String, Object> point2 = Maps.newHashMap();  point2.put("lat", 34.9351105);	point2.put("long",  127.7004632); point2.put("addr",  "전라남도 광양시 중동");points.add(point2);
        HashMap<String, Object> point3 = Maps.newHashMap();  point3.put("lat", 34.9339641);	point3.put("long",  127.7005289); point3.put("addr",  "전라남도 광양시 중동");points.add(point3);
        HashMap<String, Object> point4 = Maps.newHashMap();  point4.put("lat", 34.9335563);	point4.put("long",  127.7004538); point4.put("addr",  "전라남도 광양시 중동");points.add(point4);
        HashMap<String, Object> point5 = Maps.newHashMap();  point5.put("lat", 34.9357799);	point5.put("long",  127.6990085); point5.put("addr",  "전라남도 광양시 중동");points.add(point5);
        HashMap<String, Object> point6 = Maps.newHashMap();  point6.put("lat", 34.9346335);	point6.put("long",  127.6990555); point6.put("addr",  "전라남도 광양시 중동");points.add(point6);
        HashMap<String, Object> point7 = Maps.newHashMap();  point7.put("lat", 34.9325178);	point7.put("long",  127.6919274); point7.put("addr",  "전라남도 광양시 중동");points.add(point7);
        HashMap<String, Object> point8 = Maps.newHashMap();  point8.put("lat", 34.9328384);	point8.put("long",  127.691579);  point8.put("addr",  "전라남도 광양시 중동");points.add(point8);
        HashMap<String, Object> point9 = Maps.newHashMap();  point9.put("lat", 34.932466);	point9.put("long",  127.692315);  point9.put("addr",  "전라남도 광양시 중동");points.add(point9);
        HashMap<String, Object> point10 = Maps.newHashMap(); point10.put("lat",34.940397);	point10.put("long", 127.6926882); point10.put("addr", "전라남도 광양시 중동");points.add(point10);
        HashMap<String, Object> point11 = Maps.newHashMap(); point11.put("lat",34.9405785);	point11.put("long", 127.6927255); point11.put("addr", "전라남도 광양시 중동");points.add(point11);
        HashMap<String, Object> point12 = Maps.newHashMap(); point12.put("lat",34.9404215);	point12.put("long", 127.6922006); point12.put("addr", "전라남도 광양시 중동");points.add(point12);
        HashMap<String, Object> point13 = Maps.newHashMap(); point13.put("lat",34.9406079);	point13.put("long", 127.6927106); point13.put("addr", "전라남도 광양시 중동");points.add(point13);

        List<Pm> pms = Lists.newArrayList();

        int len = 0;


        for( HashMap<String, Object> point : points) {
            Pm pm = new Pm();

            if (len % 2 == 1 ) {
                pm.setPmPrice(0);
                pm.setPmType(PmType.BIKE);
                pm.setPmName("따릉이");
                pm.setPmId("Bike-"+ UUID.randomUUID().toString().substring(19).toUpperCase(Locale.ROOT));
            } else {
                pm.setPmPrice(700);
                pm.setPmType(PmType.KICK);
                pm.setPmName("SWING");
                pm.setPmId("SWING-"+ UUID.randomUUID().toString());
            }

            LawDong lawDong = lawDongService.getFromLnmadr((String) point.get("addr"));
            pm.setCode(lawDong.getCode());
            pm.setPmOperOpenHhmm("00:00");
            pm.setPmOperCloseHhmm("23:59");
            pm.setLatitude((Double) point.get("lat"));
            pm.setLongitude((Double) point.get("long"));
            pms.add(pm);

            len ++;
        }

        pmService.sets(pms);
    }


}
