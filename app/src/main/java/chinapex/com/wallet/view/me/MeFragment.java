package chinapex.com.wallet.view.me;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.MeRecyclerViewAdapter;
import chinapex.com.wallet.adapter.SpacesItemDecoration;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.bean.TransactionRecord;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.bean.WalletKeyStore;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.FragmentFactory;
import chinapex.com.wallet.utils.GsonUtils;
import chinapex.com.wallet.utils.SharedPreferencesUtils;
import chinapex.com.wallet.view.wallet.BackupWalletActivity;

/**
 * Created by SteelCabbage on 2018/5/21 0021.
 */

public class MeFragment extends BaseFragment implements MeRecyclerViewAdapter
        .OnItemClickListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    private static final String TAG = MeFragment.class.getSimpleName();
    private RecyclerView mRv_me;
    private MeRecyclerViewAdapter mMeRecyclerViewAdapter;
    private List<WalletBean> mWalletBeans;
    private SwipeRefreshLayout mSl_me;
    private TextView mTv_me_wallet_balance;
    private Button mBt_me_manage_wallet;
    private Button mBt_me_transaction_record;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {

        return inflater.inflate(R.layout.fragment_me, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

    }

    private void initView(View view) {
        mRv_me = view.findViewById(R.id.rv_me);
        mSl_me = view.findViewById(R.id.sl_me);
        mTv_me_wallet_balance = view.findViewById(R.id.tv_me_wallet_balance);
        mBt_me_manage_wallet = view.findViewById(R.id.bt_me_manage_wallet);
        mBt_me_transaction_record = view.findViewById(R.id.bt_me_transaction_record);

        mRv_me.setLayoutManager(new LinearLayoutManager(ApexWalletApplication.getInstance(),
                LinearLayoutManager.VERTICAL, false));
        mWalletBeans = getWalletBeans();
        mMeRecyclerViewAdapter = new MeRecyclerViewAdapter(mWalletBeans);
        mMeRecyclerViewAdapter.setOnItemClickListener(this);

        int space = 20;
        mRv_me.addItemDecoration(new SpacesItemDecoration(space));

        mRv_me.setAdapter(mMeRecyclerViewAdapter);

        mSl_me.setColorSchemeColors(this.getActivity().getResources().getColor(R.color
                .colorPrimary));
        mSl_me.setOnRefreshListener(this);

        mBt_me_manage_wallet.setOnClickListener(this);
        mBt_me_transaction_record.setOnClickListener(this);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onRefresh() {
        // TODO: 2018/5/30 0030
        new Thread(new Runnable() {
            @Override
            public void run() {
                SystemClock.sleep(2000);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSl_me.setRefreshing(false);
                    }
                });

            }
        }).start();
    }

    private void setBalanceSum() {
        List<WalletBean> walletBeans = getWalletBeans();
        double balanceSum = 0.0;
        for (WalletBean walletBean : walletBeans) {
            if (null == walletBean) {
                CpLog.e(TAG, "walletBean is null!");
                continue;
            }
            balanceSum = balanceSum + walletBean.getBalance();
        }
        mTv_me_wallet_balance.setText(String.valueOf(balanceSum));
    }

    private List<WalletBean> getWalletBeans() {
        String keyStores = (String) SharedPreferencesUtils.getParam(this.getActivity(), Constant
                .SP_WALLET_KEYSTORE, "");
        if (TextUtils.isEmpty(keyStores)) {
            CpLog.e(TAG, "keyStores is null!");
            return null;
        }

        List<WalletKeyStore> walletKeyStores = GsonUtils.json2List(keyStores);
        if (null == walletKeyStores) {
            CpLog.e(TAG, "jsonListObject is null!");
            return null;
        }

        List<WalletBean> walletBeans = new ArrayList<>();
        for (WalletKeyStore walletKeyStore : walletKeyStores) {
            WalletBean walletBean = new WalletBean();
            walletBean.setWalletName(walletKeyStore.getWalletName());
            walletBean.setWalletAddr(walletKeyStore.getWalletAddr());
            walletBean.setBalance(0.0);
            walletBean.setBackup(false);
            walletBeans.add(walletBean);
        }

        return walletBeans;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击管理钱包
            case R.id.bt_me_manage_wallet:
                toMeManagerDetailFragment();
                break;
            //点击交易记录
            case R.id.bt_me_transaction_record:
                toMeTransactionRecordFragment();
                break;
        }
    }

    private void toMeManagerDetailFragment() {
        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager()
                .beginTransaction();
        BaseFragment fragment = FragmentFactory.getFragment(10);
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fl_main, fragment, "" + 10);
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.show(fragment).hide(FragmentFactory.getFragment(2)).commit();

    }

    private void toMeTransactionRecordFragment() {
        FragmentTransaction fragmentTransaction = getActivity().getFragmentManager()
                .beginTransaction();
        BaseFragment fragment = FragmentFactory.getFragment(11);
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fl_main, fragment, "" + 11);
            fragmentTransaction.addToBackStack(null);
        }
        fragmentTransaction.show(fragment).hide(FragmentFactory.getFragment(2)).commit();

    }
}
