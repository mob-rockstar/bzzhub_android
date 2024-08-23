package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import smartdev.bzzhub.repository.model.UserProfileResponse;

public class ChooseSectorAdapterUser  extends RecyclerView.Adapter<ChooseSectorAdapterUser.Item>  {

    private Context mContext;
    private List<InterestResponse.Result> interests = new ArrayList<>();
    private List<InterestResponse.Result> allInterest = new ArrayList<>();

    public ChooseSectorAdapterUser(Context mContext, List<InterestResponse.Result> interests) {
        this.mContext = mContext;
        this.allInterest =  this.interests  = interests ;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_choose_sector, parent, false)) ;
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {

        holder.tvContent.setText(interests.get(position).getInterest());
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
                    notifyDataSetChanged();
                }catch (Exception e){

                }
        });
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
        for (InterestResponse.Result interest : interests){
            if (interest.isSelected())
                selectedIds.add(interest.getInterestId());
        }
        return selectedIds;

    }

    public void filterInterest(String keyword){
        interests = new ArrayList<>();
        if (!allInterest.isEmpty()){
            for (int i = 0;i<allInterest.size();i++){
                if (allInterest.get(i).getInterest().toLowerCase().contains(keyword.toLowerCase())){
                    interests.add(allInterest.get(i));
                }
            }
        }

        notifyDataSetChanged();
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
