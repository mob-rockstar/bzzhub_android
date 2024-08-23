package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.os.Message;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.chatmodel.Messages;
import smartdev.bzzhub.repository.chatmodel.Users;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.FormatterUtils;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Messages> messages = new ArrayList<>();
    private ArrayList<Users> users;
    private Context mContext;
    private int current_uid = 0;
    private int currentUserFlag = -1;
    public ChatAdapter(ArrayList<Messages> oneToOneMessages, Context mContext,ArrayList<Users> members) {
        messages = oneToOneMessages;
        this.mContext = mContext;
        this.users = members;

        current_uid = Constant.getInstance().getLoginID();
        currentUserFlag = Constant.getInstance().getLoginFlag();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1){
            return new SentMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_from_current_user, parent, false));
        }else {
            return new ReceivedMessageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_message_from_other_user, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case 1:
                SentMessageViewHolder sentMessageViewHolder = (SentMessageViewHolder) holder;
                sentMessageViewHolder.message.setText(messages.get(position).getMessage_content());
                sentMessageViewHolder.tvTime.setText(FormatterUtils.getListTime(messages.get(position).getMessage_time()));
                break;
            case 2:
                ReceivedMessageViewHolder receivedMessageViewHolder = (ReceivedMessageViewHolder) holder;
                receivedMessageViewHolder.message.setText(messages.get(position).getMessage_content());
                receivedMessageViewHolder.tvName.setText(FormatterUtils.getUserNameFromList(messages.get(position).getSenderID(),
                        messages.get(position).getFlag(),users));

                Glide.with(mContext).load(FormatterUtils.getUserImageFromList(messages.get(position).getSenderID(),
                        messages.get(position).getFlag(),users)).centerCrop().apply(new RequestOptions().placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_placeholder)).into(receivedMessageViewHolder.ivProfile);
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).getFlag() == currentUserFlag && messages.get(position).getSenderID() == current_uid){
            return 1;
        }else {
            return 2;
        }
    }

    public void setUserList(ArrayList<Users> mUsers){
        users = mUsers;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_profile)
        ImageView ivProfile;
        @BindView(R.id.tv_message)
        TextView message;
        @BindView(R.id.view)
        View view;

        ReceivedMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    class SentMessageViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_message)
        TextView message;
        @BindView(R.id.view)
        View view;
        @BindView(R.id.tv_time)
                TextView tvTime;


        SentMessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
