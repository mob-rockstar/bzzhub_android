package smartdev.bzzhub.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.android.synthetic.main.item_activity.view.iv_banner
import kotlinx.android.synthetic.main.item_activity.view.tv_title
import kotlinx.android.synthetic.main.item_event_main.view.*
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.CommunityEventListResponse

class CommunityMainEventAdapter (private val mContext: Context, private var items: ArrayList<CommunityEventListResponse.Result>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var listener: SelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_event_main, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(mContext).load(items[position].image).centerCrop().into( holder.itemView.iv_banner)
        Glide.with(mContext).load(items[position].profileImage).centerCrop().into( holder.itemView.iv_member)
        holder.itemView.tv_title.text = items[position].community
        holder.itemView.tv_name.text = items[position].title
        holder.itemView.tv_date.text = items[position].eventDate

        holder.itemView.setOnClickListener { listener?.onSelected(items[position]) }
    }


    interface SelectedListener {
        fun onSelected(community: CommunityEventListResponse.Result)
    }

    fun addItems(mitems: List<CommunityEventListResponse.Result>) {
        items.addAll(mitems)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}