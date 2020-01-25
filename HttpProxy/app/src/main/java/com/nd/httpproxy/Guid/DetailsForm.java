package com.nd.httpproxy.Guid;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.nd.httpproxy.DB.Sdata;
import com.nd.httpproxy.R;

import java.io.IOException;
import java.net.ServerSocket;

public class DetailsForm extends AppCompatActivity {

    //find the valid range of port in android
    EditText et_port,et_name;
    Button b_save;
    RadioButton radioButton;
    EditText et_speed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_details_form);

        initialization();

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String port = et_port.getText().toString();
                    String nameOfServer = et_name.getText().toString();
                    String speed = et_speed.getText().toString();
                    if( validation( port , nameOfServer , speed) == true){
                        startActivity(new Intent(getApplicationContext() , Home.class));
                        //startActivity( new Intent(getApplicationContext() , MainActivity.class) );
                    }
                }catch (Exception e){
                    showMsg("Enter all details.");
                    e.printStackTrace();
                }
            }
        });


    }

    private boolean validation(String Sport, String name, String speed){

        int reservedRange = 1024;
        int port = 8080;
        try{
            port = Integer.parseInt(Sport);
        }catch (Exception e){
            showMsg("Port number should be valid integer only");
            e.printStackTrace();

            return false;
        }

        if(name.isEmpty()){
            showMsg("Name of the server is Empty");
            return false;
        }

        try{
            int sp = Integer.parseInt(speed);
            if( 1> sp || sp>100 ){
                showMsg("Speed of server should be integer between 1-100%");
                return false;
            }
        }catch (Exception e){
            showMsg("Speed of server should be integer between 1-100%");
            return false;
        }

        if( port <= reservedRange  ){
            showMsg("Port 0-"+reservedRange+" are reserved. You can't use that");
            return false;
        }

        if( checkAvailibilityOfPort(port) == false ){
            showMsg("Port :" + port + " is not available. Already in use.");
            return false;
        }

        Sdata.setPort(port);
        Sdata.setNameOfServer(name);
        showMsg("Server saved successfully.");


        return true;
    }

    private boolean checkAvailibilityOfPort(int port){
        boolean result = false;
        ServerSocket s = null;
        try{
            s=new ServerSocket(port);
            result = true;
            s.close();
        }catch (Exception e){
            result=false;
            e.printStackTrace();

        }

        if(s!= null && s.isClosed()==false){
            try {
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;

    }

    private void showMsg(String msg){
        Toast.makeText(getApplicationContext() , msg , Toast.LENGTH_SHORT).show();
    }

    private void initialization(){
        et_name = this.findViewById(R.id.name_of_server);
        et_port = this.findViewById(R.id.port_number);
        b_save = this.findViewById(R.id.save_details);
        radioButton = this.findViewById(R.id.https_radio);
        et_speed = this.findViewById(R.id.speed_of_server);

        radioButton.setChecked(true);
        et_speed.setText("100");
    }

    private void hideKeybord(){

        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
