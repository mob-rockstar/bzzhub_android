package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.SelectorResponse;

public class SelectorSpinnerAdapter extends ArrayAdapter<SelectorResponse.Result> {
    private Context mContext;
    private List<SelectorResponse.Result> mList;
    public SelectorSpinnerAdapter(@NonNull Context context, ArrayList<SelectorResponse.Result> selectorList) {
        super(context,0,selectorList);
        this.mContext = context;
        mList = selectorList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_spinner, parent, false);
        }
        TextView textViewName = convertView.findViewById(R.id.tv_content);
        textViewName.setText(mList.get(position).getName());

        return convertView;
    }
}
