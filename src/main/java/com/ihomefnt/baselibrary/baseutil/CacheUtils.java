package com.ihomefnt.baselibrary.baseutil;

import android.content.Context;
import android.os.Environment;
import android.util.Base64;

import java.io.*;

/**
 * Created by liushulong on 2014/10/30.
 */
public class CacheUtils {
    private static final String TAG = CacheUtils.class.getSimpleName();
    private static final String FILE_SUBFIX = ".aihome";
    /**
     * 外部存储目录
     */
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
    private static final String BASE_FOLDER = "AiHome";

    public static CacheFile getStringCache(String fileName, Context context) {

        if (context == null) {
            return null;
        }
        String folder = context.getFilesDir() + "/" + BASE_FOLDER;
        if (!checkAndCreateFolder(folder)) {
            return null;
        }
        String filePath = folder + "/" + fileName + FILE_SUBFIX;
        File file = new File(filePath);
        if (!file.exists()) {
            return null;
        }
        FileInputStream inStream = null;
        ByteArrayOutputStream stream = null;
        try {
            inStream = new FileInputStream(filePath);
            stream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int length = -1;
            while ((length = inStream.read(buffer)) != -1) {
                stream.write(buffer, 0, length);
            }
            String streamStr = new String(Base64.decode(stream.toByteArray(), Base64.DEFAULT));

            CacheFile cacheFile = new CacheFile();
            if (streamStr != null && streamStr.length() > 14) {
                cacheFile.setUpdateTime(streamStr.substring(0, 13));
                cacheFile.setFileContent(streamStr.substring(14));
            }
            return cacheFile;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            try {
                stream.close();
                inStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static void saveStringToCache(Context context, String fileName, String jsonString) {
        if (context == null) {
            return;
        }
        String folder = context.getFilesDir() + "/" + BASE_FOLDER;
        if (!checkAndCreateFolder(folder)) {
            return;
        }
        String filePath = folder + "/" + fileName + FILE_SUBFIX;
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {

            }
        }

        String saveStr = Base64.encodeToString((System.currentTimeMillis() + "," + jsonString).getBytes(),
                Base64.DEFAULT);

        try {
            FileOutputStream outStream = new FileOutputStream(filePath);
            outStream.write(saveStr.getBytes());
            outStream.close();
        } catch (FileNotFoundException e) {
            return;
        } catch (IOException e) {
            return;
        }
    }

    public static boolean checkAndCreateFolder(String folder) {
        try {
            File dirFile = new File(folder);
            if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
                return dirFile.mkdirs();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void deleteFolder(Context context, File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteFolder(context, childFiles[i]);
            }
            file.delete();
        }
    }

    public static void deleteAllFileCache(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                file.delete();
                return;
            }

            for (int i = 0; i < childFiles.length; i++) {
                deleteAllFileCache(childFiles[i]);
            }
            file.delete();
        }
    }

    public static void deleteFileCache(File file) {
        if (file.isFile()) {
            return;
        }
        if (file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if (childFiles == null || childFiles.length == 0) {
                return;
            }
            for (int i = 0; i < childFiles.length; i++) {
                deleteAllFileCache(childFiles[i]);
            }
        }

    }

    public static void deleteCacheFile(Context context, String folderName, String fileName) {
        File dirFile = new File(context.getFilesDir() + "/" + folderName);
        if (!dirFile.exists()) {
            return;
        }
        if (!dirFile.isDirectory()) {
            dirFile.delete();
            return;
        }
        File file = new File(dirFile + "/" + fileName + FILE_SUBFIX);
        try {
            file.delete();
        } catch (Exception e) {

        }
    }

    /**
     * @return float 单位为M
     * @description 获取文件夹大小
     * @date 2013-12-03
     * @author huyongsheng
     */
    public static float getFolderSize(File folder) {
        float size = 0;
        try {
            File[] fileList = folder.listFiles();
            if (null == fileList) {
                return 0;
            }
            for (File file : fileList) {
                if (file.isDirectory()) {
                    size = size + getFolderSize(file);
                } else {
                    size = size + file.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size / 1024;
    }

}
