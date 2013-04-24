package com.edroid.demo.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.edroid.demo.R;
import com.edroid.imageloader.ImageLoaderManager;
import com.edroid.widget.BaseListAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class GridTableDemo extends Activity {
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid_table_demo);
        
        initViews();
    }
    
    private GridTable gridTable;
    private ListAdapter listAdapter;

    private void initViews() {
        listAdapter = new ListAdapter(this);
        gridTable = (GridTable) findViewById(R.id.grid);
        
        List<Person> list = new ArrayList<Person>();
        addItem(list, "http://img1.ph.126.net/AKhfuv4hbHsEvp4oetVt4Q==/2889059160975300611.jpg" );
        addItem(list, "http://img8.ph.126.net/P0FMKntBmQzNWGOSLWadYQ==/2477261270047611068.jpg" );
        addItem(list, "http://img2.ph.126.net/eusIGBAbe5Iow4WJfkm9rA==/2613776633752274462.jpg" );
        addItem(list, "http://img8.ph.126.net/JuXyKyEKqoLzpX0m_yJ3sA==/2477261270047611048.jpg" );
        addItem(list, "http://img6.ph.126.net/2F5vn9Er76YKoI2sf4TmfA==/1039205614032865466.jpg" );
        addItem(list, "http://img3.ph.126.net/630_DPr8IpOOoxQqNY4LgQ==/2805461092892233903.jpg" );
        addItem(list, "http://img1.ph.126.net/d4w-JLp5Z5S7-ufIQBMAKg==/1353050213065248191.jpg" );
        addItem(list, "http://img8.ph.126.net/zz8iuBLnB9QQvkc7Tw0rIA==/1353050213065248208.jpg" );
        addItem(list, "http://img7.ph.126.net/ywL_95Qi3Dxy8QRu7frsew==/1096345034305131657.jpg" );
        addItem(list, "http://img8.ph.126.net/OmRF1Hcsmv62sLuGO_hWdw==/1561623170807844168.jpg" );
        addItem(list, "http://img7.ph.126.net/gYHhJx8GkuIRbrdTBvtwxQ==/2830512365819480657.jpg" );
        addItem(list, "http://img9.ph.126.net/-5j0LKwj6FMHf8MH-ICVSQ==/2634042832075448449.jpg" );
        addItem(list, "http://img0.ph.126.net/MFPLan7NpypTSAbpia9ydw==/2634042832075448470.jpg" );
        addItem(list, "http://img4.ph.126.net/zFeRDr_xkM13jf-2HqrYYQ==/2628694807517944174.jpg" );
        addItem(list, "http://img8.ph.126.net/if52ExZe9mRMVkBKG7F-kw==/97953291912436058.jpg" );
        addItem(list, "http://img4.ph.126.net/DgCfAu2zkwa2GeJXg0-l2Q==/2788572594289596144.jpg" );
        addItem(list, "http://img5.ph.126.net/LqKruGRORYnl2Nob8PpH7A==/3081869520022094585.jpg" );
        addItem(list, "http://img4.ph.126.net/9EgRwCaEH-74J3IAL4Kfbw==/2618280233379644734.jpg" );
        listAdapter.refresh(list);
        gridTable.setAdapter(listAdapter, 3);
    }
    
    private void addItem(List<Person> list, String imageurl)   {
        list.add(new Person("jack", "wuhan", imageurl));
    }
    
    private class ListAdapter extends BaseListAdapter<Person> {
        private ImageLoader imageLoader;
        private DisplayImageOptions options;

        public ListAdapter(Context context) {
            super(context);
            
            imageLoader = ImageLoaderManager.getImageLoader(context);
            options = new DisplayImageOptions.Builder()
                .cacheInMemory().cacheOnDisc()
                .setMinSideLength(-1)
                .setMaxNumOfPixels(-1)
                .showStubImage(R.drawable.ic_launcher)
                .build();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView == null) {
                view = mInflater.inflate(R.layout.grid_item, null);
                Holder holder = new Holder();
                holder.iv = (ImageView) view.findViewById(R.id.item_image);
                view.setTag(holder);
            } else {
                view = convertView;
            }
            Holder holder = (Holder) view.getTag();
            Person bean = list.get(position);
            imageLoader.displayImage(bean.image, holder.iv, options);
            return view;
        }
        
        class Holder {
            ImageView iv;
        }
        
    }
    
    
    
}
