package com.nd.httpproxy.Guid;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nd.httpproxy.R;
import com.nd.httpproxy.WifiDirectPkg.MainActivity;

public class MakeServer extends AppCompatActivity {


    private Button createServer = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_make_server);

        initiallization();
        createServer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getApplicationContext() , DetailsForm.class));
                startActivity(new Intent( getApplicationContext() , MainActivity.class));
            }
        });

    }

    private void initiallization(){
        createServer = this.findViewById(R.id.make_server);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
