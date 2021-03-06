package com.finddreams.baselib.base;

import java.util.List;


import android.content.Context;
import android.widget.BaseAdapter;

/**
 * @Description:自定义的BaseAdapter，所有adapter的父类。
 * @author blin
 * @since 2015年12月14日
 */ 
public abstract class MyBaseAdapter<T, Q> extends BaseAdapter {
	//近期操作的序号
	public int releativeOptionIndex=-1;
	public int releativeOptionColor;
	
	public Context context;
	public List<T> list;//
	public Q view; // 这里不一定是ListView,比如GridView,CustomListView


	public MyBaseAdapter(Context context, List<T> list, Q view) {
		this.context = context;
		this.list = list;
		this.view = view;
	}

	public MyBaseAdapter(Context context, List<T> list) {
		this.context = context;
		this.list = list;
		
	}
	/**
	 * update
	 * @param list
	 */
	public void updateListView(List<T> list){
		this.list = list;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	


}
