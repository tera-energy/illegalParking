package com.teraenergy.illegalparking.model.entity.comment.service;

import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;

import java.util.List;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface CommentService {

    List<Comment> gets(List<Integer> receiptSeqs);

    List<Comment> gets(Integer receiptSeq);

    Comment getByOneMinute(Integer receiptSeq);

    Comment set(Comment comment);

    List<Comment> sets(List<Comment> comments);
}
