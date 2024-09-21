package com.fuckruangong.utils;

import java.io.File;

public class FileUtils {
    private static final String TXT_FILE_PATTERN = "^[a-zA-Z0-9_-]+\\.txt$";

    public static boolean isValidTxtFileName(String fileName) {
        return fileName.matches(TXT_FILE_PATTERN);
    }

    public static boolean isNotValidTxtFileName(String fileName) {
        return !isValidTxtFileName(fileName);
    }

    /**
     * 删除指定文件
     */
    public static void deleteFileIfExists(String filePath) {
        File file = new File(filePath);
        if (file.exists() && file.delete()) {
            System.out.println("已删除文件: " + filePath);
        }
    }

    public static void validateFileExists(String filePath) {
        if (!new File(filePath).exists()) {
            throw new IllegalArgumentException("文件不存在: " + filePath);
        }
    }
}
