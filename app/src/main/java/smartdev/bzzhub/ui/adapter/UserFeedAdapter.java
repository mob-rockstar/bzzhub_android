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
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindDimen;
import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.UserProfileFeedResponse;
import smartdev.bzzhub.ui.activity.UserProfileActivity;

public class UserFeedAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context mContext;
    private List<UserProfileFeedResponse.Result> feeds;
    private SelectedListener mSelectedListener;

    private final int ITEM_TYPE_NORMAL = 1;
    private final int ITEM_TYPE_ADD = 2;

    public UserFeedAdapter(Context context,List<UserProfileFeedResponse.Result> feeds,SelectedListener selectedListener ) {
        this.mContext = context;
        this.feeds = feeds;
        this.mSelectedListener = selectedListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_NORMAL)
            return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_feeds, parent, false));
        else
            return new ItemAdd(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_feed, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemAdd){
            if (mContext instanceof UserProfileActivity) {
                if (((UserProfileActivity) mContext).isOtherUser) {
                    holder.itemView.setVisibility(View.GONE);
                }
            }
            holder.itemView.setOnClickListener(v -> mSelectedListener.onAddSelected());
        }

        else {
            Item item = (Item) holder;
            Glide.with(mContext).load(feeds.get(position).getImage()).centerCrop().into(item.ivFeedImage);
            item.tvName.setText(feeds.get(position).getDescription());
            if (mContext instanceof UserProfileActivity) {
                if (((UserProfileActivity) mContext).isOtherUser) {
                    item.ivRemove.setVisibility(View.GONE);
                }
            }
            item.ivRemove.setOnClickListener(v -> {
                mSelectedListener.onRemoveSelected(feeds.get(position).getFeedId());
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == getItemCount() -1)
            return ITEM_TYPE_ADD;
        else
            return ITEM_TYPE_NORMAL;
    }

    @Override
    public int getItemCount() {
        return feeds != null ? feeds.size()+1 : 1;
    }

    public interface SelectedListener {
        void onAddSelected();
        void onRemoveSelected(Integer feedId);
    }

    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_image)
        RoundedImageView ivFeedImage;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_remove)
        ImageView ivRemove;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class ItemAdd  extends RecyclerView.ViewHolder{

        @BindView(R.id.iv_add)
        ImageView ivAdd;

        public ItemAdd(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
