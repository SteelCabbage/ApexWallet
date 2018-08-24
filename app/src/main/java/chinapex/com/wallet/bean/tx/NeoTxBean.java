package chinapex.com.wallet.bean.tx;

import neomobile.Wallet;

/**
 * Created by SteelCabbage on 2018/8/24 0024 17:46.
 * E-Mail：liuyi_61@163.com
 */
public class NeoTxBean implements ITxBean {
    private Wallet wallet;
    private String assetID;
    private String toAddress;
    private double amount;

    public NeoTxBean() {

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

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
