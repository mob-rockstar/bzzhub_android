package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.ConnectionResponse;
import smartdev.bzzhub.repository.model.UserJobResponse;

public class ConnectionAdapter extends RecyclerView.Adapter<ConnectionAdapter.Item> {

    private Context mContext;
    private List<ConnectionResponse.Result> connections = new ArrayList<>();
    private SelectedListener mSelectedListener;
    private int selected = -1;

    public ConnectionAdapter(Context mContext, List<ConnectionResponse.Result> connections, SelectedListener mSelectedListener) {
        this.mContext = mContext;
        this.connections = connections;
        this.mSelectedListener = mSelectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_connections, parent, false)) ;

    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        if (selected == position)
            holder.layoutRoot.setBackgroundColor(mContext.getResources().getColor(R.color.color_light_gray));
        else
            holder.layoutRoot.setBackgroundColor(mContext.getResources().getColor(R.color.color_white));
        Glide.with(mContext).load(connections.get(position).getImage()).fitCenter().into(holder.ivProfile);
        holder.tvName.setText(connections.get(position).getName());
        holder.itemView.setOnClickListener(v -> {
            selected = position;
            mSelectedListener.onConnectionClicked(position,connections.get(position));
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return connections.size();
    }

    public interface SelectedListener {
        void onConnectionClicked(int position, ConnectionResponse.Result connection);
    }

    class Item extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_profile)
        CircleImageView ivProfile;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.layout_root)
        ConstraintLayout layoutRoot;

        public Item(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
