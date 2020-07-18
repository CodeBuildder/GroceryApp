package kaushik.theappcompany.thegroceryapp.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import kaushik.theappcompany.thegroceryapp.Interface.ItemClickListener;
import kaushik.theappcompany.thegroceryapp.R;

public class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    public TextView productNameinCart, productDescriptioninCart, productQuantityinCart;

    private ItemClickListener itemClickListener;

    public CartViewHolder(@NonNull View itemView) {
        super(itemView);

        productDescriptioninCart = itemView.findViewById(R.id.productDescriptioninCart);
        productNameinCart = itemView.findViewById(R.id.productNameinCart);
        productQuantityinCart = itemView.findViewById(R.id.productQuantityinCart);
    }

    @Override
    public void onClick(View v) {

        itemClickListener.onClick(v, getAdapterPosition(),false);
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
}
