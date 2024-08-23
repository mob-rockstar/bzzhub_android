package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;

public class ProductAdapterHome extends RecyclerView.Adapter<ProductAdapterHome.Item> {

    private Context mContext;
    private List<CompanyDetailResponse.Product> products;
    private SelectedListener mSelectedListener;

    public ProductAdapterHome(Context mContext, List<CompanyDetailResponse.Product> products, SelectedListener selectedListener) {
        this.mContext = mContext;
        this.products = products;
        this.mSelectedListener = selectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products, parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {

        Glide.with(mContext).load(products.get(position).getImage()).centerCrop().into(holder.ivProduct);
        holder.itemView.setOnClickListener(v -> {
            mSelectedListener.onItemClicked(products.get(position).getProductId());
        });
    }

    public interface SelectedListener{
        void onItemClicked(Integer productId);
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    class Item extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_product)
        ImageView ivProduct;

        public Item(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
