package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.UserProfileResponse;

public class GroupsAdapter  extends RecyclerView.Adapter<GroupsAdapter.Item>   {

    private Context mContext;
    private List<UserProfileResponse.Group> groups;

    public GroupsAdapter(Context mContext, List<UserProfileResponse.Group> groups) {
        this.mContext = mContext;
        this.groups = groups;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_group, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.tvGroup.setText(groups.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_group)
        TextView tvGroup;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
