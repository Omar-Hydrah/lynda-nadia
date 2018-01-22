package com.nadia.database;

import android.content.Context;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.database.DatabaseUtils;
import android.database.Cursor;
import android.util.Log;

import java.util.List;
import java.util.ArrayList;

import com.nadia.model.DataItem;

public class DataSource{

	private Context context;
	private SQLiteDatabase database;
	private SQLiteOpenHelper dbHelper;

	public DataSource(Context context){
		this.context = context;
		dbHelper = new DBHelper(this.context);
		database = dbHelper.getWritableDatabase();
	}

	public void open(){
		database = dbHelper.getWritableDatabase();
	}

	public void close(){
		dbHelper.close();
	}

	public DataItem createItem(DataItem item){
		ContentValues values = item.toValues();
		database.insert(ItemsTable.TABLE_ITEMS, null, values);
		return item;
	}

	public long getDataItemsCount(){
		return DatabaseUtils.queryNumEntries(database, ItemsTable.TABLE_ITEMS);
	}

	public void seedDatabase(List<DataItem> dataItemList){
		// Inserting data into database from the SampleDataProvider
        long numItems = getDataItemsCount();
        if(numItems == 0){

            for(DataItem item : dataItemList){
                try{
                    createItem(item);
                }catch(SQLiteException e){
                    e.printStackTrace();
                    Log.e("Nadia", e.getMessage());
                }
            }
        }
	}

	public DataItem getItem(String itemId){
		Cursor cursor = database.rawQuery("select * from items where item_id = '" +
										 itemId + "'", null);

		DataItem item = null;
		if(cursor != null && cursor.moveToNext()){
			item = new DataItem();

			item.setItemId(cursor.getString(
				cursor.getColumnIndex(ItemsTable.COLUMN_ID)));

			item.setItemName(cursor.getString(
				cursor.getColumnIndex(ItemsTable.COLUMN_NAME)));

			item.setDescription(cursor.getString(
				cursor.getColumnIndex(ItemsTable.COLUMN_DESCRIPTION)));

			item.setCategory(cursor.getString(
				cursor.getColumnIndex(ItemsTable.COLUMN_CATEGORY)));

			item.setSortPosition(cursor.getInt(
				cursor.getColumnIndex(ItemsTable.COLUMN_POSITION)));

			item.setPrice(cursor.getDouble(
				cursor.getColumnIndex(ItemsTable.COLUMN_PRICE)));

			item.setImage(cursor.getString(
				cursor.getColumnIndex(ItemsTable.COLUMN_IMAGE)));

		}
		cursor.close();
		return item;
	}

	public List<DataItem> getAllItems(String category){
		List<DataItem> dataItems = new ArrayList<>();

		Cursor cursor;
		if(category == null){
			// Last parameter is "order by"
			cursor = database.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS,
										null, null, null, null, ItemsTable.COLUMN_NAME);			
		}else{
			String[] categories = {category};

			cursor = database.query(ItemsTable.TABLE_ITEMS, ItemsTable.ALL_COLUMNS,
					ItemsTable.COLUMN_CATEGORY + "=?", categories, null, null, ItemsTable.COLUMN_NAME);
		}



		while(cursor.moveToNext()){
			DataItem item = new DataItem();
			item.setItemId(cursor.getString(
				cursor.getColumnIndex(ItemsTable.COLUMN_ID)));

			item.setItemName(cursor.getString(
				cursor.getColumnIndex(ItemsTable.COLUMN_NAME)));

			item.setDescription(cursor.getString(
				cursor.getColumnIndex(ItemsTable.COLUMN_DESCRIPTION)));

			item.setCategory(cursor.getString(
				cursor.getColumnIndex(ItemsTable.COLUMN_CATEGORY)));

			item.setSortPosition(cursor.getInt(
				cursor.getColumnIndex(ItemsTable.COLUMN_POSITION)));

			item.setPrice(cursor.getDouble(
				cursor.getColumnIndex(ItemsTable.COLUMN_PRICE)));

			item.setImage(cursor.getString(
				cursor.getColumnIndex(ItemsTable.COLUMN_IMAGE)));

			dataItems.add(item);
		}

		cursor.close();

		return dataItems;
	}

}