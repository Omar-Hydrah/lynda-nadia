package com.nadia;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import com.nadia.model.DataItem;
import com.nadia.model.SampleDataProvider;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder>{
	public static final String ITEM_ID_KEY = "item_id_key";
	private List<DataItem> items;
	private Context        context;
	private SharedPreferences.OnSharedPreferenceChangeListener prefsListener;

	public RecyclerAdapter(Context context, List<DataItem> items){
		this.context = context;
		this.items   = items;
	}

	@Override
	public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
		// Reading the shared preferences, to decide whether the user wants a grid or a list.
		SharedPreferences settings = 
			PreferenceManager.getDefaultSharedPreferences(this.context);

		// A preference listener can listen to change events on the shared preferences.
			
		/*prefsListener = new SharedPreferences.OnSharedPreferenceChangeListener(){
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, 
												String key)
			{
				Log.i("Nadia", "onSharedPreferenceChanged: " + key);
			}
		};

		settings.registerOnSharedPreferenceChangeListener(prefsListener);*/

		boolean showGrid = 
			settings.getBoolean(context.getString(R.string.pref_display_grid), false);

		// Log.i("Nadia", "showGrid boolean: " + showGrid);	

		// To set the corresponding layout to the user choice.	
		// int layoutId = showGrid ? R.layout.grid_item : R.layout.list_item;	
		
		// Testing the speed of if conditions:
		int layoutId = 0;
		if(showGrid){
			layoutId = R.layout.grid_item;
		}else{
			layoutId = R.layout.list_item;
		}

		LayoutInflater inflater = LayoutInflater.from(this.context);
		View itemView = inflater.inflate(layoutId, parent, false);

		ViewHolder viewHolder = new ViewHolder(itemView);
		return viewHolder;
	}

	// Responsible for supplying data, and creating event handlers
	@Override
	public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position){
		final DataItem item = items.get(position);

		try{
			holder.itemNameText.setText(item.getItemName());
			String imageFile = item.getImage();
			InputStream stream = this.context.getAssets().open(imageFile);
			Drawable drawable = Drawable.createFromStream(stream, null);

			holder.imageView.setImageDrawable(drawable);
		}catch(IOException e){
			e.printStackTrace();
			Log.e("Nadia", "Failed to bind image to view holder");
		}

		// Event handlers
		holder.view.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View view){
				/*Toast.makeText(context, "You selected " + item.getItemName(), 
					Toast.LENGTH_LONG).show();*/	
				/*Snackbar.make(view, "You selected " + item.getItemName() ,
					Snackbar.LENGTH_LONG).setAction("Action", null).show();*/
				String itemId = item.getItemId();
				Intent intent = new Intent(context, DetailActivity.class);
				intent.putExtra(ITEM_ID_KEY, itemId);
				
				// DataItem item = SampleDataProvider.dataItemMap.get(itemId);
				// Log.i("Nadia", "itemName: " + item.getItemName());

				Log.i("Nadia", "itemId: " + itemId);
				context.startActivity(intent);
			}
		});

		holder.view.setOnLongClickListener(new View.OnLongClickListener(){
			@Override
			public boolean onLongClick(View view){
				Snackbar.make(view, "A long click " + item.getItemName(),
					Snackbar.LENGTH_LONG).setAction("Action", null).show();
				return false;
			}
		});
	}

	@Override
	public int getItemCount(){
		return items.size();
	}

	public static class ViewHolder extends RecyclerView.ViewHolder{
		public TextView itemNameText;
		public ImageView imageView;
		// A reference to handle clicks on list items.
		public View      view;
		public ViewHolder(View itemView){
			super(itemView);

			itemNameText = (TextView) itemView.findViewById(R.id.itemNameText);
			imageView    = (ImageView) itemView.findViewById(R.id.imageView);
			view = itemView;
		}
	}
}