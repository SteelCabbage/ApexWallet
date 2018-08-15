package chinapex.com.wallet.changelistener;

import java.util.ArrayList;
import java.util.List;

import chinapex.com.wallet.bean.NeoWallet;
import chinapex.com.wallet.bean.eth.EthWallet;
import chinapex.com.wallet.changelistener.eth.OnEthAddListener;
import chinapex.com.wallet.changelistener.eth.OnEthStateUpdateListener;
import chinapex.com.wallet.utils.CpLog;

/**
 * Created by SteelCabbage on 2018/4/9 0009.
 */

public class ApexListeners {

    private static final String TAG = ApexListeners.class.getSimpleName();

    private List<OnItemDeleteListener> mOnItemDeleteListeners;
    private List<OnItemAddListener> mOnItemAddListeners;
    private List<OnItemStateUpdateListener> mOnItemStateUpdateListeners;
    private List<OnItemNameUpdateListener> mOnItemNameUpdateListeners;
    private List<OnTxStateUpdateListener> mOnTxStateUpdateListeners;
    private List<OnAssetsUpdateListener> mOnAssetsUpdateListeners;
    // eth
    private List<OnEthAddListener> mOnEthAddListeners;
    private List<OnEthStateUpdateListener> mOnEthStateUpdateListeners;

    private ApexListeners() {

    }

    private static class ApexListenersHolder {
        private static final ApexListeners sApexListeners = new ApexListeners();
    }

    public static ApexListeners getInstance() {
        return ApexListenersHolder.sApexListeners;
    }

    public void doInit() {
        mOnItemDeleteListeners = new ArrayList<>();
        mOnItemAddListeners = new ArrayList<>();
        mOnItemStateUpdateListeners = new ArrayList<>();
        mOnItemNameUpdateListeners = new ArrayList<>();
        mOnTxStateUpdateListeners = new ArrayList<>();
        mOnAssetsUpdateListeners = new ArrayList<>();
        mOnEthAddListeners = new ArrayList<>();
        mOnEthStateUpdateListeners = new ArrayList<>();
    }

    public void onDestroy() {
        mOnItemDeleteListeners.clear();
        mOnItemAddListeners.clear();
        mOnItemStateUpdateListeners.clear();
        mOnItemNameUpdateListeners.clear();
        mOnTxStateUpdateListeners.clear();
        mOnAssetsUpdateListeners.clear();
        mOnEthAddListeners.clear();
        mOnEthStateUpdateListeners.clear();

        mOnItemDeleteListeners = null;
        mOnItemAddListeners = null;
        mOnItemStateUpdateListeners = null;
        mOnItemNameUpdateListeners = null;
        mOnTxStateUpdateListeners = null;
        mOnAssetsUpdateListeners = null;
        mOnEthAddListeners = null;
        mOnEthStateUpdateListeners = null;
    }

    public void addOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        if (null == mOnItemDeleteListeners || null == onItemDeleteListener) {
            CpLog.e(TAG, "1:mOnItemDeleteListeners or OnItemDeleteListener is null!");
            return;
        }

        mOnItemDeleteListeners.add(onItemDeleteListener);
    }

    public void removeOnItemDeleteListener(OnItemDeleteListener onItemDeleteListener) {
        if (null == mOnItemDeleteListeners || null == onItemDeleteListener) {
            CpLog.e(TAG, "0:mOnItemDeleteListeners or OnItemDeleteListener is null!");
            return;
        }

        mOnItemDeleteListeners.remove(onItemDeleteListener);
    }

    public void addOnItemAddListener(OnItemAddListener onItemAddListener) {
        if (null == mOnItemAddListeners || null == onItemAddListener) {
            CpLog.e(TAG, "1:mOnItemAddListeners or onItemAddListener is null!");
            return;
        }

        mOnItemAddListeners.add(onItemAddListener);
    }

    public void removeOnItemAddListener(OnItemAddListener onItemAddListener) {
        if (null == mOnItemAddListeners || null == onItemAddListener) {
            CpLog.e(TAG, "0:mOnItemAddListeners or onItemAddListener is null!");
            return;
        }

        mOnItemAddListeners.remove(onItemAddListener);
    }

    public void addOnItemStateUpdateListener(OnItemStateUpdateListener onItemStateUpdateListener) {
        if (null == mOnItemStateUpdateListeners || null == onItemStateUpdateListener) {
            CpLog.e(TAG, "1:mOnItemStateUpdateListeners or onItemStateUpdateListener is null!");
            return;
        }

        mOnItemStateUpdateListeners.add(onItemStateUpdateListener);
    }

    public void removeOnItemStateUpdateListener(OnItemStateUpdateListener
                                                        onItemStateUpdateListener) {
        if (null == mOnItemStateUpdateListeners || null == onItemStateUpdateListener) {
            CpLog.e(TAG, "0:mOnItemStateUpdateListeners or onItemStateUpdateListener is null!");
            return;
        }

        mOnItemStateUpdateListeners.remove(onItemStateUpdateListener);
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

    public void addOnAssetsUpdateListener(OnAssetsUpdateListener onAssetsUpdateListener) {
        if (null == mOnAssetsUpdateListeners || null == onAssetsUpdateListener) {
            CpLog.e(TAG, "1:mOnAssetsUpdateListeners or onAssetsUpdateListener is null!");
            return;
        }

        mOnAssetsUpdateListeners.add(onAssetsUpdateListener);
    }

    public void removeOnAssetsUpdateListener(OnAssetsUpdateListener onAssetsUpdateListener) {
        if (null == mOnAssetsUpdateListeners || null == onAssetsUpdateListener) {
            CpLog.e(TAG, "0:mOnAssetsUpdateListeners or onAssetsUpdateListener is null!");
            return;
        }

        mOnAssetsUpdateListeners.remove(onAssetsUpdateListener);
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

    public void addOnEthStateUpdateListener(OnEthStateUpdateListener onEthStateUpdateListener) {
        if (null == mOnEthStateUpdateListeners || null == onEthStateUpdateListener) {
            CpLog.e(TAG, "1:mOnEthStateUpdateListeners or onEthStateUpdateListener is null!");
            return;
        }

        mOnEthStateUpdateListeners.add(onEthStateUpdateListener);
    }

    public void removeOnEthStateUpdateListener(OnEthStateUpdateListener onEthStateUpdateListener) {
        if (null == mOnEthStateUpdateListeners || null == onEthStateUpdateListener) {
            CpLog.e(TAG, "0:mOnEthStateUpdateListeners or onEthStateUpdateListener is null!");
            return;
        }

        mOnEthStateUpdateListeners.remove(onEthStateUpdateListener);
    }

    public void notifyItemDelete(NeoWallet neoWallet) {
        if (null == mOnItemDeleteListeners) {
            CpLog.e(TAG, "mOnItemDeleteListeners is null!");
            return;
        }

        for (OnItemDeleteListener onItemDeleteListener : mOnItemDeleteListeners) {
            if (null == onItemDeleteListener) {
                CpLog.e(TAG, "OnItemDeleteListener is null!");
                continue;
            }

            onItemDeleteListener.onItemDelete(neoWallet);
        }
    }

    public void notifyItemAdd(NeoWallet neoWallet) {
        if (null == mOnItemAddListeners) {
            CpLog.e(TAG, "mOnItemAddListeners is null!");
            return;
        }

        for (OnItemAddListener onItemAddListener : mOnItemAddListeners) {
            if (null == onItemAddListener) {
                CpLog.e(TAG, "onItemAddListener is null!");
                continue;
            }

            onItemAddListener.onItemAdd(neoWallet);
        }
    }

    public void notifyItemStateUpdate(NeoWallet neoWallet) {
        if (null == mOnItemStateUpdateListeners) {
            CpLog.e(TAG, "mOnItemStateUpdateListeners is null!");
            return;
        }

        for (OnItemStateUpdateListener onItemStateUpdateListener : mOnItemStateUpdateListeners) {
            if (null == onItemStateUpdateListener) {
                CpLog.e(TAG, "onItemStateUpdateListener is null!");
                continue;
            }

            onItemStateUpdateListener.OnItemStateUpdate(neoWallet);
        }
    }

    public void notifyItemNameUpdate(NeoWallet neoWallet) {
        if (null == mOnItemNameUpdateListeners) {
            CpLog.e(TAG, "mOnItemNameUpdateListeners is null!");
            return;
        }

        for (OnItemNameUpdateListener onItemNameUpdateListener : mOnItemNameUpdateListeners) {
            if (null == onItemNameUpdateListener) {
                CpLog.e(TAG, "onItemNameUpdateListener is null!");
                continue;
            }

            onItemNameUpdateListener.OnItemNameUpdate(neoWallet);
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

    public void notifyAssetsUpdate(NeoWallet neoWallet) {
        if (null == mOnAssetsUpdateListeners) {
            CpLog.e(TAG, "mOnAssetsUpdateListeners is null!");
            return;
        }

        for (OnAssetsUpdateListener onAssetsUpdateListener : mOnAssetsUpdateListeners) {
            if (null == onAssetsUpdateListener) {
                CpLog.e(TAG, "onAssetsUpdateListener is null!");
                continue;
            }

            onAssetsUpdateListener.onAssetsUpdate(neoWallet);
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

    public void notifyEthStateUpdate(EthWallet ethWallet) {
        if (null == mOnEthStateUpdateListeners) {
            CpLog.e(TAG, "mOnEthStateUpdateListeners is null!");
            return;
        }

        for (OnEthStateUpdateListener onEthStateUpdateListener : mOnEthStateUpdateListeners) {
            if (null == onEthStateUpdateListener) {
                CpLog.e(TAG, "onEthStateUpdateListener is null!");
                continue;
            }

            onEthStateUpdateListener.onEthStateUpdate(ethWallet);
        }
    }
}
