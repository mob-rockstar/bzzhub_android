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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.UserProfileVisitorResponse;

public class VisitorAdapter extends RecyclerView.Adapter<VisitorAdapter.Item> {

    Context mContext;
    List<UserProfileVisitorResponse.Result> visitors = new ArrayList<>();

    public VisitorAdapter(Context mContext, List<UserProfileVisitorResponse.Result> visitors) {
        this.mContext = mContext;
        this.visitors = visitors;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_visitors, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        Glide.with(mContext).load(visitors.get(position).getImage()).fitCenter().into(holder.ivProfile);
        holder.tvName.setText(visitors.get(position).getName());
        holder.tvTime.setText(visitors.get(position).getDate());
        holder.tvAccountType.setText(visitors.get(position).getWho());
    }

    @Override
    public int getItemCount() {
        return visitors.size();
    }

    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_profile)
        ImageView ivProfile;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_account_type)
        TextView tvAccountType;
        @BindView(R.id.tv_time)
        TextView tvTime;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
