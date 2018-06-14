package chinapex.com.wallet.view.wallet;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseActivity;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.PhoneUtils;

public class ExportKeystoreActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ExportKeystoreActivity.class.getSimpleName();
    private TextView mTv_export_wallet_keystore;
    private Button mBt_export_wallet_keystore;
    private String mKeystore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export_keystore);

        initView();
        initData();
    }

    private void initView() {
        mTv_export_wallet_keystore = (TextView) findViewById(R.id.tv_export_wallet_keystore);
        mBt_export_wallet_keystore = (Button) findViewById(R.id.bt_export_wallet_keystore);

        mBt_export_wallet_keystore.setOnClickListener(this);
    }

    private void initData() {
        Intent intent = getIntent();
        if (null == intent) {
            CpLog.e(TAG, "intent is null!");
            return;
        }

        mKeystore = intent.getStringExtra(Constant.BACKUP_KEYSTORE);
        if (TextUtils.isEmpty(mKeystore)) {
            CpLog.e(TAG, "keystore is null!");
            return;
        }

        mTv_export_wallet_keystore.setText(mKeystore);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_export_wallet_keystore:
                PhoneUtils.copy2Clipboard(ApexWalletApplication.getInstance(), mKeystore);
                Toast.makeText(ApexWalletApplication.getInstance(), "keystore已复制到剪切板", Toast
                        .LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
