package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.UserProfileResponse;

public class ContactSuggestionAdapter extends RecyclerView.Adapter<ContactSuggestionAdapter.Item>  {

    private Context mContext;
    private List<UserProfileResponse.Friend> requests  = new ArrayList<>();
    private SelectedListener mSelectedListener;

    public ContactSuggestionAdapter(Context mContext, List<UserProfileResponse.Friend> requests, SelectedListener selectedListener) {
        this.mContext = mContext;
        this.requests = requests;
        this.mSelectedListener = selectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_contact_suggestion, parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        Glide.with(mContext).load(requests.get(position).getImage()).apply(new RequestOptions().placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_placeholder)).fitCenter().into(holder.ivProfile);
        holder.tvName.setText(requests.get(position).getFullName());
        holder.layoutAdd.setOnClickListener(v -> mSelectedListener.onSendClicked(requests.get(position).getUserId()));
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }

    public interface SelectedListener{
        void onSendClicked(Integer toUser);
    }

    class Item extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_profile)
        CircleImageView ivProfile;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.layout_add)
        RelativeLayout layoutAdd;

        public Item(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
