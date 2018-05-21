package chinapex.com.wallet.view;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseActivity;
import chinapex.com.wallet.utils.CpLog;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private Toolbar mTb_main;
    private BottomNavigationBar mBn_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initToolBar();
        initBottomNavigationBar();
    }

    private void initView() {
        mTb_main = (Toolbar) findViewById(R.id.tb_main);
        mBn_main = (BottomNavigationBar) findViewById(R.id.bn_main);

    }

    private void initToolBar() {
        setSupportActionBar(mTb_main);

        ActionBar supportActionBar = getSupportActionBar();
        if (null == supportActionBar) {
            CpLog.e(TAG, "supportActionBar is null!");
            return;
        }

        supportActionBar.setDisplayShowTitleEnabled(false);
    }

    private void initBottomNavigationBar() {
        mBn_main.addItem(new BottomNavigationItem(R.drawable.bn_main_item_discover, "发现"));
        mBn_main.addItem(new BottomNavigationItem(R.drawable.bn_main_item_discover, "资产"));
        mBn_main.addItem(new BottomNavigationItem(R.drawable.bn_main_item_discover, "我"));

    }
}
