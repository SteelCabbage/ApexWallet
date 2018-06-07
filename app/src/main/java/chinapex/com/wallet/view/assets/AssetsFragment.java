package chinapex.com.wallet.view.assets;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.zxing.activity.CaptureActivity;

import java.util.ArrayList;
import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.AssetsRecyclerViewAdapter;
import chinapex.com.wallet.adapter.SpacesItemDecoration;
import chinapex.com.wallet.adapter.DrawerMenuRecyclerViewAdapter;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.bean.DrawerMenu;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.bean.request.RequestGetAccountState;
import chinapex.com.wallet.bean.response.ResponseGetAccountState;
import chinapex.com.wallet.changelistener.ApexListeners;
import chinapex.com.wallet.changelistener.OnItemAddListener;
import chinapex.com.wallet.changelistener.OnItemDeleteListener;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.net.INetCallback;
import chinapex.com.wallet.net.OkHttpClientManager;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.GsonUtils;
import chinapex.com.wallet.view.wallet.CreateWalletActivity;

/**
 * Created by SteelCabbage on 2018/5/21 0021.
 */

public class AssetsFragment extends BaseFragment implements AssetsRecyclerViewAdapter
        .OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AssetsRecyclerViewAdapter
        .OnItemLongClickListener, OnItemDeleteListener, OnItemAddListener, DrawerLayout
        .DrawerListener, DrawerMenuRecyclerViewAdapter.DrawerMenuOnItemClickListener, View
        .OnClickListener {

    private static final String TAG = AssetsFragment.class.getSimpleName();
    private RecyclerView mRv_assets;
    private List<WalletBean> mWalletBeans;
    private SwipeRefreshLayout mSl_assets_rv;
    private AssetsRecyclerViewAdapter mAssetsRecyclerViewAdapter;
    private DrawerLayout mDl_assets;
    private RecyclerView mRv_assets_drawer_menu;
    private DrawerMenuRecyclerViewAdapter mDrawerMenuRecyclerViewAdapter;
    private ImageButton mIb_assets_ellipsis;
    private LinearLayout mLl_assets_drawer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {

        View fragment_assets = inflater.inflate(R.layout.fragment_assets, container, false);
        return fragment_assets;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        mRv_assets = (RecyclerView) view.findViewById(R.id.rv_assets);
        mSl_assets_rv = (SwipeRefreshLayout) view.findViewById(R.id.sl_assets_rv);

        mRv_assets.setLayoutManager(new LinearLayoutManager(ApexWalletApplication.getInstance(),
                LinearLayoutManager.VERTICAL,
                false));
        mWalletBeans = getData();
        mAssetsRecyclerViewAdapter = new AssetsRecyclerViewAdapter(mWalletBeans);
        mAssetsRecyclerViewAdapter.setOnItemClickListener(this);
        mAssetsRecyclerViewAdapter.setOnItemLongClickListener(this);

        int space = 20;
        mRv_assets.addItemDecoration(new SpacesItemDecoration(space));

        mRv_assets.setAdapter(mAssetsRecyclerViewAdapter);

        mSl_assets_rv.setColorSchemeColors(this.getActivity().getResources().getColor(R.color
                .colorPrimary));
        mSl_assets_rv.setOnRefreshListener(this);

        // 侧滑布局
        mLl_assets_drawer = view.findViewById(R.id.ll_assets_drawer);

        // 省略号按钮
        mIb_assets_ellipsis = view.findViewById(R.id.ib_assets_ellipsis);
        mIb_assets_ellipsis.setOnClickListener(this);

        // 侧滑菜单
        mDl_assets = (DrawerLayout) view.findViewById(R.id.dl_assets);
        mRv_assets_drawer_menu = view.findViewById(R.id.rv_assets_drawer_menu);

        mDl_assets.addDrawerListener(this);
        mRv_assets_drawer_menu.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        mDrawerMenuRecyclerViewAdapter = new DrawerMenuRecyclerViewAdapter(getAssetsMenus());
        mDrawerMenuRecyclerViewAdapter.setDrawerMenuOnItemClickListener(this);
        mRv_assets_drawer_menu.setAdapter(mDrawerMenuRecyclerViewAdapter);

    }

    private void initData() {
        ApexListeners.getInstance().addOnItemDeleteListener(this);
        ApexListeners.getInstance().addOnItemAddListener(this);
    }

    @Override
    public void onItemClick(int position) {
        CpLog.i(TAG, "onItemClick:" + position);
        startActivityParcelable(WalletDetailActivity.class, false, Constant.WALLET_BEAN,
                mWalletBeans.get(position));
    }

    @Override
    public void onItemLongClick(int position) {
        CpLog.i(TAG, "长按了onItemLongClick:" + position);
        // TODO: 2018/5/24 0024 长按左滑删除逻辑
    }

    private List<WalletBean> getData() {
        List<WalletBean> walletBeans = new ArrayList<>();
        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null！");
            return walletBeans;
        }

        walletBeans.addAll(apexWalletDbDao.queryWalletBeans(Constant.TABLE_APEX_WALLET));
        return walletBeans;
    }


    @Override
    public void onRefresh() {
        getBalance(mWalletBeans);
    }

    private void getBalance(List<WalletBean> walletBeans) {
        if (null == walletBeans || walletBeans.isEmpty()) {
            CpLog.e(TAG, "walletBeans is null or empty!");
            AssetsFragment.this.getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mSl_assets_rv.setRefreshing(false);
                }
            });
            return;
        }

        for (final WalletBean walletBean : walletBeans) {

            final RequestGetAccountState requestGetAccountState = new RequestGetAccountState();
            requestGetAccountState.setJsonrpc("2.0");
            requestGetAccountState.setMethod("getaccountstate");
            requestGetAccountState.setId(1);
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(walletBean.getWalletAddr());
            requestGetAccountState.setParams(arrayList);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    OkHttpClientManager.getInstance().postJson(Constant.URL_CLI, GsonUtils.toJsonStr
                            (requestGetAccountState), new INetCallback() {
                        @Override
                        public void onSuccess(int statusCode, String msg, String result) {
                            CpLog.i(TAG, "onSuccess");
                            ResponseGetAccountState responseGetAccountState = GsonUtils.json2Bean
                                    (result, ResponseGetAccountState.class);
                            List<ResponseGetAccountState.ResultBean.BalancesBean> balances =
                                    responseGetAccountState.getResult().getBalances();
                            if (null == balances || balances.isEmpty()) {
                                walletBean.setBalance(0.0);
                            } else {
                                for (ResponseGetAccountState.ResultBean.BalancesBean balance :
                                        balances) {
                                    if (Constant.ASSETS_NEO.equals(balance.getAsset())) {
                                        String balanceValue = balance.getValue();
                                        walletBean.setBalance(Double.valueOf(balanceValue));
                                    }
                                }
                            }

                            AssetsFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSl_assets_rv.setRefreshing(false);
                                    mAssetsRecyclerViewAdapter.notifyDataSetChanged();
                                }
                            });

                        }

                        @Override
                        public void onFailed(int failedCode, String msg) {
                            CpLog.e(TAG, "onFailed");
                            AssetsFragment.this.getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mSl_assets_rv.setRefreshing(false);
                                }
                            });
                        }
                    });
                }
            }).start();

        }
    }


    @Override
    public void onItemDelete(WalletBean walletBean) {
        if (null == walletBean) {
            CpLog.e(TAG, "onItemDelete() -> walletBean is null!");
            return;
        }

        mWalletBeans.remove(walletBean);
        mAssetsRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemAdd(WalletBean walletBean) {
        if (null == walletBean) {
            CpLog.e(TAG, "onItemAdd() -> walletBean is null!");
            return;
        }

        mWalletBeans.add(walletBean);
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAssetsRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerClosed(@NonNull View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {

    }

    private List<DrawerMenu> getAssetsMenus() {
        ArrayList<DrawerMenu> drawerMenus = new ArrayList<>();
        //drawable数组要用TypedArray获取
        TypedArray ar = getResources().obtainTypedArray(R.array.assets_drawer_icons);
        String[] menuTexts = getResources().getStringArray(R.array.assets_drawer_texts);

        for (int i = 0; i < ar.length(); i++) {
            DrawerMenu drawerMenu = new DrawerMenu();
            drawerMenu.setMenuIcon(ar.getResourceId(i, 0));
            drawerMenu.setMenuText(menuTexts[i]);
            drawerMenus.add(drawerMenu);
        }
        ar.recycle();
        return drawerMenus;
    }

    @Override
    public void drawerMenuOnItemClick(int position) {
        switch (position) {
            case 0:
                CpLog.i(TAG, "添加资产");
                break;
            case 1:
                CpLog.i(TAG, "创建钱包");
                startActivity(CreateWalletActivity.class, false);
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_assets_ellipsis:
                if (!mDl_assets.isDrawerOpen(mLl_assets_drawer)) {
                    mDl_assets.openDrawer(mLl_assets_drawer);
                }
                break;
            default:
                break;
        }
    }
}
