package com.nadia.database;

public class ItemsTable{
	public static final String TABLE_ITEMS = "items"; 
	public static final String COLUMN_ID = "item_id";
	public static final String COLUMN_NAME = "item_name";
	public static final String COLUMN_DESCRIPTION = "description";
	public static final String COLUMN_CATEGORY = "category";
	public static final String COLUMN_POSITION = "sort_position";
	public static final String COLUMN_PRICE = "price";
	public static final String COLUMN_IMAGE = "image";

	public static final String[] ALL_COLUMNS = 
		{
			COLUMN_ID, 
			COLUMN_NAME, 
			COLUMN_DESCRIPTION, 
			COLUMN_CATEGORY, 
			COLUMN_POSITION,
			COLUMN_PRICE,
			COLUMN_IMAGE
		};

	public static final String SQL_CREATE = 
			"create table " + TABLE_ITEMS + "(" +
				COLUMN_ID + " text primary key," +
				COLUMN_NAME + " text,"+
				COLUMN_DESCRIPTION + " text," +
				COLUMN_CATEGORY + " text," +
				COLUMN_POSITION + " integer," +
				COLUMN_PRICE + " real," +
				COLUMN_IMAGE + " text);";

	public static final String SQL_DELETE = 
		"drop table " + TABLE_ITEMS;		


}