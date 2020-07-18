package kaushik.theappcompany.thegroceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;
import kaushik.theappcompany.thegroceryapp.Model.Users;
import kaushik.theappcompany.thegroceryapp.Prevalent.Prevalent;

public class loginActivity extends AppCompatActivity {

    private EditText loginInputEmail;
    private EditText loginInputPassword;
    private TextView askAdmin;
    private TextView askNotAdmin;
    private Button loginButton;
    private ProgressDialog loadingBar;
    private String parentDbName = "Users";
    private CheckBox checkBoxRememberMe;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginInputEmail = (EditText) findViewById(R.id.loginInputEmail);
        loginInputPassword = (EditText) findViewById(R.id.loginInputPassword);
        askAdmin = (TextView) findViewById(R.id.askAdmin);
        askNotAdmin = (TextView) findViewById(R.id.askNotAdmin);
        loginButton = (Button) findViewById(R.id.loginButton);
        checkBoxRememberMe = (CheckBox) findViewById(R.id.checkBox2);
        Paper.init(this);


        loadingBar = new ProgressDialog(this);

        askNotAdmin.setVisibility(View.INVISIBLE);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(loginActivity.this, HomeActivity.class);
                startActivity(intent);

            }
        });

        askAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login Admin");
                askAdmin.setVisibility(View.INVISIBLE);
                askNotAdmin.setVisibility(View.VISIBLE);
                parentDbName = "Admins";
            }
        });

        askNotAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButton.setText("Login");
                askAdmin.setVisibility(View.VISIBLE);
                askNotAdmin.setVisibility(View.INVISIBLE);
                parentDbName = "Users";
            }
        });
    }

    private void loginUser() {

        String email = loginInputEmail.getText().toString();
        String password = loginInputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter your email!", Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter a Password!", Toast.LENGTH_LONG).show();
        } else {

            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we check over the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            allowAccessToAccount(email, password);
        }

    }

    private void allowAccessToAccount(final String email, final String password) {

        if(checkBoxRememberMe.isChecked()){

            Paper.book().write(Prevalent.userEmailKey, email);
            Paper.book().write(Prevalent.userPasswordKey, password);

        }


        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(parentDbName).child(email).exists()) {

                    Users usersData = dataSnapshot.child(parentDbName).child(email).getValue(Users.class);

                    if (usersData.getEmail().equals(email)) {

                        if (usersData.getPassword().equals(password)) {

                            if(parentDbName.equals("Admins")){

                                Toast.makeText(loginActivity.this, "Hey Admin!", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();

                                Intent intent = new Intent(loginActivity.this, adminCategoryActivity.class);
                                startActivity(intent);
                            }else if(parentDbName.equals("Users")){

                                loadingBar.dismiss();
                                Prevalent.currentOnlineUser = usersData;
                                Intent intent = new Intent(loginActivity.this, HomeActivity.class);
                                startActivity(intent);
                            }

                        } else {
                            Toast.makeText(loginActivity.this, "Password is Incorrect!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }

                    }


                } else {

                    Toast.makeText(loginActivity.this, "Account does not exist.", Toast.LENGTH_LONG).show();
                    loadingBar.dismiss();
                    Toast.makeText(loginActivity.this, "Please create a new account!", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(loginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
