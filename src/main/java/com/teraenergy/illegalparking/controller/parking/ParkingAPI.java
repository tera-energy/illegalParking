package com.teraenergy.illegalparking.controller.parking;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.parking.domain.Parking;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;

import java.util.List;

import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.view.RedirectView;

/**
 * Date : 2022-09-20
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class ParkingAPI {

    private final ParkingService parkingService;

    @PostMapping("/parking/get")
    @ResponseBody
    public Object getParking(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        Integer parkingSeq = jsonNode.get("parkingSeq").asInt();
        return parkingService.get(parkingSeq);
    }

    @PostMapping("/parking/gets")
    @ResponseBody
    public Object getsParking(@RequestBody String body) throws TeraException {
        JsonNode jsonNode = JsonUtil.toJsonNode(body);
        List<String> codes = Lists.newArrayList();
        JsonNode codesArrNode = jsonNode.get("codes");
        if(codesArrNode.isArray()) {
            for (JsonNode obj : codesArrNode) {
                codes.add(obj.asText());
            }
        }
        return parkingService.gets(codes);
    }

    @PostMapping("/parking/set")
    @ResponseBody
    public Object setParking(@RequestBody String body) throws TeraException {
        Parking parking = JsonUtil.toObject(body, Parking.class );
        return parkingService.set(parking);
    }

    @PostMapping("/api/parking/jusoPopup")
//    @ResponseBody
    public RedirectView getJusoPopup(@RequestBody String body) throws TeraException {
        System.out.println(body);
        return new RedirectView("/api/parking/jusoPopup");
    }


}
