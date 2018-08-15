package chinapex.com.wallet.bean.eth;

import android.os.Parcel;
import android.os.Parcelable;

import chinapex.com.wallet.bean.WalletBean;

/**
 * Created by SteelCabbage on 2018/8/13 0013 18:05.
 * E-Mail：liuyi_61@163.com
 */

public class EthWallet implements Parcelable, WalletBean {
    private String name;
    private String address;
    private int backupState;
    private String keyStore;
    private String assetsJson;
    private String assetsErc20Json;
    private boolean isSelected;
    private int selectedTag;

    public EthWallet() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getAssetsJson() {
        return assetsJson;
    }

    public void setAssetsJson(String assetsJson) {
        this.assetsJson = assetsJson;
    }

    public String getAssetsErc20Json() {
        return assetsErc20Json;
    }

    public void setAssetsErc20Json(String assetsErc20Json) {
        this.assetsErc20Json = assetsErc20Json;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EthWallet ethWallet = (EthWallet) o;

        return address.equals(ethWallet.address);
    }

    @Override
    public int hashCode() {
        return address.hashCode();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.address);
        dest.writeInt(this.backupState);
        dest.writeString(this.keyStore);
        dest.writeString(this.assetsJson);
        dest.writeString(this.assetsErc20Json);
    }

    protected EthWallet(Parcel in) {
        this.name = in.readString();
        this.address = in.readString();
        this.backupState = in.readInt();
        this.keyStore = in.readString();
        this.assetsJson = in.readString();
        this.assetsErc20Json = in.readString();
    }

    public static final Parcelable.Creator<EthWallet> CREATOR = new Parcelable.Creator<EthWallet>
            () {
        @Override
        public EthWallet createFromParcel(Parcel source) {
            return new EthWallet(source);
        }

        @Override
        public EthWallet[] newArray(int size) {
            return new EthWallet[size];
        }
    };
}
