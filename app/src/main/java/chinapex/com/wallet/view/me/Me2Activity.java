package chinapex.com.wallet.view.me;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.MeRecyclerViewAdapter;
import chinapex.com.wallet.adapter.SpacesItemDecoration;
import chinapex.com.wallet.base.BaseActivity;
import chinapex.com.wallet.bean.NeoWallet;
import chinapex.com.wallet.changelistener.ApexListeners;
import chinapex.com.wallet.changelistener.OnNeoDeleteListener;
import chinapex.com.wallet.changelistener.OnItemNameUpdateListener;
import chinapex.com.wallet.changelistener.OnItemStateUpdateListener;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.DensityUtil;

public class Me2Activity extends BaseActivity implements MeRecyclerViewAdapter
        .OnItemClickListener, OnItemNameUpdateListener, OnItemStateUpdateListener,
        OnNeoDeleteListener {
    private static final String TAG = Me2Activity.class.getSimpleName();
    private List<NeoWallet> mNeoWallets;
    private RecyclerView mRv_me2;
    private MeRecyclerViewAdapter mMeRecyclerViewAdapter;
    private TextView mTv_me2_title;
    private NeoWallet mCurrentClickedNeoWallet;
    private String mShowTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me2);

        initView();
        initData();
    }

    private void initView() {
        mRv_me2 = (RecyclerView) findViewById(R.id.rv_me2);
        mTv_me2_title = (TextView) findViewById(R.id.tv_me2_title);
    }

    private void initData() {
        Intent intent = getIntent();
        if (null == intent) {
            CpLog.e(TAG, "intent is null!");
            return;
        }

        mShowTag = intent.getStringExtra(Constant.ME_2_SHOULD_BE_SHOW);
        if (TextUtils.isEmpty(mShowTag)) {
            CpLog.e(TAG, "showTag is null!");
            return;
        }

        switch (mShowTag) {
            case Constant.ME_2_SHOULD_BE_SHOW_MANAGE_WALLET:
                mTv_me2_title.setText(ApexWalletApplication.getInstance().getResources()
                        .getString(R.string.manage_wallets));
                break;
            case Constant.ME_2_SHOULD_BE_SHOW_TX_RECORDS:
                mTv_me2_title.setText(ApexWalletApplication.getInstance().getResources()
                        .getString(R.string.transaction_records));
                break;
            default:
                break;
        }

        mNeoWallets = initWalletBeans();
        mRv_me2.setLayoutManager(new LinearLayoutManager(ApexWalletApplication.getInstance(),
                LinearLayoutManager.VERTICAL, false));
        int space = DensityUtil.dip2px(ApexWalletApplication.getInstance(), 8);
        mRv_me2.addItemDecoration(new SpacesItemDecoration(space));
        mMeRecyclerViewAdapter = new MeRecyclerViewAdapter(mNeoWallets);
        mMeRecyclerViewAdapter.setOnItemClickListener(this);
        mRv_me2.setAdapter(mMeRecyclerViewAdapter);

        ApexListeners.getInstance().addOnItemStateUpdateListener(this);
        ApexListeners.getInstance().addOnItemDeleteListener(this);
        ApexListeners.getInstance().addOnItemNameUpdateListener(this);
    }

    private List<NeoWallet> initWalletBeans() {
        List<NeoWallet> neoWallets = new ArrayList<>();
        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            return neoWallets;
        }

        neoWallets.addAll(apexWalletDbDao.queryNeoWallets());
        if (neoWallets.isEmpty()) {
            CpLog.w(TAG, "local have no wallet");
            return neoWallets;
        }

        for (NeoWallet neoWallet : neoWallets) {
            if (null == neoWallet) {
                CpLog.e(TAG, "neoWallet is null!");
                continue;
            }

            switch (mShowTag) {
                case Constant.ME_2_SHOULD_BE_SHOW_MANAGE_WALLET:
                    neoWallet.setSelectedTag(Constant.SELECTED_TAG_MANAGER_WALLET);
                    break;
                case Constant.ME_2_SHOULD_BE_SHOW_TX_RECORDS:
                    neoWallet.setSelectedTag(Constant.SELECTED_TAG_TRANSACTION_RECORED);
                    break;
                default:
                    break;
            }
        }

        return neoWallets;
    }

    @Override
    public void onItemClick(int position) {
        mCurrentClickedNeoWallet = mNeoWallets.get(position);
        if (null == mCurrentClickedNeoWallet) {
            CpLog.e(TAG, "mCurrentClickedNeoWallet is null!");
            return;
        }

        switch (mShowTag) {
            case Constant.ME_2_SHOULD_BE_SHOW_MANAGE_WALLET:
                toMeManagerDetailFragment();
                break;
            case Constant.ME_2_SHOULD_BE_SHOW_TX_RECORDS:
                toMeTransactionRecordFragment();
                break;
            default:
                break;
        }
    }

    private void toMeManagerDetailFragment() {
        startActivityBundle(Me3Activity.class,
                false,
                Constant.ME_MANAGER_DETAIL_BUNDLE,
                Constant.ME_SKIP_ACTIVITY_FRAGMENT_TAG, Constant.FRAGMENT_TAG_ME_MANAGE_DETAIL,
                Constant.PARCELABLE_WALLET_BEAN_MANAGE_DETAIL, mCurrentClickedNeoWallet);
    }

    private void toMeTransactionRecordFragment() {
        startActivityBundle(Me3Activity.class,
                false,
                Constant.ME_MANAGER_DETAIL_BUNDLE,
                Constant.ME_SKIP_ACTIVITY_FRAGMENT_TAG,
                Constant.FRAGMENT_TAG_ME_TRANSACTION_RECORD,
                Constant.PARCELABLE_WALLET_BEAN_MANAGE_DETAIL, mCurrentClickedNeoWallet);
    }

    // 备份钱包后回调
    @Override
    public void OnItemStateUpdate(NeoWallet neoWallet) {
        if (null == neoWallet) {
            CpLog.e(TAG, "neoWallet is null!");
            return;
        }

        for (NeoWallet neoWalletTmp : mNeoWallets) {
            if (null == neoWalletTmp) {
                CpLog.e(TAG, "neoWalletTmp is null!");
                continue;
            }

            if (neoWalletTmp.equals(neoWallet)) {
                neoWalletTmp.setBackupState(neoWallet.getBackupState());
            }
        }

        mMeRecyclerViewAdapter.notifyDataSetChanged();
    }

    // 删除钱包时回调
    @Override
    public void onNeoDelete(NeoWallet neoWallet) {
        if (null == neoWallet) {
            CpLog.e(TAG, "onNeoDelete() -> neoWallet is null!");
            return;
        }

        if (!mNeoWallets.contains(neoWallet)) {
            CpLog.e(TAG, "onNeoDelete() -> this wallet not exist!");
            return;
        }

        mNeoWallets.remove(neoWallet);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mMeRecyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    // 修改钱包名称回调
    @Override
    public void OnItemNameUpdate(NeoWallet neoWallet) {
        if (null == neoWallet) {
            CpLog.e(TAG, "neoWallet is null!");
            return;
        }

        for (NeoWallet neoWalletTmp : mNeoWallets) {
            if (null == neoWalletTmp) {
                CpLog.e(TAG, "neoWalletTmp is null!");
                continue;
            }

            if (neoWalletTmp.equals(neoWallet)) {
                neoWalletTmp.setName(neoWallet.getName());
            }
        }

        mMeRecyclerViewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ApexListeners.getInstance().removeOnItemStateUpdateListener(this);
        ApexListeners.getInstance().removeOnItemDeleteListener(this);
        ApexListeners.getInstance().removeOnItemNameUpdateListener(this);
    }
}
