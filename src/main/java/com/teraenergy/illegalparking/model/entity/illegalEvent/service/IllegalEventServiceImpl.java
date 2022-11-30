package com.teraenergy.illegalparking.model.entity.illegalEvent.service;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.QIllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalEvent.repository.IllegalEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Date : 2022-09-29
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Service
public class IllegalEventServiceImpl implements IllegalEventService {

    private final IllegalEventRepository illegalEventRepository;

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public IllegalEvent get(Integer illegalEventSeq) {
        Optional<IllegalEvent> optional = illegalEventRepository.findById(illegalEventSeq);
        if ( optional.isEmpty() ) {
            return null;
        }
        return optional.get();
    }

    @Override
    public List<IllegalEvent> gets() {
        return illegalEventRepository.findAll();
    }

    @Override
    public IllegalEvent set(IllegalEvent illegalEvent) {
        return illegalEventRepository.save(illegalEvent);
    }

    @Override
    public List<IllegalEvent> sets(List<IllegalEvent> illegalEvents) {
        return illegalEventRepository.saveAll(illegalEvents);
    }

    @Override
    public long getSizeInGroup(Integer groupSeq) {
        JPAQuery query = jpaQueryFactory.selectFrom(QIllegalEvent.illegalEvent);
        query.where(QIllegalEvent.illegalEvent.groupSeq.eq(groupSeq));
        return query.fetch().size();
    }

}
