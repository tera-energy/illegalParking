package com.teraenergy.illegalparking.model.entity.illegalEvent.service;

import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;

import java.util.List;

/**
 * Date : 2022-09-29
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public interface IllegalEventService {

    IllegalEvent get(Integer illegalEventSeq);

    List<IllegalEvent> gets();

    IllegalEvent set(IllegalEvent illegalEvent);

    List<IllegalEvent> sets(List<IllegalEvent> illegalEvents);

    long getSizeInGroup(Integer groupSeq);

}
