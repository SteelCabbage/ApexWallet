package chinapex.com.wallet.view.me.portrait;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.viewpager.FragmentUpdateAdapter;
import chinapex.com.wallet.base.BaseActivity;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.FragmentFactory;

public class MePortraitActivity extends BaseActivity implements MeEnterpriseKeyFragment
        .OnConfirmClickListener {

    private static final String TAG = MePortraitActivity.class.getSimpleName();
    private TextView mTv_portrait_address;
    private ImageButton mIb_portrait_switch_wallet;
    private List<WalletBean> mWalletBeans;
    private TabLayout mTl_portrait;
    private ViewPager mVp_portrait;
    private List<BaseFragment> mBaseFragments;
    private List<String> mTitles;
    private FragmentUpdateAdapter mFragmentUpdateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_portrait);

        initView();
        initData();
    }

    private void initView() {
        mTv_portrait_address = (TextView) findViewById(R.id.tv_portrait_address);
        mIb_portrait_switch_wallet = (ImageButton) findViewById(R.id.ib_portrait_switch_wallet);
        mTl_portrait = findViewById(R.id.tl_portrait);
        mVp_portrait = findViewById(R.id.vp_portrait);
    }

    private void initData() {
        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            return;
        }

        mWalletBeans = apexWalletDbDao.queryWalletBeans(Constant.TABLE_APEX_WALLET);
        if (null == mWalletBeans || mWalletBeans.isEmpty()) {
            CpLog.e(TAG, "walletBeans is null or empty!");
            return;
        }

        WalletBean walletBean = mWalletBeans.get(0);
        if (null == walletBean) {
            CpLog.e(TAG, "walletBean is null!");
            return;
        }

        mTv_portrait_address.setText(walletBean.getWalletAddr());

        mTl_portrait.setupWithViewPager(mVp_portrait);

        mBaseFragments = new ArrayList<>();
        mBaseFragments.add(FragmentFactory.getFragment(Constant.FRAGMENT_TAG_ME_COMMON_PORTRAIT));
        MeEnterpriseKeyFragment enterpriseKeyFragment = (MeEnterpriseKeyFragment) FragmentFactory
                .getFragment(Constant.FRAGMENT_TAG_ME_ENTERPRISE_KEY);
        mBaseFragments.add(enterpriseKeyFragment);
        mTitles = Arrays.asList(getResources().getStringArray(R.array.me_portrait_type));

        mFragmentUpdateAdapter = new FragmentUpdateAdapter(getFragmentManager(), mBaseFragments,
                mTitles);
        mVp_portrait.setAdapter(mFragmentUpdateAdapter);

        enterpriseKeyFragment.setOnConfirmClickListener(this);
    }


    @Override
    public void onConfirmClick() {
        mBaseFragments.remove(FragmentFactory.getFragment(Constant
                .FRAGMENT_TAG_ME_ENTERPRISE_KEY));
        BaseFragment fragment = FragmentFactory.getFragment(Constant
                .FRAGMENT_TAG_ME_ENTERPRISE_PORTRAIT);

        if (!mBaseFragments.contains(fragment)) {
            mBaseFragments.add(fragment);
        }

        mFragmentUpdateAdapter.setNewFragments();
    }
}
