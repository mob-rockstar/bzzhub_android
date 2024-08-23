package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.ApplicantResponse;

public class ApplicantsAdapter  extends RecyclerView.Adapter<ApplicantsAdapter.Item>   {

    private Context mContext;
    private List<ApplicantResponse.Result> jobList = new ArrayList<>();

    public ApplicantsAdapter(Context mContext, List<ApplicantResponse.Result> jobList) {
        this.mContext = mContext;
        this.jobList = jobList;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_applicants, parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.tvTitle.setText(jobList.get(position).getTitle());
        holder.tvDescription.setText(jobList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    class Item extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_description)
        TextView tvDescription;

        public Item(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
