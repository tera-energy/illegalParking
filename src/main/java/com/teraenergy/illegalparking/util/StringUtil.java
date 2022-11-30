package com.teraenergy.illegalparking.util;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;

import java.io.UnsupportedEncodingException;
import java.rmi.dgc.VMID;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public class StringUtil {

    public static boolean isNumber(String value) {
        try {
            if (value == null) return false;
            double d = Double.parseDouble(value);
        } catch (Exception e) {
            return  false;
        }
        return true;
    }

    /**
     * 문자열 byte 수 반환
     *
     * @param str
     * @return
     */
    public static int getByteLength(String str) throws TeraException {
        try {
            return str.getBytes("EUC-KR").length;
        } catch (UnsupportedEncodingException e) {
            throw new TeraException(TeraExceptionCode.UNSUPPORTED_FORMAT, e);
        }
    }

    /**
     * 문자열 byte 수만큼 자르기
     *
     * @param str
     * @param length byte 길이
     * @return
     */
    public static String cut(String str, int length) throws TeraException {
        return cut(str, null, length, 0, false, null);
    }

    /**
     * 문자열 byte 수만큼 자르기
     *
     * @param str
     * @param length
     * @param suffix 뒤에 추가될 문자열
     * @return
     */
    public static String cut(String str, int length, String suffix) throws TeraException {
        return cut(str, null, length, 0, false, suffix);
    }

    /**
     * 문자열 byte 수만큼 자르기
     *
     * @param str
     * @param startKeyword 시작 키워드
     * @param length       byte 길이
     * @param nPrev        시작 위치를 키워드 이전부터
     * @param isNotag      태그 제거 여부
     * @param suffix       뒤에 추가될 문자열
     * @return
     */
    public static String cut(String str, String startKeyword, int length, int nPrev, boolean isNotag, String suffix) throws TeraException {
        String r_val = str;
        int oF = 0, oL = 0, rF = 0, rL = 0;
        int nLengthPrev = 0;

        Pattern p = Pattern.compile("<(/?)([^<>]*)?>", Pattern.CASE_INSENSITIVE); // 태그제거 패턴

        if (isNotag) {
            r_val = p.matcher(r_val).replaceAll("");
        } // 태그 제거

        r_val = r_val.replaceAll("&amp;", "&");
        r_val = r_val.replaceAll("(!/|\r|\n|&nbsp;)", ""); // 공백제거

        try {
            byte[] bytes = r_val.getBytes("UTF-8"); // 바이트로 보관

            if (startKeyword != null && !startKeyword.equals("")) {

                nLengthPrev = (r_val.indexOf(startKeyword) == -1) ? 0 : r_val.indexOf(startKeyword); // 일단 위치찾고

                nLengthPrev = r_val.substring(0, nLengthPrev).getBytes("MS949").length; // 위치까지길이를 byte로 다시 구한다

                nLengthPrev = (nLengthPrev - nPrev >= 0) ? nLengthPrev - nPrev : 0; // 좀 앞부분부터 가져오도록한다.

            }

            // x부터 y길이만큼 잘라낸다. 한글안깨지게.
            int j = 0;

            if (nLengthPrev > 0)
                while (j < bytes.length) {
                    if ((bytes[j] & 0x80) != 0) {
                        oF += 2;
                        rF += 3;
                        if (oF + 2 > nLengthPrev) {
                            break;
                        }
                        j += 3;
                    } else {
                        if (oF + 1 > nLengthPrev) {
                            break;
                        }
                        ++oF;
                        ++rF;
                        ++j;
                    }
                }

            j = rF;

            while (j < bytes.length) {
                if ((bytes[j] & 0x80) != 0) {
                    if (oL + 2 > length) {
                        break;
                    }
                    oL += 2;
                    rL += 3;
                    j += 3;
                } else {
                    if (oL + 1 > length) {
                        break;
                    }
                    ++oL;
                    ++rL;
                    ++j;
                }
            }

            r_val = new String(bytes, rF, rL, "UTF-8"); // charset 옵션

            if (suffix != null && rF + rL + 3 <= bytes.length) {
                r_val += suffix;
            } // ...을 붙일지말지 옵션

        } catch (UnsupportedEncodingException e) {
            throw new TeraException(TeraExceptionCode.UNSUPPORTED_FORMAT, e);
        }

        return r_val;
    }

    /**
     * 문자열 앞 채우기
     *
     * @param str
     * @param fillWord 채워질 문자
     * @param length
     * @return
     */
    public static String padLeft(String str, String fillWord, int length) throws TeraException {
        for (int i = StringUtil.getByteLength(str); i < length; i++)
            str = fillWord + str;

        return str;
    }

    /**
     * 문자열 양옆 채우기
     *
     * @param str
     * @param fillWord 채워질 문자
     * @param length
     * @return
     */
    public static String padBoth(String str, String fillWord, int length) throws TeraException {
        int intPadPos = 0;
        for (int i = StringUtil.getByteLength(str); i < length; i++) {
            if (intPadPos == 0) {
                str += fillWord;
                intPadPos = 1;
            } else {
                str = fillWord + str;
                intPadPos = 0;
            }
        }

        return str;
    }

    /**
     * 문자열 뒤 채우기
     *
     * @param str
     * @param fillWord 채워질 문자
     * @param length
     * @return
     */
    public static String padRight(String str, String fillWord, int length) throws TeraException {
        for (int i = StringUtil.getByteLength(str); i < length; i++)
            str += fillWord;

        return str;
    }

    /**
     * 문자열 뒤 자르기
     *
     * @param str
     * @param seperator 구분자
     * @return
     */
    public static String splitLeft(String str, String seperator) {
        return str.substring(0, str.indexOf(seperator));
    }

    /**
     * 문자열 앞 자르기
     *
     * @param str
     * @param seperator 구분자
     * @return
     */
    public static String splitRight(String str, String seperator) {
        return str.substring(str.indexOf(seperator) + 1);
    }

    /**
     * HttpRequest 의 문자열을 UTF-8 형식으로 변환
     *
     * @param HttpRequestString
     * @return
     */
    public static String toUTF8String(String HttpRequestString) throws TeraException {
        try {
            return new String(HttpRequestString.getBytes("8859_1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new TeraException(TeraExceptionCode.UNSUPPORTED_FORMAT, e);
        }
    }

    public static String generateVMID() {
        return new VMID().toString().toUpperCase().replaceAll("[:-]", "");
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString().toUpperCase();
    }

    public static String convertDatetimeToString(LocalDateTime time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDateTime convertStringToDateTime(String time, String pattern) {
        return LocalDateTime.parse(time, DateTimeFormatter.ofPattern(pattern));
    }

    public static String convertDateToString(LocalDate time, String pattern) {
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    public static LocalDate convertStringToDate(String time, String pattern) {
        return LocalDate.parse(time, DateTimeFormatter.ofPattern(pattern));
    }

}
