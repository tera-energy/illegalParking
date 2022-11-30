package com.teraenergy.illegalparking.local;

import com.teraenergy.illegalparking.encrypt.YoungEncoder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RunWith(SpringRunner.class)
public class betweenTime {

    @Test
    public void inTime(){
        LocalDateTime now = LocalDateTime.now();

//        String nowStr = now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        LocalDateTime start = LocalDateTime.parse("2022-10-04 11:26", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        LocalDateTime end = LocalDateTime.parse("2022-10-04 11:30", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        if ( !now.isBefore(start) && now.isBefore(end)) {
            System.out.println( " in exist ... ");
        } else {
            System.out.println( " not in exist ... ");
        }

    }

    @Test
    public void decryptedTest() {
        String pw = "zDKudTiClBmLEFLvncNPmi8wRo+c0O34LhQ+99AX/MU=";
        System.out.println(YoungEncoder.decrypt(pw));
    }
}
