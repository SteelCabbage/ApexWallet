package chinapex.com.wallet.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseActivity;

public class CreateWalletActivity extends BaseActivity implements View.OnClickListener {


    private Button mBt_create_wallet_confirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);

        initView();
    }

    private void initView() {
        mBt_create_wallet_confirm = (Button) findViewById(R.id.bt_create_wallet_confirm);

        mBt_create_wallet_confirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_create_wallet_confirm:
                startActivity(MainActivity.class, true);
                break;
            default:
                break;
        }
    }
}
