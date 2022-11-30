package com.teraenergy.illegalparking.util;

import com.teraenergy.illegalparking.exception.TeraException;
import com.teraenergy.illegalparking.exception.enums.TeraExceptionCode;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public class FileWebUtil {


    /* 파일 복사 */
    public static void copy(File file, String destFileName) throws TeraException, IOException {
        if (destFileName == null ) throw new TeraException(TeraExceptionCode.FILE_READ_FAILURE);
        if ( file.exists() ) {
            try {
                File newFile = new File(destFileName);
                Files.copy(file.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }catch (IOException e) {
                throw new TeraException(TeraExceptionCode.FILE_READ_FAILURE, e);
            }
        } else throw new TeraException(TeraExceptionCode.FILE_READ_FAILURE);
    }

    /* 파일 저장 */
    public static void store(String filePath, String data, String charset) throws TeraException {
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(filePath);

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(fileOutputStream, charset);
            outputStreamWriter.write(data);

            BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);

            bufferedWriter.close();
        } catch (IOException e) {
            throw new TeraException(TeraExceptionCode.FILE_STORE_FAILURE, e);
        } finally {
            if (fileOutputStream != null)
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    throw new TeraException(TeraExceptionCode.UNKNOWN, e);
                }
        }
    }

    /* 파일 읽기 */
    public static String read(String filePath, String charset) throws TeraException {
        FileInputStream fileInputStream = null;

        try {
            fileInputStream = new FileInputStream(filePath);

            byte[] sentence = new byte[835];

            int readSize = fileInputStream.read(sentence);

            if (readSize == -1)
                return null;

            String data;
            int idx = 0;

            data = new String(sentence, idx, readSize, charset);
            data = data.replace(">", "\r\n");

            return data;
        } catch (IOException e) {
            throw new TeraException(TeraExceptionCode.FILE_READ_FAILURE, e);
        } finally {
            if (fileInputStream != null)
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    throw new TeraException(TeraExceptionCode.UNKNOWN, e);
                }
        }
    }

    /* 파일 삭제 */
    public static void delete(File file) throws TeraException{
        try {
            if ( file.exists())
                file.delete();
            else throw new TeraException(TeraExceptionCode.FILE_DELETE_FAILURE);
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.FILE_DELETE_FAILURE, e);
        }
    }

    public static void delete(String filePath, String fileName) throws TeraException {
        if (filePath == null || fileName == null)
            throw new TeraException(TeraExceptionCode.PARAMETER_EMPTY, "filePath");

        if (!filePath.substring(filePath.length() - 1, filePath.length()).equals("/"))
            filePath = filePath + "/";

        try {
            File file = new File(filePath + fileName);
            file.delete();
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.FILE_DELETE_FAILURE, e);
        }
    }

    /* 디렉토리 생성 */
    public static void mkdir(String path) throws TeraException {
        try {
            File dir = new File(path);
            dir.mkdirs();
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.DIRECTORY_MAKE_FAILURE, e);
        }
    }

    /* 파일 이름 생성 */
    public static String generateFileName(String originalFileName) {
        return StringUtil.generateUUID() + "." + originalFileName.substring(originalFileName.lastIndexOf(".") + 1, originalFileName.length());
    }

    /* 파일 확장자 명 가져오기 */
    public static String getExt(String fileName) throws Exception {
        String ext = "";
        if (fileName == null || fileName.length() == 0) {
            ext = fileName.substring(fileName.lastIndexOf(".") + 1);
            return ext;
        } else {
            throw new FileNotFoundException();
        }

    }

    /** File upload */

    /**
     * 파일 업로드
     *
     * @param multipartFile multipart/form-data 으로부터 넘겨받은 파일
     * @param filePath      저장될 경로
     * @return
     */
    public static String upload(MultipartFile multipartFile, String filePath) throws TeraException {
        // check filePath and mkdir
        if (filePath == null)
            return null;

        if (!filePath.substring(filePath.length() - 1, filePath.length()).equals("/"))
            filePath = filePath + "/";

        FileWebUtil.mkdir(filePath);

        // generate fileName
        String fileName = FileWebUtil.generateFileName(multipartFile.getOriginalFilename());

        // upload
        File file = new File(filePath + fileName);

        try {
            multipartFile.transferTo(file);
        } catch (IllegalStateException | IOException e) {
            throw new TeraException(TeraExceptionCode.FILE_STORE_FAILURE, e);
        }

        return fileName;
    }

    /** File download */

    /**
     * 다운로드 버퍼 크기
     */
    private static final int BUFFER_SIZE = 8192; // 8kb

    /**
     * 지정된 파일을 다운로드 한다.
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String filePath, String fileName) throws TeraException {
        download(request, response, filePath, fileName, fileName);
    }

    /**
     * 지정된 파일을 다운로드 한다.
     *
     * @param downloadFileName 다운로드 될 파일명
     * @return
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, String filePath, String fileName, String downloadFileName) throws TeraException {
        download(request, response, new File(filePath, fileName), downloadFileName);
    }

    /**
     * 지정된 파일을 다운로드 한다.
     *
     * @return
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, File file) throws TeraException {
        download(request, response, file, file.getName());
    }

    /**
     * 지정된 파일을 다운로드 한다.
     *
     * @param downloadFileName 다운로드 될 파일명
     * @return
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, File file, String downloadFileName) throws TeraException {
        String mimetype = request.getSession().getServletContext().getMimeType(file.getName());

        if (file == null || !file.exists() || file.length() <= 0 || file.isDirectory())
            throw new TeraException(TeraExceptionCode.FILE_NOT_FOUND);

        InputStream is = null;

        try {
            is = new FileInputStream(file);

            download(request, response, is, downloadFileName, file.length(), mimetype);
        } catch (FileNotFoundException e) {
            throw new TeraException(TeraExceptionCode.FILE_NOT_FOUND, e);
        } finally {
            try {
                is.close();
            } catch (Exception e) {
                throw new TeraException(TeraExceptionCode.UNKNOWN, e);
            }
        }
    }

    /**
     * 해당 입력 스트림으로부터 오는 데이터를 다운로드 한다.
     *
     * @param request
     * @param response
     * @param is               입력 스트림
     * @param downloadFileName 다운로드 될 파일명
     * @param filesize         파일 크기
     * @return
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, InputStream is, String downloadFileName, long filesize) throws TeraException {
        download(request, response, is, downloadFileName, filesize, "application/octet-stream");
    }

    /**
     * 해당 입력 스트림으로부터 오는 데이터를 다운로드 한다.
     *
     * @param request
     * @param response
     * @param is               입력 스트림
     * @param downloadFileName 다운로드 될 파일명
     * @param filesize         파일 크기
     * @param mimetype         MIME 타입 지정
     * @return
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, InputStream is, String downloadFileName, long filesize, String mimetype) throws TeraException {
        String mime = mimetype;

        if (mimetype == null || mimetype.length() == 0)
            mime = "application/octet-stream";

        byte[] buffer = new byte[BUFFER_SIZE];

        response.setContentType(mime + "; charset=utf-8;");

        // attachment; 가 붙으면 IE의 경우 무조건 다운로드창이 뜬다. 상황에 따라 써야한다.
        response.setHeader("Content-Disposition", "attachment; filename=\"" + getDownloadFileName(request, downloadFileName) + "\";");
        response.setHeader("Content-Transfer-Encoding", "binary");

        // 파일 사이즈가 정확하지 않을때는 아예 지정하지 않는다.
        if (filesize > 0)
            response.setHeader("Content-Length", "" + filesize);

        BufferedInputStream fin = null;
        BufferedOutputStream outs = null;

        try {
            fin = new BufferedInputStream(is);
            outs = new BufferedOutputStream(response.getOutputStream());
            int read = 0;

            while ((read = fin.read(buffer)) != -1) {
                outs.write(buffer, 0, read);
            }
        } catch (IOException e) {
            throw new TeraException(TeraExceptionCode.FILE_READ_FAILURE, e);
        } finally {
            try {
                outs.close();
            } catch (Exception e) {
                throw new TeraException(TeraExceptionCode.UNKNOWN, e);
            }

            try {
                fin.close();
            } catch (Exception e) {
                throw new TeraException(TeraExceptionCode.UNKNOWN, e);
            }
        }
    }

    /**
     * POI Workbook 데이터를 다운로드 한다.
     *
     * @param request
     * @param response
     * @param workbook         POI Workbook
     * @param downloadFileName 다운로드 될 파일명
     * @return
     */
    public static void download(HttpServletRequest request, HttpServletResponse response, Workbook workbook, String downloadFileName) throws TeraException {
        response.setContentType("application/x-msexcel");
        response.setHeader("Content-Disposition", "attachment; fileName=\"" + getDownloadFileName(request, downloadFileName) + "\";");

        ServletOutputStream servletOutputStream = null;
        try {
            servletOutputStream = response.getOutputStream();
            workbook.write(servletOutputStream);
        } catch (IOException e) {
            throw new TeraException(TeraExceptionCode.FILE_READ_FAILURE, e);
        } finally {
            if (servletOutputStream != null)
                try {
                    servletOutputStream.flush();
                    servletOutputStream.close();
                } catch (IOException e) {
                    throw new TeraException(TeraExceptionCode.UNKNOWN, e);
                }
        }
    }

//    TODO : JXTL 변경 내용 적용후 아래의 내용 수정 후 사용 ...  (ExcelUtil.java 수정 이 먼저임 >>>>>>>> )
//    public static void download(HttpServletRequest request, HttpServletResponse response, String templateFilePath, Map<String, Object> map, String downloadFileName) throws TeraException {
//        response.setContentType("application/x-msexcel");
//        response.setHeader("Content-Disposition", "attachment; fileName=\"" + getDownloadFileName(request, downloadFileName) + "\";");
//
//        ServletOutputStream servletOutputStream = null;
//        try {
//            servletOutputStream = response.getOutputStream();
//            ExcelUtil.writeByJXLS(servletOutputStream, templateFilePath, map);
//        } catch (IOException e) {
//            throw new TeraException(TeraExceptionCode.FILE_READ_FAILURE, e);
//        } finally {
//            if (servletOutputStream != null)
//                try {
//                    servletOutputStream.flush();
//                    servletOutputStream.close();
//                } catch (IOException e) {
//                    throw new TeraException(TeraExceptionCode.UNKNOWN, e);
//                }
//        }
//    }

    /**
     * 브라우져 별 다운로드 파일명 인코딩 적용
     *
     * @param request
     * @param fileName
     * @return
     */
    private static String getDownloadFileName(HttpServletRequest request, String fileName) throws TeraException {
        String userAgent = request.getHeader("User-Agent");
        try {
            if (userAgent != null && userAgent.indexOf("MSIE") == -1) {
                fileName = fileName.replace("%60", "`");
                fileName = fileName.replace("%40", "@");
                fileName = fileName.replace("%23", "#");
                fileName = fileName.replace("%24", "$");
                fileName = fileName.replace("%25", "%");
                fileName = fileName.replace("%5E", "^");
                fileName = fileName.replace("%26", "&");
                fileName = fileName.replace("%2B", "+");
                fileName = fileName.replace("%3D", "=");
                fileName = fileName.replace("%7B", "{");
                fileName = fileName.replace("%7D", "}");
                fileName = fileName.replace("%5B", "[");
                fileName = fileName.replace("%5D", "]");
                fileName = fileName.replace("%3B", ";");
                fileName = fileName.replace("%2C", ",");
            }

            if (userAgent != null && userAgent.indexOf("MSIE") > -1) {
                fileName = new String(fileName.getBytes("KSC5601"), "ISO8859_1");
            } else if (userAgent != null && userAgent.indexOf("Firefox") > -1) {
                fileName = new String(fileName.getBytes("UTF-8"), "8859_1");
            } else if (userAgent != null && userAgent.indexOf("Chrome") > -1) {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            } else if (userAgent != null && userAgent.indexOf("Opera") > -1) {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            } else if (userAgent != null && userAgent.indexOf("Safari") > -1) {
                fileName = new String(fileName.getBytes("UTF-8"), "ISO-8859-1");
            } else {
                fileName = new String(fileName.getBytes("EUC-KR"), "ISO-8859-1");
            }
        } catch (Exception e) {
            throw new TeraException(TeraExceptionCode.UNSUPPORTED_FORMAT, e);
        }

        return fileName;
    }

}
