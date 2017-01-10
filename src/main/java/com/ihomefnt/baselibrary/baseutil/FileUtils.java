package com.ihomefnt.baselibrary.baseutil;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.*;
import java.security.MessageDigest;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtils {
    public static final String TAG = "FileUtils";
    public static final String CACHE_FILE_SUFFIX = ".txt";
    /**
     * 外部存储目录
     */
    private static final String BASE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();

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
        return size / 1048576;
    }

    public static String createMD5(String input) throws Exception {
        byte[] data = input.getBytes("utf-8");
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        messageDigest.update(data);
        return new String(messageDigest.digest());
    }

    /**
     * unzip files
     *
     * @param inputStream
     * @param outputFolder
     */
    public static void unZip(InputStream inputStream, String outputFolder) {

        byte[] buffer = new byte[1024];

        try {
            //create output directory is not exists
            File folder = new File(outputFolder);
            if (folder.exists()) {
                folder.delete();
            }
            folder.mkdir();
            //get the zip file content
            ZipInputStream zis = new ZipInputStream(inputStream);
            //get the zipped file list entry
            ZipEntry ze = null;

            while ((ze = zis.getNextEntry()) != null) {
                if (!ze.isDirectory()) {
                    String fileName = ze.getName();
                    File newFile = new File(outputFolder + File.separator + fileName);
                    System.out.println("file unzip : " + newFile.getAbsoluteFile());

                    //create all non exists folders
                    //else you will hit FileNotFoundException for compressed folder
                    if (!new File(newFile.getParent()).exists())
                        new File(newFile.getParent()).mkdirs();

                    FileOutputStream fos = new FileOutputStream(newFile);

                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }

                    fos.close();
                }
            }

            zis.closeEntry();
            zis.close();

            System.out.println("Done");

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @param context
     * @param data
     * @return
     */
    public static String getFilePathByUri(Context context, Intent data) {
        Uri selectedImage = data.getData();
        String[] filePath = {MediaStore.Images.Media.DATA};
        Cursor c = context.getContentResolver().query(selectedImage, filePath,
                null, null, null);
        c.moveToFirst();
        int columnIndex = c.getColumnIndex(filePath[0]);
        String fileName = c.getString(columnIndex);
        c.close();
        return fileName;
    }

    public static void saveBitmap(Context context, Bitmap bm) {
        File f = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            checkAndCreateFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ihomefnt/艾佳生活");
            f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ihomefnt/艾佳生活/", new Date().getTime() + ".jpg");
        } else {
            checkAndCreateFolder(Environment.getRootDirectory().getAbsolutePath() + "/ihomefnt/艾佳生活");
            f = new File(Environment.getRootDirectory().getAbsolutePath() + "/ihomefnt/艾佳生活");
        }
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri uri = Uri.fromFile(f);
        intent.setData(uri);
        context.sendBroadcast(intent);
        Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
    }



    private static void makeDir() {

    }

    public static void saveImageToGallery(Context context, Bitmap bmp) {
        // 首先保存图片
        File appDir = new File(Environment.getExternalStorageDirectory(), "Boohee");
        if (!appDir.exists()) {
            appDir.mkdir();
        }
        String fileName = System.currentTimeMillis() + ".jpg";
        File file = new File(appDir, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 其次把文件插入到系统图库
        try {
            MediaStore.Images.Media.insertImage(context.getContentResolver(),
                    file.getAbsolutePath(), fileName, null);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // 最后通知图库更新
        context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE));
    }
}
