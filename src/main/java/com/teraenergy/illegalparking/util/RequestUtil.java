package com.teraenergy.illegalparking.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.teraenergy.illegalparking.encrypt.YoungEncoder;
import com.teraenergy.illegalparking.exception.TeraException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.utils.Lists;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
@Slf4j
public class RequestUtil {

    private CHashMap<String, Object> parameters = null;
    private CHashMap<String, List<RequestFile>> files = null;
    private boolean multipart = false;

    private HttpServletRequest request;
    private Model model;

    public RequestUtil(HttpServletRequest request) throws TeraException{
        this.parameters = new CHashMap<String, Object>();
        this.files = new CHashMap<String, List<RequestFile>>();
        this.multipart = ServletFileUpload.isMultipartContent(request);

        if (this.multipart) {
            parseRequestMultipart(request);
        } else {
            parseRequest(request);
        }

        this.request = request;
    }

    public RequestUtil(String jsonString){
        this.parameters = new CHashMap<String, Object>();
        this.files = new CHashMap<String, List<RequestFile>>();
        parseRequestJson(jsonString);
    }

    private void parseRequestMultipart(HttpServletRequest request) {
        MultipartHttpServletRequest mhsr = (MultipartHttpServletRequest) request;

        // parameter
        Enumeration<String> parameterNames = mhsr.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String value = mhsr.getParameter(parameterName);

            this.parameters.put(parameterName, value);
        }

        // file

        Iterator<String> fileNames = mhsr.getFileNames();
        while (fileNames.hasNext()) {
            MultipartFile mfile = mhsr.getFile(fileNames.next());

            List<RequestFile> requestFiles = Lists.newArrayList();
            if (mfile != null && mfile.getName() != null && mfile.getName().length() > 0 && mfile.getSize() > 0)
                requestFiles.add(new RequestFile(mfile));

            this.files.put(mfile.getName(), requestFiles);
        }

    }


    private void parseRequest(HttpServletRequest request) throws TeraException {
        // parameter
        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String parameterName = parameterNames.nextElement();
            String value = request.getParameter(parameterName);

            if (request.getMethod() == "GET")
                value = YoungEncoder.urlDecode(value);
            this.parameters.put(parameterName, value);
        }
    }

    @SuppressWarnings("unchecked")
    private void parseRequestJson(String jsonString) {
        JsonNode node = null;
        try {
            node = JsonUtil.toJsonNode(jsonString);
            this.parameters = new ObjectMapper().convertValue(node, CHashMap.class);
        } catch (TeraException e) {
        }
    }

    public void setParameterToModel(Model model){
        // parameter
        for (Map.Entry<String, Object> entry : this.parameters.entrySet()) {
            model.addAttribute(entry.getKey(), entry.getValue());
        }

        // file
        for (Map.Entry<String, List<RequestFile>> entry : this.files.entrySet())
            model.addAttribute(entry.getKey(), entry.getValue());
    }

    public void setParameter(String key, Object value) {
        request.setAttribute(key, value);
    }

    public CHashMap getParameterMap() {
        return this.parameters;
    }

    public CHashMap getMultipleFileMap() {
        return this.files;
    }

    public boolean isMultipart(){
        return this.multipart;
    }

}
