package chinapex.com.wallet.view.excitation;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseActivity;
import chinapex.com.wallet.utils.CpLog;

public class ExcitationDetailActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = ExcitationFragment.class.getSimpleName();

    private EditText mCpxAddressInput;
    private EditText mEthAddressInput;
    private TextView mWrongAddressNote;
    private Button mExcitationCommit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excitation_detail);

        initView();
    }

    private void initView() {
        mCpxAddressInput = findViewById(R.id.cpx_address_input);
        mEthAddressInput = findViewById(R.id.eth_address_input);
        mWrongAddressNote = findViewById(R.id.tv_excitation_detail_wrong_address_note);
        mExcitationCommit = findViewById(R.id.btn_excitation_submit);

        mExcitationCommit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

    }
}
