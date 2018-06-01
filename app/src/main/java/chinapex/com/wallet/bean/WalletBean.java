package chinapex.com.wallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class WalletBean implements Parcelable {

    private String mWalletName;
    private String mWalletAddr;
    private Double mBalance;
    private boolean isBackup;
    private String keyStore;
    private int backupState;

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

    public void setBalance(Double balance) {
        mBalance = balance;
    }

    public boolean isBackup() {
        return isBackup;
    }

    public void setBackup(boolean backup) {
        isBackup = backup;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public int getBackupState() {
        return backupState;
    }

    public void setBackupState(int backupState) {
        this.backupState = backupState;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mWalletName);
        dest.writeString(this.mWalletAddr);
        dest.writeValue(this.mBalance);
        dest.writeByte(this.isBackup ? (byte) 1 : (byte) 0);
        dest.writeString(this.keyStore);
        dest.writeInt(this.backupState);
    }

    protected WalletBean(Parcel in) {
        this.mWalletName = in.readString();
        this.mWalletAddr = in.readString();
        this.mBalance = (Double) in.readValue(Double.class.getClassLoader());
        this.isBackup = in.readByte() != 0;
        this.keyStore = in.readString();
        this.backupState = in.readInt();
    }

    public static final Creator<WalletBean> CREATOR = new Creator<WalletBean>() {
        @Override
        public WalletBean createFromParcel(Parcel source) {
            return new WalletBean(source);
        }

        @Override
        public WalletBean[] newArray(int size) {
            return new WalletBean[size];
        }
    };

    @Override
    public String toString() {
        return "WalletBean{" +
                "mWalletName='" + mWalletName + '\'' +
                ", mWalletAddr='" + mWalletAddr + '\'' +
                ", mBalance=" + mBalance +
                ", isBackup=" + isBackup +
                ", keyStore='" + keyStore + '\'' +
                ", backupState=" + backupState +
                '}';
    }
}
