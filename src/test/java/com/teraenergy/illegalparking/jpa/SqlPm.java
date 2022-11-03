package com.teraenergy.illegalparking.jpa;

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
import java.util.List;

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

    @Test
    public void insert(){
        List<Pm> pms = Lists.newArrayList();

        // X :127.695368, Y :34.9421689
//        X :127.695806, Y :34.9421754
        // X :126.793837, Y :35.0203561

//        Pm kick = new Pm();
//        kick.setPmId("SWING-10A-BAC-ADFAEDF");
//        kick.setPmName("킥보드");
//        kick.setPmPrice(700);
//        kick.setLatitude( 127.695368 );
//        kick.setLongitude( 34.9421689);
//        kick.setPmOperOpenHhmm("12:00");
//        kick.setPmOperCloseHhmm("18:00");
//        kick.setCode("26775");
//        kick.setPmType(PmType.KICK);
//        pms.add(kick);

//        Pm bike = new Pm();
//        bike.setPmId("GER-202305-AA");
//        bike.setPmName("자전거");
//        bike.setPmPrice(0);
//        bike.setLatitude(127.695806);
//        bike.setLongitude( 34.9421689);
//        bike.setPmOperOpenHhmm("00:00");
//        bike.setPmOperCloseHhmm("23:00");
//        bike.setCode("26775");
//        bike.setPmType(PmType.BIKE);
//        pms.add(bike);

        Pm najuKick = new Pm();
        najuKick.setPmId("SWING-10A-BAC-ADFAEDF1");
        najuKick.setPmName("킥보드2");
        najuKick.setPmPrice(700);
        najuKick.setLatitude(126.793837);
        najuKick.setLongitude(35.0203561);
        najuKick.setPmOperOpenHhmm("12:00");
        najuKick.setPmOperCloseHhmm("18:00");
        najuKick.setCode("26775");
        najuKick.setPmType(PmType.KICK);
        pms.add(najuKick);


        pmService.sets(pms);

    }
}
