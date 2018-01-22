package com.nadia;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import java.util.List;
import java.io.InputStream;
import java.io.IOException;

import com.nadia.model.DataItem;

public class DataItemAdapter extends ArrayAdapter<DataItem>{

	List<DataItem> dataItems;
	LayoutInflater inflater;

	public DataItemAdapter(Context context, List<DataItem> objects){
		super(context, R.layout.list_item, objects);

		dataItems = objects;
		inflater = LayoutInflater.from(context);
	}

	@NonNull
	@Override
	public View getView(int position, View convertView, ViewGroup parent){

		if(convertView == null){
			// Inflating the list_item view, if no view has been retrieved.
			convertView = inflater.inflate(R.layout.list_item, parent, false);
		}

		TextView itemNameText = (TextView) convertView.findViewById(R.id.itemNameText);
		ImageView itemImage   = (ImageView) convertView.findViewById(R.id.imageView);

		DataItem item = dataItems.get(position);
		itemNameText.setText(item.getItemName());

		// itemImage.setImageResource(R.drawable.apple_pie);
		Log.i("Adapter", "Image: " + item.getImage());

		InputStream stream = null;
		try{
			String imageFile = item.getImage();
			
			// Loading the images from the assets directory.
			stream = getContext().getAssets().open(imageFile);				

			Drawable drawable = Drawable.createFromStream(stream, null);				
			itemImage.setImageDrawable(drawable);				

		}catch(Exception e){
			// Log.e("Adapter", e.getMessage());
			e.printStackTrace();
		}finally{
			// Closing the input stream
			try{
				if(stream != null){
					stream.close();
				}
			}catch(IOException e){
				// Log.e("Adapter", e.getMessage());
				e.printStackTrace();
			}
		}
		return convertView;
	}
}