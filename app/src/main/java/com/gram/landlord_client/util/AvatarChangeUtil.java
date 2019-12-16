package com.gram.landlord_client.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import com.gram.landlord_client.activity.MainActivity;
import com.orhanobut.logger.Logger;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AvatarChangeUtil {
    //裁剪图
    private static final String CROP = "CROP";
    //压缩图
    private static final String COMPRESS = "COMPRESS";
    //FileProvider Authority
    public static final String FILEPROVIDER="com.gram.gram_landlord.fileprovider";

    /**
     * 判断系统及拍照
     */
    public static void takePicture(Activity activity) {
        Uri pictureUri;
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //对目标uri临时授权
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        File pictureFile = createImageFile(activity, null);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            pictureUri = FileProvider.getUriForFile(activity,
                    FILEPROVIDER, pictureFile);
        } else {
            pictureUri = Uri.fromFile(pictureFile);
        }
        //去拍照
        intent.putExtra(MediaStore.EXTRA_OUTPUT, pictureUri);
        ((MainActivity) activity).setCameraUri(pictureUri);
        if(intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, MainActivity.REQUEST_IMAGE_CAPTURE);
        }
    }

    /**
     * 从图库选图片
     */
    public static void selectPicture(Activity activity) {
        Intent intent = new Intent();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
        } else {
            intent.setAction(Intent.ACTION_GET_CONTENT);
        }
        intent.setType("image/*");
        if(intent.resolveActivity(activity.getPackageManager()) != null) {
            activity.startActivityForResult(intent, MainActivity.REQUEST_IMAGE_GET);
        } else {
            ToastUtil.showCenterToast("未找到图片查看器");
        }
    }

    /**
     * 处理ACTION_OPEN_DOCUMENT：图像选择器中Uri不被裁剪程序识别的问题
     */
    @TargetApi(19)
    public static String formatUri(Activity activity, Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(activity, uri)) {
            // ExternalStorageProvider
            if ("com.android.externalstorage.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));

                return getDataColumn(activity, contentUri, null, null);
            }
            // MediaProvider
            else if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return getDataColumn(activity, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if ("com.google.android.apps.photos.content".equals(uri.getAuthority()))
                return uri.getLastPathSegment();

            return getDataColumn(activity, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    private static String getDataColumn(Activity activity, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};

        try {
            cursor = activity.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * 创建图片文件，名称不重复。
     * @remark 图片注解，比如crop就是裁剪图、compress就是压缩图
     */
    private static File createImageFile(Activity activity, @Nullable String remark) {
        File imageFile = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA).format(new Date());
        String imageFileName = "AVATAR" + timeStamp + remark;
        //应用内部目录
        File pictureDir = activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if(pictureDir != null) {
            File storageDir = new File(pictureDir.getAbsolutePath() + "/pictures");
            try {
                imageFile = File.createTempFile(imageFileName, ".jpg", storageDir);
            } catch (IOException e) {
                Logger.e(e.getMessage());
            }
        }
        return imageFile;
    }

    /**
     * 头像图片裁剪
     */
    public static void crop(Activity activity, Uri uri) {
        File pictureCropFile = createImageFile(activity, CROP);
        Uri imgCropUri = Uri.fromFile(pictureCropFile);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        //缩略图的比例是1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 500);
        intent.putExtra("outputY", 500);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgCropUri);
        ((MainActivity) activity).setCropUri(imgCropUri);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        activity.startActivityForResult(intent, MainActivity.REQUEST_IMAGE_CROP);
    }

    /**
     * 图片压缩，sizeLimit单位为KB
     */
    public static Uri compress(Activity activity, Uri uri, long sizeLimit) {
        File pictureCompressFile = createImageFile(activity, COMPRESS);
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(uri));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            while(baos.toByteArray().length / 1024 > sizeLimit) {
                baos.reset();
                quality -= 10;
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            }
            Bitmap compressedBitmap = BitmapFactory.decodeStream(
                    new ByteArrayInputStream(baos.toByteArray()), null, null);

            FileOutputStream fos = new FileOutputStream(pictureCompressFile);
            if(compressedBitmap != null) {
                compressedBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            }
            fos.flush();
            fos.close();
            return Uri.fromFile(pictureCompressFile);
        } catch (FileNotFoundException e) {
            Logger.e(e.getMessage());
        } catch (IOException e) {
            Logger.e(e.getMessage());
        }
        return null;
    }

    /**
     * 将字节数组转换为Bitmap
     */
    public static Bitmap bytes2Bitmap(byte[] b) {
        if(b.length != 0) return BitmapFactory.decodeByteArray(b,0,b.length);
        else return null;
    }

    /**
     * 将Bitmap转换为字节数组，默认为PNG格式
     */
    public static byte[] bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 根据uri获取bitmap
     */
    public static Bitmap getBitmapFromUri(Context context, Uri uri) {
        if(uri == null) return null;
        Bitmap bm;
        try {
            bm = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bm;
        } catch (IOException e) {
            Logger.e(e.getMessage());
            return null;
        }
    }

}
