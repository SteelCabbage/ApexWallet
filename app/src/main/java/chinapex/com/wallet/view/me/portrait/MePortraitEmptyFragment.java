package chinapex.com.wallet.view.me.portrait;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.view.wallet.CreateWalletActivity;

/**
 * Created by SteelCabbage on 2018/7/23 0023 18:11.
 * E-Mail：liuyi_61@163.com
 */

public class MePortraitEmptyFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = MePortraitEmptyFragment.class.getSimpleName();
    private Button mBt_portrait_create_wallet;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me_portrait_empty, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
        initData();
    }

    private void initView(View view) {
        mBt_portrait_create_wallet = view.findViewById(R.id.bt_portrait_create_wallet);
        mBt_portrait_create_wallet.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_portrait_create_wallet:
                startActivity(CreateWalletActivity.class, true);
                break;
            default:
                break;
        }
    }
}
