package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.UserJobResponse;
import smartdev.bzzhub.ui.activity.UserProfileActivity;

public class UserJobAdapter extends RecyclerView.Adapter<UserJobAdapter.Item>  {

    private Context mContext;
    private List<UserJobResponse.Result> jobs = new ArrayList<>();
    private SelectedListener mSelectedListener;

    public UserJobAdapter(Context mContext, List<UserJobResponse.Result> jobs, SelectedListener selectedListener) {
        this.mContext = mContext;
        this.jobs = jobs;
        this.mSelectedListener = selectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_jobs, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.tvJob.setText(jobs.get(position).getTitle());
        holder.ivEdit.setOnClickListener(v -> {
            mSelectedListener.onEditClicked(jobs.get(position));
        });

        if (mContext instanceof UserProfileActivity) {
            if (((UserProfileActivity) mContext).isOtherUser) {
                holder.ivEdit.setVisibility(View.GONE);
                holder.ivRemove.setVisibility(View.GONE);
            }
        }

        holder.ivRemove.setOnClickListener(v -> {
            mSelectedListener.onRemoveClicked(jobs.get(position).getJobId());
        });
    }

    @Override
    public int getItemCount() {
        return jobs.size();
    }

    public interface SelectedListener {
        void onRemoveClicked(Integer jobId);
        void onEditClicked(UserJobResponse.Result job);
    }

    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_job)
        TextView tvJob;
        @BindView(R.id.iv_edit)
        ImageView ivEdit;
        @BindView(R.id.iv_remove)
        ImageView ivRemove;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
