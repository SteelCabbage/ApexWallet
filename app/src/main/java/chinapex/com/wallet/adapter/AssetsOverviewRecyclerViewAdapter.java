package chinapex.com.wallet.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.bean.AssetBean;
import chinapex.com.wallet.bean.BalanceBean;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;

public class AssetsOverviewRecyclerViewAdapter extends RecyclerView
        .Adapter<AssetsOverviewRecyclerViewAdapter.AssetsOverviewAdapterHolder> implements
        View.OnClickListener {

    private static final String TAG = AssetsOverviewRecyclerViewAdapter.class.getSimpleName();
    private OnItemClickListener mOnItemClickListener;
    private List<BalanceBean> mBalanceBeans;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AssetsOverviewRecyclerViewAdapter(List<BalanceBean> balanceBeans) {
        mBalanceBeans = balanceBeans;
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
        mOnItemClickListener.onItemClick((Integer) v.getTag());
    }

    @NonNull
    @Override
    public AssetsOverviewAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .recyclerview_assets_overview_item,
                parent, false);
        AssetsOverviewAdapterHolder holder = new AssetsOverviewAdapterHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AssetsOverviewAdapterHolder holder, int position) {
        BalanceBean balanceBean = mBalanceBeans.get(position);
        if (null == balanceBean) {
            CpLog.e(TAG, "balanceBean is null!");
            return;
        }

        String assetsID = balanceBean.getAssetsID();
        if (TextUtils.isEmpty(assetsID)) {
            CpLog.e(TAG, "assetsID is null!");
            return;
        }

        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            return;
        }

        AssetBean assetBean = apexWalletDbDao.queryAssetByHash(assetsID);
        if (null == assetBean) {
            CpLog.e(TAG, "assetBean is null!");
            return;
        }

        switch (assetsID) {
            case Constant.ASSETS_NEO:
                holder.mapState.setVisibility(View.GONE);
                Glide.with(ApexWalletApplication.getInstance()).load(R.drawable.logo_global_neo)
                        .into(holder.assetLogo);
                break;
            case Constant.ASSETS_NEO_GAS:
                holder.mapState.setVisibility(View.GONE);
                Glide.with(ApexWalletApplication.getInstance()).load(R.drawable.logo_global_gas)
                        .into(holder.assetLogo);
                break;
            case Constant.ASSETS_CPX:
                holder.mapState.setVisibility(View.VISIBLE);
                Glide.with(ApexWalletApplication.getInstance()).load(R.drawable.logo_nep5_cpx)
                        .into(holder.assetLogo);
                break;
            default:
                holder.mapState.setVisibility(View.GONE);
                Glide.with(ApexWalletApplication.getInstance()).load(assetBean.getImageUrl())
                        .into(holder.assetLogo);
                break;
        }

        holder.assetsName.setText(balanceBean.getAssetSymbol());
        holder.assetsValue.setText(balanceBean.getAssetsValue());

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return null == mBalanceBeans ? 0 : mBalanceBeans.size();
    }

    class AssetsOverviewAdapterHolder extends RecyclerView.ViewHolder {
        ImageView assetLogo;
        TextView assetsName;
        TextView assetsValue;
        Button mapState;

        AssetsOverviewAdapterHolder(View itemView) {
            super(itemView);
            assetLogo = itemView.findViewById(R.id.iv_assets_overview_item_logo);
            assetsName = itemView.findViewById(R.id.tv_assets_overview_assets_name);
            assetsValue = itemView.findViewById(R.id.tv_assets_overview_assets_value);
            mapState = itemView.findViewById(R.id.bt_assets_overview_map);
        }
    }
}
