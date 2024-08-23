package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.CompanyDetailResponse;
import smartdev.bzzhub.repository.model.InterestResponse;
import smartdev.bzzhub.repository.model.SelectorResponse;

public class ChooseSectorAdapter  extends RecyclerView.Adapter<ChooseSectorAdapter.Item>  {

    private Context mContext;
    private List<SelectorResponse.Result> allInterest = new ArrayList<>();
    private List<SelectorResponse.Result> interests = new ArrayList<>();

    public ChooseSectorAdapter(Context mContext, List<SelectorResponse.Result> interests) {
        this.mContext = mContext;
       this.allInterest =  this.interests  = new ArrayList<>(interests) ;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_sector, parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        holder.tvContent.setText(interests.get(position).getName());
        if ((interests.get(position).isSelected())) {
            holder.tvContent.setBackground(mContext.getResources().getDrawable(R.drawable.item_choose_sector_active));
            holder.tvContent.setTextColor(mContext.getResources().getColor(R.color.color_sector_active));
        }else {
            holder.tvContent.setBackground(mContext.getResources().getDrawable(R.drawable.item_choose_sector_deactive));
            holder.tvContent.setTextColor(mContext.getResources().getColor(R.color.color_sector_deactive));
        }

        holder.itemView.setOnClickListener(v -> {
                try {
                    if (interests.get(position).isSelected()){
                        interests.get(position).setSelected(false);
                    }else {
                        interests.get(position).setSelected(true);
                    }
                    notifyItemChanged(position);
                }catch (Exception e){

                }
        });
    }

    public void filterInterest(String keyword){
        interests = new ArrayList<>();
        if (!allInterest.isEmpty()){
            for (int i = 0;i<allInterest.size();i++){
                if (allInterest.get(i).getName().toLowerCase().contains(keyword.toLowerCase())){
                    interests.add(allInterest.get(i));
                }
            }
        }

        notifyDataSetChanged();
    }

    public String getIdList(){
        StringBuilder strIds = new StringBuilder();
        if (!selectedIdList().isEmpty()){
            for (int i =0;i<selectedIdList().size();i++){
                if (i <selectedIdList().size()-1){
                    strIds.append(selectedIdList().get(i)).append(",");
                }else {
                    strIds.append(selectedIdList().get(i));
                }
            }
        }
        return strIds.toString();
    }

    private List<Integer> selectedIdList() {
        List<Integer> selectedIds = new ArrayList<>();
        for (SelectorResponse.Result interest : interests){
            if (interest.isSelected())
                selectedIds.add(interest.getSubId());
        }
        return selectedIds;

    }

    @Override
    public int getItemCount() {
        return interests.size();
    }

    public class Item extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_sector)
        TextView tvContent;

        public Item(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
