package smartdev.bzzhub.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.dialog_add_group.view.*
import kotlinx.android.synthetic.main.item_activity.view.*
import kotlinx.android.synthetic.main.item_activity.view.tv_title
import smartdev.bzzhub.R
import smartdev.bzzhub.repository.model.CommunityEventResponse

class CommunityEventAdapter  (private val mContext: Context, private var items: ArrayList<CommunityEventResponse.Result>) :
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

        holder.itemView.tv_title.text = items[position].title
        if (items[position].country != null && items[position].city != null){
            holder.itemView.tv_address.text = mContext.resources.getString(R.string.str_address_value,items[position].country,
                    items[position].city)
        }else{
            holder.itemView.tv_address.visibility = GONE
        }

        holder.itemView.setOnClickListener {
            listener?.onSelected(items[position])
        }
    }


    interface SelectedListener {
        fun onSelected(event: CommunityEventResponse.Result)
    }

    fun addItems(mitems: List<CommunityEventResponse.Result>) {
        items.addAll(mitems)
        notifyDataSetChanged()
    }


    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}