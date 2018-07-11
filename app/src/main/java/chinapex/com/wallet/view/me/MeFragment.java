package chinapex.com.wallet.view.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.global.Constant;

/**
 * Created by SteelCabbage on 2018/7/11 0011 16:48.
 * E-Mail：liuyi_61@163.com
 */

public class MeFragment extends BaseFragment implements View.OnClickListener {
    public static final String TAG = MeFragment.class.getSimpleName();


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
        initData();
    }

    private void initView(View view) {
        ImageButton ib_me_manage_wallet = view.findViewById(R.id.ib_me_manage_wallet);
        TextView tv_me_manage_wallet = view.findViewById(R.id.tv_me_manage_wallet);
        ImageButton ib_me_tx_records = view.findViewById(R.id.ib_me_tx_records);
        TextView tv_me_tx_records = view.findViewById(R.id.tv_me_tx_records);

        ib_me_manage_wallet.setOnClickListener(this);
        tv_me_manage_wallet.setOnClickListener(this);
        ib_me_tx_records.setOnClickListener(this);
        tv_me_tx_records.setOnClickListener(this);
    }

    private void initData() {

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_me_manage_wallet:
            case R.id.tv_me_manage_wallet:
                startActivityStringExtra(Me2Activity.class, false, Constant
                        .ME_2_SHOULD_BE_SHOW, Constant.ME_2_SHOULD_BE_SHOW_MANAGE_WALLET);
                break;
            case R.id.ib_me_tx_records:
            case R.id.tv_me_tx_records:
                startActivityStringExtra(Me2Activity.class, false, Constant
                        .ME_2_SHOULD_BE_SHOW, Constant.ME_2_SHOULD_BE_SHOW_TX_RECORDS);
                break;
            default:
                break;
        }
    }
}
