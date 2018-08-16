package chinapex.com.wallet.bean;

import android.os.Parcelable;

/**
 * Created by SteelCabbage on 2018/8/15 0015 10:32.
 * E-Mail：liuyi_61@163.com
 */

public abstract class WalletBean implements Parcelable {

    public abstract void setWalletName(String walletName);

    public abstract String getWalletName();

    public abstract String getWalletAddress();

    public abstract void setAssets(String assetsJson);

    public abstract String getAssets();

    public abstract void setColorAssets(String colorAssetsJson);

    public abstract String getColorAssets();
}
