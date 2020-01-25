package com.nd.httpproxy.Service;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import com.nd.httpproxy.LogBuilder;
import com.nd.httpproxy.ProxyServer;
import com.nd.httpproxy.R;
import com.nd.httpproxy.WifiDirectPkg.MainActivity;

import static com.nd.httpproxy.Service.App.CHANNEL_ID;


public class ProxyService extends Service {
    private int port;
    private String nameOfServer = "Jarvis";
    private LogBuilder logBuilder;
    public Handler logsHandler;
    public ProxyServer t;

    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(getApplicationContext() , "Server started" , Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        port = intent.getIntExtra("port" , 8080);
        nameOfServer = intent.getStringExtra("serverName");
        t = new ProxyServer(port, nameOfServer, this);
        t.start();
        notification();
        return START_NOT_STICKY;
    }

    public void notification(){

        Intent notificationIntent = new Intent(this , MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , notificationIntent,0  );
        Toast.makeText(getApplicationContext() , "startCommand" , Toast.LENGTH_SHORT).show();
        Notification notification = new NotificationCompat.Builder(this , CHANNEL_ID )
                .setContentTitle("Http proxy")
                .setContentText("Proxy server is running....")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1,notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        t.closeServe();
        Toast.makeText(getApplicationContext() , "Server closed" , Toast.LENGTH_SHORT).show();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
