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
import smartdev.bzzhub.repository.model.RFQResponse;

public class CompanyRFQAdapter  extends RecyclerView.Adapter<CompanyRFQAdapter.Item> {

    private Context mContext;
    private List<RFQResponse.Result> rfqs = new ArrayList<>();
    private SelectedListener mSelectedListener;

    public CompanyRFQAdapter(Context mContext, List<RFQResponse.Result> rfqs, SelectedListener mSelectedListener) {
        this.mContext = mContext;
        this.rfqs = rfqs;
        this.mSelectedListener = mSelectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rfq_company, parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.tvEMail.setText(rfqs.get(position).getEmail());
        holder.tvName.setText(rfqs.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSelectedListener.onItemClicked(rfqs.get(position).getRfqId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return rfqs.size();
    }

    public interface SelectedListener {
        void onItemClicked(Integer id);
    }

    public class Item extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_name)
        TextView  tvName;
        @BindView(R.id.tv_email)
        TextView tvEMail;

        public Item(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
