package com.learning.japstu.japstu;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.os.AsyncTask;
import android.graphics.Color;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileOutputStream;

import com.learning.japstu.japstu.TopPageActivity;
import com.learning.japstu.japstu.japstuinterface.CallWebServicesInterface;
import com.learning.japstu.japstu.services.*;
import com.learning.japstu.japstu.services.APIService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.security.interfaces.RSAKey;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.learning.japstu.japstu.utils.Constants;

public class LoginActivity extends Activity implements CallWebServicesInterface{

    Button btnLogin;
    EditText edUsername, edPwd;

    TextView tvCreateAccount;
    TextView tvForgotPwd;

    String sttEvent = "LG";

    String user_name = "user1";
    String password = "123";

    public boolean bSuccess = false;
    private class LoginAsyncTask extends AsyncTask<String, Void, Void> {
        private JsonObject content;
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = ProgressDialog.show(LoginActivity.this,
                    "Login", "Checking user information");
        }

        @Override
        protected Void doInBackground(String... params) {
            try{
                content = APIService.findUserInfo(user_name, password);
            }catch (Exception e){
                finish();
            }
            return null;
        }
        protected void onPostExecute(Void unused){
            JsonObject jsonResponse = content;
            Message msg = toastHandler.obtainMessage();
            try {
                    if (user_name.equals(jsonResponse.get("userName").getAsString()) &&
                            password.equals(jsonResponse.get("password").getAsString())) {
                        bSuccess = true;
                        Message message = mHandler.obtainMessage();
                        String[] messageString = new String[3];
                        messageString[0] = jsonResponse.get("userName").getAsString();
                        messageString[1] = jsonResponse.get("nickName").getAsString();
                        messageString[2] = jsonResponse.get("userId").getAsString();
                        message.obj = messageString;
                        mHandler.sendMessage(message);
                    } else {
                        String context = "incorrect password : " + jsonResponse.get("userName");
                        msg.obj = context;
                        bSuccess = false;
                    }
            }catch (Exception e) {
                try{
                    if (jsonResponse.has("ERR002")) {
                        msg.obj = "incorrect " + user_name;
                        bSuccess = false;
                    }
                }catch (Exception eee){
                    msg.obj = getResources().getString(R.string.cannot_connec_to_server);
                }
            }finally {
                dialog.dismiss();
                if(!bSuccess) {
                    mHandler.sendEmptyMessage(0);
                    toastHandler.sendMessage(msg);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin=(Button)findViewById(R.id.btn_login);
        edUsername=(EditText)findViewById(R.id.ed_username);
        edPwd=(EditText)findViewById(R.id.ed_pwd);

        tvForgotPwd=(TextView)findViewById(R.id.txt_forgotpwd);
        tvCreateAccount=(TextView)findViewById(R.id.txt_create_account);
        tvForgotPwd.setVisibility(View.VISIBLE);

    }

    public Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(bSuccess){
                String[] content = (String[]) msg.obj;
                startTopPage(content);
            }else{
                edPwd.setText("");
            }
        }
    };

    // handler show toast

    public Handler toastHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Toast.makeText(LoginActivity.this, msg.obj.toString(), Toast.LENGTH_LONG).show();
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void doLoginEvent(View v){
        sttEvent = "LG";

        user_name = edUsername.getText().toString();
        password = edPwd.getText().toString();
        if(user_name.isEmpty()||password.isEmpty()){
            Toast.makeText(v.getContext(), R.string.request_fill_information, Toast.LENGTH_SHORT)
                    .show();
            return;
        }else{
            callWebService();
        }
    }

    public void doCreateAccountEvent(View v){

        sttEvent = "CA";
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
    }

    public void doForgotYourPwdReportEvent(View v){
        sttEvent = "FP";
        Toast.makeText(v.getContext(), R.string.forgot_pwd_warning, Toast.LENGTH_LONG).show();
    }

    @Override
    public void callWebService(){
        switch (sttEvent){
            case "LG":
                // TODO : request server user log-in account
                new LoginAsyncTask().execute("");
                break;
            case "CA":
            case "FP":
                break;
        }

    }

    @Override
    public void parseJSONContent(){
        return;
    }

    public void startTopPage(String[] content){
        if (bSuccess && content.length == 3) {
            Intent intent = new Intent(this, TopPageActivity.class);
            intent.putExtra("username", content[0]);
            intent.putExtra("nickname", content[1]);
            intent.putExtra("userId", content[2]);
            startActivity(intent);
        }else{
            Toast.makeText(LoginActivity.this, "Invalid user in database.", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        bSuccess = false;
    }
}
