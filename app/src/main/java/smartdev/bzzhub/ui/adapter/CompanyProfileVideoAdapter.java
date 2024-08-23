package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyMediaResponse;
import smartdev.bzzhub.util.Validator;

public class CompanyProfileVideoAdapter extends RecyclerView.Adapter<CompanyProfileVideoAdapter.Item>  {

    private Context mContext;
    private List<CompanyMediaResponse.Video> mVideoList;
    private SelectedListener mSelectedListener;

    public CompanyProfileVideoAdapter(Context mContext, List<CompanyMediaResponse.Video> mVideoList, SelectedListener selectedListener) {
        this.mContext = mContext;
        this.mVideoList = mVideoList;
        this.mSelectedListener = selectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_media, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        Glide.with(mContext).load((mVideoList.get(position).getThumbnail())).centerCrop().into(holder.ivMedia);
        holder.layoutRemove.setOnClickListener(v -> mSelectedListener.onRemoveClicked(mVideoList.get(position).getMediaId()));
        holder.layoutRemove.setVisibility(View.VISIBLE);
        holder.itemView.setOnClickListener(v -> mSelectedListener.onVideoSelected(mVideoList.get(position).getPath()));

    }

    public void addItems(List<CompanyMediaResponse.Video> videoList){
        videoList.addAll(videoList);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public interface SelectedListener {
        void onVideoSelected(String URL);
        void onRemoveClicked(Integer mediaId);
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
