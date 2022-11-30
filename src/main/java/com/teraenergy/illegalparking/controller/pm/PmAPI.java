package com.teraenergy.illegalparking.controller.pm;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.Lists;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.model.entity.parking.service.ParkingService;
import com.teraenergy.illegalparking.model.entity.pm.service.PmService;
import com.teraenergy.illegalparking.util.JsonUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * Date : 2022-09-20
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@RequiredArgsConstructor
@Controller
public class PmAPI {

	private final PmService pmService;

	@PostMapping("/pm/get")
	@ResponseBody
	public Object getParking(@RequestBody String body) throws TeraException {
		JsonNode jsonNode = JsonUtil.toJsonNode(body);
		Integer pmSeq = jsonNode.get("pmSeq").asInt();
		return pmService.get(pmSeq);
	}

	@PostMapping("/pm/gets")
	@ResponseBody
	public Object getsPm(@RequestBody String body) throws TeraException {
		JsonNode jsonNode = JsonUtil.toJsonNode(body);
		List<String> codes = Lists.newArrayList();
		JsonNode codesArrNode = jsonNode.get("codes");
		if(codesArrNode.isArray()) {
			for (JsonNode obj : codesArrNode) {
				codes.add(obj.asText());
			}
		}
		return pmService.gets(codes);
	}

}
