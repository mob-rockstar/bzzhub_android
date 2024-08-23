package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;

public class CategoryAdapterCompanyProfile  extends RecyclerView.Adapter<CategoryAdapterCompanyProfile.Item> {

    private Context mContext;
    private List<CompanyDetailResponse.Interst> categories;
    private boolean isVisible = false;
    private SelectedListener mSelectedListener;

    public CategoryAdapterCompanyProfile(Context mContext, List<CompanyDetailResponse.Interst> categories,
                                         SelectedListener selectedListener) {
        this.mSelectedListener = selectedListener;
        this.mContext = mContext;
        this.categories = categories;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Item viewHolder, int position) {
        viewHolder.tvContent.setText(categories.get(position).getName());
        viewHolder.tvContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isVisible){
                    viewHolder.ivRemove.setVisibility(View.VISIBLE);
                    isVisible = true;
                }else {
                    isVisible = false;
                    viewHolder.ivRemove.setVisibility(View.GONE);
                }
            }
        });
        viewHolder.ivRemove.setOnClickListener(v -> {
            mSelectedListener.onRemoveClicked(categories.get(position).getId());
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface SelectedListener{
        void onRemoveClicked(Integer id);
    }

    class Item extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.iv_remove)
        ImageView ivRemove;

        public Item(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
