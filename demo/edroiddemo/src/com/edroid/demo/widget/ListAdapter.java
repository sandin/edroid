package com.edroid.demo.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.edroid.demo.R;
import com.edroid.imageloader.ImageLoaderManager;
import com.edroid.widget.BaseListAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * Demo List Adapter
 */
public class ListAdapter extends BaseListAdapter<Person> {
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public ListAdapter(Context context) {
        super(context);

        imageLoader = ImageLoaderManager.getImageLoader(context);
        options = ImageLoaderManager.createDisplayImageOptions(
                R.drawable.ic_launcher, ImageScaleType.EXACT);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = mInflater.inflate(R.layout.demolist_item, null);
            Holder holder = new Holder();
            holder.name = (TextView) view.findViewById(R.id.item_name);
            holder.address = (TextView) view.findViewById(R.id.item_address);
            holder.image = (ImageView) view.findViewById(R.id.item_image);
            view.setTag(holder);
        } else {
            view = convertView;
        }

        Holder holder = (Holder) view.getTag();
        Person bean = list.get(position);

        holder.name.setText(bean.name);
        holder.address.setText(bean.address);
        // 异步载入网络图片
        imageLoader.displayImage(bean.image, holder.image, options);

        return view;
    }

    class Holder {
        TextView name;
        TextView address;
        ImageView image;
    }

}