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
import chinapex.com.wallet.bean.NeoWallet;
import chinapex.com.wallet.utils.CpLog;

public class SwitchWallet2RecyclerViewAdapter extends RecyclerView
        .Adapter<SwitchWallet2RecyclerViewAdapter.SwitchWallet2Holder> implements View
        .OnClickListener {

    private static final String TAG = SwitchWallet2RecyclerViewAdapter.class.getSimpleName();
    private OnItemClickListener mOnItemClickListener;
    private List<NeoWallet> mNeoWallets;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public SwitchWallet2RecyclerViewAdapter(List<NeoWallet> neoWallets) {
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

        if (null == mNeoWallets || mNeoWallets.isEmpty()) {
            CpLog.e(TAG, "mNeoWallets is null or empty!");
            return;
        }

        Integer position = (Integer) v.getTag();

        for (int i = 0; i < mNeoWallets.size(); i++) {
            NeoWallet neoWallet = mNeoWallets.get(i);
            if (null == neoWallet) {
                CpLog.i(TAG, "neoWallet is null!");
                continue;
            }

            if (i == position) {
                neoWallet.setSelected(true);
            } else {
                neoWallet.setSelected(false);
            }
        }

        notifyDataSetChanged();
        mOnItemClickListener.onItemClick(position);
    }

    @NonNull
    @Override
    public SwitchWallet2Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .recyclerview_choose_wallet, parent, false);
        SwitchWallet2Holder holder = new SwitchWallet2Holder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SwitchWallet2Holder holder, int position) {
        NeoWallet neoWallet = mNeoWallets.get(position);
        if (null == neoWallet) {
            CpLog.e(TAG, "neoWallet is null!");
            return;
        }

        holder.walletName.setText(neoWallet.getWalletName());
        holder.walletAddress.setText(neoWallet.getWalletAddr());
        if (neoWallet.isSelected()) {
            holder.checkState.setVisibility(View.VISIBLE);
        } else {
            holder.checkState.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return null == mNeoWallets ? 0 : mNeoWallets.size();
    }


    class SwitchWallet2Holder extends RecyclerView.ViewHolder {
        TextView walletName;
        TextView walletAddress;
        ImageView checkState;

        SwitchWallet2Holder(View itemView) {
            super(itemView);
            walletName = itemView.findViewById(R.id.tv_choose_wallet_name);
            walletAddress = itemView.findViewById(R.id.tv_choose_wallet_address);
            checkState = itemView.findViewById(R.id.iv_choose_wallet_check_state);
        }
    }
}
