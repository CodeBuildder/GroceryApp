package kaushik.theappcompany.thegroceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class adminAddProductActivity extends AppCompatActivity {
    private String categoryName, Description, Quantity, Pname, saveCurrentDate, saveCurrentTime;
    private ImageView productImage;
    private Button addProductButton;
    private EditText editTextProductName;
    private EditText editTextProductDescription;
    private EditText editTextProductQuantity;
    private static final int galleryPick = 1;
    private Uri imageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference productImagesRef;
    private DatabaseReference productRef;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_product);

        categoryName = getIntent().getExtras().get("Category").toString();
        productImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        productRef = FirebaseDatabase.getInstance().getReference().child("Products");


        addProductButton= (Button) findViewById(R.id.addProductButton);
        productImage = (ImageView) findViewById(R.id.productImage);
        editTextProductName = (EditText) findViewById(R.id.editTextProductName);
        editTextProductDescription = (EditText) findViewById(R.id.editTextAddDescription);
        editTextProductQuantity = (EditText) findViewById(R.id.editTextProductQuantity);
        loadingBar = new ProgressDialog(this);

        productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                validateProductData();

            }
        });
    }

    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, galleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,  Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == galleryPick && resultCode == RESULT_OK && data!= null){

            imageUri = data.getData();
            productImage.setImageURI(imageUri);
        }
    }

    private void validateProductData() {

        Description = editTextProductDescription.getText().toString();
        Quantity = editTextProductQuantity.getText().toString();
        Pname = editTextProductName.getText().toString();

        if (imageUri == null) {

            Toast.makeText(this, "Product image is mandatory!", Toast.LENGTH_SHORT).show();

        } else if (TextUtils.isEmpty(Description)) {

            Toast.makeText(this, "Please give a description!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Pname)) {

            Toast.makeText(this, "Please enter the product name!", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Quantity)) {

            Toast.makeText(this, "Please give the required Quantity!", Toast.LENGTH_SHORT).show();
        } else {
            storeProductInformation();
        }

    }

    private void storeProductInformation() {

        loadingBar.setTitle("Adding new product");
        loadingBar.setMessage("Please wait, while we add the product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM, yyyy/n");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat(" HH: mm:ss");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = productImagesRef.child(imageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(imageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(adminAddProductActivity.this, "Error: "+ message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(adminAddProductActivity.this, "Image Uploaded Successfully!", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {

                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        if(task.isSuccessful()){

                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(adminAddProductActivity.this, "Product image has been saved to the database, Kaushik!", Toast.LENGTH_SHORT).show();

                            saveProductInfoToDatabase();
                        }

                    }
                });


            }
        });

    }

    private void saveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pID", productRandomKey);
        productMap.put("Date", saveCurrentDate);
        productMap.put("Time", saveCurrentTime);
        productMap.put("Description", Description);
        productMap.put("Image", downloadImageUrl);
        productMap.put("Category", categoryName);
        productMap.put("Quantity", Quantity);
        productMap.put("Product Name", Pname);

        productRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){

                            loadingBar.dismiss();
                            Toast.makeText(adminAddProductActivity.this, "Hey Kaushik, the Product has been added successfully!", Toast.LENGTH_SHORT).show();



                            Intent intent = new Intent(adminAddProductActivity.this, adminCategoryActivity.class);
                            startActivity(intent);


                        }else {

                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(adminAddProductActivity.this, "Error "+ message , Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

}