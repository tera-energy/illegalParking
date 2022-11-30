package com.teraenergy.illegalparking.model.entity.userGroup.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Date : 2022-10-12
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Getter
@Setter
@Entity(name = "user_group")
public class UserGroup {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    Integer userGroupSeq;

    @Column (nullable = false)
    Integer userSeq;

    @Column (nullable = false)
    Integer groupSeq;

}
