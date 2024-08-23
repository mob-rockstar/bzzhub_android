package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyMediaResponse;

public class CompanyProfileImageAdapter  extends RecyclerView.Adapter<CompanyProfileImageAdapter.Item>  {
    private Context mContext;
    private List<CompanyMediaResponse.Video> mImageList;
    private SelectedListener mSelectedListener;

    public CompanyProfileImageAdapter(Context mContext, List<CompanyMediaResponse.Video> mImageList, SelectedListener mSelectedListener) {
        this.mContext = mContext;
        this.mImageList = mImageList;
        this.mSelectedListener = mSelectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.layoutRemove.setVisibility(View.VISIBLE);
        holder.layoutRemove.setOnClickListener(v -> {
            mSelectedListener.onRemoveClicked(mImageList.get(position).getMediaId());
        });

        holder.itemView.setOnClickListener(v -> {
            mSelectedListener.onSelected(mImageList.get(position).getPath());
        });
        Glide.with(mContext).load(mImageList.get(position).getPath()).centerCrop().into(holder.ivMedia);
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public interface SelectedListener{
        void onRemoveClicked(Integer mediaId);
        void onSelected(String path);
    }

    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_media)
        RoundedImageView ivMedia;
        @BindView(R.id.layout_remove)
        RelativeLayout layoutRemove;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
