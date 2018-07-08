package chinapex.com.wallet.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.bean.AssetBean;
import chinapex.com.wallet.utils.CpLog;

public class AddAssetsRecyclerViewAdapter extends RecyclerView.Adapter<AddAssetsRecyclerViewAdapter
        .AddAssetsHolder> implements View.OnClickListener {

    private static final String TAG = AddAssetsRecyclerViewAdapter.class.getSimpleName();
    private OnItemClickListener mOnItemClickListener;
    private List<AssetBean> mAssetBeans;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AddAssetsRecyclerViewAdapter(List<AssetBean> assetBeans) {
        mAssetBeans = assetBeans;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (null == mOnItemClickListener) {
            CpLog.e(TAG, "mOnItemClickListener is null!");
            return;
        }

        if (null == mAssetBeans || mAssetBeans.isEmpty()) {
            CpLog.e(TAG, "mAssetBeans is null or empty!");
            return;
        }

        Integer position = (Integer) v.getTag();
        AssetBean assetBean = mAssetBeans.get(position);
        if (null == assetBean) {
            CpLog.e(TAG, "assetBean is null or empty!");
            return;
        }

        mOnItemClickListener.onItemClick(position);


    }

    @NonNull
    @Override
    public AddAssetsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .recyclerview_add_assets_item, parent, false);
        AddAssetsHolder holder = new AddAssetsHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final AddAssetsHolder holder, int position) {
        AssetBean assetBean = mAssetBeans.get(position);
        if (null == assetBean) {
            CpLog.e(TAG, "assetBean is null!");
            return;
        }

        // TODO: 2018/7/8 logo url
        holder.assetSymbol.setText(assetBean.getSymbol());
        holder.assetName.setText(assetBean.getName());

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return null == mAssetBeans ? 0 : mAssetBeans.size();
    }


    class AddAssetsHolder extends RecyclerView.ViewHolder {
        ImageView assetLogo;
        TextView assetSymbol;
        TextView assetName;
        ImageView assetChecked;

        AddAssetsHolder(View itemView) {
            super(itemView);
            assetLogo = itemView.findViewById(R.id.iv_add_assets_logo);
            assetSymbol = itemView.findViewById(R.id.tv_add_assets_symbol);
            assetName = itemView.findViewById(R.id.tv_add_assets_name);
            assetChecked = itemView.findViewById(R.id.iv_add_assets_checked_state);
        }
    }
}
