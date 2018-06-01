package chinapex.com.wallet.view.me;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.view.dialog.SwitchWalletDialog;

/**
 * Created by SteelCabbage on 2018/5/31 0031.
 */

public class MeTransactionRecordFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = MeTransactionRecordFragment.class.getSimpleName();
    private TextView mTv_me_transaction_record_title;
    private TextView mTv_me_transaction_record_balance;
    private TextView mTv_me_transaction_record_address;
    private ImageButton mIb_me_transaction_record_switch;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me_transaction_record, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        mTv_me_transaction_record_title = view.findViewById(R.id.tv_me_transaction_record_title);
        mTv_me_transaction_record_balance = view.findViewById(R.id
                .tv_me_transaction_record_balance);
        mTv_me_transaction_record_address = view.findViewById(R.id
                .tv_me_transaction_record_address);
        mIb_me_transaction_record_switch = view.findViewById(R.id.ib_me_transaction_record_switch);

        mIb_me_transaction_record_switch.setOnClickListener(this);
    }

    private void initData() {
        MeFragment meFragment = (MeFragment) getActivity().getFragmentManager().findFragmentByTag
                (2 + "");
        WalletBean currentClickedWalletBean = meFragment.getCurrentClickedWalletBean();
        if (null == currentClickedWalletBean) {
            CpLog.e(TAG, "currentClickedWalletBean is null!");
            return;
        }

        mTv_me_transaction_record_title.setText(String.valueOf(Constant.WALLET_NAME +
                currentClickedWalletBean.getWalletName()));
        mTv_me_transaction_record_balance.setText(String.valueOf(currentClickedWalletBean.getBalance
                ()));
        mTv_me_transaction_record_address.setText(currentClickedWalletBean.getWalletAddr());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_me_transaction_record_switch:
                CpLog.i(TAG, "ib_me_transaction_record_switch is clicked!");
                showDialog();
                break;
            default:
                break;
        }
    }

    private void showDialog() {
        DialogFragment newFragment = SwitchWalletDialog.newInstance();
        newFragment.show(getFragmentManager(), "SwitchWalletDialog");
    }
}
