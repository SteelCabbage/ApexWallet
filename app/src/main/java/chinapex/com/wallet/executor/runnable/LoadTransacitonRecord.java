package chinapex.com.wallet.executor.runnable;

import android.text.TextUtils;

import java.util.Collections;
import java.util.List;

import chinapex.com.wallet.bean.TransactionRecord;
import chinapex.com.wallet.executor.callback.ILoadTransactionRecordCallback;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;

/**
 * Created by SteelCabbage on 2018/6/28 0028 10:21.
 * E-Mail：liuyi_61@163.com
 */

public class LoadTransacitonRecord implements Runnable {

    private static final String TAG = LoadTransacitonRecord.class.getSimpleName();
    private String mAddress;
    private ILoadTransactionRecordCallback mILoadTransactionRecordCallback;

    public LoadTransacitonRecord(String address, ILoadTransactionRecordCallback
            ILoadTransactionRecordCallback) {
        mAddress = address;
        mILoadTransactionRecordCallback = ILoadTransactionRecordCallback;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mAddress) || null == mILoadTransactionRecordCallback) {
            CpLog.e(TAG, "mAddress or mILoadTransactionRecordCallback is null!");
            return;
        }

        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            mILoadTransactionRecordCallback.loadTransactionRecord(null);
            return;
        }

        List<TransactionRecord> transactionRecords = apexWalletDbDao
                .queryTransactionRecordsByWalletAddress(mAddress);
        Collections.reverse(transactionRecords);
        mILoadTransactionRecordCallback.loadTransactionRecord(transactionRecords);
    }
}
