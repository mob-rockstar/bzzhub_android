package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.UserProfileResponse;
import smartdev.bzzhub.ui.activity.UserProfileActivity;

public class UserProfileInterestAdapter  extends  RecyclerView.Adapter<UserProfileInterestAdapter.Item>  {

    private Context mContext;
    private List<UserProfileResponse.Interst> interests;
    private boolean mVisible;
    private SelectedListener mSelectedListener;

    public UserProfileInterestAdapter(Context mContext, List<UserProfileResponse.Interst> interests,boolean visible
    ,SelectedListener selectedListener) {
        this.mContext = mContext;
        this.interests = interests;
        this.mVisible = visible;
        this.mSelectedListener = selectedListener;
    }

    public void updateVisiblity(boolean Visible){
        mVisible = Visible;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interests, parent, false));
    }

    public void removeItem(int position){
        interests.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        if (mVisible){
            holder.ivRemove.setVisibility(View.VISIBLE);
        }else {
            holder.ivRemove.setVisibility(View.GONE);
        }

        if (mContext instanceof UserProfileActivity){
            if( !((UserProfileActivity)mContext).isOtherUser) {
                holder.tvInterest.setOnLongClickListener(v -> {
                    mVisible = !mVisible;
                    notifyDataSetChanged();
                    return false;
                });
                holder.ivRemove.setOnClickListener(v -> {
                    this.mSelectedListener.onRemoveClicked(position,interests.get(position).getId());
                });
            }
        }

        holder.tvInterest.setText(interests.get(position).getInterest());
    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    public interface SelectedListener{
        void onRemoveClicked(int position, Integer id);

    }

    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_interest)
        TextView tvInterest;
        @BindView(R.id.iv_remove)
        ImageView ivRemove;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
