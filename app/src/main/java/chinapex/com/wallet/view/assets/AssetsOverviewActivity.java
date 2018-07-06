package chinapex.com.wallet.view.assets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.AssetsOverviewRecyclerViewAdapter;
import chinapex.com.wallet.adapter.SpacesItemDecoration;
import chinapex.com.wallet.base.BaseActivity;
import chinapex.com.wallet.bean.BalanceBean;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.executor.TaskController;
import chinapex.com.wallet.executor.callback.IGetAccountStateCallback;
import chinapex.com.wallet.executor.callback.IGetNep5BalanceCallback;
import chinapex.com.wallet.executor.runnable.GetAccountState;
import chinapex.com.wallet.executor.runnable.GetNep5Balance;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.DensityUtil;
import chinapex.com.wallet.utils.GsonUtils;
import chinapex.com.wallet.utils.PhoneUtils;
import chinapex.com.wallet.utils.ToastUtils;
import chinapex.com.wallet.view.wallet.CreateWalletActivity;
import chinapex.com.wallet.view.wallet.ImportWalletActivity;

public class AssetsOverviewActivity extends BaseActivity implements
        AssetsOverviewRecyclerViewAdapter.OnItemClickListener, IGetAccountStateCallback,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, IGetNep5BalanceCallback {

    private static final String TAG = AssetsOverviewActivity.class.getSimpleName();
    private TextView mTv_assets_overview_wallet_name;
    private TextView mTv_assets_overview_wallet_address;
    private WalletBean mWalletBean;
    private RecyclerView mRv_assets_overview;
    private List<BalanceBean> mBalanceBeans;
    private AssetsOverviewRecyclerViewAdapter mAssetsOverviewRecyclerViewAdapter;
    private SwipeRefreshLayout mSl_assets_overview_rv;
    private ImageButton mIb_assets_overview_ellipsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assets_overview);

        initView();
        initData();
        getAssetsBalance();
    }

    private void initView() {
        mTv_assets_overview_wallet_name = (TextView) findViewById(R.id
                .tv_assets_overview_wallet_name);
        mTv_assets_overview_wallet_address = (TextView) findViewById(R.id
                .tv_assets_overview_wallet_address);
        mRv_assets_overview = (RecyclerView) findViewById(R.id.rv_assets_overview);
        mSl_assets_overview_rv = (SwipeRefreshLayout) findViewById(R.id.sl_assets_overview_rv);
        mIb_assets_overview_ellipsis = (ImageButton) findViewById(R.id.ib_assets_overview_ellipsis);

        mSl_assets_overview_rv.setColorSchemeColors(this.getResources().getColor(R.color
                .colorPrimary));
        mSl_assets_overview_rv.setOnRefreshListener(this);
        mIb_assets_overview_ellipsis.setOnClickListener(this);

        // 复制地址
        mTv_assets_overview_wallet_address.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        if (null == intent) {
            CpLog.e(TAG, "intent is null!");
            return;
        }

        mWalletBean = (WalletBean) intent.getParcelableExtra(Constant.WALLET_BEAN);

        mTv_assets_overview_wallet_name.setText(mWalletBean.getWalletName());
        mTv_assets_overview_wallet_address.setText(mWalletBean.getWalletAddr());

        mRv_assets_overview.setLayoutManager(new LinearLayoutManager(ApexWalletApplication
                .getInstance(), LinearLayoutManager.VERTICAL, false));
        mBalanceBeans = getBalanceBeans();
        mAssetsOverviewRecyclerViewAdapter = new AssetsOverviewRecyclerViewAdapter(mBalanceBeans);
        mAssetsOverviewRecyclerViewAdapter.setOnItemClickListener(this);

        int space = DensityUtil.dip2px(this, 8);
        mRv_assets_overview.addItemDecoration(new SpacesItemDecoration(space));
        mRv_assets_overview.setAdapter(mAssetsOverviewRecyclerViewAdapter);
    }

    private void getAssetsBalance() {
        TaskController.getInstance().submit(new GetAccountState(mWalletBean.getWalletAddr(), this));
        TaskController.getInstance().submit(new GetNep5Balance(Constant.ASSETS_CPX, mWalletBean
                .getWalletAddr(), this));
    }

    @Override
    public void onItemClick(int position) {
        BalanceBean balanceBean = mBalanceBeans.get(position);
        if (null == balanceBean) {
            CpLog.e(TAG, "balanceBean is null!");
            return;
        }

        HashMap<String, Parcelable> parcelables = new HashMap<>();
        parcelables.put(Constant.WALLET_BEAN, mWalletBean);
        parcelables.put(Constant.BALANCE_BEAN, balanceBean);
        startActivityParcelables(BalanceDetailActivity.class, false, parcelables);

    }

    // 设置默认添加的资产
    private List<BalanceBean> getBalanceBeans() {
        mBalanceBeans = new ArrayList<>();

        List<BalanceBean> nep5Assets = getNep5Assets();
        if (null != nep5Assets && !nep5Assets.isEmpty()) {
            mBalanceBeans.addAll(nep5Assets);
        }

        List<BalanceBean> globalAssets = getGlobalAssets();
        if (null != globalAssets && !globalAssets.isEmpty()) {
            mBalanceBeans.addAll(globalAssets);
        }

        return mBalanceBeans;
    }

    private List<BalanceBean> getNep5Assets() {
        if (null == mWalletBean) {
            CpLog.e(TAG, "getNep5Assets() -> mWalletBean is null!");
            return null;
        }

        String assetsNep5Json = mWalletBean.getAssetsNep5Json();
        List<String> assetsNep5 = GsonUtils.json2List(assetsNep5Json, String.class);
        if (null == assetsNep5 || assetsNep5.isEmpty()) {
            CpLog.e(TAG, "assetsNep5 is null or empty!");
            return null;
        }

        ArrayList<BalanceBean> balanceBeans = new ArrayList<>();
        for (String assetNep5 : assetsNep5) {
            BalanceBean balanceBean = new BalanceBean();
            balanceBean.setMapState(Constant.MAP_STATE_UNFINISHED);
            balanceBean.setAssetsID(assetNep5);
            balanceBean.setAssetType(Constant.ASSET_TYPE_NEP5);
            balanceBean.setAssetDecimal(8);
            balanceBean.setAssetsValue("0");
            balanceBeans.add(balanceBean);
        }
        return balanceBeans;
    }

    private List<BalanceBean> getGlobalAssets() {
        if (null == mWalletBean) {
            CpLog.e(TAG, "getGlobalAssets() -> mWalletBean is null!");
            return null;
        }

        String assetsJson = mWalletBean.getAssetsJson();
        List<String> assets = GsonUtils.json2List(assetsJson, String.class);
        if (null == assets || assets.isEmpty()) {
            CpLog.e(TAG, "assets is null or empty!");
            return null;
        }

        ArrayList<BalanceBean> balanceBeans = new ArrayList<>();
        for (String asset : assets) {
            BalanceBean balanceBean = new BalanceBean();
            balanceBean.setMapState(Constant.MAP_STATE_UNFINISHED);
            balanceBean.setAssetsID(asset);
            balanceBean.setAssetType(Constant.ASSET_TYPE_GLOBAL);
            balanceBean.setAssetDecimal(8);
            balanceBean.setAssetsValue("0");
            balanceBeans.add(balanceBean);
        }
        return balanceBeans;
    }

    @Override
    public void getNep5Balance(Map<String, BalanceBean> balanceBeans) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSl_assets_overview_rv.setRefreshing(false);
            }
        });

        if (null == mBalanceBeans || mBalanceBeans.isEmpty()) {
            CpLog.e(TAG, "mBalanceBeans is null or empty!");
            return;
        }

        if (null == balanceBeans || balanceBeans.isEmpty()) {
            CpLog.w(TAG, "getNep5Balance() -> the current assets is null!");
            for (BalanceBean balanceBean0 : mBalanceBeans) {
                if (null == balanceBean0) {
                    CpLog.e(TAG, "balanceBean0 is null!");
                    continue;
                }

                if (Constant.ASSET_TYPE_NEP5.equals(balanceBean0.getAssetType())) {
                    balanceBean0.setAssetsValue("0");
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAssetsOverviewRecyclerViewAdapter.notifyDataSetChanged();
                }
            });

            return;
        }

        for (BalanceBean balanceBean : mBalanceBeans) {
            if (null == balanceBean) {
                CpLog.e(TAG, "balanceBean is null!");
                continue;
            }

            String assetsID = balanceBean.getAssetsID();
            if (balanceBeans.containsKey(assetsID)) {
                balanceBean.setAssetsValue(balanceBeans.get(assetsID).getAssetsValue());
            } else {
                if (Constant.ASSET_TYPE_NEP5.equals(balanceBean.getAssetType())) {
                    balanceBean.setAssetsValue("0");
                }
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAssetsOverviewRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void assetsBalance(Map<String, BalanceBean> balanceBeans) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSl_assets_overview_rv.setRefreshing(false);
            }
        });

        if (null == mBalanceBeans || mBalanceBeans.isEmpty()) {
            CpLog.e(TAG, "mBalanceBeans is null or empty!");
            return;
        }

        if (null == balanceBeans || balanceBeans.isEmpty()) {
            CpLog.w(TAG, "assetsBalance() -> the current assets is null!");
            for (BalanceBean balanceBean0 : mBalanceBeans) {
                if (null == balanceBean0) {
                    CpLog.e(TAG, "balanceBean0 is null!");
                    continue;
                }

                if (Constant.ASSET_TYPE_GLOBAL.equals(balanceBean0.getAssetType())) {
                    balanceBean0.setAssetsValue("0");
                }
            }

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAssetsOverviewRecyclerViewAdapter.notifyDataSetChanged();
                }
            });

            return;
        }

        for (BalanceBean balanceBean : mBalanceBeans) {
            if (null == balanceBean) {
                CpLog.e(TAG, "balanceBean is null!");
                continue;
            }

            String assetsID = balanceBean.getAssetsID();
            if (balanceBeans.containsKey(assetsID)) {
                balanceBean.setAssetsValue(balanceBeans.get(assetsID).getAssetsValue());
            } else {
                if (Constant.ASSET_TYPE_GLOBAL.equals(balanceBean.getAssetType())) {
                    balanceBean.setAssetsValue("0");
                }
            }
        }

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAssetsOverviewRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRefresh() {
        if (null == mWalletBean) {
            CpLog.e(TAG, "mWalletBean");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSl_assets_overview_rv.setRefreshing(false);
                }
            });
            return;
        }

        TaskController.getInstance().submit(new GetAccountState(mWalletBean.getWalletAddr(), this));
        TaskController.getInstance().submit(new GetNep5Balance(Constant.ASSETS_CPX, mWalletBean
                .getWalletAddr(), this));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_assets_overview_ellipsis:
                CpLog.i(TAG, "添加资产");
                break;
            case R.id.tv_assets_overview_wallet_address:
                String copyAddr = mTv_assets_overview_wallet_address.getText().toString().trim();
                PhoneUtils.copy2Clipboard(ApexWalletApplication.getInstance(), copyAddr);
                ToastUtils.getInstance().showToast("钱包地址已复制");
            default:
                break;
        }
    }

}
