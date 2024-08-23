package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;

public class ProductImageListAdapter extends RecyclerView.Adapter<ProductImageListAdapter.Item>  {

    private Context mContext;
    private List<String> products;
    private SelectedListener mSelectedListener;

    public ProductImageListAdapter(Context mContext, List<String> products,SelectedListener selectedListener) {
        this.mContext = mContext;
        this.products = products;
        this.mSelectedListener = selectedListener;
    }

    public void setImages(List<String> mProducts){
        this.products = mProducts;
        notifyDataSetChanged();
    }

    public void setImage(String path, int position){
        products.set(position,path);
        notifyItemChanged(position);
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_image, parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        Glide.with(mContext).applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.ic_placeholder_product_image).error(R.drawable.ic_placeholder_product_image)).load(products.get(position)).centerCrop().into(holder.ivProduct);

        holder.itemView.setOnClickListener(v -> {
            mSelectedListener.onSelected(position);
        });
    }

    public ArrayList<String> getImageList(){
        ArrayList<String> images = new ArrayList<>();
        for (int i = 0;i< products.size();i++){
            if (products.get(i) != null && !products.get(i).isEmpty()){
                images.add(products.get(i));
                Log.d("ImageURL", "url:" + products.get(i));
            }
        }
        return images;
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public interface SelectedListener{
        void onSelected(int position);
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
