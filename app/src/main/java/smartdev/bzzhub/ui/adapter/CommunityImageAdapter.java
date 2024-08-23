package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CommunityImageResponse;
import smartdev.bzzhub.repository.model.CompanyMediaResponse;

public class CommunityImageAdapter  extends RecyclerView.Adapter<CommunityImageAdapter.Item>{

    private Context mContext;
    private List<CommunityImageResponse.Result> mImageList;
    private SelectedListener mSelectedListener;

    public CommunityImageAdapter(Context mContext, List<CommunityImageResponse.Result> mImageList, SelectedListener selectedListener) {
        this.mContext = mContext;
        this.mImageList = mImageList;
        this.mSelectedListener = selectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(mContext).inflate(R.layout.item_image, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        Glide.with(mContext).load(mImageList.get(position).getPath()).centerCrop().into(holder.ivMedia);
        holder.itemView.setOnClickListener(v -> {
            mSelectedListener.onImageSelected(mImageList.get(position).getPath());
        });
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    public interface SelectedListener {
        void onImageSelected(String imageURL);
    }

    public void addItems(List<CommunityImageResponse.Result> videoList){
        mImageList.addAll(videoList);
        notifyDataSetChanged();
    }


    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_media)
        RoundedImageView ivMedia;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
