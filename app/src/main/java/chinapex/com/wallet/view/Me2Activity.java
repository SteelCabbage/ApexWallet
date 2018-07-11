package chinapex.com.wallet.view;

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
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.DensityUtil;

public class Me2Activity extends BaseActivity implements MeRecyclerViewAdapter.OnItemClickListener {
    private static final String TAG = Me2Activity.class.getSimpleName();
    private List<WalletBean> mWalletBeans;
    private RecyclerView mRv_me2;
    private MeRecyclerViewAdapter mMeRecyclerViewAdapter;
    private TextView mTv_me2_title;

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

        String showTag = intent.getStringExtra(Constant.ME_2_SHOULD_BE_SHOW);
        if (TextUtils.isEmpty(showTag)) {
            CpLog.e(TAG, "showTag is null!");
            return;
        }

        switch (showTag) {
            case Constant.ME_2_SHOULD_BE_SHOW_MANAGE_WALLET:
                mTv_me2_title.setText(ApexWalletApplication.getInstance().getResources()
                        .getString(R.string.manage_wallet));
                break;
            case Constant.ME_2_SHOULD_BE_SHOW_TX_RECORDS:
                mTv_me2_title.setText(ApexWalletApplication.getInstance().getResources()
                        .getString(R.string.transaction_records));
                break;
            default:
                break;
        }

        mWalletBeans = initWalletBeans(showTag);
        mRv_me2.setLayoutManager(new LinearLayoutManager(ApexWalletApplication.getInstance(),
                LinearLayoutManager.VERTICAL, false));
        int space = DensityUtil.dip2px(ApexWalletApplication.getInstance(), 8);
        mRv_me2.addItemDecoration(new SpacesItemDecoration(space));
        mMeRecyclerViewAdapter = new MeRecyclerViewAdapter(mWalletBeans);
        mMeRecyclerViewAdapter.setOnItemClickListener(this);
        mRv_me2.setAdapter(mMeRecyclerViewAdapter);
    }

    private List<WalletBean> initWalletBeans(String showTag) {
        List<WalletBean> walletBeans = new ArrayList<>();
        if (TextUtils.isEmpty(showTag)) {
            CpLog.e(TAG, "showTag is null!");
            return walletBeans;
        }

        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            return walletBeans;
        }

        walletBeans.addAll(apexWalletDbDao.queryWalletBeans(Constant.TABLE_APEX_WALLET));
        if (walletBeans.isEmpty()) {
            CpLog.w(TAG, "local have no wallet");
            return walletBeans;
        }

        for (WalletBean walletBean : walletBeans) {
            if (null == walletBean) {
                CpLog.e(TAG, "walletBean is null!");
                continue;
            }

            switch (showTag) {
                case Constant.ME_2_SHOULD_BE_SHOW_MANAGE_WALLET:
                    walletBean.setSelectedTag(Constant.SELECTED_TAG_MANAGER_WALLET);
                    break;
                case Constant.ME_2_SHOULD_BE_SHOW_TX_RECORDS:
                    walletBean.setSelectedTag(Constant.SELECTED_TAG_TRANSACTION_RECORED);
                    break;
                default:
                    break;
            }
        }

        return walletBeans;
    }

    @Override
    public void onItemClick(int position) {

    }
}
