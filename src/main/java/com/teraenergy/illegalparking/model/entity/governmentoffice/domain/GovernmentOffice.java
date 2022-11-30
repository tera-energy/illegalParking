package com.teraenergy.illegalparking.model.entity.governmentoffice.domain;

import com.teraenergy.illegalparking.model.entity.illegalzone.enums.LocationType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity (name = "government_office")
public class GovernmentOffice {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    Integer officeSeq;

    @Column
    String name;

    @Column
    LocationType locationType;

    @Column
    Boolean isDel = false;

    @Column
    LocalDateTime DelDt;

}
