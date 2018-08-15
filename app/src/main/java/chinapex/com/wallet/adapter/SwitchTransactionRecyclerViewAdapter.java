package chinapex.com.wallet.adapter;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.bean.NeoWallet;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.utils.CpLog;

/**
 * Created by SteelCabbage on 2018/5/24 0024.
 */

public class SwitchTransactionRecyclerViewAdapter extends RecyclerView
        .Adapter<SwitchTransactionRecyclerViewAdapter.SwitchTransactionAdapterHolder>
        implements View.OnClickListener {

    private static final String TAG = SwitchTransactionRecyclerViewAdapter.class.getSimpleName();
    private OnItemClickListener mOnItemClickListener;
    private List<NeoWallet> mNeoWallets;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public SwitchTransactionRecyclerViewAdapter(List<NeoWallet> neoWallets) {
        mNeoWallets = neoWallets;
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
    public SwitchTransactionAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .recyclerview_me_switch_transaction,
                parent, false);
        SwitchTransactionAdapterHolder holder = new SwitchTransactionAdapterHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull SwitchTransactionAdapterHolder holder, int position) {
        NeoWallet neoWallet = mNeoWallets.get(position);
        if (null == neoWallet) {
            CpLog.e(TAG, "neoWallet is null!");
            return;
        }

        holder.walletName.setText(neoWallet.getWalletName());

        if (neoWallet.isSelected()) {
            holder.walletName.setBackgroundColor(ApexWalletApplication.getInstance().getResources
                    ().getColor(R.color.c_1253BF));
            holder.walletName.setTextColor(Color.WHITE);
        } else {
            holder.walletName.setBackgroundResource(0);
            holder.walletName.setTextColor(ApexWalletApplication.getInstance().getResources
                    ().getColor(R.color.c_333333));
        }

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return null == mNeoWallets ? 0 : mNeoWallets.size();
    }

    class SwitchTransactionAdapterHolder extends RecyclerView.ViewHolder {
        TextView walletName;

        SwitchTransactionAdapterHolder(View itemView) {
            super(itemView);
            walletName = itemView.findViewById(R.id.tv_me_switch_transaction);

        }
    }
}
