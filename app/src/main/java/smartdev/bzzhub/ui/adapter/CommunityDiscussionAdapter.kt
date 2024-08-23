package smartdev.bzzhub.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_discussion.view.*
import kotlinx.android.synthetic.main.item_news_feed.view.iv_member
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.DiscussionListResponse

class CommunityDiscussionAdapter (private val mContext: Context, private var items: ArrayList<DiscussionListResponse.Result>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: SelectedListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_discussion, parent, false))

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(mContext).load(items[position].image).centerCrop().into(holder.itemView.iv_member)
        holder.itemView.tv_name.text = items[position].name
        holder.itemView.tv_group_name.text = items[position].community
        holder.itemView.tv_subject.text = items[position].title
        holder.itemView.tv_description.text = items[position].description
        holder.itemView.setOnClickListener {
            listener?.onSelected(items[position].discussionId)
        }

        if (items[position].flag == 0){
            holder.itemView.cv_background.setCardBackgroundColor(mContext.resources.getColor(R.color.color_active_community))
        }else{
            holder.itemView.cv_background.setCardBackgroundColor(mContext.resources.getColor(R.color.white))
        }
    }

    interface SelectedListener {
        fun onSelected(communityID: Int)
    }

    fun addItems(mitems: List<DiscussionListResponse.Result>) {
        items.addAll(mitems)
        notifyDataSetChanged()
    }

    fun setItems(mitems: ArrayList<DiscussionListResponse.Result>){
        items = mitems
        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}