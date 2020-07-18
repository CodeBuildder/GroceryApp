package kaushik.theappcompany.thegroceryapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;
import kaushik.theappcompany.thegroceryapp.Prevalent.Prevalent;

public class SettingsActivity extends AppCompatActivity {


    private CircleImageView settingsProfileImage;
    private EditText settingsEmailId, settingsName;
    private TextView closeSettings, updateAccountSettings, profileImageChangeButton;
    private Uri imageUri;
    private String myUrl = "";
    private StorageTask uploadTask;
    private StorageReference storageProfileReference;
    private String checker = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        storageProfileReference = FirebaseStorage.getInstance().getReference().child("Profile Pictures");
        settingsProfileImage = (CircleImageView) findViewById(R.id.settingsProfileImage);
        settingsEmailId = (EditText) findViewById(R.id.settingsEmailId);
        settingsName = (EditText) findViewById(R.id.settingsName);
        closeSettings = (TextView) findViewById(R.id.closeSettings);
        updateAccountSettings = (TextView) findViewById(R.id.updateAccountSettings);
        profileImageChangeButton = (TextView) findViewById(R.id.profileImageChangeButton);

        UserInfoDisplay(settingsProfileImage, settingsEmailId, settingsName);

        closeSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        updateAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checker.equals("clicked")){
                    UserInfoSaved();
                }else{
                    UpdateOnlyUserInfo();
                }
            }
        });

        profileImageChangeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checker = "clicked";

                CropImage.activity(imageUri)
                        .setAspectRatio(1,1)
                        .start(SettingsActivity.this);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode== RESULT_OK && data != null){

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            settingsProfileImage.setImageURI(imageUri);
        }else{
            Toast.makeText(this, "Error, please try again!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(SettingsActivity.this, SettingsActivity.class));
            finish();
        }
    }

    private void UpdateOnlyUserInfo() {

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

        HashMap<String, Object> userMap = new HashMap<>();

        userMap.put("name", settingsName.getText().toString());
        userMap.put("email", settingsEmailId.getText().toString());
        ref.child(Prevalent.currentOnlineUser.getEmail()).updateChildren(userMap);


        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));

        Toast.makeText(SettingsActivity.this, "Account Updated!", Toast.LENGTH_SHORT).show();
        finish();





    }

    private void UserInfoSaved() {

        if (TextUtils.isEmpty(settingsName.getText().toString()))
        {
            Toast.makeText(this, "Please give a name!.", Toast.LENGTH_SHORT).show();
        }

        else if (TextUtils.isEmpty(settingsEmailId.getText().toString()))
        {
            Toast.makeText(this, "Please give a email id!.", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked"))
        {
            uploadImage();
        }
    }

    private void uploadImage() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Updating Profile");
        progressDialog.setMessage("Please wait, while we update your account.");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        if(imageUri != null){
            final StorageReference fileReference = storageProfileReference
                    .child(Prevalent.currentOnlineUser.getEmail() + ".jpg");

            uploadTask = fileReference.putFile(imageUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if(!task.isSuccessful()){

                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if(task.isSuccessful()){
                        Uri downloadUrl =  task.getResult();
                        myUrl = downloadUrl.toString();

                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");

                        HashMap<String, Object> userMap = new HashMap<>();

                        userMap.put("name", settingsName.getText().toString());
                        userMap.put("email", settingsEmailId.getText().toString());
                        userMap.put("image", myUrl);
                        ref.child(Prevalent.currentOnlineUser.getEmail()).updateChildren(userMap);

                        progressDialog.dismiss();

                        startActivity(new Intent(SettingsActivity.this, HomeActivity.class));

                        Toast.makeText(SettingsActivity.this, "Account Updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    }else{

                        Toast.makeText(SettingsActivity.this, "Error, try again!", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }

                }
            });
        }else{
            Toast.makeText(this, "Image is not selected!", Toast.LENGTH_SHORT).show();
        }
    }

    private void UserInfoDisplay(final CircleImageView settingsProfileImage, final EditText settingsEmailId, final EditText settingsName) {

        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getEmail());

        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        String name = dataSnapshot.child("name").getValue().toString();
                        String email = dataSnapshot.child("email").getValue().toString();

                        Picasso.get().load(image).into(settingsProfileImage);
                        settingsName.setText(name);
                        settingsEmailId.setText(email);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}