package chinapex.com.wallet.view.assets;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.AssetsRecyclerViewAdapter;
import chinapex.com.wallet.adapter.DrawerMenu1RecyclerViewAdapter;
import chinapex.com.wallet.adapter.DrawerMenu2RecyclerViewAdapter;
import chinapex.com.wallet.adapter.EmptyAdapter;
import chinapex.com.wallet.adapter.SpacesItemDecoration;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.bean.DrawerMenu;
import chinapex.com.wallet.bean.neo.NeoWallet;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.bean.eth.EthWallet;
import chinapex.com.wallet.changelistener.ApexListeners;
import chinapex.com.wallet.changelistener.OnAssetJsonUpdateListener;
import chinapex.com.wallet.changelistener.OnNeoDeleteListener;
import chinapex.com.wallet.changelistener.OnItemNameUpdateListener;
import chinapex.com.wallet.changelistener.OnNeoAddListener;
import chinapex.com.wallet.changelistener.eth.OnEthAddListener;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.DensityUtil;
import chinapex.com.wallet.view.wallet.CreateWalletActivity;
import chinapex.com.wallet.view.wallet.ImportWalletActivity;

/**
 * Created by SteelCabbage on 2018/5/21 0021.
 */

public class AssetsFragment extends BaseFragment implements AssetsRecyclerViewAdapter
        .OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, AssetsRecyclerViewAdapter
        .OnItemLongClickListener, OnNeoDeleteListener, OnNeoAddListener, DrawerLayout
        .DrawerListener, DrawerMenu1RecyclerViewAdapter.DrawerMenu1OnItemClickListener, View
        .OnClickListener, OnItemNameUpdateListener, TextWatcher, OnAssetJsonUpdateListener,
        OnEthAddListener, DrawerMenu2RecyclerViewAdapter.DrawerMenu2OnItemClickListener {

    private static final String TAG = AssetsFragment.class.getSimpleName();
    private RecyclerView mRv_assets;
    private int mWalletType;
    private List<WalletBean> mWalletBeans;
    private List<WalletBean> mSearchWalletBeans;
    private List<WalletBean> mNeoWallets;
    private List<WalletBean> mEthWallets;
    private SwipeRefreshLayout mSl_assets_rv;
    private AssetsRecyclerViewAdapter mAssetsRecyclerViewAdapter;
    private DrawerLayout mDl_assets;
    private RecyclerView mRv_assets_drawer_menu1;
    private RecyclerView mRv_assets_drawer_menu2;
    private DrawerMenu1RecyclerViewAdapter mDrawerMenu1RecyclerViewAdapter;
    private ImageButton mIb_assets_ellipsis;
    private LinearLayout mLl_assets_drawer;
    private EditText mEt_assets_search;
    private ImageButton mIb_assets_cancel;
    private EmptyAdapter mEmptyAdapter;
    private DrawerMenu2RecyclerViewAdapter mDrawerMenu2RecyclerViewAdapter;
    private TextView mTv_assets_wallet_type;

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
        mEt_assets_search = view.findViewById(R.id.et_assets_search);
        mIb_assets_cancel = view.findViewById(R.id.ib_assets_cancel);
        mTv_assets_wallet_type = view.findViewById(R.id.tv_assets_wallet_type);

        // 搜索功能
        mEt_assets_search.addTextChangedListener(this);
        mIb_assets_cancel.setOnClickListener(this);

        mRv_assets.setLayoutManager(new LinearLayoutManager(ApexWalletApplication.getInstance(),
                LinearLayoutManager.VERTICAL, false));
        mWalletBeans = getData();
        mAssetsRecyclerViewAdapter = new AssetsRecyclerViewAdapter(mWalletBeans);
        mAssetsRecyclerViewAdapter.setOnItemClickListener(this);
        mAssetsRecyclerViewAdapter.setOnItemLongClickListener(this);

        int space = DensityUtil.dip2px(getActivity(), 8);
        mRv_assets.addItemDecoration(new SpacesItemDecoration(space));

//        mRv_assets.setAdapter(mAssetsRecyclerViewAdapter);
        mEmptyAdapter = new EmptyAdapter(mAssetsRecyclerViewAdapter, ApexWalletApplication
                .getInstance(), R.layout.recyclerview_empty_wallet);
        mRv_assets.setAdapter(mEmptyAdapter);

        mSl_assets_rv.setColorSchemeColors(this.getActivity().getResources().getColor(R.color
                .c_1253BF));
        mSl_assets_rv.setOnRefreshListener(this);

        // 侧滑布局
        mLl_assets_drawer = view.findViewById(R.id.ll_assets_drawer);

        // 省略号按钮
        mIb_assets_ellipsis = view.findViewById(R.id.ib_assets_ellipsis);
        mIb_assets_ellipsis.setOnClickListener(this);

        // 侧滑菜单
        mDl_assets = (DrawerLayout) view.findViewById(R.id.dl_assets);
        mRv_assets_drawer_menu1 = view.findViewById(R.id.rv_assets_drawer_menu1);
        mRv_assets_drawer_menu2 = view.findViewById(R.id.rv_assets_drawer_menu2);

        mDl_assets.addDrawerListener(this);
        mRv_assets_drawer_menu1.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));
        mRv_assets_drawer_menu2.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false));

        mDrawerMenu1RecyclerViewAdapter = new DrawerMenu1RecyclerViewAdapter(getDrawerMenu1());
        mDrawerMenu2RecyclerViewAdapter = new DrawerMenu2RecyclerViewAdapter(getDrawerMenu2());
        mDrawerMenu1RecyclerViewAdapter.setDrawerMenu1OnItemClickListener(this);
        mDrawerMenu2RecyclerViewAdapter.setDrawerMenu2OnItemClickListener(this);
        mRv_assets_drawer_menu1.setAdapter(mDrawerMenu1RecyclerViewAdapter);
        mRv_assets_drawer_menu2.setAdapter(mDrawerMenu2RecyclerViewAdapter);

    }

    private void initData() {
        // set title: wallet type
        mTv_assets_wallet_type.setText(Constant.WALLET_TYPE_NAME_NEO);

        // neo
        ApexListeners.getInstance().addOnItemDeleteListener(this);
        ApexListeners.getInstance().addOnItemAddListener(this);
        ApexListeners.getInstance().addOnItemNameUpdateListener(this);
        ApexListeners.getInstance().addOnAssetsUpdateListener(this);
        // eth
        ApexListeners.getInstance().addOnEthAddListener(this);

        mSearchWalletBeans = new ArrayList<>();
        mSearchWalletBeans.addAll(mWalletBeans);
    }

    @Override
    public void onItemClick(int position) {
        startActivityParcelable(AssetsOverviewActivity.class, false, Constant.WALLET_BEAN,
                mWalletBeans.get(position));
    }

    @Override
    public void onItemLongClick(int position) {
        CpLog.i(TAG, "长按了onItemLongClick:" + position);
        // 预留长按左滑删除逻辑
    }

    private List<WalletBean> getData() {
        mWalletBeans = new ArrayList<>();
        mNeoWallets = new ArrayList<>();
        mEthWallets = new ArrayList<>();
        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null！");
            return mWalletBeans;
        }

        mNeoWallets.addAll(apexWalletDbDao.queryWallets(Constant.TABLE_NEO_WALLET));
        mEthWallets.addAll(apexWalletDbDao.queryWallets(Constant.TABLE_ETH_WALLET));
        mWalletBeans.addAll(mNeoWallets);
        return mWalletBeans;
    }


    @Override
    public void onRefresh() {
        // 预留后续刷新功能
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mSl_assets_rv.setRefreshing(false);
            }
        });
    }

    @Override
    public void onNeoDelete(WalletBean walletBean) {
        if (null == walletBean) {
            CpLog.e(TAG, "onNeoDelete() -> walletBean is null!");
            return;
        }

        if (!mWalletBeans.contains(walletBean)) {
            CpLog.e(TAG, "onNeoDelete() -> this wallet not exist!");
            return;
        }
        mWalletBeans.remove(walletBean);
        mSearchWalletBeans.remove(walletBean);
        mAssetsRecyclerViewAdapter.notifyDataSetChanged();
        mEmptyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onNeoAdd(NeoWallet neoWallet) {
        if (null == neoWallet) {
            CpLog.e(TAG, "onNeoAdd() -> neoWallet is null!");
            return;
        }

        if (mNeoWallets.contains(neoWallet)) {
            CpLog.e(TAG, "onNeoAdd() -> this wallet has existed!");
            return;
        }

        mNeoWallets.add(neoWallet);

        if (mWalletType != Constant.WALLET_TYPE_NEO) {
            CpLog.w(TAG, "mWalletType:" + mWalletType + ",no need to update UI!");
            return;
        }

        mWalletBeans.add(neoWallet);
        mSearchWalletBeans.add(neoWallet);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAssetsRecyclerViewAdapter.notifyDataSetChanged();
                mEmptyAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onEthAdd(EthWallet ethWallet) {
        if (null == ethWallet) {
            CpLog.e(TAG, "onEthAdd() -> ethWallet is null!");
            return;
        }

        if (mEthWallets.contains(ethWallet)) {
            CpLog.e(TAG, "onEthAdd() -> this wallet has existed!");
            return;
        }

        mEthWallets.add(ethWallet);

        if (mWalletType != Constant.WALLET_TYPE_ETH) {
            CpLog.w(TAG, "mWalletType:" + mWalletType + ",no need to update UI!");
            return;
        }

        mWalletBeans.add(ethWallet);
        mSearchWalletBeans.add(ethWallet);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mAssetsRecyclerViewAdapter.notifyDataSetChanged();
                mEmptyAdapter.notifyDataSetChanged();
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

    private List<DrawerMenu> getDrawerMenu1() {
        ArrayList<DrawerMenu> drawerMenus = new ArrayList<>();
        //drawable数组要用TypedArray获取
        TypedArray ar = getResources().obtainTypedArray(R.array.assets_drawer1_icons);
        String[] menuTexts = getResources().getStringArray(R.array.assets_drawer1_texts);

        for (int i = 0; i < ar.length(); i++) {
            DrawerMenu drawerMenu = new DrawerMenu();
            drawerMenu.setMenuIcon(ar.getResourceId(i, 0));
            drawerMenu.setMenuText(menuTexts[i]);
            drawerMenus.add(drawerMenu);
        }
        ar.recycle();
        return drawerMenus;
    }

    private List<DrawerMenu> getDrawerMenu2() {
        ArrayList<DrawerMenu> drawerMenus = new ArrayList<>();
        //drawable数组要用TypedArray获取
        TypedArray ar = getResources().obtainTypedArray(R.array.assets_drawer2_icons);
        String[] menuTexts = getResources().getStringArray(R.array.assets_drawer2_texts);

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
    public void drawerMenu1OnItemClick(int position) {
        switch (position) {
            case 0:
                // neo
                mWalletType = Constant.WALLET_TYPE_NEO;
                mTv_assets_wallet_type.setText(Constant.WALLET_TYPE_NAME_NEO);

                changeWalletType(mNeoWallets);
                break;
            case 1:
                // eth
                mWalletType = Constant.WALLET_TYPE_ETH;
                mTv_assets_wallet_type.setText(Constant.WALLET_TYPE_NAME_ETH);

                changeWalletType(mEthWallets);
                break;
            default:
                break;
        }
    }

    @Override
    public void drawerMenu2OnItemClick(int position) {
        switch (position) {
            case 0:
                // 创建钱包
                startActivity(CreateWalletActivity.class, false);
                break;
            case 1:
                // 导入钱包
                startActivity(ImportWalletActivity.class, false);
                break;
            default:
                break;
        }
        closeDrawer(mLl_assets_drawer);
    }

    private void changeWalletType(List<? extends WalletBean> walletBeans) {
        mWalletBeans.clear();
        mSearchWalletBeans.clear();
        mWalletBeans.addAll(walletBeans);
        mSearchWalletBeans.addAll(walletBeans);
        mAssetsRecyclerViewAdapter.notifyDataSetChanged();
        mEmptyAdapter.notifyDataSetChanged();
        mEt_assets_search.getText().clear();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_assets_ellipsis:
                openDrawer(mLl_assets_drawer);
                break;
            case R.id.ib_assets_cancel:
                mEt_assets_search.getText().clear();
                break;
            default:
                break;
        }
    }

    private void openDrawer(View drawer) {
        if (!mDl_assets.isDrawerOpen(drawer)) {
            mDl_assets.openDrawer(drawer);
        }
    }

    private void closeDrawer(View drawer) {
        if (mDl_assets.isDrawerOpen(drawer)) {
            mDl_assets.closeDrawer(drawer);
        }
    }

    @Override
    public void OnItemNameUpdate(WalletBean walletBean) {
        if (null == walletBean) {
            CpLog.e(TAG, "walletBean is null!");
            return;
        }

        for (WalletBean walletBeanTmp : mWalletBeans) {
            if (null == walletBeanTmp) {
                CpLog.e(TAG, "walletBeanTmp is null!");
                continue;
            }

            if (walletBeanTmp.equals(walletBean)) {
                walletBeanTmp.setName(walletBean.getName());
                WalletBean searchTmpWallet = mSearchWalletBeans.get(mWalletBeans.indexOf
                        (walletBeanTmp));
                if (null == searchTmpWallet) {
                    CpLog.e(TAG, "searchTmpWallet is null!");
                    continue;
                }

                searchTmpWallet.setName(walletBean.getName());
            }
        }

        mAssetsRecyclerViewAdapter.notifyDataSetChanged();
        mEmptyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAssetJsonUpdate(WalletBean walletBean) {
        if (null == walletBean) {
            CpLog.e(TAG, "walletBean is null!");
            return;
        }

        if (null == mWalletBeans || mWalletBeans.isEmpty()) {
            CpLog.e(TAG, "mWalletBeans is null or empty!");
            return;
        }

        for (WalletBean walletBeanTmp : mWalletBeans) {
            if (null == walletBeanTmp) {
                CpLog.e(TAG, "walletBeanTmp is null!");
                continue;
            }

            if (walletBeanTmp.equals(walletBean)) {
                walletBeanTmp.setColorAssetJson(walletBean.getColorAssetJson());
                walletBeanTmp.setAssetJson(walletBean.getAssetJson());
            }
        }

        mAssetsRecyclerViewAdapter.notifyDataSetChanged();
        mEmptyAdapter.notifyDataSetChanged();
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mWalletBeans.clear();
        mWalletBeans.addAll(mSearchWalletBeans);

        if (TextUtils.isEmpty(s)) {
            CpLog.w(TAG, "onTextChanged() -> is empty!");
            mIb_assets_cancel.setVisibility(View.INVISIBLE);
            mAssetsRecyclerViewAdapter.notifyDataSetChanged();
            mEmptyAdapter.notifyDataSetChanged();
            return;
        }

        mIb_assets_cancel.setVisibility(View.VISIBLE);
        Iterator<WalletBean> iterator = mWalletBeans.iterator();
        while (iterator.hasNext()) {
            WalletBean walletBean = iterator.next();
            if (null == walletBean) {
                CpLog.e(TAG, "walletBean is null!");
                continue;
            }

            if (!walletBean.getAddress().contains(s)) {
                iterator.remove();
            }
        }

        mAssetsRecyclerViewAdapter.notifyDataSetChanged();
        mEmptyAdapter.notifyDataSetChanged();
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ApexListeners.getInstance().removeOnItemDeleteListener(this);
        ApexListeners.getInstance().removeOnItemAddListener(this);
        ApexListeners.getInstance().removeOnItemNameUpdateListener(this);
        ApexListeners.getInstance().removeOnAssetsUpdateListener(this);

        // eth
        ApexListeners.getInstance().removeOnEthAddListener(this);
    }


}
