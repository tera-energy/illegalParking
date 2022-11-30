package com.teraenergy.illegalparking.util;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import org.springframework.web.multipart.MultipartFile;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public class RequestFile {
    private MultipartFile multipartFile = null;

    private String path = null;
    private String name = null;
    private String ext = null;
    private long size;
    private String realName = null;

    /**
     * multipart/form-data 로부터 넘겨진 MultipartFile 로 초기화
     *
     * @param file
     */
    public RequestFile(MultipartFile file) {
        this.multipartFile = file;

        this.name = file.getOriginalFilename();
        this.ext = this.name.substring(this.name.lastIndexOf(".") + 1, this.name.length());
        this.size = file.getSize();
    }

    // 파일 최대 크기 설정
    public void setImageMaxSize(String unit, long size) throws TeraException {
        if (unit != null) {
            switch (unit){
                case "KB":
                    size = 1024 * size;
                    break;
                case "MB":
                    size = (1024 * 1024) * size;
                    break;
                case "B":
                default:
                    break;
            }
        }

        if (this.size > size ) {
            throw new TeraException(TeraExceptionCode.FILE_STORE_FAILURE);
        }
    }

    /**
     * 현재 파일 업로드
     *
     * @param filePath 업로드될 서버 경로
     * @return
     */
    public void upload(String filePath) throws TeraException {
        if (filePath == null)
            throw new TeraException(TeraExceptionCode.PARAMETER_EMPTY, "filePath");

        if (!filePath.substring(filePath.length() - 1, filePath.length()).equals("/"))
            filePath = filePath + "/";

        String realName = FileWebUtil.upload(multipartFile, filePath);

        this.realName = realName;
        this.path = filePath;
    }

    /**
     * 현재 파일 삭제
     *
     * @return
     */
    public void delete() throws TeraException {
        if (this.path == null || this.realName == null)
            return;

        FileWebUtil.delete(this.path, this.realName);
    }

    /**
     * MultipartFile 반환
     *
     * @return
     */
    public MultipartFile getMultipartFile() {
        return multipartFile;
    }

    /**
     * 파일 경로 반환 (서버에 저장되기 전에는 null)
     *
     * @return
     */
    public String getPath() {
        return path;
    }

    /**
     * MultipartFile 의 파일명 반환
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * 파일 확장자 반환
     *
     * @return
     */
    public String getExt() {
        return ext;
    }

    /**
     * 파일 크기 반환
     *
     * @return
     */
    public long getSize() {
        return size;
    }

    /**
     * 서버에 저장된 파일명 반환 (서버에 저장되기 전에는 null)
     *
     * @return
     */
    public String getRealName() {
        return realName;
    }

}
