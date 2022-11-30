package com.teraenergy.illegalparking.model.entity.comment.domain;

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
@Entity (name = "comment")
public class Comment {

    @Id
    @GeneratedValue (strategy = GenerationType.AUTO)
    Integer commentSeq;

    @Column (nullable = false)
    Integer receiptSeq;

    @Column (nullable = false)
    String content;

    @Column
    LocalDateTime regDt = LocalDateTime.now();

    @Column (nullable = false)
    boolean isDel = false;

    @Column
    LocalDateTime delDt;
}
