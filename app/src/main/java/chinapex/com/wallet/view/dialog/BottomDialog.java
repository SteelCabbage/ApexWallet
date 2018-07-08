package chinapex.com.wallet.view.dialog;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import java.util.List;

import chinapex.com.wallet.R;
import chinapex.com.wallet.adapter.AddAssetsRecyclerViewAdapter;
import chinapex.com.wallet.bean.AssetBean;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;

/**
 * Created by SteelCabbage on 2018/5/31 0031.
 */

public class BottomDialog extends DialogFragment implements View.OnClickListener,
        AddAssetsRecyclerViewAdapter.OnItemClickListener {

    private static final String TAG = BottomDialog.class.getSimpleName();
    private RecyclerView mRv_add_assets;
    private List<AssetBean> mAssetBeans;
    private AddAssetsRecyclerViewAdapter mAddAssetsRecyclerViewAdapter;


    public static BottomDialog newInstance() {
        return new BottomDialog();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        // 去掉边框
        Window window = getDialog().getWindow();
        if (null == window) {
            CpLog.e(TAG, "window is null!");
            return null;
        }
        window.setBackgroundDrawable(new ColorDrawable(0));

        // 点击空白区域不可取消
        setCancelable(false);

        // 设置style
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomDialog);

        // 可设置dialog的位置
        window.setGravity(Gravity.BOTTOM);

        // 消除边距
        window.getDecorView().setPadding(0, 0, 0, 0);

        // 设置全屏
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        lp.windowAnimations = R.style.BottomDialogAnim;
        window.setAttributes(lp);

        // 沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        return inflater.inflate(R.layout.dialog_bottom, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void initView(View view) {
        ImageButton ib_add_assets_close = view.findViewById(R.id.ib_add_assets_close);
        mRv_add_assets = view.findViewById(R.id.rv_add_assets);
        ib_add_assets_close.setOnClickListener(this);
    }

    private void initData() {
        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            return;
        }

        mAssetBeans = apexWalletDbDao.queryAssetsByType(Constant.ASSET_TYPE_NEP5);
        if (null == mAssetBeans || mAssetBeans.isEmpty()) {
            CpLog.e(TAG, "assetBeans is null or empty!");
            return;
        }

        mAddAssetsRecyclerViewAdapter = new AddAssetsRecyclerViewAdapter(mAssetBeans);
        mAddAssetsRecyclerViewAdapter.setOnItemClickListener(this);
        mRv_add_assets.setLayoutManager(new LinearLayoutManager(ApexWalletApplication.getInstance
                (), LinearLayoutManager.VERTICAL, false));
        mRv_add_assets.setAdapter(mAddAssetsRecyclerViewAdapter);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_add_assets_close:
                dismiss();
                break;
            default:
                break;
        }
    }

    @Override
    public void onItemClick(int position) {
        if (null == mAssetBeans || mAssetBeans.isEmpty()) {
            CpLog.e(TAG, "mAssetBeans is null or empty!");
            return;
        }

        AssetBean assetBean = mAssetBeans.get(position);
        if (null == assetBean) {
            CpLog.e(TAG, "assetBean is null!");
            return;
        }

        CpLog.i(TAG, assetBean.getSymbol());
    }
}
