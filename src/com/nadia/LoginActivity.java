package com.nadia;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity{

	public static final String EMAIL_KEY = "email_key";
	// UI Preferences
	private TextView emailView;
	private EditText passwordView;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		emailView = (TextView) findViewById(R.id.email);
		passwordView = (EditText) findViewById(R.id.password);


		SharedPreferences prefs = 
			getSharedPreferences(MainActivity.MY_GLOBAL_PREFS, MODE_PRIVATE);

		// The second parameter is the default value, 
		// if the target key was not found.	
		String email = prefs.getString(EMAIL_KEY, "");	

		// Log.i("Nadia", email);
		// Log.i("Nadia", "empty string: " + TextUtils.isEmpty(email));

		if(!TextUtils.isEmpty(email)){
			emailView.setText(email);
		}

		Button loginButton = (Button) findViewById(R.id.loginButton);
		loginButton.setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View view){
				Snackbar.make(view, "Email registeration success", Snackbar.LENGTH_SHORT)
					.setAction("Action", null).show();

				attemptLogin();	

			}

		});
	}

	private void attemptLogin(){
		emailView.setError(null);
		passwordView.setError(null);

		String email = emailView.getText().toString();
		String password = emailView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password
		if(TextUtils.isEmpty(password) && !isValidPassword(password)){
			passwordView.setError("This password is too short");
			focusView = passwordView;
			cancel = true;
		}

		if(TextUtils.isEmpty(email)){
			emailView.setError("Email is a required field");
			focusView = emailView;
			cancel = true;
		}else if(!isValidEmail(email)){
			emailView.setError("Email address is invalid");
			focusView = emailView;
			cancel = true;
		}

		if(cancel){
			focusView.requestFocus();
		}else{
			getIntent().putExtra(EMAIL_KEY, email);
			setResult(RESULT_OK, getIntent());
			finish(); // Calling the parent intent
		}
	}

	private boolean isValidEmail(String email){return email.contains("@");}
	private boolean isValidPassword(String password){return password.length() > 4;}
}