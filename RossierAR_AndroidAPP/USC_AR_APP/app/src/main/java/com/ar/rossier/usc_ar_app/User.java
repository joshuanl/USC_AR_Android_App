package com.ar.rossier.usc_ar_app;

import android.util.Log;

import com.ar.rossier.usc_ar_app.dBase.DatabaseContent;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by itsfunshine on 2/24/2016.
 */
public class User {
    private String name;
    private String email;
    private String password;
    private String userName;
    private String securityQuestion1;
    private String securityQuestion2;
    private String answer1;
    private String answer2;
    private String lastLoginDate;
    private int numberOfScans;
    private int numberOfTriggers;
    private DatabaseContent DBContent;
    private String lastLogin;
    private ArrayList<String> loginRecord;

    public User(){

    }//end of default constructor

    public User(String name, String email, String password, String securityQ1,
                String securityQ2, String answer1, String answer2, DatabaseContent DBContent){
        this.name = name;
        this.email = email;
        this.password = password;
        this.userName = "";
        this.securityQuestion1 = securityQ1;
        this.securityQuestion2 = securityQ2;
        this.answer1 = answer1;
        this.answer2 = answer2;
        this.DBContent = DBContent;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        //System.out.println(dateFormat.format(date)); //2014/08/06 15:59:48
        lastLogin = dateFormat.format(date);
        loginRecord = new ArrayList<String>();
        loginRecord.add(lastLogin);

        Log.d("USER", "SQ1: " + securityQuestion1 + " A1: " + this.answer1 + " A2: " + this.answer2);

        for(int i=0; i < email.length(); i++){

            if(email.substring(i, i+1).compareTo("@") == 0){
                break;
            }
            userName += email.substring(i, i+1);
        }
    }//end of user

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getIDName(){ return userName; }

    public String getPassword() {return password;}

    public String getSecurityQuestion1(){return securityQuestion1;}

    public String getSecurityQuestion2(){return securityQuestion2;}

    public String getAnswer1(){return answer1;}

    public String getAnswer2(){return answer2;}

    public DatabaseContent getDBContent(){return DBContent;}

    public void setDBContent(DatabaseContent dbContent){this.DBContent = dbContent;}

    public String getLastLogin(){return lastLogin;}

    public static String getUserIDName(String email){
        String username = "";
        for(int i=0; i < email.length(); i++){

            if(email.substring(i, i+1).compareTo("@") == 0){
                break;
            }
            username += email.substring(i, i+1);
        }
        return username;
    }

    public ArrayList<String> getLoginRecord(){return loginRecord;}

    public void updateLoginRecord(String loginDate){
        loginRecord.add(loginDate);
        lastLogin = loginDate;
    }

}
