package kaushik.theappcompany.thegroceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    private Button registerButton;
    private EditText registerInputEmail;
    private EditText registerInputName;
    private EditText registerInputPassword;
    private ProgressDialog loadingBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerButton = (Button) findViewById(R.id.registerButton);
        registerInputEmail = (EditText) findViewById(R.id.registerInputEmail);
        registerInputName = (EditText) findViewById(R.id.registerInputName);
        registerInputPassword = (EditText) findViewById(R.id.registerInputPassword);


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateAccount();
            }
        });

    }

    private void CreateAccount() {


        String name = registerInputName.getText().toString();
        String email = registerInputEmail.getText().toString();
        String password = registerInputPassword.getText().toString();
        loadingBar = new ProgressDialog(this);

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please Enter a Name! ", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email)){
            Toast.makeText(this, "Please Enter your Email-id! ", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "Please Enter a Password! ", Toast.LENGTH_SHORT).show();
        }else{

            loadingBar.setTitle("Create Account");
            loadingBar.setMessage("Please wait, while we are checking your credentials." );
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            ValidateEmailId(name, email, password);
        }
    }

    private void ValidateEmailId(final String name, final String email, final String password) {

        final DatabaseReference RootRef;

        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("Users").child(email).exists())){

                    HashMap<String, Object> userDataMap = new HashMap<>();
                    userDataMap.put("email", email);
                    userDataMap.put("password", password);
                    userDataMap.put("name", name);

                    RootRef.child("Users").child(email).updateChildren(userDataMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                        Toast.makeText(RegisterActivity.this, "Congratulations, your Account has been created!", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();

                                        Intent intent = new Intent(RegisterActivity.this, loginActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        Toast.makeText(RegisterActivity.this, "Network Error, please try again later!", Toast.LENGTH_LONG).show();
                                        loadingBar.dismiss();
                                    }

                                }
                            });


                }else{
                    Toast.makeText(RegisterActivity.this, "The User already exists, please try another email!", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();

                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}