package kaushik.theappcompany.thegroceryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import kaushik.theappcompany.thegroceryapp.Model.Cart;
import kaushik.theappcompany.thegroceryapp.Prevalent.Prevalent;
import kaushik.theappcompany.thegroceryapp.ViewHolder.CartViewHolder;

public class CartActivity extends AppCompatActivity {

    private RecyclerView cartList;
    private RecyclerView.LayoutManager layoutManager;
    private Button proceedToCheckoutButton;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartList = (RecyclerView) findViewById(R.id.orderList);
        cartList.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        cartList.setLayoutManager(layoutManager);

        proceedToCheckoutButton = (Button) findViewById(R.id.proceedToCheckoutButton);


        proceedToCheckoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(CartActivity.this, ConfirmFinalOrderActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                .setQuery(cartListRef.child("User View")
                .child(Prevalent.currentOnlineUser.getEmail()).child("Products"), Cart.class)
                        .build();

        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter =
                new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull CartViewHolder cartViewHolder, final int i, @NonNull final Cart model) {

                        cartViewHolder.productQuantityinCart.setText("x" + model.getQuantity());
                        cartViewHolder.productNameinCart.setText(model.getPname());

                        cartViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                CharSequence options []= new CharSequence[]{

                                        "Edit",
                                        "Delete"

                                };
                                AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                                builder.setTitle("Cart Options: ");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        if(which==0){

                                            Intent intent = new Intent(CartActivity.this, ProductDetailsActivity.class);
                                            intent.putExtra("Pid", model.getPid());
                                            startActivity(intent);
                                        }

                                        if(which==1){

                                            cartListRef.child("User View")
                                                    .child(Prevalent.currentOnlineUser.getEmail())
                                                    .child("Products")
                                                    .child(model.getPid())
                                                    .removeValue()
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {

                                                            if(task.isSuccessful()){
                                                                Toast.makeText(CartActivity.this, "Item Removed.", Toast.LENGTH_SHORT).show();
                                                                Intent intent = new Intent(CartActivity.this, HomeActivity.class);
                                                                startActivity(intent);
                                                            }

                                                        }
                                                    });
                                        }

                                    }
                                });

                                builder.show();
                            }
                        });


                    }

                    @NonNull
                    @Override
                    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cartlayout,parent,false);
                        CartViewHolder holder = new CartViewHolder(view);
                        return holder;
                    }
                };

          cartList.setAdapter(adapter);
          adapter.startListening();


    }
}