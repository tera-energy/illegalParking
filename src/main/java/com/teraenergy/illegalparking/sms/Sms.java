package com.teraenergy.illegalparking.sms;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Maps;
import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.util.JsonUtil;
import org.apache.commons.compress.utils.Lists;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;

/**
 * Date : 2022-10-23
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */

public class Sms {

    // 문자 전송
    static public void sendSMS(String phoneNumber, String content) throws TeraException {
        String hostNameUrl = "https://sens.apigw.ntruss.com";            // 호스트 URL
        String requestUrl = "/sms/v2/services/";                         // 요청 URL
        String requestUrlType = "/messages";                             // 요청 URL

        String accessKey = "Z5cIzHss1rH6qzBzZDEi";                          // 네이버 클라우드 플랫폼 회원에게 발급되는 개인 인증키			// Access Key : https://www.ncloud.com/mypage/manage/info > 인증키 관리 > Access Key ID
        String secretKey = "AB4tsaR7TtnFkgCUxC31LeTo6onaI3e0npRNSLc5";      // 2차 인증을 위해 서비스마다 할당되는 service secret key	// Service Key : https://www.ncloud.com/mypage/manage/info > 인증키 관리 > Access Key ID
        String serviceId = "ncp:sms:kr:283490566916:illegalparking_app";    // 프로젝트에 할당된 SMS 서비스 ID							// service ID : https://console.ncloud.com/sens/project > Simple & ... > Project > 서비스 ID

        // 박성수 주임 - 키
//        String accessKey = "4VqjVzPCbUUnTO9nDy92";                       // 네이버 클라우드 플랫폼 회원에게 발급되는 개인 인증키			// Access Key : https://www.ncloud.com/mypage/manage/info > 인증키 관리 > Access Key ID
//        String secretKey = "6Pm4jOKBTTtAYDR3rWQhdSnwXFzk6xxuNx7JJYWJ";   // 2차 인증을 위해 서비스마다 할당되는 service secret key	// Service Key : https://www.ncloud.com/mypage/manage/info > 인증키 관리 > Access Key ID
//        String serviceId = "ncp:sms:kr:294175196378:sms_auth_test";        // 프로젝트에 할당된 SMS 서비스 ID							// service ID : https://console.ncloud.com/sens/project > Simple & ... > Project > 서비스 ID

        String method = "POST";                                          // 요청 method
        String timestamp = Long.toString(System.currentTimeMillis());    // current timestamp (epoch)
        requestUrl += serviceId + requestUrlType;
        String apiUrl = hostNameUrl + requestUrl;

        // JSON 을 활용한 body data 생성
        HashMap<String, Object> bodyMap = Maps.newHashMap();

        List<HashMap<String, Object>> toMessages = Lists.newArrayList();

        HashMap<String, Object> toMessage = Maps.newHashMap();
        toMessage.put("subject","111");							 // Optional, messages.subject	개별 메시지 제목, LMS, MMS에서만 사용 가능
        toMessage.put("to", phoneNumber);                    // Mandatory(필수), messages.to	수신번호, -를 제외한 숫자만 입력 가능
        toMessage.put("content",content);	                     // Optional, messages.content	개별 메시지 내용, SMS: 최대 80byte, LMS, MMS: 최대 2000byte

        toMessages.add(toMessage);

        bodyMap.put("type", "SMS");                          // Madantory, 메시지 Type (SMS | LMS | MMS), (소문자 가능)
        bodyMap.put("contentType","COMM");					     // Optional, 메시지 내용 Type (AD | COMM) * AD: 광고용, COMM: 일반용 (default: COMM) * 광고용 메시지 발송 시 불법 스팸 방지를 위한 정보통신망법 (제 50조)가 적용됩니다.
        bodyMap.put("countryCode","82");					 // Optional, 국가 전화번호, (default: 82)
        bodyMap.put("from", "07047661008");                  // Mandatory, 발신번호, 사전 등록된 발신번호만 사용 가능
//        bodyMap.put("from", "01079297878");                  // Mandatory, 발신번호, 사전 등록된 발신번호만 사용 가능 (박주임 전화 번호)
        bodyMap.put("subject","222");						 // Optional, 기본 메시지 제목, LMS, MMS에서만 사용 가능
        bodyMap.put("content", "불법주정차 신고 알림.");    // Mandatory(필수), 기본 메시지 내용, SMS: 최대 80byte, LMS, MMS: 최대 2000byte
        bodyMap.put("messages", toMessages);                 // Mandatory(필수), 아래 항목들 참조 (messages.XXX), 최대 1,000개

        JsonNode bodyJson = JsonUtil.toJsonNode(bodyMap);
        String body = bodyJson.toString();
        System.out.println(body);

        try {
            URL url = new URL(apiUrl);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setUseCaches(false);
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setRequestProperty("content-type", "application/json");
            con.setRequestProperty("x-ncp-apigw-timestamp", timestamp);
            con.setRequestProperty("x-ncp-iam-access-key", accessKey);
            con.setRequestProperty("x-ncp-apigw-signature-v2", makeSignature(requestUrl, timestamp, method, accessKey, secretKey));
            con.setRequestMethod(method);
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());

            wr.write(body.getBytes());
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            System.out.println("responseCode" + " " + responseCode);
            if (responseCode == 202) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else { // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(" =============================== ");
            System.out.println(response.toString());
            System.out.println(" =============================== ");
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    static private String makeSignature(String url, String timestamp, String method, String accessKey, String secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        String space = " ";                    // one space
        String newLine = "\n";                 // new line

        String message = new StringBuilder()
                .append(method)
                .append(space)
                .append(url)
                .append(newLine)
                .append(timestamp)
                .append(newLine)
                .append(accessKey)
                .toString();

        SecretKeySpec signingKey;
        String encodeBase64String;
        try {

            signingKey = new SecretKeySpec(secretKey.getBytes("UTF-8"), "HmacSHA256");
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(message.getBytes("UTF-8"));
            encodeBase64String = Base64.getEncoder().encodeToString(rawHmac);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            encodeBase64String = e.toString();
        }

        return encodeBase64String;
    }

}
