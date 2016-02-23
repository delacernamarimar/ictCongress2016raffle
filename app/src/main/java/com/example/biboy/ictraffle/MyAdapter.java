package com.example.biboy.ictraffle;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	Context context;
	ArrayList<Student> list;
	LayoutInflater inflater;
	public MyAdapter(Context context, ArrayList<Student> list) {
		super();
		this.context = context;
		this.list = list;
		this.inflater=LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ContactHandler handler=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.mylayout, null);
			handler=new ContactHandler();
			
			handler.qr=(TextView) convertView.findViewById(R.id.textView1);
			handler.name=(TextView) convertView.findViewById(R.id.textView2);
			handler.id=(TextView) convertView.findViewById(R.id.textView3);

			convertView.setTag(handler);
		}
		else{
			handler = (ContactHandler) convertView.getTag();
			//dri nko vid 16:58
		}
		/// populating the individual listview  item
		handler.qr.setText(list.get(arg0).getQr());
		handler.name.setText(list.get(arg0).getName());
		handler.id.setText(list.get(arg0).getId());
		
		return convertView;
	}
	

	static class ContactHandler{
	
		TextView qr,name,id;
	}
}
