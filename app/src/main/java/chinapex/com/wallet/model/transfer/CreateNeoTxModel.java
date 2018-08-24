package chinapex.com.wallet.model.transfer;

import chinapex.com.wallet.bean.tx.ITxBean;
import chinapex.com.wallet.bean.tx.NeoTxBean;
import chinapex.com.wallet.executor.TaskController;
import chinapex.com.wallet.executor.callback.IGetUtxosCallback;
import chinapex.com.wallet.executor.runnable.GetUtxos;
import chinapex.com.wallet.utils.CpLog;
import neomobile.Wallet;

/**
 * Created by SteelCabbage on 2018/8/24 0024 15:48.
 * E-Mail：liuyi_61@163.com
 */
public class CreateNeoTxModel implements ICreateTxModel, IGetUtxosCallback {
    private static final String TAG = CreateNeoTxModel.class.getSimpleName();
    private ICreateTxModelCallback mICreateTxModelCallback;
    private NeoTxBean mNeoTxBean;

    public CreateNeoTxModel(ICreateTxModelCallback ICreateTxModelCallback) {
        mICreateTxModelCallback = ICreateTxModelCallback;
    }

    @Override
    public void createGlobalTx(ITxBean iTxBean) {
        if (null == iTxBean) {
            CpLog.e(TAG, "iTxBean is null!");
            return;
        }

        if (iTxBean instanceof NeoTxBean) {
            mNeoTxBean = (NeoTxBean) iTxBean;
        }

        if (null == mNeoTxBean) {
            CpLog.e(TAG, "mNeoTxBean is null!");
            return;
        }

        Wallet wallet = mNeoTxBean.getWallet();
        if (null == wallet) {
            CpLog.e(TAG, "neo wallet is null!");
            return;
        }

        TaskController.getInstance().submit(new GetUtxos(wallet.address(), this));
    }

    @Override
    public void getUtxos(String utxos) {

    }

    @Override
    public void createColorTx(ITxBean iTxBean) {

    }


}
