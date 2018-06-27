package chinapex.com.wallet.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.math.BigDecimal;
import java.util.ArrayList;

import chinapex.com.wallet.R;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.GsonUtils;
import neomobile.Neomobile;
import neomobile.Tx;
import neomobile.Wallet;

public class TestNep5TransferActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = TestNep5TransferActivity.class.getSimpleName();
    private Button mBt_test_nep5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_nep5_transfer);

        initView();
    }

    private void initView() {
        mBt_test_nep5 = (Button) findViewById(R.id.bt_test_nep5);
        mBt_test_nep5.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_test_nep5:
                testNep5();
                break;
            default:
                break;
        }
    }

    private void testNep5() {
        Wallet wallet = null;
        CpLog.i(TAG, "start!");
        try {
            wallet = Neomobile.fromKeyStore(keysotre, "Bobo0722");
        } catch (Exception e) {
            CpLog.e(TAG, "fromKeyStore exception:" + e.getMessage());
        }

        if (null == wallet) {
            CpLog.e(TAG, "wallet is null!");
            return;
        }

        Tx nep5Tx = null;
        try {
            nep5Tx = wallet.createNep5Tx(
                    Constant.ASSETS_CPX,
                    Neomobile.decodeAddress("AWCsFDK673i1eipiSD4Erk2cW9DAqMZyEU"),
                    Neomobile.decodeAddress("ALDbmTMY54RZnLmibH3eXfHvrZt4fLiZhh"),
                    new BigDecimal("100000000").longValue(),
                    utxos
            );
        } catch (Exception e) {
            CpLog.e(TAG, "createNep5Tx exception:" + e.getMessage());
        }

        if (null == nep5Tx) {
            CpLog.e(TAG, "nep5Tx is null!");
            return;
        }

        String nep5TxData = nep5Tx.getData();
        CpLog.i(TAG, "nep5TxData===" + nep5TxData + "===");
        String nep5TxID = nep5Tx.getID();
        CpLog.i(TAG, "nep5TxID===" + nep5TxID + "===");

    }


    private static final String keysotre = "{\"address\":\"AWCsFDK673i1eipiSD4Erk2cW9DAqMZyEU\"," +
            "\"crypto\":{\"cipher\":\"aes-128-ctr\"," +
            "\"ciphertext\":\"dca21c8e2b0e7587e5fb6763c7081440ae67d2107b9222a3aeb5afa48ca42bce\"," +
            "\"cipherparams\":{\"iv\":\"30fb179d85f7210409be40e19a2137f2\"},\"kdf\":\"scrypt\"," +
            "\"kdfparams\":{\"dklen\":32,\"n\":4096,\"p\":6,\"r\":8," +
            "\"salt\":\"62b6966543bb192b32f4e24c8e074dffd675f62910cdd998ccb8d55d29a0e27d\"}," +
            "\"mac\":\"ce736e3f4317c0d015cb7d6b21fe82da2d2b8a62078a099b7209a71941375af4\"}," +
            "\"id\":\"80930332-6e5b-4bc8-a774-94f49bbf639f\",\"version\":3}";

    private static final String utxos = GsonUtils.toJsonStr(new ArrayList<>());
}
