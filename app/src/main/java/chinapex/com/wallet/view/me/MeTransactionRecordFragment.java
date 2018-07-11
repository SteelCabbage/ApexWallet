package chinapex.com.wallet.view.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.TransactionRecordRecyclerViewAdapter;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.bean.TransactionRecord;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.changelistener.ApexListeners;
import chinapex.com.wallet.changelistener.OnTxStateUpdateListener;
import chinapex.com.wallet.executor.TaskController;
import chinapex.com.wallet.executor.callback.IGetTransactionHistoryCallback;
import chinapex.com.wallet.executor.callback.ILoadTransactionRecordCallback;
import chinapex.com.wallet.executor.runnable.GetTransactionHistory;
import chinapex.com.wallet.executor.runnable.LoadTransacitonRecord;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.PhoneUtils;
import chinapex.com.wallet.utils.ToastUtils;
import chinapex.com.wallet.view.MeSkipActivity;
import chinapex.com.wallet.view.dialog.SwitchWalletDialog;

/**
 * Created by SteelCabbage on 2018/5/31 0031.
 */

public class MeTransactionRecordFragment extends BaseFragment implements View.OnClickListener,
        SwitchWalletDialog.onItemSelectedListener, SwipeRefreshLayout.OnRefreshListener,
        TransactionRecordRecyclerViewAdapter.OnItemClickListener, IGetTransactionHistoryCallback,
        ILoadTransactionRecordCallback, OnTxStateUpdateListener, TextWatcher {

    private static final String TAG = MeTransactionRecordFragment.class.getSimpleName();
    private TextView mTv_me_transaction_record_title;
    private TextView mTv_me_transaction_record_address;
    private ImageButton mIb_me_transaction_record_switch;
    private WalletBean mCurrentClickedWalletBean;
    private SwipeRefreshLayout mSl_transaction_record;
    private RecyclerView mRv_transaction_record;
    private List<TransactionRecord> mTransactionRecords;
    private List<TransactionRecord> mSearchTxRecords;
    private TransactionRecordRecyclerViewAdapter mTransactionRecordRecyclerViewAdapter;
    private EditText mEt_tx_records_search;
    private TextView mTv_tx_records_cancel;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me_transaction_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
        loadTxsFromDb();
        incrementalUpdateTxDbFromNet();
    }

    private void initView(View view) {
        mTv_me_transaction_record_title = view.findViewById(R.id.tv_me_transaction_record_title);
        mTv_me_transaction_record_address = view.findViewById(R.id
                .tv_me_transaction_record_address);
        mIb_me_transaction_record_switch = view.findViewById(R.id.ib_me_transaction_record_switch);
        mSl_transaction_record = view.findViewById(R.id.sl_transaction_record);
        mRv_transaction_record = view.findViewById(R.id.rv_transaction_record);

        // 搜索功能
        mEt_tx_records_search = view.findViewById(R.id.et_tx_records_search);
        mTv_tx_records_cancel = view.findViewById(R.id.tv_tx_records_cancel);

        mEt_tx_records_search.addTextChangedListener(this);
        mTv_tx_records_cancel.setOnClickListener(this);


        mRv_transaction_record.setLayoutManager(new LinearLayoutManager(ApexWalletApplication
                .getInstance(), LinearLayoutManager.VERTICAL, false));
        mTransactionRecords = new ArrayList<>();
        mSearchTxRecords = new ArrayList<>();
        mTransactionRecordRecyclerViewAdapter = new TransactionRecordRecyclerViewAdapter
                (mTransactionRecords);
        mTransactionRecordRecyclerViewAdapter.setOnItemClickListener(this);
        mRv_transaction_record.setAdapter(mTransactionRecordRecyclerViewAdapter);

        mIb_me_transaction_record_switch.setOnClickListener(this);
        mSl_transaction_record.setColorSchemeColors(this.getActivity().getResources().getColor(R
                .color.colorPrimary));
        mSl_transaction_record.setOnRefreshListener(this);

        // copy address
        mTv_me_transaction_record_address.setOnClickListener(this);
    }

    private void initData() {
        MeSkipActivity meSkipActivity = (MeSkipActivity) getActivity();
        mCurrentClickedWalletBean = meSkipActivity.getWalletBean();
        if (null == mCurrentClickedWalletBean) {
            CpLog.e(TAG, "currentClickedWalletBean is null!");
            return;
        }

        mTv_me_transaction_record_title.setText(mCurrentClickedWalletBean.getWalletName());
        mTv_me_transaction_record_address.setText(mCurrentClickedWalletBean.getWalletAddr());
        ApexListeners.getInstance().addOnTxStateUpdateListener(this);
    }

    private void loadTxsFromDb() {
        String address = mTv_me_transaction_record_address.getText().toString().trim();
        mTransactionRecords.clear();
        mSearchTxRecords.clear();
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTransactionRecordRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
        TaskController.getInstance().submit(new LoadTransacitonRecord(address, this));
    }

    @Override
    public void loadTransactionRecord(List<TransactionRecord> transactionRecords) {
        if (null == transactionRecords || transactionRecords.isEmpty()) {
            CpLog.w(TAG, "loadTransactionRecord() -> transactionRecords is null or empty!");
            return;
        }

        mTransactionRecords.addAll(transactionRecords);
        mSearchTxRecords.addAll(transactionRecords);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                CpLog.i(TAG, "loadTransactionRecord ok!");
                mTransactionRecordRecyclerViewAdapter.notifyDataSetChanged();
            }
        });

        if (mSl_transaction_record.isRefreshing()) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSl_transaction_record.setRefreshing(false);
                }
            });
        }

        mEt_tx_records_search.getText().clear();
    }

    private void incrementalUpdateTxDbFromNet() {
        String walletAddr = mTv_me_transaction_record_address.getText().toString().trim();
        TaskController.getInstance().submit(new GetTransactionHistory(walletAddr, this));
    }

    @Override
    public void getTransactionHistory(List<TransactionRecord> transactionRecords) {
        if (null == transactionRecords || transactionRecords.isEmpty()) {
            CpLog.w(TAG, "getTransactionHistory() -> transactionRecords is null or empty!");
            if (mSl_transaction_record.isRefreshing()) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSl_transaction_record.setRefreshing(false);
                    }
                });
            }
            return;
        }

        loadTxsFromDb();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_me_transaction_record_switch:
                showDialog(mCurrentClickedWalletBean);
                break;
            case R.id.tv_tx_records_cancel:
                mEt_tx_records_search.getText().clear();
                break;
            case R.id.tv_me_transaction_record_address:
                String copyAddr = mTv_me_transaction_record_address.getText().toString().trim();
                PhoneUtils.copy2Clipboard(ApexWalletApplication.getInstance(), copyAddr);
                ToastUtils.getInstance().showToast("钱包地址已复制");
                break;
            default:
                break;
        }
    }

    private void showDialog(WalletBean currentClickedWalletBean) {
        SwitchWalletDialog switchWalletDialog = SwitchWalletDialog.newInstance();
        switchWalletDialog.setCurrentWalletBean(currentClickedWalletBean);
        switchWalletDialog.setOnItemSelectedListener(this);
        switchWalletDialog.show(getFragmentManager(), "SwitchWalletDialog");
    }

    @Override
    public void onItemSelected(WalletBean walletBean) {
        if (null == walletBean) {
            CpLog.e(TAG, "walletBean is null!");
            return;
        }

        mCurrentClickedWalletBean = walletBean;
        mTv_me_transaction_record_title.setText(walletBean.getWalletName());
        mTv_me_transaction_record_address.setText(walletBean.getWalletAddr());

        // update transactionRecord
        loadTxsFromDb();
        incrementalUpdateTxDbFromNet();
    }

    @Override
    public void onItemClick(int position) {
        if (null == mTransactionRecords || mTransactionRecords.isEmpty()) {
            CpLog.e(TAG, "mTransactionRecords is null or is empty!");
            return;
        }

        TransactionRecord transactionRecord = mTransactionRecords.get(position);
        if (null == transactionRecord) {
            CpLog.e(TAG, "transactionRecord is null!");
            return;
        }

        startActivityParcelable(TransactionDetailActivity.class, false, Constant
                .PARCELABLE_TRANSACTION_RECORD, transactionRecord);
    }

    @Override
    public void onRefresh() {
        incrementalUpdateTxDbFromNet();
    }

    @Override
    public void onTxStateUpdate(String txID, int state, long txTime) {
        if (TextUtils.isEmpty(txID)) {
            CpLog.e(TAG, "txID is null!");
            return;
        }

        if (null == mTransactionRecords || mTransactionRecords.isEmpty()) {
            CpLog.e(TAG, "mTransactionRecords is null or empty!");
            return;
        }

        for (TransactionRecord transactionRecord : mTransactionRecords) {
            if (null == transactionRecord) {
                continue;
            }

            if (txID.equals(transactionRecord.getTxID())) {
                transactionRecord.setTxState(state);
                if (txTime != Constant.NO_NEED_MODIFY_TX_TIME) {
                    transactionRecord.setTxTime(txTime);
                }
            }
        }

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTransactionRecordRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApexListeners.getInstance().removeOnTxStateUpdateListener(this);
        CpLog.w(TAG, "onDestroy");
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        if (null == mSearchTxRecords || mSearchTxRecords.isEmpty()) {
            CpLog.e(TAG, "mSearchTxRecords is null or empty!");
            return;
        }

        mTransactionRecords.clear();
        mTransactionRecords.addAll(mSearchTxRecords);

        if (TextUtils.isEmpty(s)) {
            CpLog.w(TAG, "onTextChanged() -> is empty!");
            mTransactionRecordRecyclerViewAdapter.notifyDataSetChanged();
            return;
        }

        Iterator<TransactionRecord> iterator = mTransactionRecords.iterator();
        while (iterator.hasNext()) {
            TransactionRecord transactionRecord = iterator.next();
            if (null == transactionRecord) {
                CpLog.e(TAG, "transactionRecord is null!");
                continue;
            }

            if (!transactionRecord.getTxID().contains(s)) {
                iterator.remove();
            }
        }

        mTransactionRecordRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
