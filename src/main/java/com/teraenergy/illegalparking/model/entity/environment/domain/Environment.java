package com.teraenergy.illegalparking.model.entity.environment.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Date : 2022-09-25
 * Author : young
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity
public class Environment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer environmentSeq;

    @Column
    LocalDateTime regDt;

}
