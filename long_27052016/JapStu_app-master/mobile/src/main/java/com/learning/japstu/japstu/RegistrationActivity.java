package com.learning.japstu.japstu;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.JsonObject;
import com.learning.japstu.japstu.services.APIService;

import android.os.Handler;
import java.util.logging.LogRecord;

public class RegistrationActivity extends AppCompatActivity {

    private String sUsername = "";
    private String sPwd = "";
    private String sNickname = "";
    private String sEmail = "";
    private int iAge = 0;

    private boolean bSuccess = false;
    private static final int SUCCESS = 1;
    private static final int FAILED = 2;
    private static final int CONNECTION_ERROR = 0;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SUCCESS:

                    System.out.println("SYSTEM OUT : register successfully" );
                    Toast.makeText(RegistrationActivity.this,
                            "Account has been registered to system.",
                            Toast.LENGTH_LONG).show();
                    break;
                case FAILED:
                    break;
                case CONNECTION_ERROR:
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // set color
        ((TextView)findViewById(R.id.title_registration))
                .setTextColor(getResources().getColor(R.color.background_color));
        ((Button)findViewById(R.id.btn_register)).setTextColor(Color.WHITE);
        ((TextView)findViewById(R.id.lbl_age)).setTextColor(Color.BLACK);
        ((TextView)findViewById(R.id.lbl_username)).setTextColor(Color.BLACK);
        ((TextView)findViewById(R.id.lbl_password)).setTextColor(Color.BLACK);
        ((TextView)findViewById(R.id.lbl_cfmpwd)).setTextColor(Color.BLACK);
        ((TextView)findViewById(R.id.lbl_email)).setTextColor(Color.BLACK);
        ((TextView)findViewById(R.id.lbl_nickname)).setTextColor(Color.BLACK);

    }

    @Override
    public void onBackPressed() {
        ((EditText)findViewById(R.id.username)).clearFocus();
        ((EditText)findViewById(R.id.password)).clearFocus();
        ((EditText)findViewById(R.id.confirmPWD)).clearFocus();
        ((EditText)findViewById(R.id.userEmail)).clearFocus();
        ((EditText)findViewById(R.id.age)).clearFocus();
        ((EditText)findViewById(R.id.nickname)).clearFocus();

        boolean bAgree = true;
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        dialog.cancel();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        finish();
                        break;
                }
            }
        };
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Keep your informations ?")
        .setPositiveButton("Agree", dialogClickListener)
        .setNegativeButton("Discard", dialogClickListener);
        alertDialogBuilder.show();

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }
    public void doBackButtonEvent(View v){
        onBackPressed();
    }

    public void doSubmitRegistration(View v){
        // TODO : validate form
        if(validateRegistrationInformation()){
            // TODO : submit to server
            new RegisterToServerAsyncTask().execute("");
            finish();
        }
    }


    private class RegisterToServerAsyncTask extends AsyncTask <String, Void, Void> {
        ProgressDialog pg;
        JsonObject content;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pg = ProgressDialog.show(RegistrationActivity.this,
                    "Submit", "Registering to server");

        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                // call API to login
                if(APIService.registerAccount(sUsername, sPwd, sNickname, sEmail, iAge, null)){
                    mHandler.sendEmptyMessage(1);
                }else{
                    mHandler.sendEmptyMessage(2);
                }
            }catch (Exception e){
                // TODO : catch exception when failure connecting to server
                System.out.println("SYSTEM OUT : " + e.toString());
                Toast.makeText(RegistrationActivity.this,
                        R.string.cannot_connec_to_server,
                        Toast.LENGTH_LONG).show();
                mHandler.sendEmptyMessage(0);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pg.dismiss();
        }
    }

    private boolean validateRegistrationInformation(){
        EditText username = (EditText)findViewById(R.id.username);
        EditText password = (EditText)findViewById(R.id.password);
        EditText confirmPWD = (EditText)findViewById(R.id.confirmPWD);
        EditText userEmail = (EditText)findViewById(R.id.userEmail);
        EditText nickname = (EditText)findViewById(R.id.nickname);
        String temp = username.getText().toString();
        if(temp.isEmpty()){
            Toast.makeText(this, "Username cannot be empty.", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            temp = password.getText().toString();
            if(temp.isEmpty()){
                Toast.makeText(this, "Password cannot be empty.", Toast.LENGTH_SHORT)
                        .show();
                return false;
            }else{
                temp = confirmPWD.getText().toString();
                if(temp.isEmpty()) {
                    Toast.makeText(this, "Please confirm your password.", Toast.LENGTH_SHORT).show();
                    return false;
                }else if(!temp.equals(password.getText().toString())){
                    Toast.makeText(this, "Confirmed password incorrectly. ", Toast.LENGTH_SHORT).show();
                    confirmPWD.setText("");
                    return false;
                }else{
                    temp = userEmail.getText().toString();
                    if(temp.isEmpty()) {
                        Toast.makeText(this, "Email cannot be empty.", Toast.LENGTH_SHORT).show();
                        return false;
                    }else if(!temp.contains("@")){
                            Toast.makeText(this, "Email has format abc@xyz", Toast.LENGTH_SHORT).show();
                            userEmail.setText("");
                            return false;
                    }else{
                        temp = nickname.getText().toString();
                        if(temp.isEmpty()) {
                            Toast.makeText(this, "Nickname cannot be empty.", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    }
                }
            }
        }
        sUsername = username.getText().toString();
        sPwd = password.getText().toString();
        sNickname = nickname.getText().toString();
        sEmail = userEmail.getText().toString();
        iAge = Integer.parseInt(((TextView) findViewById(R.id.age)).getText().toString());
        return true;
    }
}
