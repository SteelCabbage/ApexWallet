package chinapex.com.wallet.bean;

public class WalletBean {

    private String mWalletName;
    private String mWalletAddr;
    private Double mBalance;

    public WalletBean() {
    }

    public String getWalletName() {
        return mWalletName;
    }

    public void setWalletName(String walletName) {
        mWalletName = walletName;
    }

    public String getWalletAddr() {
        return mWalletAddr;
    }

    public void setWalletAddr(String walletAddr) {
        mWalletAddr = walletAddr;
    }

    public Double getBalance() {
        return mBalance;
    }

    public void setBalance(double balance) {
        mBalance = balance;
    }

    @Override
    public String toString() {
        return "WalletBean{" +
                "mWalletName='" + mWalletName + '\'' +
                ", mWalletAddr='" + mWalletAddr + '\'' +
                ", mBalance='" + mBalance + '\'' +
                '}';
    }

}
