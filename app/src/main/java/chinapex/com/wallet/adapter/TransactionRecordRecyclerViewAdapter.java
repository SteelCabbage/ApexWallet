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
import chinapex.com.wallet.bean.TransactionRecord;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.PhoneUtils;

public class TransactionRecordRecyclerViewAdapter extends RecyclerView
        .Adapter<TransactionRecordRecyclerViewAdapter.TransactionRecordAdapterHolder> implements
        View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = TransactionRecordRecyclerViewAdapter.class.getSimpleName();
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private List<TransactionRecord> mTransactionRecords;


    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public TransactionRecordRecyclerViewAdapter(List<TransactionRecord> transactionRecords) {
        mTransactionRecords = transactionRecords;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public void onClick(View v) {
        if (null == mOnItemClickListener) {
            CpLog.e(TAG, "mOnItemClickListener is null!");
            return;
        }
        mOnItemClickListener.onItemClick((Integer) v.getTag());
    }

    @Override
    public boolean onLongClick(View v) {
        if (null == mOnItemLongClickListener) {
            CpLog.e(TAG, "mOnItemLongClickListener is null!");
            return false;
        }
        mOnItemLongClickListener.onItemLongClick((Integer) v.getTag());
        return true;
    }

    @NonNull
    @Override
    public TransactionRecordAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int
            viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout
                .recyclerview_transaction_record, parent, false);
        TransactionRecordAdapterHolder holder = new TransactionRecordAdapterHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionRecordAdapterHolder holder, int position) {
        TransactionRecord transactionRecord = mTransactionRecords.get(position);
        if (null == transactionRecord) {
            CpLog.e(TAG, "transactionRecord is null!");
            return;
        }

        // TODO: 2018/6/21 0021 logoUrl
        holder.txID.setText(transactionRecord.getTxID());
        holder.txAmount.setText(transactionRecord.getTxAmount());
        holder.txTime.setText(PhoneUtils.getFormatTime(transactionRecord.getTxTime()));

        switch (transactionRecord.getTxState()) {
            case Constant.TRANSACTION_STATE_FAIL:
                holder.txState.setText(Constant.TRANSACTION_STATE_FAIL_TEXT);
                holder.txState.setTextColor(ApexWalletApplication.getInstance().getResources()
                        .getColor(R.color.c_E16A67));
                break;
            case Constant.TRANSACTION_STATE_SUCCESS:
                holder.txState.setText(Constant.TRANSACTION_STATE_SUCCESS_TEXT);
                holder.txState.setTextColor(ApexWalletApplication.getInstance().getResources()
                        .getColor(R.color.c_54CA80));
                break;
            case Constant.TRANSACTION_STATE_CONFIRMING:
                holder.txState.setText(Constant.TRANSACTION_STATE_CONFIRMING_TEXT);
                holder.txState.setTextColor(ApexWalletApplication.getInstance().getResources()
                        .getColor(R.color.colorPrimary));
                break;
            default:
                break;
        }

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return null == mTransactionRecords ? 0 : mTransactionRecords.size();
    }

    class TransactionRecordAdapterHolder extends RecyclerView.ViewHolder {
        ImageView txLogo;
        TextView txID;
        TextView txAmount;
        TextView txTime;
        TextView txState;

        TransactionRecordAdapterHolder(View itemView) {
            super(itemView);
            txLogo = itemView.findViewById(R.id.iv_transaction_record_tx_logo);
            txID = itemView.findViewById(R.id.tv_transaction_record_tx_id);
            txAmount = itemView.findViewById(R.id.tv_transaction_record_tx_amount);
            txTime = itemView.findViewById(R.id.tv_transaction_record_tx_time);
            txState = itemView.findViewById(R.id.tv_transaction_record_tx_state);
        }
    }
}