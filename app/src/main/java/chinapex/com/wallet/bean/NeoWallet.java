package chinapex.com.wallet.bean;

import android.os.Parcel;
import android.os.Parcelable;

import chinapex.com.wallet.utils.WalletUtils;

public class NeoWallet implements Parcelable, WalletBean {

    private String mWalletName;
    private String mWalletAddr;
    private int backupState;
    private String keyStore;
    private String assetsJson;
    private String assetsNep5Json;
    private boolean isSelected;
    private int selectedTag;

    public NeoWallet() {
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

    public int getBackupState() {
        return backupState;
    }

    public void setBackupState(int backupState) {
        this.backupState = backupState;
    }

    public String getKeyStore() {
        return keyStore;
    }

    public void setKeyStore(String keyStore) {
        this.keyStore = keyStore;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getSelectedTag() {
        return selectedTag;
    }

    public void setSelectedTag(int selectedTag) {
        this.selectedTag = selectedTag;
    }

    public String getAssetsJson() {
        return assetsJson;
    }

    public void setAssetsJson(String assetsJson) {
        this.assetsJson = assetsJson;
    }

    public String getAssetsNep5Json() {
        return assetsNep5Json;
    }

    public void setAssetsNep5Json(String assetsNep5Json) {
        this.assetsNep5Json = assetsNep5Json;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NeoWallet that = (NeoWallet) o;

        return mWalletAddr.equals(that.mWalletAddr);
    }

    @Override
    public int hashCode() {
        return mWalletAddr.hashCode();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mWalletName);
        dest.writeString(this.mWalletAddr);
        dest.writeInt(this.backupState);
        dest.writeString(this.keyStore);
        dest.writeString(this.assetsJson);
        dest.writeString(this.assetsNep5Json);
    }

    protected NeoWallet(Parcel in) {
        this.mWalletName = in.readString();
        this.mWalletAddr = in.readString();
        this.backupState = in.readInt();
        this.keyStore = in.readString();
        this.assetsJson = in.readString();
        this.assetsNep5Json = in.readString();
    }

    public static final Creator<NeoWallet> CREATOR = new Creator<NeoWallet>() {
        @Override
        public NeoWallet createFromParcel(Parcel source) {
            return new NeoWallet(source);
        }

        @Override
        public NeoWallet[] newArray(int size) {
            return new NeoWallet[size];
        }
    };

}
