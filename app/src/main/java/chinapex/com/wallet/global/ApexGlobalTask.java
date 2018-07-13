package chinapex.com.wallet.global;

import android.text.TextUtils;

import java.util.List;
import java.util.concurrent.ScheduledFuture;

import chinapex.com.wallet.bean.TransactionRecord;
import chinapex.com.wallet.changelistener.ApexListeners;
import chinapex.com.wallet.executor.TaskController;
import chinapex.com.wallet.executor.callback.ICheckIsUpdateAssetsCallback;
import chinapex.com.wallet.executor.callback.IGetAssetsCallback;
import chinapex.com.wallet.executor.callback.IGetTransactionHistoryCallback;
import chinapex.com.wallet.executor.callback.IUpdateTxStateCallback;
import chinapex.com.wallet.executor.runnable.CheckIsUpdateAssets;
import chinapex.com.wallet.executor.runnable.GetAssets;
import chinapex.com.wallet.executor.runnable.GetTransactionHistory;
import chinapex.com.wallet.executor.runnable.UpdateTxState;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;

/**
 * Created by SteelCabbage on 2018/6/10 15:23
 * E-Mail：liuyi_61@163.com
 */
public class ApexGlobalTask implements ICheckIsUpdateAssetsCallback, IGetAssetsCallback {

    private static final String TAG = ApexGlobalTask.class.getSimpleName();
    private ScheduledFuture mCheckIsUpdateAssetsSF;

    private ApexGlobalTask() {

    }

    private static class ApexCacheHolder {
        private static final ApexGlobalTask sApexGlobalTask = new ApexGlobalTask();
    }

    public static ApexGlobalTask getInstance() {
        return ApexCacheHolder.sApexGlobalTask;
    }

    public void doInit() {
        TaskController.getInstance().submit(new CheckIsUpdateAssets(this));
    }

    @Override
    public void checkIsUpdateAssets(boolean isUpdate) {
        if (isUpdate) {
            CpLog.i(TAG, "need to update assets!");
            mCheckIsUpdateAssetsSF = TaskController.getInstance().schedule(new
                    GetAssets(this), 0, Constant.ASSETS_POLLING_TIME);
        }
    }

    @Override
    public void getAssets(String msg) {
        if (TextUtils.isEmpty(msg)) {
            CpLog.e(TAG, "getAssets() -> msg is null!");
            return;
        }

        if (Constant.UPDATE_ASSETS_OK.equals(msg)) {
            CpLog.i(TAG, "update assets ok!");
            mCheckIsUpdateAssetsSF.cancel(true);
        }
    }

    public void startPolling(String txId, String walletAddress) {
        if (TextUtils.isEmpty(txId) || TextUtils.isEmpty(walletAddress)) {
            CpLog.e(TAG, "txId or walletAddress is null!");
            return;
        }

        ImpUpdateTxStateCallback impUpdateTxStateCallback = new ImpUpdateTxStateCallback(txId);
        ScheduledFuture updateTxStateSF = TaskController.getInstance().schedule(new UpdateTxState
                (txId, walletAddress, impUpdateTxStateCallback), 0, Constant.TX_POLLING_TIME);
        impUpdateTxStateCallback.setScheduledFuture(updateTxStateSF);
    }

}
