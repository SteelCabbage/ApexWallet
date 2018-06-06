package chinapex.com.wallet.changelistener;

import java.util.ArrayList;

import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.utils.CpLog;

/**
 * Created by SteelCabbage on 2018/4/9 0009.
 */

public class ApexListeners {

    private static final String TAG = ApexListeners.class.getSimpleName();

    private ArrayList<onItemDeleteListener> mOnItemDeleteListeners;

    private ApexListeners() {

    }

    private static class ChangeListenerControllerHolder {
        private static final ApexListeners S_APEX_LISTENER_CONTROLLER = new
                ApexListeners();
    }

    public static ApexListeners getInstance() {
        return ChangeListenerControllerHolder.S_APEX_LISTENER_CONTROLLER;
    }

    public void doInit() {
        mOnItemDeleteListeners = new ArrayList<>();
    }

    public void onDestroy() {
        if (null == mOnItemDeleteListeners) {
            CpLog.e(TAG, "onDestroy() -> mOnItemDeleteListeners is null!");
            return;
        }

        mOnItemDeleteListeners.clear();
        mOnItemDeleteListeners = null;
    }


    public void addOnItemDeleteListener(onItemDeleteListener onItemDeleteListener) {
        if (null == mOnItemDeleteListeners || null == onItemDeleteListener) {
            CpLog.e(TAG, "mOnItemDeleteListeners or onItemDeleteListener is null!");
            return;
        }

        mOnItemDeleteListeners.add(onItemDeleteListener);
    }

    public void notifyItemDelete(WalletBean walletBean) {
        if (null == mOnItemDeleteListeners) {
            CpLog.e(TAG, "mOnItemDeleteListeners is null!");
            return;
        }

        for (onItemDeleteListener onItemDeleteListener : mOnItemDeleteListeners) {
            if (null == onItemDeleteListener) {
                CpLog.e(TAG, "onItemDeleteListener is null!");
                continue;
            }

            onItemDeleteListener.onItemDelete(walletBean);
        }
    }
}
