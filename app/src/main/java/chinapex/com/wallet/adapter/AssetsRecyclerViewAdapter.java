package chinapex.com.wallet.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.utils.CpLog;

public class AssetsRecyclerViewAdapter extends RecyclerView.Adapter<AssetsRecyclerViewAdapter.AssetsAdapterHolder> implements
        View.OnClickListener {

    private static final String TAG = AssetsRecyclerViewAdapter.class.getSimpleName();
    private OnItemClickListener mOnItemClickListener;
    private List<WalletBean> mWalletBeans;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AssetsRecyclerViewAdapter(List<WalletBean> walletBeans) {
        mWalletBeans = walletBeans;
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
    public AssetsAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item,
                parent, false);
        AssetsAdapterHolder holder = new AssetsAdapterHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull AssetsAdapterHolder holder, int position) {
        holder.walletName.setText(mWalletBeans.get(position).getWalletName());
        holder.walletAddr.setText(mWalletBeans.get(position).getWalletAddr());
        holder.balance.setText(String.valueOf(mWalletBeans.get(position).getBalance()));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return null == mWalletBeans ? 0 : mWalletBeans.size();
    }

    class AssetsAdapterHolder extends RecyclerView.ViewHolder {
        TextView walletName;
        TextView walletAddr;
        TextView balance;

        AssetsAdapterHolder(View itemView) {
            super(itemView);
            walletName = itemView.findViewById(R.id.tv_assets_rv_item_wallet_name);
            walletAddr = itemView.findViewById(R.id.tv_assets_rv_item_wallet_addr);
            balance = itemView.findViewById(R.id.tv_assets_rv_item_balance);
        }
    }
}
