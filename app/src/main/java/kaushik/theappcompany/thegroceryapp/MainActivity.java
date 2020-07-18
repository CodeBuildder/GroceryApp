package kaushik.theappcompany.thegroceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import kaushik.theappcompany.thegroceryapp.Model.Users;
import kaushik.theappcompany.thegroceryapp.Prevalent.Prevalent;

public class MainActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (Button) findViewById(R.id.loginButton);
        signupButton = (Button) findViewById(R.id.signupButton);
        loadingBar = new ProgressDialog(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        Paper.init(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);

            }
        });

        String userEmailKey = Paper.book().read(Prevalent.userEmailKey);
        String userPasswordKey = Paper.book().read(Prevalent.userPasswordKey);

        if(userEmailKey != "" && userPasswordKey != "" ){

            if(!TextUtils.isEmpty(userEmailKey)&& !TextUtils.isEmpty(userPasswordKey)){

                allowAccess(userEmailKey, userPasswordKey);

                loadingBar.setTitle("Welcome Back");
                loadingBar.setMessage("Please wait, while we redirect you to your profile.");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();
            }
        }
    }

    private void allowAccess(final String email, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("Users").child(email).exists()) {

                    Users usersData = dataSnapshot.child("Users").child(email).getValue(Users.class);

                    if (usersData.getEmail().equals(email)) {

                        if (usersData.getPassword().equals(password)) {

                            Toast.makeText(MainActivity.this, "Welcome Back!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                        }

                        else{
                            loadingBar.dismiss();
                            Toast.makeText(MainActivity.this, "Password is Incorrect!", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {

                    Toast.makeText(MainActivity.this, "Account does not exist.", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(MainActivity.this, "Please create a new account!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


}