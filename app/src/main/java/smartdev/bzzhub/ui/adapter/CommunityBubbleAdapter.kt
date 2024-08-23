package smartdev.bzzhub.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_bubble_community.view.*
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.ColorResponse

class CommunityBubbleAdapter(private val mContext: Context, private val titleList: List<String>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    public var selected = 4
    var listener: SelectedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_bubble_community, parent, false))
    }

    override fun getItemCount(): Int {
       return  this.titleList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position < 4){
            holder.itemView.tv_content.visibility = VISIBLE
            holder.itemView.tv_content.text = titleList[position]
            holder.itemView.iv_community.visibility = GONE
        }else{
            holder.itemView.tv_content.visibility = GONE
            holder.itemView.iv_community.visibility = VISIBLE
        }

        if (position == selected){
            holder.itemView.layout_background.backgroundTintList = mContext.resources.getColorStateList(R.color.color_active_community)
        }else{
            holder.itemView.layout_background.backgroundTintList = null
        }

        holder.itemView.setOnClickListener {
        //    selected = position
            listener?.onBubbleSelected(position)
            notifyDataSetChanged()
        }
    }

    interface SelectedListener {
        fun onBubbleSelected(position: Int)
    }

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}