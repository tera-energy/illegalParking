package com.teraenergy.illegalparking.exception;

/**
 * Date : 2022-09-30
 * Author : young
 * Editor :
 * Project : illegalParking
 * Description :
 */
public class TeraErrCodeUtil{


    public static String parseMessage(String message, String... args) {
        if (message == null || message.trim().length() <= 0) {
            return message;
        }

        if (args == null || args.length <= 0) {
            return message;
        }

        String[] splitMsgs = message.split("%");
        if (splitMsgs == null || splitMsgs.length <= 1) {
            for (int i = 0; i < args.length; i++) {
                message = message + "\n" + args[i];
            }
        } else {
            for (int i = 0; i < args.length; i++) {
                String replaceChar = "%" + (i + 1);
                message = message.replaceFirst(replaceChar, args[i]);
            }
        }

        return message;
    }
}
