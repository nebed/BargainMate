package com.example.android.bargainmate;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.agrawalsuneet.loaderspack.loaders.CurvesLoader;
import com.example.android.bargainmate.Utils.NetworkUtils;
import com.example.android.bargainmate.data.BargainContract;
import com.example.android.bargainmate.data.BargainPreferences;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, BargainAdapter.BargainAdapterOnClickHandler {
    SearchView searchView;
    RecyclerView list;
    private BargainAdapter bargainAdapter;
    private int mPosition=RecyclerView.NO_POSITION;
    private static final int BARGAIN_LOADER_ID=560904;
    ConstraintLayout root;

    LinearLayout progressBar;
    private AdView adView;
    CurvesLoader spinKit;
    InterstitialAd mInterAd;
    LinearLayout empty;



    public static final String[] BARGAIN_PROJECTION={
            BargainContract.BargainEntry._ID,
            BargainContract.BargainEntry.COLUMN_TITLE,
            BargainContract.BargainEntry.COLUMN_PRICE,
            BargainContract.BargainEntry.COLUMN_URL,
            BargainContract.BargainEntry.COLUMN_IMAGE_URL,
            BargainContract.BargainEntry.COLUMN_VENDOR
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list = findViewById(R.id.list);
        progressBar = findViewById(R.id.prog);
        adView = findViewById(R.id.ad_view);
        spinKit = findViewById(R.id.spin);
        root=findViewById(R.id.root_view);
        empty=findViewById(R.id.empty);



         checkInternet(root);


        list.setLayoutManager(new LinearLayoutManager(this));

        bargainAdapter = new BargainAdapter(this, this);

        list.setAdapter(bargainAdapter);


        MobileAds.initialize(this, getString(R.string.admob_app_id));


        getSupportLoaderManager().initLoader(BARGAIN_LOADER_ID, null, this);

        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice("48709D5C5E343F786ADFBA371BFBF7BA")
                .build();

        adView.setAdListener(new AdListener() {
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

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();
                String _id = Integer.toString(id);

                Uri uri = BargainContract.BargainEntry.BARGAIN_CONTENT_URI;
                uri = uri.buildUpon().appendPath(_id).build();

                getContentResolver().delete(uri, null, null);

                getSupportLoaderManager().restartLoader(BARGAIN_LOADER_ID, null, MainActivity.this);

            }
        }).attachToRecyclerView(list);

        mInterAd = new InterstitialAd(MainActivity.this);
        mInterAd.setAdUnitId(getResources().getString(R.string.admob_list_interstital_id));


        AdRequest adRequestInter = new AdRequest.Builder()
                .addTestDevice("48709D5C5E343F786ADFBA371BFBF7BA")
                .build();

        mInterAd.loadAd(adRequestInter);

        if (mInterAd != null) {
            mInterAd.setAdListener(new AdListener() {
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

            });
        }
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
        getMenuInflater().inflate(R.menu.main_menu, menu);

        final MenuItem myActionMenuItem = menu.findItem(R.id.search);
        searchView=(SearchView) myActionMenuItem.getActionView();
        searchView.setQueryHint("Product Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                checkForsearch(root, query);
                myActionMenuItem.collapseActionView();
                searchView.setQueryHint(query);

                if (!searchView.isIconified()){
                    searchView.setIconified(true);
                }
                return false;

            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.about) {
            Intent intent=new Intent(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
           /* URI for all rows of bargain data in our bargain table */
        Uri bargainQueryUri = BargainContract.BargainEntry.BARGAIN_CONTENT_URI;

        Boolean searchJumia = BargainPreferences.searchOn(this, "ju");
        Boolean searchJiji = BargainPreferences.searchOn(this, "ji");
        Boolean searchKara = BargainPreferences.searchOn(this, "ka");
        Boolean searchKonga = BargainPreferences.searchOn(this, "ko");
        Boolean searchSlot = BargainPreferences.searchOn(this, "sl");

        List<String> selectionList = new ArrayList<>();
        if (searchJumia){
            selectionList.add("jumia");
        }
        if (searchJiji){
            selectionList.add("jiji");
        }
        if (searchKara){
            selectionList.add("kara");
        }
        if (searchKonga){
            selectionList.add("konga");
        }
        if (searchSlot){
            selectionList.add("slot");
        }

        String selection = BargainContract.BargainEntry.COLUMN_VENDOR+" IN(?,?,?,?,?)";
        String [] selectionArgs=selectionList.toArray(new String[0]);

        return new CursorLoader(this,
                bargainQueryUri,
                BARGAIN_PROJECTION,
                selection,
                selectionArgs,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        list.setVisibility(View.VISIBLE);
        bargainAdapter.swapCursor(data);
        if (mPosition == RecyclerView.NO_POSITION) mPosition = 0;
        list.smoothScrollToPosition(mPosition);

        if (!data.moveToFirst()){
            empty.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.INVISIBLE);}

            if(data.getCount()>0){
                empty.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
            showInter();


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bargainAdapter.swapCursor(null);
    }

    @Override
    public void onClick(String url) {
        Intent webViewIntent = new Intent(MainActivity.this, WebActivity.class);
        Uri uurl=Uri.parse(url);
        webViewIntent.putExtra("link",uurl.toString());
        startActivity(webViewIntent);
        overridePendingTransition(0,0);
        showInter();
    }

    public void showLoading(){
        progressBar.setVisibility(View.VISIBLE);
        list.setVisibility(View.INVISIBLE);
    }

    public void showInter(){

        if (mInterAd !=null && mInterAd.isLoaded()){
            mInterAd.show();
        }
    }

    public void checkInternet(View view){
        ConnectivityManager connectivityManager=
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo=connectivityManager.getActiveNetworkInfo();

        if (!(activeNetInfo !=null && activeNetInfo.isConnectedOrConnecting())){

            final Snackbar snackbar=Snackbar.make(view, "You are using Bargain Mate in Offline Mode", Snackbar.LENGTH_LONG);
            snackbar.show();

        }}

    public void checkForsearch(View view, String query){
        ConnectivityManager connectivityManager=
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo=connectivityManager.getActiveNetworkInfo();

        if (!(activeNetInfo !=null && activeNetInfo.isConnectedOrConnecting())){

            final Snackbar snackbar=Snackbar.make(view, "You Need an Internet Connection to use Bargain Mate", Snackbar.LENGTH_LONG);

            list.setVisibility(View.VISIBLE);
            snackbar.show();

        }

        else {
            showLoading();
            NetworkUtils.startImmediateSync(MainActivity.this, query);
        }
    }

//    final Snackbar snackbar=Snackbar.make(view, "You Need an Internet Connection to use Bargain Mate", Snackbar.LENGTH_INDEFINITE);
//            snackbar.setAction("Dismiss", new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//            snackbar.dismiss();
//        }
//    });
//            snackbar.show();

}