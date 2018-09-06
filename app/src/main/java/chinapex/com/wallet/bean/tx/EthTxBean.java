package chinapex.com.wallet.bean.tx;

import java.math.BigDecimal;

import ethmobile.Wallet;

/**
 * Created by SteelCabbage on 2018/8/24 0024 17:47.
 * E-Mail：liuyi_61@163.com
 */
public class EthTxBean implements ITxBean {
    private Wallet wallet;
    private String assetID;
    private int assetDecimal;
    private String fromAddress;
    private String toAddress;
    private String amount;
    private double gasPrice;
    private BigDecimal gasLimit;

    public EthTxBean() {

    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public String getAssetID() {
        return assetID;
    }

    public void setAssetID(String assetID) {
        this.assetID = assetID;
    }

    public int getAssetDecimal() {
        return assetDecimal;
    }

    public void setAssetDecimal(int assetDecimal) {
        this.assetDecimal = assetDecimal;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public double getGasPrice() {
        return gasPrice;
    }

    public void setGasPrice(double gasPrice) {
        this.gasPrice = gasPrice;
    }

    public BigDecimal getGasLimit() {
        return gasLimit;
    }

    public void setGasLimit(BigDecimal gasLimit) {
        this.gasLimit = gasLimit;
    }
}
