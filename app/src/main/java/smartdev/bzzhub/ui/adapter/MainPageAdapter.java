package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.Company;
import smartdev.bzzhub.repository.model.JobResponse;
import smartdev.bzzhub.ui.activity.MainActivity;
import smartdev.bzzhub.util.Constant;
import smartdev.bzzhub.util.navigation.NavigationManager;
import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static smartdev.bzzhub.util.navigation.Arg.ARG_COMPANY_ID;
import static smartdev.bzzhub.util.navigation.Arg.ARG_CONNECTED_STATUS;

public class MainPageAdapter extends RecyclerView.Adapter<MainPageAdapter.Item>{

    private Context mContext;
    private  List<Company> mCompanyList;
    private SelectedListener mListener;

    public MainPageAdapter(Context context, List<Company> companyList,SelectedListener selectedListener) {
        this.mCompanyList = companyList;
        this.mContext = context;
        this.mListener = selectedListener;
    }

    public void setProducts(List<Company> CompanyList){
        mCompanyList = CompanyList;
        notifyDataSetChanged();
    }

    public void addProducts(List<Company> CompanyList){
        mCompanyList.addAll(CompanyList);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
        try {
            Company company = mCompanyList.get(position);
            holder.tAddress.setText(mContext.getResources().getString(R.string.str_address_value,company.getCountry() == null ? "":company.getCountry(),
                    company.getCity() == null ? "":company.getCity()));
            holder.tvCompanyName.setText(company.getCompanyName());
            holder.tDescription.setText(company.getDescription());
            Glide.with(mContext).load(company.getBanner()).centerCrop().into(holder.ivBanner);
            holder.cvParent.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putInt(ARG_COMPANY_ID,company.getCompanyId());
                bundle.putInt(ARG_CONNECTED_STATUS,company.getConnection());
                if (Constant.getInstance().getLoginType() != null && Constant.getInstance().getLoginType() == 1 &&
                Constant.getInstance().getCompanyProfile().getCompanyId().equals(company.getCompanyId())){
                    if (mContext instanceof MainActivity){
                        ((MainActivity)mContext).onProfileClicked();
                    }
                }else {
                    NavigationManager.gotoCompanyDetailsActivity((AppCompatActivity)mContext,bundle);
                }

            });
            if (Constant.getInstance().getLoginType() != null && Constant.getInstance().getLoginType() == 1){
                holder.layout.setVisibility(View.VISIBLE);
                if (company.getConnection() == 0){
                    holder.tConnect.setText("Connect");
                    holder.layout.setOnClickListener(v -> {
                        mCompanyList.get(position).setConnection(1);
                        notifyItemChanged(position);
                        mListener.onConnectClicked(company);
                    });
                }else if (company.getConnection() == 1){
                    holder.tConnect.setText("Connected");
                }else {
                    Log.d("asaasa",String.valueOf(company.getConnection()));
                    holder.layout.setVisibility(View.GONE);
                }
                if (Constant.getInstance().getCompanyProfile().getCompanyId().equals(company.getCompanyId())){
                    holder.layout.setVisibility(View.GONE);
                    Log.d("asaasa","samecompany");
                }
            }else {
                Log.d("asaasa","logintypeissue");
                holder.layout.setVisibility(View.GONE);
            }
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {
        return mCompanyList.size();

}
    public interface SelectedListener{
        void onConnectClicked(Company company);
    }


    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_banner)
        ImageView ivBanner;
        @BindView(R.id.tv_company_name)
        TextView tvCompanyName;
        @BindView(R.id.tv_address)
        TextView tAddress;
        @BindView(R.id.tv_description)
        TextView tDescription;
        @BindView(R.id.cv_connect)
        CardView layout;
        @BindView(R.id.tv_connect)
        TextView tConnect;
        @BindView(R.id.cv_parent)
        CardView cvParent;


        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
