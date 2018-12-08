package com.example.android.bargainmate;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class AboutActivity extends AppCompatActivity {

    private AdView adView;
    InterstitialAd mInterAd;
    TextView desc;
    FrameLayout site, email, settings;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        adView=findViewById(R.id.ad_view2);
        desc=findViewById(R.id.textView2);
        site=findViewById(R.id.site);
        email=findViewById(R.id.email);
        settings=findViewById(R.id.setings);
        desc.setText("Bargain Mate helps you find the \nbest deals from all online stores");
        setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        site.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ourSite();
            }
        });
        email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                emailMe();
            }
        });
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent setInt=new Intent(AboutActivity.this, SettingsActivity.class);
                startActivity(setInt);
            }
        });

        MobileAds.initialize(this, getString(R.string.admob_app_id));

        mInterAd = new InterstitialAd(AboutActivity.this);
        mInterAd.setAdUnitId(getResources().getString(R.string.admob_web_interstital_id));


        AdRequest adRequestInter=new AdRequest.Builder()
                .addTestDevice("48709D5C5E343F786ADFBA371BFBF7BA")
                .build();

        mInterAd.loadAd(adRequestInter);

        if (mInterAd !=null){

        mInterAd.setAdListener(new AdListener(){
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
            }

            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
            }

        });}



        AdRequest adRequest=new AdRequest.Builder()
                .addTestDevice("48709D5C5E343F786ADFBA371BFBF7BA")
                .build();

        if (adView !=null)
        {adView.setAdListener(new AdListener(){
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




        });}

        adView.loadAd(adRequest);

    }

    @Override
    protected void onPause() {
        if (adView !=null){
            adView.pause();
        }

        if (mInterAd !=null && mInterAd.isLoaded()){
            mInterAd.show();
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.godown, R.anim.godown);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==android.R.id.home){
            onBackPressed();

        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        if(adView !=null){
            adView.destroy();
        }
        super.onDestroy();
    }

    public void ourSite() {

        Uri uri = Uri.parse("https://onlinestorecompare.herokuapp.com/#");
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }}

    public void emailMe(){
        Intent intent=new Intent(Intent.ACTION_SEND);
        intent.setData(Uri.parse("mailto:"));
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"anekepeter.dev@gmail.com"});
        intent.putExtra(Intent.EXTRA_SUBJECT, "RE: From Bargain Mate: ");
        startActivity(intent);
    }

}
