package com.jsondata.www.generic;

import com.jsondata.www.generic.Object;

import java.util.ArrayList;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ObjectAdapter extends ArrayAdapter<Object> {
    private ArrayList<Object> items;

    public ObjectAdapter(Context context, int textViewResourceId, ArrayList<Object> items) {
        super(context, textViewResourceId, items);
        this.items = items;
    }
    
	public Object clickAtPosition(int position) {
		Object Object = getItem(position);
		return Object;
	}
	
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemView = null;
        if (null == convertView) {// http://stackoverflow.com/questions/4321343/android-getsystemservice-inside-custom-arrayadapter
        	try{
        		LayoutInflater vi = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        		itemView = vi.inflate(R.layout.row, null);
        	} catch (Exception e) {
        		Log.e("ERROR", "ERROR IN CODE (LayoutInflater) :" + e.toString());
        	}
        }else{
        	itemView = convertView;
        }
        Object l = items.get(position);
        if (l != null) {
            TextView txtName = (TextView) itemView.findViewById(R.id.name);
            TextView txtUrl = (TextView) itemView.findViewById(R.id.url);
            
            if (txtName!= null) txtName.setText(l.getName());
            if (txtUrl!= null) txtUrl.setText(l.getUrl());
            txtUrl.setVisibility(View.GONE);// or android:visibility="gone"
        }
        return itemView;
    }
}