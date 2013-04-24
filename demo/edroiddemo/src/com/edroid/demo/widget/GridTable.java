package com.edroid.demo.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.AdapterView.OnItemClickListener;

public class GridTable extends TableLayout {
    
    private BaseAdapter adapter;
    private int rowSize = 3;
    
    private int paddingLeft = 0;
    private int paddingTop = 2;
    private int paddingRight = 0;
    private int paddingBottom = 2;
    
    private OnItemClickListener onItemClickListener;

    public GridTable(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
    }

    public GridTable(Context context) {
        super(context);
        
//        setChildrenDrawnWithCacheEnabled(true);
//        setChildrenDrawingCacheEnabled(true);
        // TODO Auto-generated constructor stub
    }
    
    
    public void setAdapter(BaseAdapter adapter, int rowSize) {
        this.adapter = adapter;
        this.rowSize = rowSize;
        
        initViews();
    }
    
    private void initViews() {
        int count = adapter.getCount();
        int rows = (int) Math.ceil((double)count / rowSize);
        for (int i = 0; i < rows; i++) { // row
            TableRow row = createRow();
            //System.out.println("tr------------- " + i);
            for (int l = 0; l < rowSize; l++) { // col
                final int index = (i*rowSize) + l;
                //System.out.println("td-------------- " + l + ", index: " + index + ",  count: " + count);
                if (index >= 0 && index < count)  {
                    View view = adapter.getView(index, null, null);
                    view.setOnClickListener(new OnClickListener() {
                        public void onClick(View v) {
                            if (onItemClickListener != null) {
                                long id = (adapter != null) ? adapter.getItemId(index) : -1;
                                onItemClickListener.onItemClick(null, null, index, id);
                            }
                        }
                    });
                    row.addView(view);
                }
            }
            this.addView(row);
        }
    }
    
    private TableRow createRow() {
        TableRow row = new TableRow(getContext());
        row.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        row.setGravity(Gravity.CENTER);
        return row;
    }
    
    /**
     * 在最后增加一列
     * 
     * 必须在setAdapter之后（之前是没有子元素的）
     * 
     * @param view
     */
    public void addFooterView(View view) {
        Log.i("GridTable", "addFooterView: " + view);
        TableRow theLastRow = (TableRow) this.getChildAt(getChildCount()-1);
        Log.i("GridTable", "theLastRow: " + getChildCount() + " " + theLastRow);
        if (theLastRow != null) { // the last row
            if (theLastRow.getChildCount() == rowSize) { // 满员
                Log.i("GridTable", "addFooterView to a new row");
                theLastRow = createRow(); // 添加新行
                theLastRow.addView(view);
                this.addView(theLastRow);
            } else {
                Log.i("GridTable", "addFooterView to the last row");
                theLastRow.addView(view);
            }
        }
    }
    
    public void setOnItemClickListener(OnItemClickListener l) {
        onItemClickListener = l;
    }
    

}
