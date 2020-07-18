package kaushik.theappcompany.thegroceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import kaushik.theappcompany.thegroceryapp.Model.Products;
import kaushik.theappcompany.thegroceryapp.Prevalent.Prevalent;

public class ProductDetailsActivity extends AppCompatActivity {

        private Button addProductToCartButton;
        private ImageView productImageDetails;
        private ElegantNumberButton numberButton;
        private TextView  productQuantityDetails, productDescriptionDetails, productNameDetails;
        private String productID = "", state = "Normal";


        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_product_details);

            productID = getIntent().getStringExtra("Pid");

            addProductToCartButton= (Button) findViewById(R.id.addProductToCartButton);
            numberButton = (ElegantNumberButton) findViewById(R.id.numberButton);
            productImageDetails = (ImageView) findViewById(R.id.productImageDetails);
            productNameDetails= (TextView) findViewById(R.id.productNameDetails);
            productDescriptionDetails = (TextView) findViewById(R.id.productDescriptionDetails);
            productQuantityDetails= (TextView) findViewById(R.id.productQuantityDetails);


            getProductDetails(productID);


            addProductToCartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view)
                {
                    if (state.equals("Order Placed") || state.equals("Order Shipped"))
                    {
                        Toast.makeText(ProductDetailsActivity.this, "you can add purchase more products, once your order is shipped or confirmed.", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        addingToCartList();
                    }
                }
            });
        }


        @Override
        protected void onStart()
        {
            super.onStart();

            CheckOrderState();
        }

        private void addingToCartList()
        {
            String saveCurrentTime, saveCurrentDate;

            Calendar calForDate = Calendar.getInstance();
            SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy");
            saveCurrentDate = currentDate.format(calForDate.getTime());

            SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss ");
            saveCurrentTime = currentDate.format(calForDate.getTime());

            final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

            final HashMap<String, Object> cartMap = new HashMap<>();
            cartMap.put("Pid", productID);
            cartMap.put("Pname", productNameDetails.getText().toString());
            cartMap.put("Quantity", productQuantityDetails.getText().toString());
            cartMap.put("Date", saveCurrentDate);
            cartMap.put("Time", saveCurrentTime);
            cartMap.put("Quantity", numberButton.getNumber());
            cartMap.put("Discount", "");

            cartListRef.child("User View").child(Prevalent.currentOnlineUser.getEmail())
                    .child("Products").child(productID)
                    .updateChildren(cartMap)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getEmail())
                                        .child("Products").child(productID)
                                        .updateChildren(cartMap)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    Toast.makeText(ProductDetailsActivity.this, "Item Added to Cart.", Toast.LENGTH_SHORT).show();

                                                    Intent intent = new Intent(ProductDetailsActivity.this, HomeActivity.class);
                                                    startActivity(intent);
                                                }
                                            }
                                        });
                            }
                        }
                    });
        }


        private void getProductDetails(String productID)
        {
            DatabaseReference productsRef = FirebaseDatabase.getInstance().getReference().child("Products");

            productsRef.child(productID).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        Products products = dataSnapshot.getValue(Products.class);

                        productNameDetails.setText(products.getPname());
                        productQuantityDetails.setText(products.getQuantity());
                        productDescriptionDetails.setText(products.getDescription());
                        Picasso.get().load(products.getImage()).into(productImageDetails);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }

            });
        }

        private void CheckOrderState()
        {
            DatabaseReference ordersRef;
            ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getEmail());

            ordersRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    if (dataSnapshot.exists())
                    {
                        String shippingState = dataSnapshot.child("state").getValue().toString();

                        if (shippingState.equals("shipped"))
                        {
                            state = "Order Shipped";
                        }
                        else if(shippingState.equals("not shipped"))
                        {
                            state = "Order Placed";
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }