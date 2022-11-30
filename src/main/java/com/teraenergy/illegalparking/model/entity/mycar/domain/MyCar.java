package com.teraenergy.illegalparking.model.entity.mycar.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Date : 2022-10-18
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity(name = "my_car")
public class MyCar {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer carSeq;

    @Column (nullable = false)
    String carNum;

    @Column
    String carName;

    @Column
    String carGrade;

    @Column(nullable = false)
    boolean isAlarm = false;

    @Column(nullable = false)
    Integer userSeq;

    @Column(nullable = false)
    boolean isDel = false;

    @Column
    LocalDateTime delDt;

    public void setIsAlarm(boolean isAlarm) {
        this.isAlarm = isAlarm;
    }
}
