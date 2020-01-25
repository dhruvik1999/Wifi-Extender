package com.nd.httpproxy;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.nd.httpproxy.Service.ProxyService;

import static java.lang.Integer.parseInt;

public class MainActivity extends AppCompatActivity {

    private EditText mPort, mNameOfServer;
    private TextView mLogs;
    private Button mOpenPort, mClosePort;
    private int port;
    private String nameOfServer = "Jarvis";
    public Handler logsHandler;
    public ProxyServer t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initiallization();



        mOpenPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                port = parseInt(mPort.getText().toString());
                nameOfServer = mNameOfServer.getText().toString();
                //startServer(port,nameOfServer);
                startService(port,nameOfServer);
            }
        });

        mClosePort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //stopServer();
                stopService();
            }
        });

    }

    public void startService(int port, String nameOfServer){



        Intent serviceIntent = new Intent( getApplicationContext() , ProxyService.class);
        serviceIntent.putExtra("port" , port);
        serviceIntent.putExtra("serverName",nameOfServer);
        startService(serviceIntent);

    }

    public void stopService(){
        Intent serviceIntent = new Intent(getApplicationContext() , ProxyService.class);
        stopService(serviceIntent);
    }

    private void startServer(int port,String nameOfServer){
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this,1000);
            }
        },1000);
        t = new ProxyServer(port, nameOfServer, this);
        t.start();
    }

    private void stopServer(){
        t.closeServe();
    }

    private void initiallization(){
        mPort = this.findViewById(R.id.port);
        mOpenPort = this.findViewById(R.id.open_port);
        mNameOfServer = this.findViewById(R.id.name_server);
        mLogs = this.findViewById(R.id.logs);
        mClosePort = this.findViewById(R.id.close_port);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //t.closeServe();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //t.closeServe();
    }
}