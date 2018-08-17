package chinapex.com.wallet.view.assets;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import chinapex.com.wallet.bean.AssetBean;
import chinapex.com.wallet.bean.BalanceBean;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.bean.neo.NeoWallet;
import chinapex.com.wallet.changelistener.ApexListeners;
import chinapex.com.wallet.executor.TaskController;
import chinapex.com.wallet.executor.callback.IGetAccountStateCallback;
import chinapex.com.wallet.executor.callback.IGetNep5BalanceCallback;
import chinapex.com.wallet.executor.runnable.GetAccountState;
import chinapex.com.wallet.executor.runnable.GetNep5Balance;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.DensityUtil;
import chinapex.com.wallet.utils.GsonUtils;
import chinapex.com.wallet.utils.PhoneUtils;
import chinapex.com.wallet.utils.ToastUtils;
import chinapex.com.wallet.view.dialog.AddAssetsDialog;

public class AssetsOverviewActivity extends BaseActivity implements
        AssetsOverviewRecyclerViewAdapter.OnItemClickListener, IGetAccountStateCallback,
        SwipeRefreshLayout.OnRefreshListener, View.OnClickListener, IGetNep5BalanceCallback,
        AddAssetsDialog.onCheckedAssetsListener {

    private static final String TAG = AssetsOverviewActivity.class.getSimpleName();
    private TextView mTv_assets_overview_wallet_name;
    private TextView mTv_assets_overview_wallet_address;
    private WalletBean mWalletBean;
    private RecyclerView mRv_assets_overview;
    private List<BalanceBean> mBalanceBeans;
    private AssetsOverviewRecyclerViewAdapter mAssetsOverviewRecyclerViewAdapter;
    private SwipeRefreshLayout mSl_assets_overview_rv;
    private ImageButton mIb_assets_overview_ellipsis;
    private List<String> mCurrentAssets;
    private int mCurrentWalletType;

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
                .c_1253BF));
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

        mWalletBean = intent.getParcelableExtra(Constant.WALLET_BEAN);
        switch (mWalletBean.getClass().getSimpleName()) {
            case "NeoWallet":
                mCurrentWalletType = Constant.WALLET_TYPE_NEO;
                break;
            case "EthWallet":
                mCurrentWalletType = Constant.WALLET_TYPE_ETH;
                break;
            default:
                CpLog.e(TAG, "mWalletBean is unknown type!");
                return;
        }

        mTv_assets_overview_wallet_name.setText(mWalletBean.getName());
        mTv_assets_overview_wallet_address.setText(mWalletBean.getAddress());

        mRv_assets_overview.setLayoutManager(new LinearLayoutManager(ApexWalletApplication
                .getInstance(), LinearLayoutManager.VERTICAL, false));
        mCurrentAssets = new ArrayList<>();
        mBalanceBeans = new ArrayList<>();
        getBalanceBeans();
        mAssetsOverviewRecyclerViewAdapter = new AssetsOverviewRecyclerViewAdapter(mBalanceBeans);
        mAssetsOverviewRecyclerViewAdapter.setOnItemClickListener(this);

        int space = DensityUtil.dip2px(this, 8);
        mRv_assets_overview.addItemDecoration(new SpacesItemDecoration(space));
        mRv_assets_overview.setAdapter(mAssetsOverviewRecyclerViewAdapter);
    }

    private void getAssetsBalance() {
        TaskController.getInstance().submit(new GetAccountState(mNeoWallet.getAddress(), this));

        if (null == mCurrentAssets || mCurrentAssets.isEmpty()) {
            CpLog.e(TAG, "mCurrentAssets is null or empty!");
            return;
        }

        for (String currentAsset : mCurrentAssets) {
            if (TextUtils.isEmpty(currentAsset)) {
                CpLog.e(TAG, "currentAsset is null or empty!");
                continue;
            }

            if (Constant.ASSETS_NEO.equals(currentAsset)
                    || Constant.ASSETS_NEO_GAS.equals(currentAsset)) {
                CpLog.w(TAG, "currentAsset is not nep5");
                continue;
            }

            TaskController.getInstance().submit(new GetNep5Balance(currentAsset, mNeoWallet
                    .getAddress(), this));
        }

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
    private void getBalanceBeans() {
        if (null == mBalanceBeans) {
            CpLog.e(TAG, "mBalanceBeans is null!");
            return;
        }

        List<BalanceBean> colorAssets = getColorAssets();
        if (null != colorAssets && !colorAssets.isEmpty()) {
            mBalanceBeans.addAll(colorAssets);
        }

        List<BalanceBean> globalAssets = getGlobalAssets();
        if (null != globalAssets && !globalAssets.isEmpty()) {
            mBalanceBeans.addAll(globalAssets);
        }
    }

    private List<BalanceBean> getColorAssets() {
        if (null == mWalletBean) {
            CpLog.e(TAG, "getColorAssets() -> mWalletBean is null!");
            return null;
        }

        String colorAssetJson = mWalletBean.getColorAssetJson();
        List<String> colorAssets = GsonUtils.json2List(colorAssetJson, String.class);
        if (null == colorAssets || colorAssets.isEmpty()) {
            CpLog.e(TAG, "colorAssets is null or empty!");
            return null;
        }

        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null");
            return null;
        }

        ArrayList<BalanceBean> balanceBeans = new ArrayList<>();
        for (String colorAsset : colorAssets) {
            AssetBean assetBean = apexWalletDbDao.queryAssetByHash(colorAsset);
            if (null == assetBean) {
                CpLog.e(TAG, "assetBean is null!");
                continue;
            }

            BalanceBean balanceBean = new BalanceBean();
            balanceBean.setMapState(Constant.MAP_STATE_UNFINISHED);
            balanceBean.setAssetsID(colorAsset);
            balanceBean.setAssetSymbol(assetBean.getSymbol());
            balanceBean.setAssetType(Constant.ASSET_TYPE_NEP5);
            balanceBean.setAssetDecimal(Integer.valueOf(assetBean.getPrecision()));
            balanceBean.setAssetsValue("0");
            balanceBeans.add(balanceBean);
            mCurrentAssets.add(colorAsset);
        }
        return balanceBeans;
    }

    private List<BalanceBean> getGlobalAssets() {
        if (null == mWalletBean) {
            CpLog.e(TAG, "getGlobalAssets() -> mWalletBean is null!");
            return null;
        }

        String assetJson = mWalletBean.getAssetJson();
        List<String> globalAssets = GsonUtils.json2List(assetJson, String.class);
        if (null == globalAssets || globalAssets.isEmpty()) {
            CpLog.e(TAG, "globalAssets is null or empty!");
            return null;
        }

        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null");
            return null;
        }

        ArrayList<BalanceBean> balanceBeans = new ArrayList<>();
        for (String globalAsset : globalAssets) {
            AssetBean assetBean = apexWalletDbDao.queryAssetByHash(globalAsset);
            if (null == assetBean) {
                CpLog.e(TAG, "assetBean is null!");
                continue;
            }

            BalanceBean balanceBean = new BalanceBean();
            balanceBean.setMapState(Constant.MAP_STATE_UNFINISHED);
            balanceBean.setAssetsID(globalAsset);
            balanceBean.setAssetSymbol(assetBean.getSymbol());
            balanceBean.setAssetType(Constant.ASSET_TYPE_GLOBAL);
            balanceBean.setAssetDecimal(Integer.valueOf(assetBean.getPrecision()));
            balanceBean.setAssetsValue("0");
            balanceBeans.add(balanceBean);
            if (mCurrentAssets.size() >= 1) {
                mCurrentAssets.add(1, globalAsset);
            } else {
                mCurrentAssets.add(globalAsset);
            }
        }
        return balanceBeans;
    }

    @Override
    public void getNep5Balance(Map<String, BalanceBean> balanceBeans) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (mSl_assets_overview_rv.isRefreshing()) {
                    mSl_assets_overview_rv.setRefreshing(false);
                }
            }
        });

        if (null == mBalanceBeans || mBalanceBeans.isEmpty()) {
            CpLog.e(TAG, "mBalanceBeans is null or empty!");
            return;
        }

        if (null == balanceBeans || balanceBeans.isEmpty()) {
            CpLog.w(TAG, "balanceBeans is null or empty!");
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
    public void getNeoGlobalAssetBalance(Map<String, BalanceBean> balanceBeans) {
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
            CpLog.w(TAG, "getNeoGlobalAssetBalance() -> the current assets is null!");
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
            CpLog.e(TAG, "mNeoWallet");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSl_assets_overview_rv.setRefreshing(false);
                }
            });
            return;
        }

        switch (mCurrentWalletType) {
            case Constant.WALLET_TYPE_NEO:
                TaskController.getInstance().submit(new GetAccountState(mWalletBean.getAddress(),
                        this));
                TaskController.getInstance().submit(new GetNep5Balance(Constant.ASSETS_CPX,
                        mWalletBean.getAddress(), this));
                break;
            case Constant.WALLET_TYPE_ETH:
                // TODO: 2018/8/16 0016 eth逻辑
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_assets_overview_ellipsis:
                showAddAssetsDialog();
                break;
            case R.id.tv_assets_overview_wallet_address:
                String copyAddr = mTv_assets_overview_wallet_address.getText().toString().trim();
                PhoneUtils.copy2Clipboard(ApexWalletApplication.getInstance(), copyAddr);
                ToastUtils.getInstance().showToast(ApexWalletApplication.getInstance()
                        .getResources().getString(R.string.wallet_address_copied));
            default:
                break;
        }
    }

    public void showAddAssetsDialog() {
        AddAssetsDialog addAssetsDialog = AddAssetsDialog.newInstance();
        addAssetsDialog.setOnCheckedAssetsListener(this);
        addAssetsDialog.setCurrentAssets(mCurrentAssets);
        addAssetsDialog.show(getFragmentManager(), "AddAssetsDialog");
    }

    @Override
    public void onCheckedAssets(List<String> checkedAssets) {
        if (null == checkedAssets) {
            CpLog.w(TAG, "checkedAssets is null or empty!");
            return;
        }

        List<String> colorAssets = new ArrayList<>();
        List<String> globalAssets = new ArrayList<>();

        for (String checkedAsset : checkedAssets) {
            if (TextUtils.isEmpty(checkedAsset)) {
                CpLog.e(TAG, "checkedAsset is null!");
                continue;
            }

            if (Constant.ASSETS_NEO.equals(checkedAsset)
                    || Constant.ASSETS_NEO_GAS.equals(checkedAsset)) {
                globalAssets.add(checkedAsset);
            } else {
                colorAssets.add(checkedAsset);
            }
        }

        mWalletBean.setAssetJson(GsonUtils.toJsonStr(globalAssets));
        mWalletBean.setColorAssetJson(GsonUtils.toJsonStr(colorAssets));
        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            return;
        }

        switch (mCurrentWalletType) {
            case Constant.WALLET_TYPE_NEO:
                apexWalletDbDao.updateCheckedAssets(Constant.TABLE_NEO_WALLET, mWalletBean);
                break;
            case Constant.WALLET_TYPE_ETH:
                apexWalletDbDao.updateCheckedAssets(Constant.TABLE_ETH_WALLET, mWalletBean);
                break;
        }

        ApexListeners.getInstance().notifyAssetsUpdate(mWalletBean);

        if (null == mBalanceBeans || null == mCurrentAssets) {
            CpLog.e(TAG, "mBalanceBeans or mCurrentAssets is null!");
            return;
        }

        mBalanceBeans.clear();
        mCurrentAssets.clear();

        if (checkedAssets.isEmpty()) {
            mAssetsOverviewRecyclerViewAdapter.notifyDataSetChanged();
            return;
        }

        getBalanceBeans();
        getAssetsBalance();
    }
}
