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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.thesweetspot.MainActivity.showCart;
import static com.example.thesweetspot.RegisterActivity.setSignUpFragment;

public class ProductDetailsActivity extends AppCompatActivity {

    public static boolean running_wishlist_query = false;
    public static boolean running_cart_query = false;
    public static boolean running_rating_query = false;

    private ViewPager productImagesViewPager;
    private TabLayout viewPagerIndicator;
    public static FloatingActionButton addToWishListBtn;
    public static boolean ALREADY_ADDED_TO_WISH_LIST = false;
    public static boolean ALREADY_ADDED_TO_CART_LIST = false;

    private Button buyNowButton;
    private LinearLayout addToCartButton;
    public static MenuItem cartItem;

    private Button couponRedeemButton;
    private LinearLayout couponRedemtionLayout;

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
    private String productDescription;
    private String productOtherDetails;
    private List<ProductSpecificationModel> productSpecificationModelList = new ArrayList<>();

    private ConstraintLayout productDetailsOnlyContainer;
    private ConstraintLayout productDetailsTabsContainer;
    private ViewPager productDetailsViewPager;
    private TabLayout productDetailsTabLayout;
    private TextView productOnlyDescriptionBody;
    ///////Product Description


    //////// rating layout
    public static int initialRating;
    public static LinearLayout rateNowContainer;
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

    private Dialog signInDialog;
    private Dialog loadingDialog;
    private TextView badgeCount;

    private FirebaseUser currentUser;
    public static String productID;
    private FirebaseFirestore firebaseFirestore;
    private DocumentSnapshot documentSnapshot;

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
        addToCartButton = findViewById(R.id.add_to_cart_btn);
        firebaseFirestore = FirebaseFirestore.getInstance();
        couponRedemtionLayout = findViewById(R.id.coupon_redemption_layout);

        initialRating = -1;

        ///////loading dialog
        loadingDialog = new Dialog(ProductDetailsActivity.this);
        loadingDialog.setContentView(R.layout.loading_progress_dialog);
        loadingDialog.setCancelable(false);
        loadingDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.slider_background));
        loadingDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadingDialog.show();
        ///////loading dialog

        final List<String> productImages = new ArrayList<>();

        productID = getIntent().getStringExtra("PRODUCT_ID");
        firebaseFirestore.collection("PRODUCTS").document(productID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            documentSnapshot = task.getResult();
                            for (long x = 1; x < (long) documentSnapshot.get("no_of_product_images") + 1; x++) {
                                productImages.add(documentSnapshot.get("product_image_" + x).toString());
                            }
                            ProductImagesAdapter productImagesAdapter = new ProductImagesAdapter(productImages);
                            productImagesViewPager.setAdapter(productImagesAdapter);

                            productTitle.setText(documentSnapshot.get("product_title").toString());
                            productPrice.setText("Rs." + documentSnapshot.get("product_price").toString() + "/-");
                            cutPrice.setText("Rs." + documentSnapshot.get("cut_price").toString() + "/-");
                            averageRatingMiniView.setText(documentSnapshot.get("average_rating").toString());
                            long totalRatingValue = (long) documentSnapshot.get("total_ratings");
                            totalRatingsMiniView.setText((int) totalRatingValue + " ratings");
                            if ((boolean) documentSnapshot.get("COD")) {
                                codIndicator.setVisibility(View.VISIBLE);
                                tvCodIndicator.setVisibility(View.VISIBLE);
                            } else {
                                codIndicator.setVisibility(View.INVISIBLE);
                                tvCodIndicator.setVisibility(View.INVISIBLE);
                            }
                            long freeCoupon = (long) documentSnapshot.get("free_coupons");
                            rewardTitle.setText((int) freeCoupon + " " + documentSnapshot.get("free_coupon_title").toString());
                            rewardBody.setText(documentSnapshot.get("free_coupon_body").toString());
                            if ((boolean) documentSnapshot.get("use_tab_layout")) {
                                productDetailsTabsContainer.setVisibility(View.VISIBLE);
                                productDetailsOnlyContainer.setVisibility(View.GONE);
                                productDescription = documentSnapshot.get("product_description").toString();
                                productOtherDetails = documentSnapshot.get("product_other_details").toString();
                                for (long x = 1; x <= (long) documentSnapshot.get("total_spec_titles"); x++) {
                                    productSpecificationModelList.add(new ProductSpecificationModel(0, documentSnapshot.get("spec_title_" + x).toString()));
                                    for (long y = 1; y <= (long) documentSnapshot.get("spec_title_" + x + "_total_fields"); y++) {
                                        productSpecificationModelList.add(new ProductSpecificationModel(1,
                                                documentSnapshot.get("spec_title_" + x + "_field_" + y + "_name").toString(),
                                                documentSnapshot.get("spec_title_" + x + "_field_" + y + "_value").toString()
                                        ));
                                    }
                                }
                            } else {
                                productDetailsTabsContainer.setVisibility(View.GONE);
                                productDetailsOnlyContainer.setVisibility(View.VISIBLE);
                                productOnlyDescriptionBody.setText(documentSnapshot.get("product_description").toString());
                            }
                            long tempTotalRatings = (long) documentSnapshot.get("total_ratings");
                            totalRatings.setText((int) tempTotalRatings + " ratings");
                            for (int x = 0; x < 5; x++) {
                                TextView rating = (TextView) ratingsNumberContainer.getChildAt(x);
                                rating.setText(String.valueOf((long) documentSnapshot.get((5 - x) + "_star")));
                                ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                progressBar.setMax((int) tempTotalRatings);
                                progressBar.setProgress(Integer.parseInt(String.valueOf((long) documentSnapshot.get((5 - x) + "_star"))));
                            }
                            totalRatingsFigure.setText(String.valueOf((long) documentSnapshot.get("total_ratings")));
                            averageRating.setText(documentSnapshot.get("average_rating").toString());
                            productDetailsViewPager.setAdapter(new ProductDetailsAdapter(getSupportFragmentManager(), 0, productDetailsTabLayout.getTabCount(), productDescription, productOtherDetails, productSpecificationModelList));

                            if (currentUser != null) {
                                if (DBqueries.myRatings.size() == 0) {
                                    DBqueries.loadRatingList(ProductDetailsActivity.this);
                                }
                                if (DBqueries.cartList.size() == 0) {
                                    DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false, badgeCount);
                                }
                                if (DBqueries.wishList.size() == 0) {
                                    DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
                                } else {
                                    loadingDialog.dismiss();
                                }

                            } else {
                                loadingDialog.dismiss();
                            }

                            if (DBqueries.myRatedIds.contains(productID)) {
                                int index = DBqueries.myRatedIds.indexOf(productID);
                                initialRating = Integer.parseInt(String.valueOf(DBqueries.myRatings.get(index))) - 1;
                                setRating(initialRating);
                            }

                            if (DBqueries.wishList.contains(productID)) {
                                ALREADY_ADDED_TO_CART_LIST = true;
                            } else {
                                ALREADY_ADDED_TO_CART_LIST = false;
                            }

                            if (DBqueries.wishList.contains(productID)) {
                                ALREADY_ADDED_TO_WISH_LIST = true;
                                addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                            } else {
                                addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                ALREADY_ADDED_TO_WISH_LIST = false;
                            }
                            if ((boolean) documentSnapshot.get("in_stock")) {

                                addToCartButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (currentUser == null) {
                                            signInDialog.show();
                                        } else {

                                            if (!running_cart_query) {
                                                running_cart_query = true;
                                                if (ALREADY_ADDED_TO_CART_LIST) {
                                                    running_cart_query = false;
                                                    Toast.makeText(ProductDetailsActivity.this, "Already added to cart!", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Map<String, Object> addProduct = new HashMap<>();
                                                    addProduct.put("product_ID_" + DBqueries.cartList.size(), productID);
                                                    addProduct.put("list_size", (long) (DBqueries.cartList.size() + 1));

                                                    firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_CART")
                                                            .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {

                                                                if (DBqueries.wishListModelList.size() != 0) {
                                                                    DBqueries.cartItemModelList.add(new CartItemModel(
                                                                            CartItemModel.CART_ITEM,
                                                                            productID,
                                                                            documentSnapshot.get("product_image_1").toString(),
                                                                            documentSnapshot.get("product_title").toString(),
                                                                            (long) documentSnapshot.get("free_coupons"),
                                                                            documentSnapshot.get("product_price").toString(),
                                                                            documentSnapshot.get("cut_price").toString(),
                                                                            (long) 1,
                                                                            (long) 0,
                                                                            (long) 0,
                                                                            (boolean)documentSnapshot.get("in_stock")
                                                                    ));
                                                                }

                                                                ALREADY_ADDED_TO_CART_LIST = true;
                                                                DBqueries.cartList.add(productID);
                                                                Toast.makeText(ProductDetailsActivity.this, "Added to cart successfully!", Toast.LENGTH_SHORT).show();
                                                                invalidateOptionsMenu();
                                                                running_cart_query = false;
                                                            } else {
                                                                running_cart_query = false;
                                                                String error = task.getException().getMessage();
                                                                Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });

                                                }
                                            }

                                        }
                                    }
                                });

                            } else {
                                buyNowButton.setVisibility(View.GONE);
                                TextView outOfStock = (TextView) addToCartButton.getChildAt(0);
                                outOfStock.setText("Out of Stock");
                                outOfStock.setTextColor(getResources().getColor(R.color.colorPrimary));
                                outOfStock.setCompoundDrawables(null, null, null, null);
                            }
                        } else {
                            loadingDialog.dismiss();
                            String error = task.getException().getMessage();
                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        viewPagerIndicator.setupWithViewPager(productImagesViewPager, true);

        addToWishListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    if (!running_wishlist_query) {
                        running_wishlist_query = true;
                        if (ALREADY_ADDED_TO_WISH_LIST) {
                            int index = DBqueries.wishList.indexOf(productID);
                            DBqueries.removeFromWishList(index, ProductDetailsActivity.this);
                            addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                        } else {
                            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                            Map<String, Object> addProduct = new HashMap<>();
                            addProduct.put("product_ID_" + DBqueries.wishList.size(), productID);
                            addProduct.put("list_size", (long) (DBqueries.wishList.size() + 1));

                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_WISHLIST")
                                    .update(addProduct).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {


                                        if (DBqueries.wishListModelList.size() != 0) {
                                            DBqueries.wishListModelList.add(new WishListModel(
                                                    productID,
                                                    documentSnapshot.get("product_image_1").toString(),
                                                    documentSnapshot.get("product_title").toString(),
                                                    (long) documentSnapshot.get("free_coupons"),
                                                    documentSnapshot.get("average_rating").toString(),
                                                    (long) documentSnapshot.get("total_ratings"),
                                                    documentSnapshot.get("product_price").toString(),
                                                    documentSnapshot.get("cut_price").toString(),
                                                    (boolean) documentSnapshot.get("COD")
                                            ));
                                        }

                                        ALREADY_ADDED_TO_WISH_LIST = true;
                                        addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
                                        DBqueries.wishList.add(productID);
                                        Toast.makeText(ProductDetailsActivity.this, "Added to wishlist!", Toast.LENGTH_SHORT).show();

                                    } else {
                                        addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
                                        String error = task.getException().getMessage();
                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }
                                    running_wishlist_query = false;

                                }
                            });

                        }
                    }
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
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            final int starPosition = x;
            rateNowContainer.getChildAt(x).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentUser == null) {
                        signInDialog.show();
                    } else {

                        if (starPosition != initialRating) {
                            if (!running_rating_query) {

                                running_rating_query = true;
                                setRating(starPosition);
                                Map<String, Object> updateRating = new HashMap<>();
                                if (DBqueries.myRatedIds.contains(productID)) {
                                    TextView oldRating = (TextView) ratingsNumberContainer.getChildAt(5 - initialRating - 1);
                                    TextView finalRating = (TextView) ratingsNumberContainer.getChildAt(5 - starPosition - 1);

                                    updateRating.put((initialRating + 1) + "_star", Long.parseLong(oldRating.getText().toString()) - 1);
                                    updateRating.put((starPosition + 1) + "_star", Long.parseLong(finalRating.getText().toString()) + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition + 1, false));

                                } else {

                                    updateRating.put((starPosition + 1) + "_star", (long) documentSnapshot.get((starPosition + 1) + "_star") + 1);
                                    updateRating.put("average_rating", calculateAverageRating((long) starPosition - initialRating, true));
                                    updateRating.put("total_ratings", (long) documentSnapshot.get("total_ratings") + 1);

                                }
                                firebaseFirestore.collection("PRODUCTS").document(productID)
                                        .update(updateRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Map<String, Object> myRating = new HashMap<>();
                                            if ((DBqueries.myRatedIds.contains(productID))) {
                                                myRating.put("rating_" + DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);
                                            } else {
                                                myRating.put("list_size", (long) DBqueries.myRatedIds.size() + 1);
                                                myRating.put("product_ID_" + DBqueries.myRatedIds.size(), productID);
                                                myRating.put("rating_" + DBqueries.myRatedIds.size(), (long) starPosition + 1);
                                            }
                                            firebaseFirestore.collection("USERS").document(currentUser.getUid()).collection("USER_DATA").document("MY_RATINGS")
                                                    .update(myRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {

                                                        if ((DBqueries.myRatedIds.contains(productID))) {

                                                            DBqueries.myRatings.set(DBqueries.myRatedIds.indexOf(productID), (long) starPosition + 1);

                                                            TextView oldRating = (TextView) ratingsNumberContainer.getChildAt(5 - initialRating - 1);
                                                            TextView finalRating = (TextView) ratingsNumberContainer.getChildAt(5 - starPosition - 1);
                                                            oldRating.setText(String.valueOf(Integer.parseInt(oldRating.getText().toString()) - 1));
                                                            finalRating.setText(String.valueOf(Integer.parseInt(finalRating.getText().toString()) + 1));

                                                            Toast.makeText(ProductDetailsActivity.this, "Thank you for rating this product!", Toast.LENGTH_SHORT).show();

                                                        } else {
                                                            DBqueries.myRatedIds.add(productID);
                                                            DBqueries.myRatings.add((long) starPosition + 1);

                                                            TextView rating = (TextView) ratingsNumberContainer.getChildAt(5 - starPosition - 1);
                                                            rating.setText(String.valueOf(Integer.parseInt(rating.getText().toString()) + 1));

                                                            totalRatingsMiniView.setText(Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")) + 1) + " ratings");
                                                            totalRatings.setText(Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings")) + 1) + " ratings");
                                                            totalRatingsFigure.setText(Integer.parseInt(String.valueOf((long) documentSnapshot.get("total_ratings"))) + 1);

                                                            Toast.makeText(ProductDetailsActivity.this, "Thank you for rating this product!", Toast.LENGTH_SHORT).show();
                                                        }

                                                        for (int x = 0; x < 5; x++) {
                                                            TextView ratingFigures = (TextView) ratingsNumberContainer.getChildAt(x);
                                                            ProgressBar progressBar = (ProgressBar) ratingsProgressBarContainer.getChildAt(x);
                                                            int maxProgress = Integer.parseInt(totalRatingsFigure.getText().toString());
                                                            progressBar.setMax(maxProgress);

                                                            progressBar.setProgress(Integer.parseInt(ratingFigures.getText().toString()));
                                                        }
                                                        initialRating = starPosition;
                                                        averageRating.setText(calculateAverageRating(0, true));
                                                        averageRatingMiniView.setText(calculateAverageRating(0, true));

                                                        if (DBqueries.wishList.contains(productID) && DBqueries.wishListModelList.size() != 0) {

                                                            int index = DBqueries.wishList.indexOf(productID);
                                                            DBqueries.wishListModelList.get(index).setRating(averageRating.getText().toString());
                                                            DBqueries.wishListModelList.get(index).setTotalRatings(Long.parseLong(totalRatingsFigure.getText().toString()));
                                                        }

                                                    } else {
                                                        setRating(initialRating);
                                                        String error = task.getException().getMessage();
                                                        Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                                    }
                                                    running_rating_query = false;
                                                }
                                            });
                                        } else {
                                            running_rating_query = false;
                                            setRating(initialRating);
                                            String error = task.getException().getMessage();
                                            Toast.makeText(ProductDetailsActivity.this, error, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            });
        }
        //////// rating layout

        buyNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    Intent deliveryIntent = new Intent(ProductDetailsActivity.this, DeliveryActivity.class);
                    startActivity(deliveryIntent);
                }
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
        rewardModelList.add(new RewardModel("Cashback", "Valid till 5th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount", "Valid till 10th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free", "Valid till 15th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback", "Valid till 5th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount", "Valid till 10th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free", "Valid till 15th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Cashback", "Valid till 5th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Discount", "Valid till 10th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));
        rewardModelList.add(new RewardModel("Buy 1 Get 1 Free", "Valid till 15th oct,2021", "Get 20% cashback on any product above Rs.200/- and below Rs.3000/-"));

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

        /////////sign in dialog
        signInDialog = new Dialog(ProductDetailsActivity.this);
        signInDialog.setContentView(R.layout.sign_in_dialog);
        signInDialog.setCancelable(true);
        signInDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button dialogSignInBtn = signInDialog.findViewById(R.id.sign_in_btn);
        Button dialogSignUpBtn = signInDialog.findViewById(R.id.sign_up_btn);

        final Intent registerIntent = new Intent(ProductDetailsActivity.this, RegisterActivity.class);
        dialogSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseButton = true;
                SignUpFragment.disableCloseButton = true;
                signInDialog.dismiss();
                setSignUpFragment = false;
                startActivity(registerIntent);
            }
        });
        dialogSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInFragment.disableCloseButton = true;
                SignUpFragment.disableCloseButton = true;
                signInDialog.dismiss();
                setSignUpFragment = true;
                startActivity(registerIntent);
            }
        });
        /////////sign in dialog

    }

    @Override
    protected void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            couponRedemtionLayout.setVisibility(View.GONE);
        } else {
            couponRedemtionLayout.setVisibility(View.VISIBLE);
        }

        if (currentUser != null) {
            if (DBqueries.myRatings.size() == 0) {
                DBqueries.loadRatingList(ProductDetailsActivity.this);
            }

            if (DBqueries.wishList.size() == 0) {
                DBqueries.loadWishList(ProductDetailsActivity.this, loadingDialog, false);
            } else {
                loadingDialog.dismiss();
            }

        } else {
            loadingDialog.dismiss();
        }

        if (DBqueries.myRatedIds.contains(productID)) {
            int index = DBqueries.myRatedIds.indexOf(productID);
            initialRating = Integer.parseInt(String.valueOf(DBqueries.myRatings.get(index))) - 1;
            setRating(initialRating);
        }

        if (DBqueries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_CART_LIST = true;
        } else {
            ALREADY_ADDED_TO_CART_LIST = false;
        }

        if (DBqueries.wishList.contains(productID)) {
            ALREADY_ADDED_TO_WISH_LIST = true;
            addToWishListBtn.setSupportImageTintList(getResources().getColorStateList(R.color.colorPrimary));
        } else {
            addToWishListBtn.setSupportImageTintList(ColorStateList.valueOf(Color.parseColor("#9e9e9e")));
            ALREADY_ADDED_TO_WISH_LIST = false;
        }
        invalidateOptionsMenu();
    }

    public static void showDialogRecyclerView() {
        if (couponsRecyclerView.getVisibility() == View.GONE) {
            couponsRecyclerView.setVisibility(View.VISIBLE);
            selectedCoupon.setVisibility(View.GONE);
        } else {
            couponsRecyclerView.setVisibility(View.GONE);
            selectedCoupon.setVisibility(View.VISIBLE);
        }
    }

    public static void setRating(int starPosition) {
        for (int x = 0; x < rateNowContainer.getChildCount(); x++) {
            ImageView starBtn = (ImageView) rateNowContainer.getChildAt(x);
            starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#bebebe")));
            if (x <= starPosition) {
                starBtn.setImageTintList(ColorStateList.valueOf(Color.parseColor("#ffbb00")));
            }
        }
    }

    private String calculateAverageRating(long currentUserRating, boolean update) {
        Double totalStars = Double.valueOf(0);
        for (int x = 1; x <= 5; x++) {
            TextView ratingNo = (TextView) ratingsNumberContainer.getChildAt(5 - x);
            totalStars = totalStars + Long.parseLong(ratingNo.getText().toString()) * x;
        }
        totalStars = totalStars + currentUserRating;
        if (update) {
            return String.valueOf(totalStars / Long.parseLong(totalRatingsFigure.getText().toString())).substring(0, 3);
        } else {
            return String.valueOf(totalStars / (Long.parseLong(totalRatingsFigure.getText().toString()) + 1)).substring(0, 3);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search_and_cart_icon, menu);

        cartItem = menu.findItem(R.id.main_cart_icon);

        cartItem.setActionView(R.layout.badge_layout);
        ImageView badgeIcon = cartItem.getActionView().findViewById(R.id.badge_icon);
        badgeIcon.setImageResource(R.drawable.cart_white_icon);
        badgeCount = cartItem.getActionView().findViewById(R.id.badge_count);

        if (currentUser != null) {
            if (DBqueries.cartList.size() == 0) {
                DBqueries.loadCartList(ProductDetailsActivity.this, loadingDialog, false, badgeCount);
            } else {
                badgeCount.setVisibility(View.VISIBLE);
                if (DBqueries.cartList.size() < 99) {
                    badgeCount.setText(String.valueOf(DBqueries.cartList.size()));
                } else badgeCount.setText("99");
            }
        }

        cartItem.getActionView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentUser == null) {
                    signInDialog.show();
                } else {
                    Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                    showCart = true;
                    startActivity(cartIntent);
                }
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        } else if (id == R.id.main_search_icon) {
            return true;
        } else if (id == R.id.main_cart_icon) {
            if (currentUser == null) {
                signInDialog.show();
            } else {
                Intent cartIntent = new Intent(ProductDetailsActivity.this, MainActivity.class);
                showCart = true;
                startActivity(cartIntent);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
