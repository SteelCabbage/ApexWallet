package chinapex.com.wallet.executor.runnable.eth;

import chinapex.com.wallet.executor.callback.eth.IGetEthNonceCallback;

/**
 * Created by SteelCabbage on 2018/9/6 0006 17:30.
 * E-Mail：liuyi_61@163.com
 */
public class GetEthNonce implements Runnable {
    private static final String TAG = GetEthNonce.class.getSimpleName();
    private IGetEthNonceCallback mIGetEthNonceCallback;

    public GetEthNonce(IGetEthNonceCallback IGetEthNonceCallback) {
        mIGetEthNonceCallback = IGetEthNonceCallback;
    }

    @Override
    public void run() {

    }
}
