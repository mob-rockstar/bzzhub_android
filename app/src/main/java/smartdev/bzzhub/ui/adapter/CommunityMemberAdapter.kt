package smartdev.bzzhub.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_community_members.view.*
import okhttp3.internal.notify
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.CommunityEventListResponse
import smartdev.bzzhub.repository.model.CommunityMemberResponse

class CommunityMemberAdapter (private val mContext: Context, private var items: ArrayList<CommunityMemberResponse.Result>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    var listener: SelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_community_members, parent, false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Glide.with(mContext).load(items[position].image).centerCrop().into( holder.itemView.iv_member)

        holder.itemView.tv_name.text = items[position].name
        holder.itemView.tv_business_type.text = """Interest: ${items[position].interest}"""
        if (items[position].city != null && items[position].country != null){
            holder.itemView.tv_address.text = mContext.resources.getString(R.string.str_address_value,items[position].city ,items[position].country)
        }

        holder.itemView.setOnClickListener {
            listener?.onMemberSelected(items[position])
        }
    }

    fun addItems(mItems: ArrayList<CommunityMemberResponse.Result>){
        items.addAll(mItems)
        notifyDataSetChanged()
    }

    fun setItems(mItems: ArrayList<CommunityMemberResponse.Result>){
        items = mItems
        notifyDataSetChanged()
    }

    interface SelectedListener {
        fun onMemberSelected(member: CommunityMemberResponse.Result)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

}