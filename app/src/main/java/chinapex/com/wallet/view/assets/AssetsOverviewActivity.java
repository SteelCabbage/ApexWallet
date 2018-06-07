package chinapex.com.wallet.view.assets;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.AssetsOverviewRecyclerViewAdapter;
import chinapex.com.wallet.adapter.SpacesItemDecoration;
import chinapex.com.wallet.base.BaseActivity;
import chinapex.com.wallet.bean.BalanceBean;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.executor.TaskController;
import chinapex.com.wallet.executor.callback.IGetAccountStateCallback;
import chinapex.com.wallet.executor.runnable.GetAccountState;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.DensityUtil;

public class AssetsOverviewActivity extends BaseActivity implements
        AssetsOverviewRecyclerViewAdapter.OnItemClickListener, IGetAccountStateCallback {

    private static final String TAG = AssetsOverviewActivity.class.getSimpleName();
    private TextView mTv_assets_overview_wallet_name;
    private TextView mTv_assets_overview_wallet_address;
    private WalletBean mWalletBean;
    private RecyclerView mRv_assets_overview;
    private List<BalanceBean> mBalanceBeans;
    private AssetsOverviewRecyclerViewAdapter mAssetsOverviewRecyclerViewAdapter;

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

        mRv_assets_overview.setLayoutManager(new LinearLayoutManager(ApexWalletApplication
                .getInstance(), LinearLayoutManager.VERTICAL, false));
        mBalanceBeans = getBalanceBeans();
        mAssetsOverviewRecyclerViewAdapter = new AssetsOverviewRecyclerViewAdapter(mBalanceBeans);
        mAssetsOverviewRecyclerViewAdapter.setOnItemClickListener(this);

        int space = DensityUtil.dip2px(this, 5);
        mRv_assets_overview.addItemDecoration(new SpacesItemDecoration(space));
        mRv_assets_overview.setAdapter(mAssetsOverviewRecyclerViewAdapter);
    }

    private void initData() {
        Intent intent = getIntent();
        if (null == intent) {
            CpLog.e(TAG, "intent is null!");
            return;
        }

        mWalletBean = (WalletBean) intent.getParcelableExtra(Constant.WALLET_BEAN);
        mTv_assets_overview_wallet_name.setText(String.valueOf(Constant.WALLET_NAME + mWalletBean
                .getWalletName()));
        mTv_assets_overview_wallet_address.setText(mWalletBean.getWalletAddr());
    }

    private void getAssetsBalance() {
        TaskController.getInstance().submit(new GetAccountState(mWalletBean.getWalletAddr(), this));
    }

    @Override
    public void onItemClick(int position) {

    }

    private List<BalanceBean> getBalanceBeans() {
        ArrayList<BalanceBean> balanceBeans = new ArrayList<>();

        for (int i = 0; i < 2; i++) {
            BalanceBean balanceBean = new BalanceBean();
            balanceBean.setMapState(0);
            balanceBean.setAssetsID(Constant.ASSETS_NEO);
            balanceBean.setAssetsValue(1.6789 + i + "");
            balanceBeans.add(balanceBean);
        }
        for (int i = 0; i < 2; i++) {
            BalanceBean balanceBean = new BalanceBean();
            balanceBean.setMapState(0);
            balanceBean.setAssetsID(Constant.ASSETS_NEO_GAS);
            balanceBean.setAssetsValue(1.9876 + i + "");
            balanceBeans.add(balanceBean);
        }
        for (int i = 0; i < 3; i++) {
            BalanceBean balanceBean = new BalanceBean();
            balanceBean.setMapState(0);
            balanceBean.setAssetsID(Constant.ASSETS_CPX);
            balanceBean.setAssetsValue(1.5566 + i + "");
            balanceBeans.add(balanceBean);
        }
        return balanceBeans;
    }

    @Override
    public void assetsBalance(List<BalanceBean> balanceBeans) {
        if (null == balanceBeans || balanceBeans.isEmpty()) {
            CpLog.e(TAG, "assetsBalance is null or empty!");
            return;
        }

        mBalanceBeans.clear();
        mBalanceBeans.addAll(balanceBeans);
    }
}
