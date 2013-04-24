package com.edroid.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * `BaseListAdapter` 是 `BaseAdapter` 的实现类，类似于 `ArrayAdapter`,
 * 该类设计用来将任何类型的对象列表和ListView进行绑定。
 * 
 * @author lds
 *
 * @param <T>
 */
public abstract class BaseListAdapter<T> extends BaseAdapter {
    protected Context mContext;
    protected LayoutInflater mInflater;

    protected List<T> list;

    public BaseListAdapter(Context context) {
        this(context, new ArrayList<T>());
    }

    public BaseListAdapter(Context context, List<T> data) {
        mContext = context;
        list = data;

        mInflater = (LayoutInflater) this.mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && position < list.size()) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public abstract View getView(int position, View convertView,
            ViewGroup parent);

    /**
     * 刷新列表
     * 
     * @param list
     * @param append
     *            是否将新数据追加到尾部
     */
    public void refresh(List<T> list, boolean append) {
        if (!append) {
            this.list.clear();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    /**
     * 刷新列表
     * 
     * 新元素会替换到所有旧元素
     * 
     * @param list
     */
    public void refresh(List<T> list) {
        refresh(list, false);
    }

    /**
     * 删除元素
     * 
     * @param item
     */
    public void remove(T item) {
        if (list.remove(item)) {
            notifyDataSetChanged();
        }
    }

    /**
     * 删除元素
     * 
     * @param position index of item
     */
    public void remove(int position) {
        if (position >= 0 && position < list.size()) {
            if (list.remove(position) != null) {
                notifyDataSetChanged();
            }
        }
    }

}
