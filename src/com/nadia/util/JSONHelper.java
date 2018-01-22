package com.nadia.util;

import android.content.Context;
import android.util.Log;
import android.os.Environment;

import java.util.List;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;

import com.nadia.model.DataItem;
import com.google.gson.Gson;

public class JSONHelper {
	private static final String FILE_NAME = "menuitems.json";
	private static final String TAG = "JSONHelper";

	public static boolean exportToJSON(Context context , List<DataItem> dataItemList){
		DataItems menuData = new DataItems();

		menuData.setDataItems(dataItemList);

		Gson gson = new Gson();
		String jsonString = gson.toJson(menuData);
		// Log.i("Nadia", "exporToJSON: " + jsonString);

		FileOutputStream outputStream = null;
		try{
			File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME );
		
			outputStream = new FileOutputStream(file);
			outputStream.write(jsonString.getBytes());

			return true;

		}catch(IOException e){
			e.printStackTrace();
			Log.e("Nadia", e.getMessage());
		}finally{
			try{
				if(outputStream != null){
					outputStream.close();
				}
			}catch(IOException e){
				e.printStackTrace();
			}
		}
		return false;
	}

	public static List<DataItem> importFromJSON(Context context){
		FileReader reader = null;

		try{
			File file = new File(Environment.getExternalStorageDirectory(), FILE_NAME);
			reader = new FileReader(file);

			Gson gson = new Gson();
			DataItems dataItems = gson.fromJson(reader, DataItems.class);
			return dataItems.getDataItems();

		}catch(IOException e){
			e.printStackTrace();
			Log.e("Nadia", "importFromJSON: " + e.getMessage());

		}finally{
			if(reader != null){
				try{
					reader.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}

		return null;
	}

	static class DataItems{
		List<DataItem> dataItems;


		public List<DataItem> getDataItems(){
			return dataItems;
		}

		public void setDataItems(List<DataItem> dataItems){
			this.dataItems = dataItems;
		}
	}
}