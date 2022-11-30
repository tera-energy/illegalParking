package com.teraenergy.illegalparking.model.entity.illegalzone.domain;

import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "illegal_zone")
public class IllegalZone {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Integer zoneSeq;    // 불법 주정차 구역 키

    @Column(nullable = false)
    String polygon;     // 불법 주정차 구역

    @Column(nullable = false)
    String code;    // 법정동 키

    @Column(nullable = false)
    Boolean isDel;      // 삭제 여부

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "eventSeq" )
    IllegalEvent illegalEvent;

    @Transient
    Integer eventSeq;

}
