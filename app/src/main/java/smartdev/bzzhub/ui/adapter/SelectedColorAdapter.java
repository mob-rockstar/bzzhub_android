package smartdev.bzzhub.ui.adapter;

import android.content.Context;
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
import smartdev.bzzhub.util.Constant;

public class SelectedColorAdapter   extends RecyclerView.Adapter<SelectedColorAdapter.Item> {

    private Context mContext;
    private List<ColorResponse.Result> colors = new ArrayList<>();

    public SelectedColorAdapter(Context mContext, List<ColorResponse.Result> colors) {
        this.mContext = mContext;
        this.colors = colors;
    }

    public void setColors(List<ColorResponse.Result> mColors){
        colors = mColors;
        notifyDataSetChanged();
    }

    public void addColor(ColorResponse.Result mColor){
        colors.add(mColor);
        notifyDataSetChanged();
    }

    public void removeColor(ColorResponse.Result mColor){
        try {
            for (ColorResponse.Result color : colors){
                if (color.equals(mColor)){
                    colors.remove(color);
                }
            }

        }catch (Exception e){

        }
        notifyDataSetChanged();
    }

    public List<ColorResponse.Result>  getColors(){
        return colors;
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
                    colors.get(position).getEnglish());
        }else{
            holder.tvName.setText(
                    colors.get(position).getArabic());
        }
    }

    public interface SelectedListener {
        void onSelected(ColorResponse.Result color);
    }

    @Override
    public int getItemCount() {
        return colors.size();
    }


    public String getSelectedSize(){
        StringBuilder strLanguage = new StringBuilder();
        if (!colors.isEmpty()){
            for (int i = 0;i<colors.size();i++){
                strLanguage.append(colors.get(i).getLangid()).append(i<colors.size()-1 ? " ," : "");
            }
        }

        return strLanguage.toString();
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
