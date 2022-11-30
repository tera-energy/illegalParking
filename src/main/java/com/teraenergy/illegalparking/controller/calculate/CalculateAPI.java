package com.teraenergy.illegalparking.controller.calculate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import com.teraenergy.illegalparking.model.dto.calculate.service.ProductDtoService;
import com.teraenergy.illegalparking.model.entity.product.domain.Product;
import com.teraenergy.illegalparking.model.entity.product.enums.Brand;
import com.teraenergy.illegalparking.model.entity.product.service.ProductService;
import com.teraenergy.illegalparking.model.entity.user.domain.User;
import com.teraenergy.illegalparking.model.entity.user.service.UserService;
import com.teraenergy.illegalparking.util.JsonUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Date : 2022-09-27
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

@Slf4j
@RequiredArgsConstructor
@Controller
public class CalculateAPI {

    private final ProductService productService;

    private final ProductDtoService productDtoService;

    private final UserService userService;

    private final ObjectMapper objectMapper;

    @PostMapping("/calculate/product/get")
    @ResponseBody
    public Object getProduct(@RequestBody String body) throws JsonProcessingException {
        JsonNode bodyJson = objectMapper.readTree(body);
        Integer productSeq = bodyJson.get("productSeq").asInt();
        Product product = productService.get(productSeq);
        return productDtoService.get(product);
    }

    @PostMapping("/calculate/product/gets")
    @ResponseBody
    public Object getsProduct(@RequestBody String body) throws JsonProcessingException {
        JsonNode bodyJson = objectMapper.readTree(body);
        List<Integer> productSeqs = bodyJson.findValuesAsText("productSeqs").stream().map(s -> Integer.parseInt(s)).collect(Collectors.toList());
        List<Product> products = productService.gets(productSeqs);
        return productDtoService.gets(products);
    }

    @PostMapping("/calculate/product/set")
    @ResponseBody
    public Object setProduct(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Product product = convertProduct(jsonNode);
            return productService.set(product);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TeraException(TeraExceptionCode.PRODUCT_CREATE_FAIL);
        }
    }

    @PostMapping("/calculate/product/sets")
    @ResponseBody
    public Object setsProduct(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Iterator<JsonNode> iterator = jsonNode.elements();
            List<Product> products = Lists.newArrayList();
            while (iterator.hasNext()) {
                products.add(convertProduct(iterator.next()));
            }
            products = productService.sets(products);
            return products;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TeraException(TeraExceptionCode.PRODUCT_CREATE_FAIL);
        }
    }

    @PostMapping("/calculate/product/modify")
    @ResponseBody
    public Object modifyProduct(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Product product = convertProduct(jsonNode);
            return productService.set(product);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TeraException(TeraExceptionCode.PRODUCT_MODIFY_FAIL);
        }
    }

    @PostMapping("/calculate/product/remove")
    @ResponseBody
    public Object removeProduct(@RequestBody String body) throws TeraException {
        try {
            JsonNode jsonNode = JsonUtil.toJsonNode(body);
            Integer productSeq = jsonNode.get("productSeq").asInt();
            return productService.remove(productSeq);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new TeraException(TeraExceptionCode.PRODUCT_REMOVE_FAIL);
        }
    }

    private Product convertProduct(JsonNode jsonNode) throws TeraException {
        try {
            Product product = new Product();
            if (jsonNode.get("productSeq") != null) {
                product.setProductSeq(jsonNode.get("productSeq").asInt());
            }
            product.setIsDel(false);
            product.setBrand(Brand.valueOf(jsonNode.get("brand").asText()));
            product.setName(jsonNode.get("name").asText());
            Integer userSeq = jsonNode.get("userSeq").asInt();
            User user = userService.get(userSeq);
            product.setUserSeq(userSeq);
            product.setPointValue(jsonNode.get("pointValue").asLong());
            product.setThumbnail(jsonNode.get("thumbnail").asText());
            return product;
        } catch (Exception e){
            throw new TeraException(e.getMessage(), e);
        }
    }

}


