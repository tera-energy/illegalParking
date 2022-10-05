package com.teraenergy.illegalparking.controller.area;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.model.dto.illegalzone.domain.IllegalZoneDto;
import com.teraenergy.illegalparking.model.dto.illegalzone.service.IllegalZoneDtoService;
import com.teraenergy.illegalparking.model.entity.calculate.domain.Product;
import com.teraenergy.illegalparking.model.entity.environment.enums.ZoneGroupType;
import com.teraenergy.illegalparking.model.entity.illegalEvent.domain.IllegalEvent;
import com.teraenergy.illegalparking.model.entity.illegalEvent.service.IllegalEventService;
import com.teraenergy.illegalparking.model.entity.illegalzone.domain.IllegalZone;
import com.teraenergy.illegalparking.model.entity.illegalzone.enums.IllegalType;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneMapperService;
import com.teraenergy.illegalparking.model.entity.illegalzone.service.IllegalZoneService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Date : 2022-09-14
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@RequiredArgsConstructor
@Controller
public class AreaAPI {

    private final ObjectMapper objectMapper;

    private final IllegalZoneService illegalZoneService;

    private final IllegalZoneMapperService illegalZoneMapperService;

    private final IllegalZoneDtoService illegalZoneDtoService;

    private final IllegalEventService illegalEventService;

    @PostMapping("/area/markers")
    @ResponseBody
    public JsonNode markers(@RequestParam(value = "dongId", defaultValue = "1") String dongId) throws JsonProcessingException {
        HashMap<String, String> result = Maps.newHashMap();
        String jsonStr = objectMapper.writeValueAsString(result);
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/area/coordinates")
    @ResponseBody
    public JsonNode coordinates(@RequestParam(value = "dongId", defaultValue = "1") String dongId) throws JsonProcessingException {
        HashMap<String, String> result = Maps.newHashMap();
        String jsonStr = objectMapper.writeValueAsString(result);
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/area/zone/get")
    @ResponseBody
    public JsonNode getZone(HttpServletRequest request, @RequestBody String body) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(body);

        Integer zoneSeq = jsonNode.get("zoneSeq").asInt();
        IllegalZone illegalZone = illegalZoneService.get(zoneSeq);

//        IllegalZone illegalZone = illegalZoneMapperService.get(zoneSeq);
//        IllegalEvent illegalEvent = illegalEventService.get(illegalZone.getEventSeq());
//        illegalZone.setIllegalEvent(illegalEvent);

        String jsonStr = objectMapper.writeValueAsString(illegalZone);

//        String jsonStr = objectMapper.writeValueAsString(illegalZoneMapperService.get(jsonNode.get("zoneSeq").asInt()));
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/area/zone/gets")
    @ResponseBody
    public JsonNode getsZone(HttpServletRequest request, @RequestBody String body) throws Exception {
        JsonNode jsonNode = objectMapper.readTree(body);
        String jsonStr = objectMapper.writeValueAsString(_getZone(jsonNode));
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/area/zone/set")
    @ResponseBody
    public JsonNode setZone(@RequestBody Map<String, Object> param) throws JsonProcessingException {
        Map<String, String> map = Maps.newHashMap();
        try {
            List<Map<String, Object>> polygons = (List<Map<String, Object>>) param.get("polygonData");

            List<IllegalZone> illegalZones = Lists.newArrayList();
            StringBuilder stringBuilder;

            for (Map<String, Object> dataMap : polygons) {
                List<Object> pointList = (List<Object>) dataMap.get("points");
                stringBuilder = new StringBuilder();
                stringBuilder.append("POLYGON((");

                for (Object point : pointList) {
                    stringBuilder.append(((Map<String, Long>) point).get("x"));
                    stringBuilder.append(" ");
                    stringBuilder.append(((Map<String, Long>) point).get("y"));
                    stringBuilder.append(",");
                }

                stringBuilder.append(((Map<String, Long>) pointList.get(0)).get("x"));
                stringBuilder.append(" ");
                stringBuilder.append(((Map<String, Long>) pointList.get(0)).get("y"));
                stringBuilder.append("))");

                IllegalZone illegalZone = new IllegalZone();
                illegalZone.setPolygon(stringBuilder.toString());
//                illegalZone.setName("");
//                illegalZone.setIllegalTypeSeq(Integer.parseInt((String) param.get("illegalType")));
                illegalZone.setCode((String) dataMap.get("code"));
                illegalZone.setIsDel(false);
                illegalZones.add(illegalZone);

                stringBuilder.setLength(0);
            }

            illegalZoneMapperService.sets(illegalZones);

            map.put("success","true");
        } catch (Exception e) {
            map.put("success","false");
        }

        String jsonStr = objectMapper.writeValueAsString(map);
        return objectMapper.readTree(jsonStr);

    }

    @PostMapping("/area/zone/modify")
    @ResponseBody
    public JsonNode modifyZone(@RequestBody String body) throws Exception {
        Map<String, String> map = Maps.newHashMap();
        try {
            JsonNode jsonNode = objectMapper.readTree(body);

            IllegalZone illegalZone = illegalZoneMapperService.get(jsonNode.get("zoneSeq").asInt());

            IllegalEvent illegalEvent = new IllegalEvent();
            illegalEvent.setIllegalType(
                IllegalType.valueOf(jsonNode.get("illegalType").asText()));
            illegalEvent.setZoneGroupType(
                ZoneGroupType.valueOf(jsonNode.get("zoneGroupType").asText()));
            illegalEvent.setName(jsonNode.get("name").asText());
            illegalEvent.setUsedFirst(jsonNode.get("usedFirst").asBoolean());
            illegalEvent.setFirstStartTime(jsonNode.get("firstStartTime").asText());
            illegalEvent.setFirstEndTime(jsonNode.get("firstEndTime").asText());
            illegalEvent.setUsedSecond(jsonNode.get("usedSecond").asBoolean());
            illegalEvent.setSecondStartTime(jsonNode.get("secondStartTime").asText());
            illegalEvent.setSecondEndTime(jsonNode.get("secondEndTime").asText());
            if(illegalZone.getEventSeq() != null) {
                illegalEvent.setEventSeq(illegalZone.getEventSeq());
            }
            illegalEvent = illegalEventService.set(illegalEvent);
            illegalZoneMapperService.modifyByEvent(jsonNode.get("zoneSeq").asInt(), illegalEvent.getEventSeq());
            map.put("success","true");
        } catch (Exception e) {
            map.put("success","false");
        }
        String jsonStr = objectMapper.writeValueAsString(map);
        return objectMapper.readTree(jsonStr);
    }

    @PostMapping("/area/zone/remove")
    @ResponseBody
    public JsonNode removeZone(@RequestBody String body) throws Exception {
        Map<String, String> map = Maps.newHashMap();
        try {
            JsonNode jsonNode = objectMapper.readTree(body);
            illegalZoneMapperService.delete(jsonNode.get("zoneSeq").asInt());
            map.put("success","true");
        } catch (Exception e) {
            map.put("success","false");
        }
        String jsonStr = objectMapper.writeValueAsString(map);
        return objectMapper.readTree(jsonStr);
    }

    private HashMap<String, Object> _getParam(HttpServletRequest request) {
        HashMap<String, Object> parameterMap = Maps.newHashMap();
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            String value = request.getParameter(name);
            parameterMap.put(name, value);
        }
        return parameterMap;
    }

    private Map<String, Object> _getZone(JsonNode param) throws ParseException {
        String select = param.get("select").asText();
        List<String> codes = Lists.newArrayList();
        if("dong".equals(select) || "typeAndDong".equals(select)) {
            JsonNode codesArrNode = param.get("codes");
            if(codesArrNode.isArray()) {
                for (JsonNode obj : codesArrNode) {
                    codes.add(obj.asText());
                }
            }
        }

        List<IllegalZone> illegalZones = null;
        switch (select) {
            case "type":
                illegalZones = illegalZoneMapperService.getsByIllegalType(param.get("illegalType").asText());
                break;
            case "dong":
                illegalZones = illegalZoneMapperService.getsByCode(codes);
                break;
            case "typeAndDong":
                illegalZones = illegalZoneMapperService.getsByIllegalTypeAndCode(param.get("illegalType").asText(), codes);
                break;
            default :
                illegalZones = illegalZoneMapperService.gets();
                break;
        }

        List<Integer> zoneSeqs = Lists.newArrayList();
        List<String> zoneTypes = Lists.newArrayList();
        List<String> polygons = Lists.newArrayList();

        for (IllegalZone illegalZone : illegalZones) {
            Polygon polygon = (Polygon) new WKTReader().read(illegalZone.getPolygon());
            StringBuilder builder = new StringBuilder();
            int first = 0;
            Coordinate firstCoordinate = null;
            for (Coordinate coordinate : polygon.getCoordinates()) {
                if ( first == 0) {
                    firstCoordinate = coordinate;
                }
                builder.append(coordinate.getX())
                        .append(" ")
                        .append(coordinate.getY()).append(",");
                first ++;
            }
            builder.append(firstCoordinate.getX())
                    .append(" ")
                    .append(firstCoordinate.getY());

            polygons.add(builder.toString());
            if ( illegalZone.getEventSeq() == null) zoneTypes.add("");
            else zoneTypes.add(illegalEventService.get(illegalZone.getEventSeq()).getIllegalType().toString());
            zoneSeqs.add(illegalZone.getZoneSeq());
        }

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("zonePolygons", polygons);
        resultMap.put("zoneSeqs", zoneSeqs);
        resultMap.put("zoneTypes", zoneTypes);

        return resultMap;
    }
}