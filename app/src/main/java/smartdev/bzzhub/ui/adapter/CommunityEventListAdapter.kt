package smartdev.bzzhub.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_activity.view.*
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.CommunityEventListResponse

class CommunityEventListAdapter (private val mContext: Context, private var items: ArrayList<CommunityEventListResponse.Result>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var listener: SelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_activity, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(mContext).load(items[position].image).centerCrop().into( holder.itemView.iv_banner)

        holder.itemView.tv_title.text = items[position].name
    }


    interface SelectedListener {
        fun onSelected(communityID: Int)
    }

    fun addItems(mitems: List<CommunityEventListResponse.Result>) {
        items.addAll(mitems)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}