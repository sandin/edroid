package com.edroid.utils;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;

public class Photographer {

    public Photographer(Context context) {

    }
    
    public static Dialog buildDialog(final Activity activity,
            final File photoFile,
            final int openAlbumRequestCode, 
            final int takePictureRequestCode) {  
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);  
//        builder.setIcon(R.drawable.ic_launcher);  
        builder.setTitle("提示");  
        builder.setMessage("选择上传图片方式");  
        builder.setPositiveButton("本地相册",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                        Intent intent = createOpenAlbumIntent();
                        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"),
                                openAlbumRequestCode);
                        dialog.cancel();
                    }  
                });  
        builder.setNeutralButton("相机拍摄",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                        Intent intent = createTakePictureIntent(photoFile);
                        activity.startActivityForResult(Intent.createChooser(intent, "选择图片"),
                                takePictureRequestCode);
                        dialog.cancel();
                    }  
                });  
        builder.setNegativeButton("取消",  
                new DialogInterface.OnClickListener() {  
                    public void onClick(DialogInterface dialog, int whichButton) {  
                        dialog.cancel();
                    }  
                });  
        return builder.create();  
    }  
    
    /**
     * 调用系统相机
     * 
     * @param photoFile
     * @return
     */
    public static Intent createTakePictureIntent(File photoFile) {
        final Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        try {
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return takePictureIntent;
    }
    
    /**
     * 打开系统相册
     * @return
     */
    public static Intent createOpenAlbumIntent() {
        final Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
        openAlbumIntent.addCategory(Intent.CATEGORY_OPENABLE);
        openAlbumIntent.setType("image/*");
        return openAlbumIntent;
    }

}
