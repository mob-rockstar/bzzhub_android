package smartdev.bzzhub.ui.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_community.view.*
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.ColorResponse
import smartdev.bzzhub.repository.model.CommunityListResponse

class CommunityAdapter(private val mContext: Context, private var items: List<CommunityListResponse.Result>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var listener: SelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_community, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun addCommunities(additionalItems: List<CommunityListResponse.Result>){
        items += additionalItems
        notifyDataSetChanged()
    }

    fun setItems(mItems: List<CommunityListResponse.Result>){
        items = mItems
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(mContext).load(items[position].image).centerCrop().into(holder.itemView.iv_community)
        holder.itemView.tv_name.text = items[position].title
        holder.itemView.tv_member_count.text = items[position].members!!.toString()

        holder.itemView.tv_member_count.setOnClickListener {
            listener?.onCountSelected(items[position].communityId)
        }

        holder.itemView.view_member.setOnClickListener {
            listener?.onCountSelected(items[position].communityId)
        }
        holder.itemView.iv_member.setOnClickListener {
            listener?.onCountSelected(items[position].communityId)
        }

        holder.itemView.setOnClickListener{
            listener?.onSelected(items[position].communityId)
        }
    }


    interface SelectedListener {
        fun onCountSelected(communityID: Int)
        fun onSelected(communityID: Int)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}