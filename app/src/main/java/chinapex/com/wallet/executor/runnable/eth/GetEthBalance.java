package chinapex.com.wallet.executor.runnable.eth;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;

import chinapex.com.wallet.bean.AssetBean;
import chinapex.com.wallet.bean.BalanceBean;
import chinapex.com.wallet.bean.request.RequestGetEthRpc;
import chinapex.com.wallet.bean.response.ResponseGetEthRpcResult;
import chinapex.com.wallet.executor.callback.eth.IGetEthBalanceCallback;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.net.INetCallback;
import chinapex.com.wallet.net.OkHttpClientManager;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.GsonUtils;
import chinapex.com.wallet.utils.WalletUtils;

/**
 * Created by SteelCabbage on 2018/5/17 0017.
 */

public class GetEthBalance implements Runnable, INetCallback {

    private static final String TAG = GetEthBalance.class.getSimpleName();

    private String mAddress;
    private IGetEthBalanceCallback mIGetEthBalanceCallback;

    public GetEthBalance(String address, IGetEthBalanceCallback IGetEthBalanceCallback) {
        mAddress = address;
        mIGetEthBalanceCallback = IGetEthBalanceCallback;
    }

    @Override
    public void run() {
        if (TextUtils.isEmpty(mAddress) || null == mIGetEthBalanceCallback) {
            CpLog.e(TAG, "run() -> mAddress or mIGetEthBalanceCallback is null！");
            return;
        }

        RequestGetEthRpc requestGetEthRpc = new RequestGetEthRpc();
        requestGetEthRpc.setJsonrpc("2.0");
        requestGetEthRpc.setMethod("eth_getBalance");
        requestGetEthRpc.setId(1);
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add(mAddress);
        arrayList.add("latest");
        requestGetEthRpc.setParams(arrayList);

        OkHttpClientManager.getInstance().postJsonByAuth(Constant.URL_CLI_ETH, GsonUtils.toJsonStr(requestGetEthRpc), this);
    }

    @Override
    public void onSuccess(int statusCode, String msg, String result) {
        ResponseGetEthRpcResult responseGetEthRpcResult = GsonUtils.json2Bean(result, ResponseGetEthRpcResult.class);
        if (null == responseGetEthRpcResult) {
            CpLog.e(TAG, "responseGetEthRpcResult is null!");
            mIGetEthBalanceCallback.getEthBalance(null);
            return;
        }

        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication.getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            mIGetEthBalanceCallback.getEthBalance(null);
            return;
        }

        AssetBean assetBean = apexWalletDbDao.queryAssetByHash(Constant.TABLE_ETH_ASSETS, Constant.ASSETS_ETH);
        if (null == assetBean) {
            CpLog.e(TAG, "assetBean is null!");
            mIGetEthBalanceCallback.getEthBalance(null);
            return;
        }

        String ethBalance = WalletUtils.toDecString(responseGetEthRpcResult.getResult(), assetBean.getPrecision());
        if (TextUtils.isEmpty(ethBalance)) {
            CpLog.e(TAG, "ethBalance is null or empty!");
            mIGetEthBalanceCallback.getEthBalance(null);
            return;
        }

        HashMap<String, BalanceBean> balanceBeans = new HashMap<>();
        BalanceBean balanceBean = new BalanceBean();
        balanceBean.setMapState(Constant.MAP_STATE_UNFINISHED);
        balanceBean.setWalletType(Constant.WALLET_TYPE_ETH);
        balanceBean.setAssetsID(Constant.ASSETS_ETH);
        balanceBean.setAssetSymbol(assetBean.getSymbol());
        balanceBean.setAssetType(Constant.ASSET_TYPE_ETH);
        balanceBean.setAssetDecimal(Integer.valueOf(assetBean.getPrecision()));
        balanceBean.setAssetsValue(ethBalance);
        balanceBeans.put(Constant.ASSETS_ETH, balanceBean);

        mIGetEthBalanceCallback.getEthBalance(balanceBeans);
    }

    @Override
    public void onFailed(int failedCode, String msg) {
        CpLog.e(TAG, "onFailed() -> msg:" + msg);
        mIGetEthBalanceCallback.getEthBalance(null);
    }
}
