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
import smartdev.bzzhub.repository.model.Size;
import smartdev.bzzhub.util.Constant;

public class SelectedSizeAdapter  extends RecyclerView.Adapter<SelectedSizeAdapter.Item>  {

    private Context mContext;
    private List<Size> sizeList = new ArrayList<>();

    public SelectedSizeAdapter(Context mContext, List<Size> sizeList) {
        this.mContext = mContext;
        this.sizeList = sizeList;
    }

    public void setData(List<Size> mSizeList){
        sizeList = mSizeList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {

        if(Constant.getLanguage(mContext).equals("en")){
            holder.tvName.setText(
                    sizeList.get(position).getName());
        }else{
            holder.tvName.setText(
                    sizeList.get(position).getArabicName());
        }
    }

    public List<Size> getProductList(){
        return sizeList;
    }

    public String getSelectedSize(){
        StringBuilder strLanguage = new StringBuilder();
        if (!sizeList.isEmpty()){
            for (int i = 0;i<sizeList.size();i++){
                strLanguage.append(sizeList.get(i).getId()).append(i<sizeList.size()-1 ? "," : "");
            }
        }

        return strLanguage.toString();
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_name)
        TextView tvName;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}