package com.edroid.demo;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.edroid.utils.Photographer;

public class TakePhotoActivity extends Activity implements OnClickListener {
    
    private static final int OPEN_ALBUM_REQUEST_CODE = 0;
    private static final int TAKE_PICTURE_REQUEST_CODE = 1;
    private File mPhotoFile;

    private ImageView preview;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.take_photo);
        
        findViewById(R.id.upload_photo).setOnClickListener(this);
        preview = (ImageView) findViewById(R.id.photo);
        
        mPhotoFile = new File(Environment.getExternalStorageDirectory(), "test.jpg");
        if (mPhotoFile.exists()) {
            mPhotoFile.delete();
        }
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
        case R.id.upload_photo:
            Photographer.buildDialog(this, mPhotoFile,
                    OPEN_ALBUM_REQUEST_CODE, TAKE_PICTURE_REQUEST_CODE)
                    .show();
            break;
        default:
            break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(requestCode + " " + resultCode + " ");
        
        if (resultCode != RESULT_OK) {
            return;
        }
        
        switch (requestCode) {
        case OPEN_ALBUM_REQUEST_CODE: {
            Uri uri = data.getData();
            setPreviewImage(getRealPathFromURI(uri));
            break;
        }
            
        case TAKE_PICTURE_REQUEST_CODE: {
            setPreviewImage(mPhotoFile.getAbsolutePath());
            break;
        }
        
        default:
            break;
        }
    }
    
    private String getRealPathFromURI(Uri contentURI) {
        Cursor cursor = getContentResolver()
                   .query(contentURI, null, null, null, null); 
        cursor.moveToFirst(); 
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
        return cursor.getString(idx); 
    }
    
    private void setPreviewImage(String photoPath) {
        if (TextUtils.isEmpty(photoPath)) {
            return;
        }
        try {
            Options options = new BitmapFactory.Options();
            options.inSampleSize = 4;
            Bitmap bm = BitmapFactory.decodeFile(photoPath, options);
            preview.setImageBitmap(bm);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
    }
    

}
