package chinapex.com.wallet.view.me;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseActivity;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.FragmentFactory;

public class MePortraitActivity extends BaseActivity {

    private static final String TAG = MePortraitActivity.class.getSimpleName();
    private TextView mTv_portrait_address;
    private ImageButton mIb_portrait_switch_wallet;

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
    }

    private void initData() {
        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            return;
        }

        List<WalletBean> walletBeans = apexWalletDbDao.queryWalletBeans(Constant.TABLE_APEX_WALLET);
        if (null == walletBeans || walletBeans.isEmpty()) {
            CpLog.e(TAG, "walletBeans is null or empty!");
            // TODO: 2018/7/23 0023 引导用户创建
            mTv_portrait_address.setVisibility(View.INVISIBLE);
            mIb_portrait_switch_wallet.setVisibility(View.INVISIBLE);
            switchEmptyFragment();
            return;
        }

        switchPortraitFragment();

        WalletBean walletBean = walletBeans.get(0);
        if (null == walletBean) {
            CpLog.e(TAG, "walletBean is null!");
            return;
        }

        mTv_portrait_address.setText(walletBean.getWalletAddr());
    }

    private void switchEmptyFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        BaseFragment fragment = FragmentFactory.getFragment(Constant
                .FRAGMENT_TAG_ME_PORTRAIT_EMPTY);
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fl_portrait, fragment, Constant
                    .FRAGMENT_TAG_ME_PORTRAIT_EMPTY);
        }
        fragmentTransaction.show(fragment).hide(FragmentFactory.getFragment(Constant
                .FRAGMENT_TAG_ME_PORTRAIT)).commit();
    }

    private void switchPortraitFragment() {
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        BaseFragment fragment = FragmentFactory.getFragment(Constant.FRAGMENT_TAG_ME_PORTRAIT);
        if (!fragment.isAdded()) {
            fragmentTransaction.add(R.id.fl_portrait, fragment, Constant.FRAGMENT_TAG_ME_PORTRAIT);
        }
        fragmentTransaction.show(fragment).hide(FragmentFactory.getFragment(Constant
                .FRAGMENT_TAG_ME_PORTRAIT_EMPTY)).commit();
    }
}
