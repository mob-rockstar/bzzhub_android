package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;

public class CompanyProfileProductAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<CompanyDetailResponse.Product> products;
    private SelectedListener mSelectedListener;
    private boolean visible = false;

    public CompanyProfileProductAdapter(Context mContext, List<CompanyDetailResponse.Product> products,
                                        SelectedListener selectedListener,boolean mVisible) {
        this.mContext = mContext;
        this.products = products;
        this.mSelectedListener = selectedListener;
        this.visible = mVisible;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_products_profile, parent, false)) ;
        }else
            return new ItemAdd(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_product, parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof Item){
            Item item =  (Item) holder;
            item.ivRemove.setVisibility(visible ?View.VISIBLE : View.GONE);

            item.itemView.setOnClickListener(v -> {
                mSelectedListener.onItemClicked(products.get(position).getProductId());
            });

            item.itemView.setOnLongClickListener(v -> {
                visible = !visible;
                notifyDataSetChanged();
                return false;
            });

            item.ivRemove.setOnClickListener(v -> mSelectedListener.onRemoveProductClicked(products.get(position).getProductId()));
            Glide.with(mContext).load(products.get(position).getImage()).centerCrop().into(((Item)holder).ivProduct);
        }else if (holder instanceof ItemAdd){
            ((ItemAdd) holder).itemView.setOnClickListener(v -> mSelectedListener.onAddClicked());
        }
    }

    @Override
    public int getItemCount() {
        return products.size() + 1 ;
    }

    @Override
    public int getItemViewType(int position) {
        if (position < products.size())
            return 1;
        else
            return 2;
    }

    public interface SelectedListener{
        void onAddClicked();
        void onItemClicked(Integer productId);
        void onRemoveProductClicked(Integer id);
    }

    class Item extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_product)
        RoundedImageView ivProduct;
        @BindView(R.id.ic_remove)
        ImageView ivRemove;

        public Item(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemAdd extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_add)
        ImageView ivAdd;

        public ItemAdd(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
