package chinapex.com.wallet.model.transfer;

import chinapex.com.wallet.bean.tx.EthTxBean;
import chinapex.com.wallet.bean.tx.ITxBean;
import chinapex.com.wallet.utils.CpLog;
import ethmobile.Wallet;

/**
 * Created by SteelCabbage on 2018/8/24 0024 15:56.
 * E-Mail：liuyi_61@163.com
 */
public class CreateEthTxModel implements ICreateTxModel {
    private static final String TAG = CreateEthTxModel.class.getSimpleName();
    private ICreateTxModelCallback mICreateTxModelCallback;
    private EthTxBean mEthTxBean;

    public CreateEthTxModel(ICreateTxModelCallback ICreateTxModelCallback) {
        mICreateTxModelCallback = ICreateTxModelCallback;
    }

    @Override
    public void createGlobalTx(ITxBean iTxBean) {
        if (null == mICreateTxModelCallback) {
            CpLog.e(TAG, "mICreateTxModelCallback is null!");
            return;
        }

        if (null == iTxBean) {
            CpLog.e(TAG, "iTxBean is null!");
            return;
        }

        if (iTxBean instanceof EthTxBean) {
            mEthTxBean = (EthTxBean) iTxBean;
        }

        if (null == mEthTxBean) {
            CpLog.e(TAG, "mEthTxBean is null!");
            return;
        }

        Wallet wallet = mEthTxBean.getWallet();
        if (null == wallet) {
            CpLog.e(TAG, "eth wallet is null!");
            return;
        }


    }

    @Override
    public void createColorTx(ITxBean iTxBean) {

    }
}
