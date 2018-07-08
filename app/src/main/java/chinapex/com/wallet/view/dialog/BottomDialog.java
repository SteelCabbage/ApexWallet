package chinapex.com.wallet.view.dialog;

import android.app.DialogFragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import chinapex.com.wallet.R;
import chinapex.com.wallet.utils.CpLog;

/**
 * Created by SteelCabbage on 2018/5/31 0031.
 */

public class BottomDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = BottomDialog.class.getSimpleName();


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
        ib_add_assets_close.setOnClickListener(this);
    }

    private void initData() {

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
}
