package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.UserProfileResponse;
import smartdev.bzzhub.ui.activity.UserProfileActivity;

public class UserProfileFriendAdapter extends  RecyclerView.Adapter<UserProfileFriendAdapter.Item>  {

    private Context mContext;
    private List<UserProfileResponse.Friend> friends;

    public UserProfileFriendAdapter(Context mContext, List<UserProfileResponse.Friend> friends) {
        this.mContext = mContext;
        this.friends = friends;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        Glide.with(mContext).setDefaultRequestOptions(new RequestOptions().error(R.drawable.ic_placeholder)).load(friends.get(position).getImage()).fitCenter().into(holder.ivFriend);

        holder.tvName.setText(friends.get(position).getFullName());

 /*       holder.ivFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, UserProfileActivity.class);
                intent.putExtra("User_id", friends.get(position).getUserId());
                mContext.startActivity(intent);
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_profile)
        ImageView ivFriend;

        @BindView(R.id.tv_name)
        TextView tvName;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
