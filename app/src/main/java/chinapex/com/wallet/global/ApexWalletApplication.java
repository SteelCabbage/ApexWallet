package chinapex.com.wallet.global;

import android.app.Application;
import android.text.TextUtils;

import java.util.Locale;

import chinapex.com.wallet.changelistener.ApexListeners;
import chinapex.com.wallet.executor.TaskController;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.PhoneUtils;
import chinapex.com.wallet.utils.SharedPreferencesUtils;

/**
 * Created by SteelCabbage on 2018/5/21 0021.
 */

public class ApexWalletApplication extends Application {
    private static final String TAG = ApexWalletApplication.class.getSimpleName();
    private static ApexWalletApplication sApexWalletApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        CpLog.i(TAG, "ApexWalletApplication start!");
        sApexWalletApplication = this;
        TaskController.getInstance().doInit();
        ApexListeners.getInstance().doInit();
        ApexGlobalTask.getInstance().doInit();
        setLanguage();
    }

    public static ApexWalletApplication getInstance() {
        return sApexWalletApplication;
    }

    private void setLanguage() {
        String defLanguage = Locale.getDefault().toString();
        String spLanguage = (String) SharedPreferencesUtils.getParam(ApexWalletApplication
                .getInstance(), Constant.CURRENT_LANGUAGE, defLanguage);

        if (TextUtils.isEmpty(spLanguage)) {
            CpLog.e(TAG, "languageValue is null or empty!");
            return;
        }

        if (spLanguage.contains(Locale.CHINA.toString())) {
            PhoneUtils.updateLocale(Locale.SIMPLIFIED_CHINESE);
        } else if (spLanguage.contains(Locale.ENGLISH.toString())) {
            PhoneUtils.updateLocale(Locale.US);
        } else {
            PhoneUtils.updateLocale(Locale.getDefault());
        }
    }
}
