package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.SubCategoryResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SubcategoryAdapter  extends RecyclerView.Adapter<SubcategoryAdapter.Item> {

    private Context mContext;
    private List<SubCategoryResponse.Result> categories;
    private SelectedListener mSelectedListener;

    public SubcategoryAdapter(Context mContext, List<SubCategoryResponse.Result> categories, SelectedListener mSelectedListener) {
        this.mContext = mContext;
        this.categories = categories;
        this.mSelectedListener = mSelectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dialog_business_category, parent, false)) ;

    }

    @Override
    public void onBindViewHolder(@NonNull Item viewHolder, int position) {
        viewHolder.tvOutlet.setText(categories.get(position).getName());
        viewHolder.itemView.setOnClickListener(view -> mSelectedListener.onSubcategorySelected(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public interface SelectedListener {
        void onSubcategorySelected(int position);
    }

    class Item extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_outlet)
        TextView tvOutlet;

        public Item(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
