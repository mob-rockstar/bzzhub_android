package smartdev.bzzhub.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_news_feed.view.*
import okhttp3.internal.notify
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.CommunityFeedResponse

class CommunityFeedAdapter(private val mContext: Context, private var items: ArrayList<CommunityFeedResponse.Result>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: SelectedListener? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_news_feed, parent, false))

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(mContext).load(items[position].feedImage).centerCrop().into(holder.itemView.iv_image)
        Glide.with(mContext).load(items[position].image).centerCrop().into(holder.itemView.iv_member)
        holder.itemView.tv_title.text = items[position].title
        holder.itemView.setOnClickListener {
            listener?.onSelected(items[position].newFeedsId)
        }
    }

    interface SelectedListener {
        fun onSelected(communityID: Int)
    }

    fun addFeeds( mitems: List<CommunityFeedResponse.Result>){
        items.addAll(mitems)
        notifyDataSetChanged()
    }

    fun setItems(mitems: ArrayList<CommunityFeedResponse.Result>){
        items = mitems
        notifyDataSetChanged()
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}