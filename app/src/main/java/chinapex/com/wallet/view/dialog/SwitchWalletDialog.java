package chinapex.com.wallet.view.dialog;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import chinapex.com.wallet.R;
import chinapex.com.wallet.utils.CpLog;

/**
 * Created by SteelCabbage on 2018/5/31 0031.
 */

public class SwitchWalletDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = SwitchWalletDialog.class.getSimpleName();
    private ImageButton mIb_switch_wallet_close;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        View dialog_switch_wallet = inflater.inflate(R.layout.dialog_switch_wallet, container,
                false);
        return dialog_switch_wallet;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);
    }

    private void initView(View view) {
        mIb_switch_wallet_close = view.findViewById(R.id.ib_switch_wallet_close);
        mIb_switch_wallet_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_switch_wallet_close:
                CpLog.i(TAG, "ib_switch_wallet_close is clicked!");
                break;
            default:
                break;
        }
    }
}
