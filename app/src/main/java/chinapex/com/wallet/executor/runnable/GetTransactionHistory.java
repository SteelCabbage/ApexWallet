package chinapex.com.wallet.executor.runnable;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import chinapex.com.wallet.bean.TransactionRecord;
import chinapex.com.wallet.bean.response.ResponseGetTransactionHistory;
import chinapex.com.wallet.executor.callback.IGetTransactionHistoryCallback;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.net.INetCallback;
import chinapex.com.wallet.net.OkHttpClientManager;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.GsonUtils;

/**
 * Created by SteelCabbage on 2018/6/22 0022 11:42.
 * E-Mail：liuyi_61@163.com
 */

public class GetTransactionHistory implements Runnable, INetCallback {

    private static final String TAG = GetTransactionHistory.class.getSimpleName();
    private long mTime;
    private String mAddress;
    private IGetTransactionHistoryCallback mIGetTransactionHistoryCallback;

    public GetTransactionHistory(long time, String address, IGetTransactionHistoryCallback
            IGetTransactionHistoryCallback) {
        mTime = time;
        mAddress = address;
        mIGetTransactionHistoryCallback = IGetTransactionHistoryCallback;
    }

    @Override
    public void run() {
        if (null == mIGetTransactionHistoryCallback || TextUtils.isEmpty(mAddress)) {
            CpLog.e(TAG, "mIGetTransactionHistoryCallback or mAddress is null!");
            return;
        }

//        /**
//         * http://tracker.chinapex.com
//         * .cn/tool/transaction-history/AQVh2pG732YvtNaxEGkQUei3YA4cvo7d2i?beginTime=0
//         */
//
//        mAddress = "AQVh2pG732YvtNaxEGkQUei3YA4cvo7d2i";
//        mTime = 0;

        String url = Constant.URL_TRANSACTION_HISTORY + mAddress + "?beginTime=" + mTime;
        OkHttpClientManager.getInstance().get(url, this);
    }

    @Override
    public void onSuccess(int statusCode, String msg, String result) {
        if (TextUtils.isEmpty(result)) {
            CpLog.e(TAG, "result is null!");
            mIGetTransactionHistoryCallback.getTransactionHistory(null);
            return;
        }

        ResponseGetTransactionHistory responseGetTransactionHistory = GsonUtils.json2Bean(result,
                ResponseGetTransactionHistory.class);
        if (null == responseGetTransactionHistory) {
            CpLog.e(TAG, "responseGetTransactionHistory is null!");
            mIGetTransactionHistoryCallback.getTransactionHistory(null);
            return;
        }

        List<ResponseGetTransactionHistory.ResultBean> resultBeans = responseGetTransactionHistory
                .getResult();
        if (null == resultBeans || resultBeans.isEmpty()) {
            CpLog.e(TAG, "resultBeans is null or empty!");
            mIGetTransactionHistoryCallback.getTransactionHistory(null);
            return;
        }

        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            mIGetTransactionHistoryCallback.getTransactionHistory(null);
            return;
        }

        List<TransactionRecord> transactionRecords = new ArrayList<>();

        for (ResponseGetTransactionHistory.ResultBean resultBean : resultBeans) {
            if (null == resultBean) {
                CpLog.e(TAG, "resultBean is null!");
                continue;
            }

            TransactionRecord transactionRecord = new TransactionRecord();
            transactionRecord.setWalletAddress(mAddress);
            String resultBeanType = resultBean.getType();
            transactionRecord.setTxType(resultBeanType);
            transactionRecord.setTxID(resultBean.getTxid());
            transactionRecord.setTxAmount(resultBean.getValue());
            switch (resultBeanType) {
                case Constant.ASSET_TYPE_NEP5:
                    String vmstate = (String) resultBean.getVmstate();
                    if (!TextUtils.isEmpty(vmstate) && !vmstate.contains("FAULT")) {
                        transactionRecord.setTxState(Constant.TRANSACTION_STATE_SUCCESS);
                    }
                    transactionRecord.setTxState(Constant.TRANSACTION_STATE_FAIL);
                    break;
                default:
                    transactionRecord.setTxState(Constant.TRANSACTION_STATE_SUCCESS);
                    break;
            }
            transactionRecord.setTxFrom(resultBean.getFrom());
            transactionRecord.setTxTo(resultBean.getTo());
            transactionRecord.setGasConsumed(null == resultBean.getGas_consumed() ? "0" : (String)
                    resultBean.getGas_consumed());
            transactionRecord.setAssetID(resultBean.getAssetId());
            transactionRecord.setAssetSymbol(resultBean.getSymbol());
            transactionRecord.setAssetLogoUrl(resultBean.getImageURL());
            transactionRecord.setAssetDecimal(null == resultBean.getDecimal() ? 0 : Integer
                    .valueOf((String) resultBean.getDecimal()));
            transactionRecord.setTxTime(resultBean.getTime());

            apexWalletDbDao.insertTxRecord(transactionRecord);
            transactionRecords.add(transactionRecord);
        }

        mIGetTransactionHistoryCallback.getTransactionHistory(transactionRecords);
    }

    @Override
    public void onFailed(int failedCode, String msg) {
        CpLog.e(TAG, "getTransactionHistory net onFailed!");
        mIGetTransactionHistoryCallback.getTransactionHistory(null);
    }
}
