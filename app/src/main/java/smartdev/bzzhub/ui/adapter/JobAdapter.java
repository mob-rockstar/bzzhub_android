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
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.JobResponse;

public class JobAdapter extends RecyclerView.Adapter<JobAdapter.Item> {

    private Context mContext;
    private List<JobResponse.Result> jobList;
    private SelectedListener mSelectedListener;

    public JobAdapter(Context mContext, List<JobResponse.Result> jobList,SelectedListener selectedListener) {
        this.mContext = mContext;
        this.jobList = jobList;
        this.mSelectedListener = selectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(mContext).inflate(R.layout.item_job, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.ivImage.setVisibility(View.GONE);
        if (jobList.get(position).getUserId() != null){
            Glide.with(mContext).load(jobList.get(position).getImage()).centerCrop().into(holder.ivImage);
        }
        Glide.with(mContext).load(jobList.get(position).getBanner()).centerCrop().into(holder.ivJob);
        holder.tvJob.setText(jobList.get(position).getTitle());
        if (jobList.get(position).getCountry() == null || jobList.get(position).getCity() == null){
            holder.tvAddress.setVisibility(View.GONE);
        }else {
            holder.tvAddress.setVisibility(View.VISIBLE);
            holder.tvAddress.setText(mContext.getResources().getString(R.string.str_address_value,jobList.get(position).getCountry(),
                    jobList.get(position).getCity()));
        }
        holder.itemView.setOnClickListener(v -> {
            mSelectedListener.onSelected(jobList.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    public void addJobs(List<JobResponse.Result> mJobs){
        jobList.addAll(mJobs);
        notifyDataSetChanged();
    }

    public void setJobs(List<JobResponse.Result> mJobs){
        jobList = mJobs;
        notifyDataSetChanged();
    }

    public interface SelectedListener{
        void onSelected(JobResponse.Result job);
    }

    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_job)
        RoundedImageView ivJob;
        @BindView(R.id.tv_job)
        TextView tvJob;
        @BindView(R.id.tv_address)
        TextView tvAddress;
        @BindView(R.id.iv_image)
        ImageView ivImage;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
