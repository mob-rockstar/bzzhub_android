package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.tbuonomo.viewpagerdotsindicator.SpringDotsIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.ColorResponse;
import smartdev.bzzhub.repository.model.Size;
import smartdev.bzzhub.util.Constant;

public class SizeAdapter extends RecyclerView.Adapter<SizeAdapter.Item>  {

    private Context mContext;
    private List<Size> sizeList = new ArrayList<>();
    private SelectedListener mSelectedListener;

    public SizeAdapter(Context mContext, List<Size> sizeList, SelectedListener mSelectedListener) {
        this.mContext = mContext;
        this.sizeList = sizeList;
        this.mSelectedListener = mSelectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {

        if (sizeList.get(position).isSelected()){
                holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.ic_rounded_background_size_selected));
        }else {
            holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.ic_rounded_background_product));
        }

        holder.itemView.setOnClickListener(v -> {
            sizeList.get(position).setSelected(!sizeList.get(position).isSelected());
            notifyDataSetChanged();
        });

        if(Constant.getLanguage(mContext).equals("en")){
            holder.tvName.setText(

                    sizeList.get(position).getName());
        }else{
            holder.tvName.setText(
                    sizeList.get(position).getArabicName());
        }
    }

    public List<Size> getSelectedSize(){
        List<Size> selectedSizeArray = new ArrayList<>();
        for (int i = 0;i<sizeList.size();i++){
            if (sizeList.get(i).isSelected()){
                selectedSizeArray.add(sizeList.get(i));
            }
        }
        return selectedSizeArray;
    }

    @Override
    public int getItemCount() {
        return sizeList.size();
    }

    public interface SelectedListener {
        void onSizeSelected(Size size);
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
