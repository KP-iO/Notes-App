package com.example.notesapp;
/**
 * Created by khrishawn
 */
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notesapp.Service.MusicService;

/**
 * Created by khrishawn
 */
public class Instructions extends AppCompatActivity {
private WebView webView;


@Override
// Opens html file in webview
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
   setContentView(R.layout.instructions);

    webView = (WebView) findViewById(R.id.webview);
    webView.setWebViewClient(new WebViewClient());
    webView.loadUrl("file:///android_asset/Instructions/Instructions.html");


}
}
