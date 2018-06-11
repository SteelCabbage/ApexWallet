package chinapex.com.wallet.global;

/**
 * Created by SteelCabbage on 2018/5/21 0021.
 */

public class Constant {
    // application state value
    public static final String IS_FIRST_ENTER = "isFirstEnter";

    // net
    public static final long CONNECT_TIMEOUT = 5;
    public static final long READ_TIMEOUT = 5;
    public static final long WRITE_TIMEOUT = 5;
    public static final int NET_ERROR = -1;
    public static final int NET_SUCCESS = 1;
    public static final int NET_BODY_NULL = 0;
    //    public static final String HOSTNAME_VERIFIER = "40.125.171.0";
//    public static final String URL_CLI = "http://40.125.171.0:20332";
//    public static final String URL_UTXOS = "http://40.125.171.0:8083/utxos/";
    public static final String HOSTNAME_VERIFIER = "dev.chinapex.com.cn";
    public static final String URL_CLI = "http://dev.chinapex.com.cn:10086/neo-cli/";
    public static final String URL_UTXOS = "http://dev.chinapex.com.cn:10086/tool/utxos/";

    // fragmentTag
    public static final String FRAGMENT_TAG_IMPORT_MNEMONIC = "ImportMnemonicFragment";
    public static final String FRAGMENT_TAG_IMPORT_KEYSTORE = "ImportKeystoreFragment";
    public static final String FRAGMENT_TAG_BACKUP = "BackupFragment";
    public static final String FRAGMENT_TAG_COPY_MNEMONIC = "CopyMnemonicFragment";
    public static final String FRAGMENT_TAG_CONFIRM_MNEMONIC = "ConfirmMnemonicFragment";
    public static final String FRAGMENT_TAG_ME_MANAGE_DETAIL = "MeManageDetailFragment";
    public static final String FRAGMENT_TAG_ME_TRANSACTION_RECORD = "MeTransactionRecordFragment";

    // import wallet from mnemonic
    public static final String WALLET_NAME_IMPORT_DEFAULT = "IMPORT_DEFAULT";

    // assets
    public static final String WALLET_BEAN = "walletBean";
    public static final String BALANCE_BEAN = "balanceBean";

    // me
    public static final String ME_MANAGER_DETAIL_BUNDLE = "meManagerDetailBundle";
    public static final String ME_SKIP_ACTIVITY_FRAGMENT_TAG = "meSkipActivityFragmentTag";
    public static final String PARCELABLE_WALLET_BEAN_MANAGE_DETAIL =
            "parcelableWalletBeanManageDetail";

    // backup wallet
    public static final String BACKUP_MNEMONIC = "backupMnemonic";
    public static final int BACKUP_UNFINISHED = 0;
    public static final int BACKUP_FINISH = 1;

    // wallet detail transfer
    public static final String PARCELABLE_WALLET_BEAN_TRANSFER = "parcelableWalletBeanTransfer";
    // wallet detail gathering
    public static final String PARCELABLE_WALLET_BEAN_GATHERING = "parcelableWalletBeanGathering";

    // asset name
    public static final String ASSETS_NEO =
            "0xc56f33fc6ecfcd0c225c4ab356fee59390af8560be0e930faebe74a6daff7c9b";
    public static final String ASSETS_NEO_GAS =
            "0x602c79718b16e442de58778e148d0b1084e3b2dffd5de6b7b16cee7969282de7";
    public static final String ASSETS_CPX = "";

    public static final String MARK_NEO = "NEO";
    public static final String MARK_NEO_GAS = "NeoGas";
    public static final String MARK_CPX = "CPX";

    public static final String UNIT_NEO = "neo";
    public static final String UNIT_NEO_GAS = "gas";
    public static final String UNIT_CPX = "cpx";

    // wallet title
    public static final String WALLET_NAME = "Wallet ";

    // selected tag
    public static final int SELECTED_TAG_MANAGER_WALLET = 0;
    public static final int SELECTED_TAG_TRANSACTION_RECORED = 1;


    // db
    public static final String TABLE_APEX_WALLET = "apex_wallet";

    public static final String FIELD_ID = "_id";
    public static final String FIELD_WALLET_NAME = "wallet_name";
    public static final String FIELD_WALLET_ADDRESS = "wallet_address";
    public static final String FIELD_WALLET_ASSETS_ID = "assets_id";
    public static final String FIELD_WALLET_BALANCE = "wallet_balance";
    public static final String FIELD_BACKUP_STATE = "backup_state";
    public static final String FIELD_WALLET_KEYSTORE = "wallet_keystore";
    public static final String FIELD_CREATE_TIME = "create_time";

    public static final String SQL_CREATE_APEX_WALLET = "create table " + TABLE_APEX_WALLET
            + " (" + FIELD_ID + " integer primary key autoincrement, "
            + FIELD_WALLET_NAME + " text, "
            + FIELD_WALLET_ADDRESS + " text, "
            + FIELD_WALLET_ASSETS_ID + " text, "
            + FIELD_WALLET_BALANCE + " real, "
            + FIELD_BACKUP_STATE + " integer, "
            + FIELD_WALLET_KEYSTORE + " text, "
            + FIELD_CREATE_TIME + " integer)";

}
