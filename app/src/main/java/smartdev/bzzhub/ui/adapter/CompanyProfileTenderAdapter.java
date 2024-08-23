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
import smartdev.bzzhub.repository.model.BookResponse;

public class CompanyProfileTenderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context mContext;
    private List<BookResponse.PDFResult> mPDFList;
    private SelectedListener mSelectedListener;

    public CompanyProfileTenderAdapter(Context mContext, List<BookResponse.PDFResult> mPDFList, SelectedListener mSelectedListener) {
        this.mContext = mContext;
        this.mPDFList = mPDFList;
        this.mSelectedListener = mSelectedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_company_profile, parent, false));
        }else {
            return new ItemAdd(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_feed, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof Item){
            Item holder = (Item) viewHolder;
            holder.tvName.setText(mPDFList.get(position).getTitle());
            holder.ivRemove.setOnClickListener(v -> {
                mSelectedListener.onRemoved(mPDFList.get(position).getTenderId());
            });
            holder.itemView.setOnClickListener(v -> mSelectedListener.onSelected(mPDFList.get(position).getFilePath()));
        }else {
            viewHolder.itemView.setOnClickListener(v -> {
                    mSelectedListener.onAddClicked();
            });
        }
    }

    @Override
    public int getItemCount() {
        return mPDFList.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == mPDFList.size()){
            return 2;
        }else {
            return 1;
        }
    }

    public interface SelectedListener {
        void onSelected(String path);
        void onRemoved(Integer tenderId);
        void onAddClicked();
    }

    public class Item extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_remove)
        ImageView ivRemove;

        public Item(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ItemAdd extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_add)
        ImageView iv_add;

        public ItemAdd(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
