package com.ar.rossier.usc_ar_app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

public class LoginActivity extends AppCompatActivity {


    private Firebase mFBD;
    private EditText emailField;
    private EditText passwordField;
    public static String userID;
    public static String userPW;
    public static String userEmail;
    public static User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Firebase.setAndroidContext(this);

        mFBD = new Firebase("https://usc-rossier-ar.firebaseio.com");
        emailField = (EditText) findViewById(R.id.emailLTF);
        passwordField = (EditText) findViewById(R.id.passwordLTF);


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onLoginUserButtonClicked(View view){
        final String e, p, n; //email, password, name
        e = emailField.getText().toString();
        userEmail = e;
        p = passwordField.getText().toString();
        userPW = p;
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);

        Log.d("LOGIN", "LOGIN BUTTON CLICKED");
        mFBD.authWithPassword(e, p, new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                System.out.println("User ID: " + authData.getUid() + ", Provider: " + authData.getProvider());
                builder.setMessage("Loging in!");
                builder.show();
                userID = authData.getUid();
                //mUser = mFBD.
                startHomeScreen();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                builder.setMessage("Login Information Not Correct!");
                builder.setPositiveButton("Open", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });
    }

    public void startHomeScreen(){
        startActivity(new Intent(this, DatabaseListActivity.class));
    }
}

