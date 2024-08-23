package smartdev.bzzhub.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_news_feed.view.*
import kotlinx.android.synthetic.main.item_thread_comment.view.*
import kotlinx.android.synthetic.main.item_thread_comment.view.iv_member
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.DiscussionCommentResponse
import smartdev.bzzhub.repository.model.DiscussionListResponse

class DiscussionCommentAdapter(private val mContext: Context, private var items: ArrayList<DiscussionCommentResponse.Result>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_thread_comment, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tv_member.text = items[position].name
        holder.itemView.tv_comment.text = items[position].post
        Glide.with(mContext).load(items[position].image).centerCrop().into(holder.itemView.iv_member)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}