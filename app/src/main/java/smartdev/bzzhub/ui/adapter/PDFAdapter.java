package smartdev.bzzhub.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import smartdev.bzzhub.R;
import smartdev.bzzhub.repository.model.BookResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PDFAdapter  extends RecyclerView.Adapter<PDFAdapter.Item>{
    private Context mContext;
    private List<BookResponse.PDFResult> mPDFList;
    private SelectedListener mSelectedListener;

    public PDFAdapter(Context mContext, List<BookResponse.PDFResult> mPDFList,SelectedListener selectedListener) {
        this.mContext = mContext;
        this.mPDFList = mPDFList;
        this.mSelectedListener = selectedListener;
    }

    @NonNull
    @Override
    public Item onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Item(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pdf_documents, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull Item holder, int position) {
            holder.tvTitle.setText(mPDFList.get(position).getTitle());
            holder.itemView.setOnClickListener(v -> mSelectedListener.onSelected(position));
    }

    @Override
    public int getItemCount() {
        return mPDFList.size();
    }

    public interface SelectedListener {
        void onSelected(int position);
    }

    class Item extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_title)
        TextView tvTitle;

        public Item(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
