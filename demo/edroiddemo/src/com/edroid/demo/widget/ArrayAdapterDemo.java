package com.edroid.demo.widget;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ListView;

import com.edroid.demo.R;

public class ArrayAdapterDemo extends ListActivity {
    
    private ListView listView;
    private ListAdapter listAdapter;
//    private ListAdapter2 listAdapter2;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demolist);
        initViews();
    }

    private void initViews() {
       listView = getListView(); 
       listAdapter = new ListAdapter(this);
//       listAdapter2 = new ListAdapter2(this);
       
       // add footer
       LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       View footer = inflater.inflate(R.layout.footer, null);
       footer.setOnClickListener(new OnClickListener() {
           public void onClick(View v) {
               loadData(true);
           }
       });
       listView.addFooterView(footer);
       registerForContextMenu(listView);
       
       listView.setAdapter(listAdapter);
       loadData(false);
    }
    
    // 模拟异步载入数据
    private void loadData(boolean append) {
        List<Person> list = new ArrayList<Person>();
        list.add(new Person("张三", "北京市", "http://tp2.sinaimg.cn/1652719137/50/5629205749/1"));
        list.add(new Person("李四", "上海市", "http://tp2.sinaimg.cn/1652719137/50/5629205749/1"));
        list.add(new Person("王石", "长江商学院", "http://tp2.sinaimg.cn/1652719137/50/5629205749/1"));
        list.add(new Person("小三", "长江商学院", "http://tp2.sinaimg.cn/1652719137/50/5629205749/1"));
        
        // 将数据填充到列表中
        listAdapter.refresh(list, append);
    }
    
    
    // 删除功能
    private static final int CONTEXT_DELETE_ID = 1;
    
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
            ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add(0, CONTEXT_DELETE_ID, 0, "删除");
    }
    
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        boolean result = super.onContextItemSelected(item);
        AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
                .getMenuInfo();
        Person bean = (Person) listAdapter.getItem(info.position);

        switch (item.getItemId()) {
        case CONTEXT_DELETE_ID:
            listAdapter.remove(bean);
            return true;
        default:
            return result;
        }
    }

}
