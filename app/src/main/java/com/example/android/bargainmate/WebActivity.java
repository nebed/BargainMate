package com.example.android.bargainmate;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.ShareCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.agrawalsuneet.loaderspack.loaders.CurvesLoader;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class WebActivity extends AppCompatActivity {
    String url;
    WebView webView;
    private AdView adView;
    CurvesLoader curvesLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webView=findViewById(R.id.web_view);
        adView=findViewById(R.id.ad_view1);
        curvesLoader=findViewById(R.id.spin1);

        MobileAds.initialize(this, getString(R.string.admob_app_id));



        url = getIntent().getStringExtra("link");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                if (curvesLoader.isShown()){
                curvesLoader.setVisibility(View.GONE);}
            }


        });


        webView.loadUrl(url);
        AdRequest adRequest=new AdRequest.Builder()
                .addTestDevice("48709D5C5E343F786ADFBA371BFBF7BA")
                .build();

        adView.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }

            @Override
            public void onAdLoaded() {
             super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
            }

        });

        adView.loadAd(adRequest);


    }

    @Override
    protected void onPause() {
        if (adView !=null){
            adView.pause();
        }
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (adView !=null){
            adView.resume();
        }
    }

    @Override
    protected void onDestroy() {
        if(adView !=null){
            adView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_web, menu);
        return true;
    }




    public void share() {

        String mimeType = "text/plain", title = "share", subject = url;
        Intent inten = ShareCompat.IntentBuilder.from(WebActivity.this)
                .setChooserTitle(title)
                .setType(mimeType)
                .setText(subject)
                .getIntent();

        if (inten.resolveActivity(getPackageManager()) != null) {
            startActivity(inten);
        }

    }

    public void others() {

        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.godown, R.anim.godown);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id){
            case R.id.about:
                Intent intb =new Intent(WebActivity.this, AboutActivity.class);
                startActivity(intb);
                return true;
            case android.R.id.home:
                onBackPressed();

                return true;
            case R.id.other:
                others();
                return true;
            case R.id.share:
                share();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

