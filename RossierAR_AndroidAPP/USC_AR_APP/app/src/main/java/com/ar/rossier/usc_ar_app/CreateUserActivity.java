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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.ar.rossier.usc_ar_app.dBase.DatabaseContent;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.Map;

public class CreateUserActivity extends AppCompatActivity {

    // UI references.
    private Firebase mFBD;
    private EditText nameField;
    private EditText reenterPasswordField;
    private EditText emailField;
    private EditText passwordField;
    private EditText answerField1;
    private EditText answerField2;
    private Spinner spinner1;
    private Spinner spinner2;

    private  int errorCode; // 0: name     1: email   2: pw

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        Firebase.setAndroidContext(this);

        mFBD = new Firebase("https://usc-rossier-ar.firebaseio.com");
        nameField = (EditText) findViewById(R.id.nameCUTF);
        emailField = (EditText) findViewById(R.id.emailCUTF);
        passwordField = (EditText) findViewById(R.id.passwordCUTF);
        reenterPasswordField = (EditText) findViewById(R.id.reenterPasswordCUTF);
        answerField1 = (EditText) findViewById(R.id.securityQAnswerCUTF1);
        answerField2 = (EditText) findViewById(R.id.securityQAnswerCUTF2);
        spinner1 = (Spinner) findViewById(R.id.spinner1);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
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

    private boolean isValidSecurityQuestions(String s1, String s2, String a1, String a2){
        Log.d("CREATE", "s1: " + s1);
        Log.d("CREATE", "s2: " + s2);
        if(s1.contains("Security") || s2.contains("Security")){
            return false;
        }
        if(a1.length() == 0 || a2.length() == 0){
            return false;
        }
        if(s1.contains(s2)){
            return false;
        }
        return true;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@") && (email.contains(".com") || email.contains("edu"));
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }

    public void onRegisterButtonClicked(View view) {

        // TODO: register the new account here.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error!");
        String e, p, n, p2, sp1, sp2, a1, a2; //email, password, name, pw2, spinner1, spinner2, answer1, answer2
        n = nameField.getText().toString();
        e = emailField.getText().toString();
        p = passwordField.getText().toString();
        p2 = reenterPasswordField.getText().toString();
        sp1 = spinner1.getSelectedItem().toString();
        sp2 = spinner2.getSelectedItem().toString();
        a1 = answerField1.getText().toString();
        a2 = answerField2.getText().toString();

        if(!isEmailValid(e)){
            //validField.setText("Not a valid email!");
            builder.setMessage("Not a valid email!");
            builder.show();
            return;
        }//email not valid

        if(!isPasswordValid(p)){
            //validField.setText("Password needs at least 6 characters");
            builder.setMessage("Password needs at least 6 characters");
            builder.show();
            return;
        }//password not email

        if(p.compareTo(p2) != 0){
            //validField.setText("Passwords do not match!");
            builder.setMessage("Passwords do not match!");
            builder.show();
            return;
        }

        if(!isValidSecurityQuestions(sp1, sp2, a1, a2)){
            builder.setMessage("Need two different security questions and answers for both!");
            builder.show();
            return;
        }

        User mUser = new User(n, e, p, sp1, sp2, a1, a2, new DatabaseContent());

        //Adding user to Firebase
        Firebase userRef = mFBD.child("users").child(mUser.getIDName());
        //userRef.setValue((mUser));
        mFBD.createUser(mUser.getEmail(), mUser.getPassword(), new Firebase.ValueResultHandler<Map<String, Object>>() {
            @Override
            public void onSuccess(Map<String, Object> result) {
                System.out.println("Successfully created user account with uid: " + result.get("uid"));
                Log.d("mFBB", "Created user account with uid: " + result.get("uid"));
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
                Log.d("mFBB", "Error on creating user account");
                return;
            }
        });

        Log.d("mFBB", "Created userRef under users > idName");
        userRef.setValue(mUser);
        Log.d("mFBB", "set the value of userRef: " + mUser.getIDName() + " to user object");
        AlertDialog.Builder abuilder = new AlertDialog.Builder(this);
        //abuilder.setCancelable(false);
        abuilder.setMessage("User account created!");
        abuilder.setPositiveButton("Open", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startMainActivity();
            }
        });
        abuilder.show();
    }//end of register button
    public void startMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
