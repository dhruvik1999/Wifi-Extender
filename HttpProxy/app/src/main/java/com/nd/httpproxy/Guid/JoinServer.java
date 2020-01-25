package com.nd.httpproxy.Guid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

import com.nd.httpproxy.R;

public class JoinServer extends AppCompatActivity {


    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_server);

        webView = this.findViewById(R.id.wv_guide);
        webView.loadUrl("file:///android_asset/guide.html");

    }
}
