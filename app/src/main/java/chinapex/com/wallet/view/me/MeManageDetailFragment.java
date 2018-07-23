package chinapex.com.wallet.view.me;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import chinapex.com.wallet.R;
import chinapex.com.wallet.base.BaseFragment;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.changelistener.ApexListeners;
import chinapex.com.wallet.changelistener.OnItemStateUpdateListener;
import chinapex.com.wallet.global.ApexWalletApplication;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.model.ApexWalletDbDao;
import chinapex.com.wallet.utils.CpLog;
import chinapex.com.wallet.utils.PhoneUtils;
import chinapex.com.wallet.utils.ToastUtils;
import chinapex.com.wallet.view.dialog.BackupWalletPwdDialog;
import chinapex.com.wallet.view.dialog.DeleteWalletPwdDialog;
import chinapex.com.wallet.view.dialog.ExportKeystorePwdDialog;

/**
 * Created by SteelCabbage on 2018/5/31 0031.
 */

public class MeManageDetailFragment extends BaseFragment implements View.OnClickListener,
        OnItemStateUpdateListener {

    private static final String TAG = MeManageDetailFragment.class.getSimpleName();
    private TextView mTv_me_manager_detail_title;
    private TextView mTv_me_manager_detail_address;
    private Button mBt_me_manager_detail_backup;
    private Button mBt_me_manager_detail_delete;
    private WalletBean mCurrentClickedWalletBean;
    private ImageButton mIb_manage_detail_export;
    private EditText mEt_me_manager_detail_bottom_wallet_name;
    private Button mBt_me_manager_detail_save;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_me_manage_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        initData();
    }

    private void initView(View view) {
        mTv_me_manager_detail_title = view.findViewById(R.id.tv_me_manager_detail_title);
        mTv_me_manager_detail_address = view.findViewById(R.id.tv_me_manager_detail_address);
        mEt_me_manager_detail_bottom_wallet_name = view.findViewById(R.id
                .et_me_manager_detail_bottom_wallet_name);
        mBt_me_manager_detail_backup = view.findViewById(R.id.bt_me_manager_detail_backup);
        mBt_me_manager_detail_delete = view.findViewById(R.id.bt_me_manager_detail_delete);
        mBt_me_manager_detail_save = view.findViewById(R.id.bt_me_manager_detail_save);
        mIb_manage_detail_export = view.findViewById(R.id.ib_manage_detail_export);

        mBt_me_manager_detail_backup.setOnClickListener(this);
        mBt_me_manager_detail_delete.setOnClickListener(this);
        mBt_me_manager_detail_save.setOnClickListener(this);
        mIb_manage_detail_export.setOnClickListener(this);
        ApexListeners.getInstance().addOnItemStateUpdateListener(this);

        // 复制地址
        mTv_me_manager_detail_address.setOnClickListener(this);
    }

    private void initData() {
        Me3Activity me3Activity = (Me3Activity) getActivity();
        mCurrentClickedWalletBean = me3Activity.getWalletBean();
        if (null == mCurrentClickedWalletBean) {
            CpLog.e(TAG, "currentClickedWalletBean is null!");
            return;
        }

        mTv_me_manager_detail_title.setText(mCurrentClickedWalletBean.getWalletName());
        mTv_me_manager_detail_address.setText(mCurrentClickedWalletBean.getWalletAddr());
        mEt_me_manager_detail_bottom_wallet_name.setText(String.valueOf(mCurrentClickedWalletBean
                .getWalletName()));

        setIsShowBackupKey(mCurrentClickedWalletBean.getBackupState());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_manage_detail_export:
                showExportKeystorePwdDialog();
                break;
            case R.id.bt_me_manager_detail_backup:
                showBackupWalletPwdDialog();
                break;
            case R.id.bt_me_manager_detail_delete:
                showDeleteWalletPwdDialog();
                break;
            case R.id.bt_me_manager_detail_save:
                modifyWalletName();
                break;
            case R.id.tv_me_manager_detail_address:
                String copyAddr = mTv_me_manager_detail_address.getText().toString().trim();
                PhoneUtils.copy2Clipboard(ApexWalletApplication.getInstance(), copyAddr);
                ToastUtils.getInstance().showToast(ApexWalletApplication.getInstance()
                        .getResources().getString(R.string.wallet_address_copied));
                break;
            default:
                break;
        }
    }

    private void modifyWalletName() {
        ApexWalletDbDao apexWalletDbDao = ApexWalletDbDao.getInstance(ApexWalletApplication
                .getInstance());
        if (null == apexWalletDbDao) {
            CpLog.e(TAG, "apexWalletDbDao is null!");
            return;
        }

        String newWalletName = mEt_me_manager_detail_bottom_wallet_name.getText().toString().trim();
        apexWalletDbDao.updateWalletName(Constant.TABLE_APEX_WALLET, mCurrentClickedWalletBean
                .getWalletAddr(), newWalletName);
        mTv_me_manager_detail_title.setText(newWalletName);
        mCurrentClickedWalletBean.setWalletName(newWalletName);
        ApexListeners.getInstance().notifyItemNameUpdate(mCurrentClickedWalletBean);
        ToastUtils.getInstance().showToast(ApexWalletApplication.getInstance().getResources()
                .getString(R.string.wallet_name_save_success));
    }

    public void showDeleteWalletPwdDialog() {
        DeleteWalletPwdDialog deleteWalletPwdDialog = DeleteWalletPwdDialog.newInstance();
        deleteWalletPwdDialog.setCurrentWalletBean(mCurrentClickedWalletBean);
        deleteWalletPwdDialog.show(getFragmentManager(), "DeleteWalletPwdDialog");
    }

    public void showBackupWalletPwdDialog() {
        BackupWalletPwdDialog backupWalletPwdDialog = BackupWalletPwdDialog.newInstance();
        backupWalletPwdDialog.setCurrentWalletBean(mCurrentClickedWalletBean);
        backupWalletPwdDialog.show(getFragmentManager(), "BackupWalletPwdDialog");
    }

    public void showExportKeystorePwdDialog() {
        ExportKeystorePwdDialog exportKeystorePwdDialog = ExportKeystorePwdDialog.newInstance();
        exportKeystorePwdDialog.setCurrentWalletBean(mCurrentClickedWalletBean);
        exportKeystorePwdDialog.show(getFragmentManager(), "ExportKeystorePwdDialog");
    }

    @Override
    public void OnItemStateUpdate(WalletBean walletBean) {
        if (null == walletBean) {
            CpLog.e(TAG, "walletBean is null!");
            return;
        }

        setIsShowBackupKey(walletBean.getBackupState());
    }

    private void setIsShowBackupKey(int backupState) {
        switch (backupState) {
            //未备份
            case Constant.BACKUP_UNFINISHED:
                mBt_me_manager_detail_backup.setVisibility(View.VISIBLE);
                break;
            //已备份
            case Constant.BACKUP_FINISH:
                mBt_me_manager_detail_backup.setVisibility(View.INVISIBLE);
                break;
            default:
                break;
        }
    }
}
