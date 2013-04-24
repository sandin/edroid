package com.edroid.demo.imageloader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.edroid.demo.R;
import com.edroid.imageloader.ImageLoaderManager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

public class ImageLoaderActivity extends Activity {

    private ImageLoader imageLoader;
    private ImageView iv;
    private Button reload;
    private EditText urlInput;
    private DisplayImageOptions options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_imageloader);

        String url = "http://y2.ifengimg.com/c38d6880f23f60de/2012/1031/rdn_509071d8b4ba6.jpg";
        imageLoader = ImageLoaderManager.getImageLoader(this);
        options = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc().setMinSideLength(700)
                .showStubImage(R.drawable.ic_launcher).setMaxNumOfPixels(-1)
                .build();

        iv = (ImageView) findViewById(R.id.image);
        reload = (Button) findViewById(R.id.reload);
        urlInput = (EditText) findViewById(R.id.url);

        urlInput.setText(url);
        reload.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                loadImage(urlInput.getText().toString());
            }
        });
    }

    /**
     * 异步载入图片
     * 
     * @param url
     */
    private void loadImage(String url) {
        imageLoader.displayImage(url, iv, options);
    }

}
