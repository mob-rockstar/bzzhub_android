package smartdev.bzzhub.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_feed_comment.view.*
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.FeedCommentResponse

class NewsCommentAdapter(private val mContext: Context, private var items: ArrayList<FeedCommentResponse.Result>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_feed_comment, parent, false))

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.tv_comment.text = items[position].comment
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}