package chinapex.com.wallet.global;

import android.text.TextUtils;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import chinapex.com.wallet.bean.TransactionRecord;
import chinapex.com.wallet.changelistener.ApexListeners;
import chinapex.com.wallet.executor.TaskController;
import chinapex.com.wallet.executor.callback.eth.IGetEthBlockNumberCallback;
import chinapex.com.wallet.executor.callback.eth.IGetEthTransactionHistoryCallback;
import chinapex.com.wallet.executor.callback.eth.IGetEthTransactionReceiptCallback;
import chinapex.com.wallet.executor.runnable.eth.GetEthBlockNumber;
import chinapex.com.wallet.executor.runnable.eth.GetEthTransactionHistory;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;

/**
 * Created by SteelCabbage on 2018/7/13 0013 11:55.
 * E-Mail：liuyi_61@163.com
 */

public class UpdateEthTxState implements IGetEthTransactionReceiptCallback, IGetEthTransactionHistoryCallback,
        IGetEthBlockNumberCallback {

    private static final String TAG = UpdateEthTxState.class.getSimpleName();

    private String mTxId;
    private ScheduledFuture mGetTxReceiptSF;
    private ScheduledFuture mUpdateTxRecordsSF;
    private ScheduledFuture mGetBlockNumberSF;
    private long mTxOfBlockNum;
    private long mCurrentBlockNum;

    public UpdateEthTxState(String txId) {
        mTxId = txId;
    }

    public void setGetTxReceiptSF(ScheduledFuture getTxReceiptSF) {
        mGetTxReceiptSF = getTxReceiptSF;
    }

    @Override
    public void getEthTransactionReceipt(String walletAddress, String blockNumber, boolean isSuccess) {
        if (TextUtils.isEmpty(mTxId) || TextUtils.isEmpty(walletAddress) || null == mGetTxReceiptSF) {
            CpLog.e(TAG, "getEthTransactionReceipt() -> mTxId or walletAddress or mGetTxReceiptSF is null!");
            return;
        }

        if (TextUtils.isEmpty(blockNumber)) {
            CpLog.w(TAG, "getEthTransactionReceipt()-> blockNumber is null,this tx is not in block!");
            return;
        }

        try {
            mTxOfBlockNum = Long.valueOf(blockNumber);
        } catch (NumberFormatException e) {
            CpLog.e(TAG, "getEthTransactionReceipt NumberFormatException:" + e.getMessage());
            return;
        }

        mGetTxReceiptSF.cancel(true);
        mGetBlockNumberSF = TaskController.getInstance().schedule(new GetEthBlockNumber(this), 0, Constant.TX_ETH_POLLING_TIME);
        mUpdateTxRecordsSF = TaskController.getInstance().schedule(new GetEthTransactionHistory(walletAddress, this), 0,
                Constant.TX_ETH_POLLING_TIME);

    }

    @Override
    public void getEthBlockNumber(String blockNumber) {
        if (TextUtils.isEmpty(blockNumber)) {
            CpLog.e(TAG, "getEthBlockNumber() -> blockNumber is null!");
            return;
        }

        try {
            mCurrentBlockNum = Long.valueOf(blockNumber);
        } catch (NumberFormatException e) {
            CpLog.e(TAG, "getEthBlockNumber NumberFormatException:" + e.getMessage());
            return;
        }

        if (mCurrentBlockNum - mTxOfBlockNum >= Constant.TX_ETH_CONFIRM_OK) {
            CpLog.i(TAG, "TX_ETH_CONFIRM_OK");
            mUpdateTxRecordsSF.cancel(false);
            mGetBlockNumberSF.cancel(false);
        }
    }

    @Override
    public void getEthTransactionHistory(List<TransactionRecord> transactionRecords) {
        if (Constant.TX_ETH_CONFIRM_OK > mCurrentBlockNum - mTxOfBlockNum) {
            return;
        }

        handleTx();
    }

    private void handleTx() {
        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication.getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            return;
        }

        List<TransactionRecord> cacheTxs = apexWalletDbDao.queryTxCacheByTxId(Constant.TABLE_ETH_TX_CACHE, mTxId);
        if (null == cacheTxs || cacheTxs.isEmpty()) {
            apexWalletDbDao.updateTxState(Constant.TABLE_ETH_TRANSACTION_RECORD, mTxId, Constant.TRANSACTION_STATE_SUCCESS);
            ApexListeners.getInstance().notifyTxStateUpdate(mTxId, Constant.TRANSACTION_STATE_SUCCESS, Constant
                    .NO_NEED_MODIFY_TX_TIME);
            return;
        }

        for (TransactionRecord cacheTx : cacheTxs) {
            if (null == cacheTx) {
                CpLog.e(TAG, "cacheTx is null!");
                continue;
            }

            cacheTx.setTxState(Constant.TRANSACTION_STATE_FAIL);
            apexWalletDbDao.insertTxRecord(Constant.TABLE_ETH_TRANSACTION_RECORD, cacheTx);
        }
        ApexListeners.getInstance().notifyTxStateUpdate(mTxId, Constant.TRANSACTION_STATE_FAIL, Constant.NO_NEED_MODIFY_TX_TIME);
        apexWalletDbDao.delCacheByTxId(Constant.TABLE_ETH_TX_CACHE, mTxId);
    }

}
