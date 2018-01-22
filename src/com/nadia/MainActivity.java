package com.nadia;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.GridLayoutManager;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.util.Log;
import android.widget.TextView;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.util.List;
import java.util.Collections;
import java.util.Comparator;
import java.util.ArrayList;

import com.nadia.model.DataItem;
import com.nadia.model.SampleDataProvider;
import com.nadia.util.JSONHelper;
import com.nadia.database.DBHelper;
import com.nadia.database.DataSource;

public class MainActivity extends AppCompatActivity
{

	private static final int LOGIN_REQUEST = 1001;
	public static final String MY_GLOBAL_PREFS = "my_global_prefs"; 
	List<DataItem> dataItemList = SampleDataProvider.dataItemList;
	List<String> itemNames = new ArrayList<>();
    private static final int REQUEST_PERMISSION_WRITE = 1002;
    private boolean permissionGranted;

    SQLiteDatabase database;
    DataSource dataSource;
    List<DataItem> dbItems;
    DrawerLayout drawerLayout;
    ListView drawerList;
    String[] categories;
    RecyclerView recyclerView;
    RecyclerAdapter adapter;


    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkPermissions();
        // SQLiteOpenHelper dbHelper = new DBHelper(this);
        // database = dbHelper.getWritableDatabase();
        dataSource = new DataSource(this);
        dataSource.open();
        dataSource.seedDatabase(dataItemList);

        Toast.makeText(this, "Database acquired", Toast.LENGTH_SHORT).show();

        // Managing sliding navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        categories = getResources().getStringArray(R.array.categories);
        drawerList = (ListView) findViewById(R.id.left_drawer);
        drawerList.setAdapter(new ArrayAdapter<>(this, 
            R.layout.drawer_list_item, categories));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, 
                int position, long id)
            {
                String category = categories[position];
                Toast.makeText(MainActivity.this, "You chose " + category,
                    Toast.LENGTH_SHORT).show();
                drawerLayout.closeDrawer(drawerList);
                displayDataItems(category);
            }
        });


        /*DataItem item = new DataItem(
        	null, "My menu item", "a category", "a description",
        	1, 9.95, "apple_pie.jpg"
        );*/

        /*TextView textView = (TextView) findViewById(R.id.merhabaText);
        textView.setText(item.toString());*/
        /*Collections.sort(dataItemList, new Comparator<DataItem>(){
        	@Override
        	public int compare(DataItem o1, DataItem o2){
        		return o1.getItemName().compareTo(o2.getItemName());
        	}
        });*/
        // dbItems = dataSource.getAllItems(); // @displayDataItems()
		/*for(DataItem item : dataItemList){
			itemNames.add(item.getItemName());
		}        
		Collections.sort(itemNames);*/
		/*ArrayAdapter<String> adapter = new ArrayAdapter<>(
			this, android.R.layout.simple_list_item_1, itemNames 
		);*/
		// DataItemAdapter adapter = new DataItemAdapter(this, dataItemList);
		// adapter = new RecyclerAdapter(this, dbItems); // @displayDataItems() 

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        // Second parameter is the default value
        boolean showGrid = settings.getBoolean(getString(R.string.pref_display_grid), false); 


		// ListView listView = (ListView) findViewById(android.R.id.list);
		// listView.setAdapter(adapter);
		recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        
        if(showGrid){
            // The column span property, converts the list to a grid.
            recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        }

		// recyclerView.setAdapter(adapter); // @displayDataItems()
        displayDataItems(null);
    }


    private void displayDataItems(String category){
        dbItems = dataSource.getAllItems(category);
        adapter = new RecyclerAdapter(this, dbItems);
        recyclerView.setAdapter(adapter);
    }   

    @Override
    public void onPause(){
        super.onPause();
        dataSource.close();
    }

    @Override
    public void onResume(){
        super.onResume();
        dataSource.open();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
    	getMenuInflater().inflate(R.menu.menu_main, menu);
    	return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
    	int id = item.getItemId();

        switch(id){
            case R.id.action_login:
        		Intent intent = new Intent(this, LoginActivity.class);
        		startActivityForResult(intent, LOGIN_REQUEST);                
                return true;

            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, PrefsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_export:
                // Saves json data to menuitems.json file
                boolean result = JSONHelper.exportToJSON(this, dataItemList);
                if(result){
                    Toast.makeText(this, "Data exported", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, "Export failed", Toast.LENGTH_SHORT).show();
                }
                return true;    
            case R.id.action_import:
                List<DataItem> dataItems = JSONHelper.importFromJSON(this);
                if(dataItems != null){
                    for( DataItem dataItem : dataItems){
                        Log.i("Nadia", "dataItem: " + dataItem.getItemName());
                    }
                }

                return true;    
            case R.id.action_display_all:
                displayDataItems(null);
                return true;

            case R.id.action_choose_category:                
                drawerLayout.openDrawer(drawerList);
                return true;
            default:
                return super.onOptionsItemSelected(item);    
        }
    }

    // Result came from the called "LoginActivity"
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
    	super.onActivityResult(requestCode, resultCode, data);

    	if(requestCode == LOGIN_REQUEST && resultCode == RESULT_OK ){
    		String email = data.getStringExtra(LoginActivity.EMAIL_KEY);

    		Toast.makeText(this, "You're logged in as " + email, Toast.LENGTH_SHORT)
    			.show();

    		SharedPreferences.Editor editor = 
	    		getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE).edit();	

	    	editor.putString(LoginActivity.EMAIL_KEY, email);
	    	editor.apply();

	    	SharedPreferences prefs = 
	    		getSharedPreferences(MY_GLOBAL_PREFS, MODE_PRIVATE);	
	    	String savedEmail = prefs.getString(LoginActivity.EMAIL_KEY, "");
	    	// Log.i("Nadia", "MainActivity - restored email: " + savedEmail);	
    	}
    }

    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state));
    }

    // Initiate request for permissions.
    private boolean checkPermissions() {

        if (!isExternalStorageReadable() || !isExternalStorageWritable()) {
            Toast.makeText(this, "This app only works on devices with usable external storage",
                    Toast.LENGTH_SHORT).show();
            return false;
        }

        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            // ActivityCompat.requestPermissions(MainActivity.this,
            requestPermissions(
                    // new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"},
                    REQUEST_PERMISSION_WRITE);
            return false;
        } else {
            return true;
        }
    }

    // Handle permissions result
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_WRITE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    permissionGranted = true;
                    Toast.makeText(this, "External storage permission granted",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "You must grant permission!", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
