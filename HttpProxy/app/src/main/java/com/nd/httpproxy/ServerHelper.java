package com.nd.httpproxy;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.widget.TextView;

import com.nd.httpproxy.DB.Sdata;
import com.nd.httpproxy.Service.ProxyService;

import java.net.ServerSocket;

public class ServerHelper {

    private Context context;
    public ServerHelper(Context context){
        this.context = context;
    }

    public void startService(int port, String nameOfServer){
        Intent serviceIntent = new Intent( context , ProxyService.class);
        serviceIntent.putExtra("port" , port);
        serviceIntent.putExtra("serverName",nameOfServer);
        Sdata.setServerOn();
        context.startService(serviceIntent);
    }

    public void stopService(){
        Intent serviceIntent = new Intent(context , ProxyService.class);
        context.stopService(serviceIntent);
        Sdata.setServerOff();
    }


    public void startLogs(final TextView textView){
        final Handler handler=new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText( LogBuilder.getLog() );
                handler.postDelayed(this,1000);
            }
        },1000);
    }

}
