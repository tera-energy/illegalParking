package com.teraenergy.illegalparking.model.entity.comment.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.entity.comment.domain.Comment;
import com.teraenergy.illegalparking.model.entity.comment.domain.QComment;
import com.teraenergy.illegalparking.model.entity.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Date : 2022-10-07
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class CommentServiceImple implements CommentService{

    private final CommentRepository commentRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Comment> gets(List<Integer> receiptSeqs) {
        JPAQuery query = jpaQueryFactory.selectFrom(QComment.comment);
        query.where(QComment.comment.receiptSeq.in(receiptSeqs));
        query.where(QComment.comment.isDel.isFalse());
        return query.fetch();
    }

    @Override
    public List<Comment> gets(Integer receiptSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QComment.comment);
        query.where(QComment.comment.receiptSeq.eq(receiptSeq));
        query.where(QComment.comment.isDel.isFalse());
        return query.fetch();
    }

    @Override
    public Comment getByOneMinute(Integer receiptSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QComment.comment);
        query.where(QComment.comment.receiptSeq.eq(receiptSeq));
        query.where(QComment.comment.content.eq(TeraExceptionCode.REPORT_OCCUR_ONE.getMessage()).
                or(QComment.comment.content.eq(TeraExceptionCode.REPORT_OCCUR_FIVE.getMessage()))
        );
        query.where(QComment.comment.isDel.isFalse());
        return query.fetchOne() == null ? null : (Comment) query.fetchOne();
    }

    @Override
    public Comment set(Comment comment) {
        return commentRepository.save(comment);
    }

    @Override
    public List<Comment> sets(List<Comment> comments) {
        return commentRepository.saveAll(comments);
    }

}
