package com.example.ett15084.webbrowser;

import android.content.ClipData;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    WebView webView;
    EditText writeURL;
    TextWatcher listener;
    CharSequence webPageWritten = "";
    ProgressBar progressBar;
    ImageView tinyImage; // Nettisivun pikkukuvalogohomma
    LinearLayout myLinearLayout;
    MenuItem goToPage;
    EditText searchPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView = findViewById(R.id.webView);
        myLinearLayout = findViewById(R.id.myLinearLayout);
        goToPage = findViewById(R.id.goToPage); // GO nappula

        webView.getSettings().setJavaScriptEnabled(true);
        //webView.getSettings().getJavaScriptEnabled(); // Miksi true ei kelpaa?
        writeURL = findViewById(R.id.writeURL);

        progressBar = findViewById(R.id.progressBar);
        progressBar.setMax(100);
        tinyImage = findViewById(R.id.tinyImage);



        searchPage();
        showPage();
    }

    public void searchPage(){
        ActionBar actionBar = getSupportActionBar();
        getSupportActionBar().setCustomView(R.layout.action_bar);
        searchPage = actionBar.getCustomView().findViewById(R.id.searchPage);

        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME); // Miksi pitää olla vertical bar eikä pilkku?
        searchPage.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                System.out.println("Nappia paintettu");
                showPage();
                return false;
            }
        });
        searchPage.setOnClickListener(new View.OnClickListener() { // Osoitekenttää klikatessa se tyhjentyy kirjoittamista varten
            @Override
            public void onClick(View v) {
                searchPage.setText("");
            }
        });

        listener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                webPageWritten = s;
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        searchPage.addTextChangedListener(listener);

        /*goToPage.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                showPage();
                return false;
            }
        });*/


    }

    public void setWriteURL(){
        listener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                webPageWritten = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
                showPage();
            }
        };
        writeURL.addTextChangedListener(listener);

    }

    public void showPage(){
        if(webPageWritten.length() == 0){
            webView.loadUrl("http://www.google.com");
        }
        else {
            webView.loadUrl("http://" + webPageWritten);
        }

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                myLinearLayout.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 1s = 1000ms
                        myLinearLayout.setVisibility(View.GONE);
                    }
                }, 1000);
                //myLinearLayout.setVisibility(View.GONE);
                super.onPageFinished(view, url);
            }
        });
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                progressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                //getSupportActionBar().setTitle(title);
                searchPage.setText(title);
                System.out.println(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                tinyImage.setImageBitmap(icon);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_homma, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuBack:
                onBackPressed();
                break;

            case R.id.menuForward:
                onForwardPressed();
                break;

            case R.id.refresh:
                webView.reload();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onForwardPressed(){
        if(webView.canGoForward()){
            webView.goForward();
        }

        else{
            Toast.makeText(this, "Can't go forward", Toast.LENGTH_SHORT).show();

        }
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack()){
            webView.goBack();
        }
        else {
            finish();
        }
}
}
