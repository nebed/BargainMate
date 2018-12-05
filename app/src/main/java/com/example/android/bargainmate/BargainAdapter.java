package com.example.android.bargainmate;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bargainmate.data.BargainContract;
import com.example.android.bargainmate.data.BargainPreferences;
import com.squareup.picasso.Picasso;

import java.text.NumberFormat;


/**
 * Created in com.example.android.bargainmate by PETERR on 11/27/2018.
 */
//i removed <BargainAdapter.BargainViewHolder> because i wanted to add admob ads in the recyclerView
public class BargainAdapter extends RecyclerView.Adapter {

    private final Context mContext;

    private Cursor mCursor;

    final private BargainAdapterOnClickHandler mClickHandler;

    private static final int NORMAL_ITEM_VIEW_TYPE = 0;

    private static final int UNIFIED_NATIVE_AD_VIEW_TYPE = 1;

    private static final int REPEATING_GAP=5;

    public interface BargainAdapterOnClickHandler {
        void onClick(String url);
    }

    public BargainAdapter(@NonNull Context context, BargainAdapterOnClickHandler clickHandler) {
        mContext = context;
        mClickHandler = clickHandler;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        if (viewType == UNIFIED_NATIVE_AD_VIEW_TYPE){
            View adExpressLayout=LayoutInflater.from(mContext).inflate(
                    R.layout.ad_list_item, parent, false);
            return new AdViewHolder(adExpressLayout);

        }


        View view = LayoutInflater.from(mContext).inflate(R.layout.list_item, parent, false);

        view.setFocusable(true);

        return new BargainViewHolder(view);

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        int viewType=getItemViewType(position);
        if (viewType==NORMAL_ITEM_VIEW_TYPE) {

            BargainViewHolder holderB = (BargainViewHolder) holder;

            int idIndex = mCursor.getColumnIndex(BargainContract.BargainEntry._ID);


            int titleIndex = mCursor.getColumnIndex(BargainContract.BargainEntry.COLUMN_TITLE);
            int VendorIndex = mCursor.getColumnIndex(BargainContract.BargainEntry.COLUMN_VENDOR);
            int UrlIndex = mCursor.getColumnIndex(BargainContract.BargainEntry.COLUMN_URL);
            int imageUrlIndex = mCursor.getColumnIndex(BargainContract.BargainEntry.COLUMN_IMAGE_URL);
            int priceIndex = mCursor.getColumnIndex(BargainContract.BargainEntry.COLUMN_PRICE);

            mCursor.moveToPosition(position); // get to the right location in the cursor

            Boolean searchJumia = BargainPreferences.searchOn(mContext, "ju");
            Boolean searchJiji = BargainPreferences.searchOn(mContext, "ji");
            Boolean searchKara = BargainPreferences.searchOn(mContext, "ka");
            Boolean searchKonga = BargainPreferences.searchOn(mContext, "ko");
            Boolean searchSlot = BargainPreferences.searchOn(mContext, "sl");



            // Determine the values of the wanted data
        final int id = mCursor.getInt(idIndex);
        String title= mCursor.getString(titleIndex);
        String vendor = mCursor.getString(VendorIndex);
        String imageUrl =mCursor.getString(imageUrlIndex);
        String url=mCursor.getString(UrlIndex);
        String pric=mCursor.getString(priceIndex);

        int colorGuy =0;


        switch (vendor){
            case "jumia":
               colorGuy=R.color.colorJumia;
                break;
            case "jiji":
                colorGuy=R.color.colorJiji;
                break;
            case "konga":
                colorGuy=R.color.colorKonga;
                break;
            case "slot":
                colorGuy=R.color.colorSlot;
                break;
            case "kara":
                colorGuy=R.color.colorKara;
                break;
        }


        //making the price look like a price not rubbish string
            String price=null;

            if (!pric.isEmpty()){

        NumberFormat numberFormat=NumberFormat.getInstance();
        price = numberFormat.format(Double.parseDouble(pric));}


        //Set values
        holder.itemView.setTag(id);
        holderB.title.setText(title);
        holderB.vendor.setText(vendor);
        // see, i got the naira sign
            price ="₦"+price;
        holderB.price.setText(price);
        holderB.vendor.setTextColor(mContext.getResources().getColor(colorGuy));
        holderB.line.setBackgroundColor(mContext.getResources().getColor(colorGuy));


        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.ic_broken_image_black_24dp)
                .into(holderB.image);}



        else
        {
            //i am leaving in all this logic for the eventual addition of native ads in the recyclerView
            AdViewHolder holderA=(AdViewHolder) holder;
            ViewGroup adCard= holderA.adCardView;

            if (adCard.getChildCount() >0){
                adCard.removeAllViews();
            }
            if (adCard.getParent() !=null){
                ((ViewGroup) adCard.getParent()).removeView(adCard);
            }
            adCard.setVisibility(View.GONE);

//            NativeExpressAdView adViewNative=new NativeExpressAdView(mContext);
//            adViewNative.setAdUnitId(mContext.getResources().getString(R.string.admob_banner_id));
//
//            float scale =mContext.getResources().getDisplayMetrics().widthPixels;
//            int adWidth=(int) (scale/mContext.getResources().getDisplayMetrics().density);
//
//            AdSize adSize=new AdSize((int)(360), 150);
//            adViewNative.setAdSize(adSize);
//
//            adViewNative.setAdListener(new AdListener(){
//                @Override
//                public void onAdLoaded() {
//
//                    super.onAdLoaded();
//                    Toast.makeText(mContext, "Native Ad in Adapter Loaded", Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onAdFailedToLoad(int i) {
//                    super.onAdFailedToLoad(i);
//
//                    Toast.makeText(mContext, "Native Ad in adapter failed to Load" +i, Toast.LENGTH_LONG).show();
//
//                }
//            });
//
//            adViewNative.loadAd(new AdRequest.Builder()
//                    .addTestDevice("48709D5C5E343F786ADFBA371BFBF7BA")
//                    .build()
//            );
//
//
//            adCard.addView(adViewNative);


        }
    }


    @Override
    public int getItemCount() {
        if (null == mCursor) {return 0;}
        //added this to account for the additional itemViews that will used
        //by the ads
        return mCursor.getCount();// +(mCursor.getCount()/REPEATING_GAP);
    }

    void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if (position%REPEATING_GAP ==0){
            //this is the ItemViewType of the Native Ad
            //coming soon to bargains near you
            return UNIFIED_NATIVE_AD_VIEW_TYPE;
        }
        return NORMAL_ITEM_VIEW_TYPE;
    }

    class BargainViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // this class is the ViewHolder for the normal items


        TextView title;
        TextView vendor;
        TextView price;
        TextView line;
        ImageView image;



        BargainViewHolder(View view){
            super(view);

            image=view.findViewById(R.id.image);
            title=view.findViewById(R.id.title);
            vendor=view.findViewById(R.id.vendor);
            price=view.findViewById(R.id.price);
            line=view.findViewById(R.id.line);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //this works out the onClick actions of the items
            int adapterPosition = getAdapterPosition();
            mCursor.moveToPosition(adapterPosition);
            String url = mCursor.getString(3);
            mClickHandler.onClick(url);

        }
    }

    class AdViewHolder extends RecyclerView.ViewHolder{
        // this class is the ViewHolder for the Ad items
        CardView adCardView;

        public AdViewHolder(View itemView) {
            super(itemView);

            adCardView=itemView.findViewById(R.id.ad_card_view);
        }
    }
}
