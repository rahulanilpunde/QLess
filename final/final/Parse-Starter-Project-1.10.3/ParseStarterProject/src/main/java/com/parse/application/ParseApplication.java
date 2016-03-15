package com.parse.application;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;

public class ParseApplication extends Application {
 
    @Override
    public void onCreate() {
        super.onCreate();
 
        // Add your initialization code here
       // Parse.initialize(this, YOUR_APPLICATION_ID, YOUR_CLIENT_KEY);
        Parse.initialize(this, "L7sHCHJgx5vE7wTew2PViesoY5R9rJvKijmNpaPS", "ckEf789GCLvT7DmhDwntgvwqwwLQErU16baoDWpD");

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
 
        // If you would like all objects to be private by default, remove this
        // line.
        defaultACL.setPublicReadAccess(true);
        ParseACL.setDefaultACL(defaultACL, true);
    }
}