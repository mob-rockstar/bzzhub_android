package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;

public class CategoryAdapterHome  extends RecyclerView.Adapter<CategoryAdapterHome.Item> {

    private Context mContext;
    private List<CompanyDetailResponse.Interst> categories;

    public CategoryAdapterHome(Context mContext, List<CompanyDetailResponse.Interst> categories) {
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
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class Item extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_content)
        TextView tvContent;

        public Item(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
