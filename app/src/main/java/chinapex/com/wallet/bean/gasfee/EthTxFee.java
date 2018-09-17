package chinapex.com.wallet.bean.gasfee;

/**
 * Created by SteelCabbage on 2018/9/17 0017 16:44.
 * E-Mail：liuyi_61@163.com
 */
public class EthTxFee implements ITxFee {
    private String balance;
    private String amount;
    private String gasPrice;
    private String gasLimit;

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(String gasPrice) {
        this.gasPrice = gasPrice;
    }

    public String getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(String gasLimit) {
        this.gasLimit = gasLimit;
    }
}
