package kaushik.theappcompany.thegroceryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class adminCategoryActivity extends AppCompatActivity {

    private ImageView fruits;
    private ImageView veges;
    private ImageView dairy;
    private ImageView flour;
    private ImageView spices;
    private ImageView rice;
    private ImageView snacks;
    private ImageView soaps;
    private ImageView stationary;
    private ImageView tissue;
    private ImageView toiletries;

    private Button logoutButton, checkOrderButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_category);

        logoutButton = (Button) findViewById(R.id.logoutButton);
        checkOrderButton = (Button) findViewById(R.id.checkOrderButton);

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(adminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        checkOrderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent= new Intent(adminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);

            }
        });

        fruits = (ImageView) findViewById(R.id.fruits);
        veges = (ImageView) findViewById(R.id.veges);
        dairy = (ImageView) findViewById(R.id.dairy);
        flour = (ImageView) findViewById(R.id.flour);
        spices = (ImageView) findViewById(R.id.spices);
        rice = (ImageView) findViewById(R.id.rice);
        snacks = (ImageView) findViewById(R.id.snacks);
        soaps = (ImageView) findViewById(R.id.soap);
        stationary = (ImageView) findViewById(R.id.stationary);
        tissue = (ImageView) findViewById(R.id.tissue);
        toiletries = (ImageView) findViewById(R.id.toiletries);




        fruits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryActivity.this, adminAddProductActivity.class);
                intent.putExtra("Category",  "fruits");
                startActivity(intent);
            }
        });

        veges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryActivity.this, adminAddProductActivity.class);
                intent.putExtra("Category",  "veges");
                startActivity(intent);
            }
        });

        dairy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryActivity.this, adminAddProductActivity.class);
                intent.putExtra("Category",  "dairy");
                startActivity(intent);
            }
        });

        flour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryActivity.this, adminAddProductActivity.class);
                intent.putExtra("Category",  "flour");
                startActivity(intent);
            }
        });

        spices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryActivity.this, adminAddProductActivity.class);
                intent.putExtra("Category",  "spices");
                startActivity(intent);
            }
        });

        snacks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryActivity.this, adminAddProductActivity.class);
                intent.putExtra("Category",  "snacks");
                startActivity(intent);
            }
        });

        rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryActivity.this, adminAddProductActivity.class);
                intent.putExtra("Category",  "rice");
                startActivity(intent);
            }
        });

        tissue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryActivity.this, adminAddProductActivity.class);
                intent.putExtra("Category",  "tissue");
                startActivity(intent);
            }
        });

        toiletries.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryActivity.this, adminAddProductActivity.class);
                intent.putExtra("Category",  "toiletries");
                startActivity(intent);
            }
        });

        soaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryActivity.this, adminAddProductActivity.class);
                intent.putExtra("Category",  "soaps");
                startActivity(intent);
            }
        });

        stationary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(adminCategoryActivity.this, adminAddProductActivity.class);
                intent.putExtra("Category",  "stationary");
                startActivity(intent);
            }
        });
    }
}