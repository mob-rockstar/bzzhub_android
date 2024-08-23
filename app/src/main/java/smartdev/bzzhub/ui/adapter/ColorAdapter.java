package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
import smartdev.bzzhub.repository.model.ColorResponse;
import smartdev.bzzhub.repository.model.Size;
import smartdev.bzzhub.util.Constant;

public class ColorAdapter  extends RecyclerView.Adapter<ColorAdapter.Item>  {

    private Context mContext;
    private List<ColorResponse.Result> colors = new ArrayList<>();
    private SelectedListener mSelectedListener;

    public ColorAdapter(Context mContext, List<ColorResponse.Result> colors, SelectedListener mSelectedListener) {
        this.mContext = mContext;
        this.colors = colors;
        this.mSelectedListener = mSelectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {

        if (colors.get(position).isSelected()){
            holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.ic_rounded_background_size_selected));
        }else {
            holder.tvName.setBackground(mContext.getResources().getDrawable(R.drawable.ic_rounded_background_product));
        }
        holder.itemView.setOnClickListener(v -> {
            colors.get(position).setSelected(!colors.get(position).isSelected());
            notifyDataSetChanged();
        });

        if(Constant.getLanguage(mContext).equals("en")){
            holder.tvName.setText(
                    colors.get(position).getEnglish());
        }else{
            holder.tvName.setText(
                    colors.get(position).getArabic());
        }

    }

    public interface SelectedListener {
        void onColorSelected(ColorResponse.Result color);
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }

    public List<ColorResponse.Result> getSelectedSize(){
        List<ColorResponse.Result> selectedSizeArray = new ArrayList<>();
        for (int i = 0;i<colors.size();i++){
            if (colors.get(i).isSelected()){
                selectedSizeArray.add(colors.get(i));
            }
        }
        return selectedSizeArray;
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
