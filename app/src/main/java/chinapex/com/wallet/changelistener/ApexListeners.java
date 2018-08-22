package chinapex.com.wallet.changelistener;

import java.util.ArrayList;
import java.util.List;

import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.bean.eth.EthWallet;
import chinapex.com.wallet.changelistener.eth.OnEthAddListener;
import chinapex.com.wallet.utils.CpLog;

/**
 * Created by SteelCabbage on 2018/4/9 0009.
 */

public class ApexListeners {

    private static final String TAG = ApexListeners.class.getSimpleName();

    private List<OnWalletDeleteListener> mOnWalletDeleteListeners;
    private List<OnWalletAddListener> mOnWalletAddListeners;
    private List<OnWalletBackupStateUpdateListener> mOnWalletBackupStateUpdateListeners;
    private List<OnItemNameUpdateListener> mOnItemNameUpdateListeners;
    private List<OnTxStateUpdateListener> mOnTxStateUpdateListeners;
    private List<OnAssetJsonUpdateListener> mOnAssetJsonUpdateListeners;
    // eth
    private List<OnEthAddListener> mOnEthAddListeners;

    private ApexListeners() {

    }

    private static class ApexListenersHolder {
        private static final ApexListeners sApexListeners = new ApexListeners();
    }

    public static ApexListeners getInstance() {
        return ApexListenersHolder.sApexListeners;
    }

    public void doInit() {
        mOnWalletDeleteListeners = new ArrayList<>();
        mOnWalletAddListeners = new ArrayList<>();
        mOnWalletBackupStateUpdateListeners = new ArrayList<>();
        mOnItemNameUpdateListeners = new ArrayList<>();
        mOnTxStateUpdateListeners = new ArrayList<>();
        mOnAssetJsonUpdateListeners = new ArrayList<>();
        mOnEthAddListeners = new ArrayList<>();
    }

    public void onDestroy() {
        mOnWalletDeleteListeners.clear();
        mOnWalletAddListeners.clear();
        mOnWalletBackupStateUpdateListeners.clear();
        mOnItemNameUpdateListeners.clear();
        mOnTxStateUpdateListeners.clear();
        mOnAssetJsonUpdateListeners.clear();
        mOnEthAddListeners.clear();

        mOnWalletDeleteListeners = null;
        mOnWalletAddListeners = null;
        mOnWalletBackupStateUpdateListeners = null;
        mOnItemNameUpdateListeners = null;
        mOnTxStateUpdateListeners = null;
        mOnAssetJsonUpdateListeners = null;
        mOnEthAddListeners = null;
    }

    public void addOnItemDeleteListener(OnWalletDeleteListener onWalletDeleteListener) {
        if (null == mOnWalletDeleteListeners || null == onWalletDeleteListener) {
            CpLog.e(TAG, "1:mOnWalletDeleteListeners or OnWalletDeleteListener is null!");
            return;
        }

        mOnWalletDeleteListeners.add(onWalletDeleteListener);
    }

    public void removeOnItemDeleteListener(OnWalletDeleteListener onWalletDeleteListener) {
        if (null == mOnWalletDeleteListeners || null == onWalletDeleteListener) {
            CpLog.e(TAG, "0:mOnWalletDeleteListeners or OnWalletDeleteListener is null!");
            return;
        }

        mOnWalletDeleteListeners.remove(onWalletDeleteListener);
    }

    public void addOnWalletAddListener(OnWalletAddListener onWalletAddListener) {
        if (null == mOnWalletAddListeners || null == onWalletAddListener) {
            CpLog.e(TAG, "1:mOnWalletAddListeners or onWalletAddListener is null!");
            return;
        }

        mOnWalletAddListeners.add(onWalletAddListener);
    }

    public void removeOnWalletAddListener(OnWalletAddListener onWalletAddListener) {
        if (null == mOnWalletAddListeners || null == onWalletAddListener) {
            CpLog.e(TAG, "0:mOnWalletAddListeners or onWalletAddListener is null!");
            return;
        }

        mOnWalletAddListeners.remove(onWalletAddListener);
    }

    public void addOnItemStateUpdateListener(OnWalletBackupStateUpdateListener onWalletBackupStateUpdateListener) {
        if (null == mOnWalletBackupStateUpdateListeners || null == onWalletBackupStateUpdateListener) {
            CpLog.e(TAG, "1:mOnWalletBackupStateUpdateListeners or onWalletBackupStateUpdateListener is null!");
            return;
        }

        mOnWalletBackupStateUpdateListeners.add(onWalletBackupStateUpdateListener);
    }

    public void removeOnItemStateUpdateListener(OnWalletBackupStateUpdateListener
                                                        onWalletBackupStateUpdateListener) {
        if (null == mOnWalletBackupStateUpdateListeners || null == onWalletBackupStateUpdateListener) {
            CpLog.e(TAG, "0:mOnWalletBackupStateUpdateListeners or onWalletBackupStateUpdateListener is null!");
            return;
        }

        mOnWalletBackupStateUpdateListeners.remove(onWalletBackupStateUpdateListener);
    }

    public void addOnItemNameUpdateListener(OnItemNameUpdateListener onItemNameUpdateListener) {
        if (null == mOnItemNameUpdateListeners || null == onItemNameUpdateListener) {
            CpLog.e(TAG, "1:mOnItemNameUpdateListeners or onItemNameUpdateListener is null!");
            return;
        }

        mOnItemNameUpdateListeners.add(onItemNameUpdateListener);
    }

    public void removeOnItemNameUpdateListener(OnItemNameUpdateListener onItemNameUpdateListener) {
        if (null == mOnItemNameUpdateListeners || null == onItemNameUpdateListener) {
            CpLog.e(TAG, "0:mOnItemNameUpdateListeners or onItemNameUpdateListener is null!");
            return;
        }

        mOnItemNameUpdateListeners.remove(onItemNameUpdateListener);
    }

    public void addOnTxStateUpdateListener(OnTxStateUpdateListener onTxStateUpdateListener) {
        if (null == mOnTxStateUpdateListeners || null == onTxStateUpdateListener) {
            CpLog.e(TAG, "1:mOnTxStateUpdateListeners or onTxStateUpdateListener is null!");
            return;
        }

        mOnTxStateUpdateListeners.add(onTxStateUpdateListener);
    }

    public void removeOnTxStateUpdateListener(OnTxStateUpdateListener onTxStateUpdateListener) {
        if (null == mOnTxStateUpdateListeners || null == onTxStateUpdateListener) {
            CpLog.e(TAG, "0:mOnTxStateUpdateListeners or onTxStateUpdateListener is null!");
            return;
        }

        mOnTxStateUpdateListeners.remove(onTxStateUpdateListener);
    }

    public void addOnAssetsUpdateListener(OnAssetJsonUpdateListener onAssetJsonUpdateListener) {
        if (null == mOnAssetJsonUpdateListeners || null == onAssetJsonUpdateListener) {
            CpLog.e(TAG, "1:mOnAssetJsonUpdateListeners or onAssetJsonUpdateListener is null!");
            return;
        }

        mOnAssetJsonUpdateListeners.add(onAssetJsonUpdateListener);
    }

    public void removeOnAssetsUpdateListener(OnAssetJsonUpdateListener onAssetJsonUpdateListener) {
        if (null == mOnAssetJsonUpdateListeners || null == onAssetJsonUpdateListener) {
            CpLog.e(TAG, "0:mOnAssetJsonUpdateListeners or onAssetJsonUpdateListener is null!");
            return;
        }

        mOnAssetJsonUpdateListeners.remove(onAssetJsonUpdateListener);
    }

    public void addOnEthAddListener(OnEthAddListener onEthAddListener) {
        if (null == mOnEthAddListeners || null == onEthAddListener) {
            CpLog.e(TAG, "1:mOnEthAddListeners or onEthAddListener is null!");
            return;
        }

        mOnEthAddListeners.add(onEthAddListener);
    }

    public void removeOnEthAddListener(OnEthAddListener onEthAddListener) {
        if (null == mOnEthAddListeners || null == onEthAddListener) {
            CpLog.e(TAG, "0:mOnEthAddListeners or onEthAddListener is null!");
            return;
        }

        mOnEthAddListeners.remove(onEthAddListener);
    }

    public void notifyWalletDelete(WalletBean walletBean) {
        if (null == mOnWalletDeleteListeners) {
            CpLog.e(TAG, "mOnWalletDeleteListeners is null!");
            return;
        }

        for (OnWalletDeleteListener onWalletDeleteListener : mOnWalletDeleteListeners) {
            if (null == onWalletDeleteListener) {
                CpLog.e(TAG, "OnWalletDeleteListener is null!");
                continue;
            }

            onWalletDeleteListener.onWalletDelete(walletBean);
        }
    }

    public void notifyWalletAdd(WalletBean walletBean) {
        if (null == mOnWalletAddListeners) {
            CpLog.e(TAG, "mOnWalletAddListeners is null!");
            return;
        }

        for (OnWalletAddListener onWalletAddListener : mOnWalletAddListeners) {
            if (null == onWalletAddListener) {
                CpLog.e(TAG, "onWalletAddListener is null!");
                continue;
            }

            onWalletAddListener.onWalletAdd(walletBean);
        }
    }

    public void notifyWalletBackupStateUpdate(WalletBean walletBean) {
        if (null == mOnWalletBackupStateUpdateListeners) {
            CpLog.e(TAG, "mOnWalletBackupStateUpdateListeners is null!");
            return;
        }

        for (OnWalletBackupStateUpdateListener onWalletBackupStateUpdateListener : mOnWalletBackupStateUpdateListeners) {
            if (null == onWalletBackupStateUpdateListener) {
                CpLog.e(TAG, "onWalletBackupStateUpdateListener is null!");
                continue;
            }

            onWalletBackupStateUpdateListener.onWalletBackupStateUpdate(walletBean);
        }
    }

    public void notifyItemNameUpdate(WalletBean walletBean) {
        if (null == mOnItemNameUpdateListeners) {
            CpLog.e(TAG, "mOnItemNameUpdateListeners is null!");
            return;
        }

        for (OnItemNameUpdateListener onItemNameUpdateListener : mOnItemNameUpdateListeners) {
            if (null == onItemNameUpdateListener) {
                CpLog.e(TAG, "onItemNameUpdateListener is null!");
                continue;
            }

            onItemNameUpdateListener.OnItemNameUpdate(walletBean);
        }
    }

    public void notifyTxStateUpdate(String txID, int state, long txTime) {
        if (null == mOnTxStateUpdateListeners) {
            CpLog.e(TAG, "mOnTxStateUpdateListeners is null!");
            return;
        }

        for (OnTxStateUpdateListener onTxStateUpdateListener : mOnTxStateUpdateListeners) {
            if (null == onTxStateUpdateListener) {
                CpLog.e(TAG, "onTxStateUpdateListener is null!");
                continue;
            }

            onTxStateUpdateListener.onTxStateUpdate(txID, state, txTime);
        }
    }

    public void notifyAssetJsonUpdate(WalletBean walletBean) {
        if (null == mOnAssetJsonUpdateListeners) {
            CpLog.e(TAG, "mOnAssetJsonUpdateListeners is null!");
            return;
        }

        for (OnAssetJsonUpdateListener onAssetJsonUpdateListener : mOnAssetJsonUpdateListeners) {
            if (null == onAssetJsonUpdateListener) {
                CpLog.e(TAG, "onAssetJsonUpdateListener is null!");
                continue;
            }

            onAssetJsonUpdateListener.onAssetJsonUpdate(walletBean);
        }
    }

    public void notifyEthAdd(EthWallet ethWallet) {
        if (null == mOnEthAddListeners) {
            CpLog.e(TAG, "mOnEthAddListeners is null!");
            return;
        }

        for (OnEthAddListener onEthAddListener : mOnEthAddListeners) {
            if (null == onEthAddListener) {
                CpLog.e(TAG, "onEthAddListener is null!");
                continue;
            }

            onEthAddListener.onEthAdd(ethWallet);
        }
    }

}
