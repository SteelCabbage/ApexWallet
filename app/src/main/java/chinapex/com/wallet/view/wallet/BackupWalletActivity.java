package chinapex.com.wallet.view.wallet;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import chinapex.com.wallet.R;
import chinapex.com.wallet.utils.FragmentFactory;

public class BackupWalletActivity extends AppCompatActivity {

    private TextView mTv_backup_title;
    private Toolbar mTb_backup;
    private String[] mBackupTitles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_backup_wallet);

        initData();
        initView();
        initToolBar();
        initFragment();
    }

    private void initData() {
        mBackupTitles = getResources().getStringArray(R.array.backup_item_title);
    }

    private void initView() {
        mTb_backup = (Toolbar) findViewById(R.id.tb_backup);
        mTv_backup_title = (TextView) findViewById(R.id.tv_backup_title);
    }

    private void initToolBar() {
        mTb_backup.setTitle("");
        setSupportActionBar(mTb_backup);
        mTv_backup_title.setText(mBackupTitles[0]);

    }

    private void initFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        for (int i = 0; i < mBackupTitles.length; i++) {
            Fragment fragment = fragmentManager.findFragmentByTag(i + "");
            if (null != fragment) {
                fragmentTransaction.remove(fragment);
            }
        }
        fragmentTransaction.add(R.id.fl_backup, FragmentFactory.getFragment(0), "0");
        fragmentTransaction.commit();

        mTv_backup_title.setText(mBackupTitles[0]);
    }
}
