package chinapex.com.wallet.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.bean.PortraitBean;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;

public class PortraitRecyclerViewAdapter extends RecyclerView.Adapter implements View
        .OnClickListener {

    private static final String TAG = PortraitRecyclerViewAdapter.class.getSimpleName();
    public static final int TYPE_INPUT = 0;
    public static final int TYPE_LEVEL_ONE_LINKAGE = 1;
    public static final int TYPE_LEVEL_TWO_LINKAGE = 2;
    public static final int TYPE_LEVEL_THREE_LINKAGE = 3;
    public static final int TYPE_TAGS = 4;
    private OnItemClickListener mOnItemClickListener;
    private List<PortraitBean> mPortraitBeans;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public PortraitRecyclerViewAdapter(List<PortraitBean> portraitBeans) {
        mPortraitBeans = portraitBeans;
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                        .recyclerview_me_item,
                parent, false);
        MeAdapterHolder holder = new MeAdapterHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        WalletBean walletBean = mWalletBeans.get(position);


        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return null == mPortraitBeans ? 0 : mPortraitBeans.size();
    }

    class MeAdapterHolder extends RecyclerView.ViewHolder {
        TextView walletName;
        TextView walletAddr;
        TextView isBackup;


        MeAdapterHolder(View itemView) {
            super(itemView);
            walletName = itemView.findViewById(R.id.tv_me_rv_item_wallet_name);
            walletAddr = itemView.findViewById(R.id.tv_me_rv_item_wallet_addr);
            isBackup = itemView.findViewById(R.id.tv_me_rv_item_backup);
        }
    }
}
