package chinapex.com.wallet.executor.callback.eth;

/**
 * Created by SteelCabbage on 2018/9/20 0020 13:31.
 * E-Mail：liuyi_61@163.com
 */
public interface IGetEthTransactionReceiptCallback {
    void getEthTransactionReceipt(String walletAddress, String blockNumber, boolean isSuccess);
}
