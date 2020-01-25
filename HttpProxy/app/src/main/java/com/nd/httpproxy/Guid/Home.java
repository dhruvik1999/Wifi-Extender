package com.nd.httpproxy.Guid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.nd.httpproxy.DB.Sdata;
import com.nd.httpproxy.R;
import com.nd.httpproxy.ServerHelper;
import com.nd.httpproxy.WifiDirectPkg.MainActivity;
import com.nd.httpproxy.WifiDirectPkg.WifiAccessPoint;
import com.nd.httpproxy.WifiDirectPkg.WifiConnection;
import com.nd.httpproxy.WifiDirectPkg.WifiServiceSearcher;

public class Home extends AppCompatActivity {

    TextView tv_port, tv_name, tv_logs;
    Button b_start, b_stop;
    ToggleButton tb_server;
    private ServerHelper serverHelper;

    WifiAccessPoint mWifiAccessPoint = null;
    WifiConnection mWifiConnection = null;

    @Override
    protected void onStart() {
        super.onStart();
        serverHelper = new ServerHelper(this);
        tv_logs = this.findViewById(R.id.tv_logs);
        serverHelper.startLogs(tv_logs);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_home);

        initiallization();

        b_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Sdata.getServerStatus() == false ) {
                    serverHelper.startService(Sdata.getPort(), Sdata.getNameOfServer());
                    serverHelper.startLogs(tv_logs);
                   // startActivity(new Intent( getApplicationContext(), MainActivity.class));

                    mWifiAccessPoint = new WifiAccessPoint(getApplicationContext());
                    mWifiAccessPoint.Start();

//                    mWifiServiceSearcher = new WifiServiceSearcher(that);
//                    mWifiServiceSearcher.Start();

                }else{
                    Toast.makeText(getApplicationContext() , "Server is already started" , Toast.LENGTH_SHORT).show();
                }
            }
        });

        b_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( Sdata.getServerStatus()==true ) {
                    serverHelper.stopService();
                    mWifiAccessPoint.Stop();
                }else{
                    Toast.makeText(getApplicationContext() , "Server is already closed" , Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void initiallization() {
        tv_port = this.findViewById(R.id.show_port);
        tv_name = this.findViewById(R.id.show_name);
        b_start = this.findViewById(R.id.show_start);
        b_stop = this.findViewById(R.id.show_stop);

        tv_name.setText(Sdata.getNameOfServer());
        tv_port.setText(String.valueOf(Sdata.getPort()));
    }
}
