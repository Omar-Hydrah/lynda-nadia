package com.nadia;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.Toast;
import android.widget.TextView;
import android.widget.ImageView;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.InputStream;
import java.io.IOException;

import com.nadia.model.DataItem; 
import com.nadia.model.SampleDataProvider;
import com.nadia.database.DataSource;

public class DetailActivity extends AppCompatActivity{

	DataSource dataSource;

	@Override
	public void onCreate(Bundle savedInstanceState){

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail);

		dataSource = new DataSource(this);
		dataSource.open();

		String itemId = getIntent().getExtras().getString(RecyclerAdapter.ITEM_ID_KEY);
		// DataItem item = SampleDataProvider.dataItemMap.get(itemId);
		Log.i("Nadia", "Fetching item id");
		DataItem item = new DataItem();

		try{
			item = dataSource.getItem(itemId);
			if(item == null){
				Log.e("Nadia", "Item was not found");
			}
		}catch(Exception e){
			e.printStackTrace();
			Log.e("Nadia", "DataItem- DetailActivity: " + e.getMessage());
		}

		// Log.i("Nadia", "item name: " + item.getItemName());
		/*Toast.makeText(this, "Received item " + item.getItemName(),
						Toast.LENGTH_SHORT).show();*/

		TextView foodName        = (TextView) findViewById(R.id.foodName);
		TextView foodPrice       = (TextView) findViewById(R.id.foodPrice);
		TextView foodDescription = (TextView) findViewById(R.id.foodDescription);
		ImageView foodImage      = (ImageView) findViewById(R.id.foodImage); 

		foodName.setText(item.getItemName());
		foodPrice.setText("$" + item.getPrice());
		foodDescription.setText(item.getDescription());


		InputStream stream = null;
		// try(InputStream stream = getAssets().open(item.getImage())){
		try{
			stream = getAssets().open(item.getImage());
			Drawable drawable = Drawable.createFromStream(stream, null);

			foodImage.setImageDrawable(drawable);
		}catch(IOException e){
			e.printStackTrace();
			Log.e("Nadia", "Detail activity: " + e.getMessage());
		}finally{
			try{
				stream.close();
			}catch(IOException e){
				e.printStackTrace();
				Log.e("Nadia", "Detail activity: " + e.getMessage());
			}
		}
	}

	@Override
	public void onResume(){
		super.onResume();
		dataSource.open();
	}

	@Override
	public void onPause(){
		super.onPause();
		dataSource.close();
	}
}