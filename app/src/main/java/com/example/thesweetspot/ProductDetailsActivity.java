package com.example.thesweetspot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.example.thesweetspot.MainActivity.showCart;

public class ProductDetailsActivity extends AppCompatActivity {

    private ViewPager productImagesViewPager;
    private TabLayout viewPagerIndicator;
    private FloatingActionButton addToWishListBtn;
    private static Boolean ALREADY_ADDED_TO_WISH_LIST = false;

    private Button buyNowButton;
    private Button couponRedeemButton;

    private TextView productTitle;
    private TextView productPrice;
    private TextView cutPrice;
    private TextView tvCodIndicator;
    private ImageView codIndicator;
    private TextView averageRatingMiniView;
    private TextView totalRatingsMiniView;
    private TextView rewardTitle;
    private TextView rewardBody;

    ///////Product Description
    private String  productDescription;
    private String productOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();

    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTabLayout;
    private TextView productOnlyDescriptionBody;

    ///////Product Description



    //////// rating layout
    private LinearLayout rateNowContainer;
    private TextView totalRatings;
    private TextView totalRatingsFigure;
    private LinearLayout ratingsNumberContainer;
    private LinearLayout ratingsProgressBarContainer;
    private TextView averageRating;
    //////// rating layout

    //////Coupon Dialog
    public static TextView couponTitle;
    public static TextView couponBody;
    public static TextView couponExpiryDate;
    private static RecyclerView couponsRecyclerView;
    private static LinearLayout selectedCoupon;
    //////Coupon Dialog

    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productImagesViewPager = findViewById(R.id.product_images_viewPager);
        viewPagerIndicator = findViewById(R.id.viewPager_indicator);
        addToWishListBtn = findViewById(R.id.add_to_wishlist_button);
        productDetailsViewPager = findViewById(R.id.product_details_viewPager);
        buyNowButton = findViewById(R.id.buy_now_btn);
        productDetailsTabLayout = findViewById(R.id.product_details_tabLayout);
        couponRedeemButton = findViewById(R.id.coupon_redemption_button);
        productPrice = findViewById(R.id.product_price);
        cutPrice = findViewById(R.id.cutted_price);
        productTitle = findViewById(R.id.product_title);
        averageRatingMiniView = findViewById(R.id.tv_product_rating_miniView);
        totalRatingsMiniView = findViewById(R.id.total_ratings_miniView);
        tvCodIndicator = findViewById(R.id.tv_cod_indicator);
        codIndicator = findViewById(R.id.cod_indicator_imageView);
        rewardTitle = findViewById(R.id.reward_title);
        rewardBody = findViewById(R.id.reward_body);
        productDetailsTabsContainer = findViewById(R.id.product_details_tabs_container);
        productDetailsOnlyContainer = findViewById(R.id.product_details_container);
        productOnlyDescriptionBody = findViewById(R.id.product_details_body);
        totalRatings = findViewById(R.id.total_ratings);
        ratingsNumberContainer = findViewById(R.id.ratings_number_container);
        ratingsProgressBarContainer = findViewById(R.id.ratings_progress_bar_container);
        totalRatingsFigure = findViewById(R.id.total_ratings_figure);
        averageRating = findViewById(R.id.average_rating);

        firebaseFirestore = FirebaseFirestore.getInstance();

        final List<String> productImages = new ArrayList<>();

        firebaseFirestore.collection("PRODUCTS").document("gZKCoU6VBOZ1Re0sankj").get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){

                            DocumentSnapshot documentSnapshot = task.getResult();
                            for(long x=1; x < (long)documentSnapshot.get("no_of_product_images")+1 ; x++){
                                productImages.add(documentSnapshot.get("product_image_"+x).toString());
                            }
                            ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                            productImagesViewPager.setAdapter(productImagesAdapter);

                            productTitle.setText(documentSnapshot.get("product_title").toString());
                            productPrice.setText("Rs."+documentSnapshot.get("product_price").toString()+"/-");
                            cutPrice.setText("Rs."+documentSnapshot.get("cut_price").toString()+"/-");
                            averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                            long totalRating = (long)documentSnapshot.get("total_ratings");
                            totalRatingsMiniView.setText((int) totalRating+ " ratings");
                            if((boolean)documentSnapshot.get("COD")){
                                codIndicator.setVisibility(View.VISIBLE);
                                tvCodIndicator.setVisibility(View.VISIBLE);
                            }
                            else{
                                codIndicator.setVisibility(View.INVISIBLE);
                                tvCodIndicator.setVisibility(View.INVISIBLE);
                            }
                            long freeCoupon = (long)documentSnapshot.get("free_coupons");
                            rewardTitle.setText((int)freeCoupon+" "+documentSnapshot.get("free_coupon_title").toString());
                            rewardBody.setText(documentSnapshot.get("free_coupon_body").toString());
                            if((boolean)documentSnapshot.get("use_tab_layout")){
                                productDetailsTabsContainer.setVisibility(View.VISIBLE);
                                productDetailsOnlyContainer.setVisibility(View.GONE);
                                productDescription = documentSnapshot.get("product_description").toString();
                                productOtherDetails = documentSnapshot.get("product_other_details").toString();
                                for(long x=1; x <= (long)documentSnapshot.get("total_spec_titles"); x++){
                                    productSpecificationModelList.add(new ProductSpecificationModel(0,documentSnapshot.get("spec_title_"+x).toString()));
                                    for(long y = 1; y <= (long)documentSnapshot.get("spec_title_"+x+"_total_fields"); y++){
                                        productSpecificationModelList.add(new ProductSpecificationModel(1,
                                                documentSnapshot.get("spec_title_"+x+"_field_"+y+"_name").toString(),
                                                documentSnapshot.get("spec_title_"+x+"_field_"+y+"_value").toString()
                                        ));
                                    }
                                }
                            }
                            else{
                                productDetailsTabsContainer.setVisibility(View.GONE);
                                productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                                productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                            }
                            long tempTotalRatings = (long)documentSnapshot.get("total_ratings");
                            totalRatings.setText((int) tempTotalRatings+" ratings");
                            for(int x = 0; x < 5; x++){
                                TextView rating = (TextView)ratingsNumberContainer.getChildAt(x);
                                long tempStarRatingNo = (long)documentSnapshot.get((5-x)+"_star");
                                rating.setText(String.valueOf((long)documentSnapshot.get((5-x)+"_star")));
                                ProgressBar progressBar = (ProgressBar)ratingsProgressBarContainer.getChildAt(x);
                                progressBar.setMax((int)tempTotalRatings);
                                progressBar.setProgress(Integer.parseInt(String.valueOf((long)documentSnapshot.get((5-x)+"_star"))));
                            }
                            totalRatingsFigure.setText(String.valueOf((long)documentSnapshot.get("total_ratings")));
                            averageRating.setText(documentSnapshot.get("average_rating").toString());
                            productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(),0,productDetailsTabLayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));


                        }
                        else{
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        viewPagerIndicator.setupWithViewPager(productImagesViewPager,true);

        addToWishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ALREADY_ADDED_TO_WISH_LIST){

                    ALREADY_ADDED_TO_WISH_LIST=false;
                    addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                }else{

                    ALREADY_ADDED_TO_WISH_LIST=true;
                    addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                }
            }
        });

        productDetailsViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(productDetailsTabLayout));
        productDetailsTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                productDetailsViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //////// rating layout
        rateNowContainer = findViewById(R.id.rate_now_container);
        for (int x=0; x<rateNowContainer.getChildCount(); x++){
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setRating(starPosition);
                }
            });
        }
        //////// rating layout

        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                startActivity(deliveryIntent);
            }
        });

        ////////coupon dialog redeem
        final Dialog checkCouponPriceDialog = new Dialog(ProductDetailsActivity.this);
        checkCouponPriceDialog.setContentView(R.layout.coupon_redeem_dialog);
        checkCouponPriceDialog.setCancelable(true);
        checkCouponPriceDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ImageView toggleRecyclerView = checkCouponPriceDialog.findViewById(R.id.toggle_recycler_view);
        couponsRecyclerView = checkCouponPriceDialog.findViewById(R.id.coupons_recycler_view);
        selectedCoupon = checkCouponPriceDialog.findViewById(R.id.selected_coupon_container);
        couponTitle = checkCouponPriceDialog.findViewById(R.id.coupon_title);
        couponExpiryDate = checkCouponPriceDialog.findViewById(R.id.coupon_validity);
        couponBody = checkCouponPriceDialog.findViewById(R.id.coupon_body);

        TextView originalPrice = checkCouponPriceDialog.findViewById(R.id.original_price);
        TextView discountedPrice = checkCouponPriceDialog.findViewById(R.id.discounted_price);

        LinearLayoutManager layoutManager = new LinearLayoutManager(ProductDetailsActivity.this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        couponsRecyclerView.setLayoutManager(layoutManager);

        List<RewardModel> rewardModelList = new ArrayList<>();
        rewardModelList.add(new RewardModel("Cashback","Valid till 5th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","Valid till 10th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free","Valid till 15th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","Valid till 5th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","Valid till 10th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free","Valid till 15th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback","Valid till 5th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount","Valid till 10th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free","Valid till 15th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));

        MyRewardsAdapter myRewardsAdapter = new MyRewardsAdapter(rewardModelList, true);
        couponsRecyclerView.setAdapter(myRewardsAdapter);
        myRewardsAdapter.notifyDataSetChanged();

        toggleRecyclerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogRecyclerView();
            }
        });
        ////////coupon redeem dialog

        couponRedeemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCouponPriceDialog.show();
            }
        });

    }

    public static void showDialogRecyclerView(){
        if(couponsRecyclerView.getVisibility() == View.GONE){
            couponsRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupon.setVisibility(View.GONE);
        }
        else{
            couponsRecyclerView.setVisibility(View.GONE);
            selectedCoupon.setVisibility(View.VISIBLE);
        }
    }

    private void setRating(int starPosition) {
        for(int x = 0; x<rateNowContainer.getChildCount();x++){
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if(x <= starPosition){
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            finish();
            return true;
        }else if(id == R.id.main_search_icon){
            return true;
        }else if(id == R.id.main_cart_icon){
            Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
            showCart = true;
            startActivity(cartIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
