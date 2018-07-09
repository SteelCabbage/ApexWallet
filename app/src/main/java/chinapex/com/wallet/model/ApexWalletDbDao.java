package chinapex.com.wallet.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import chinapex.com.wallet.bean.AssetBean;
import chinapex.com.wallet.bean.TransactionRecord;
import chinapex.com.wallet.bean.WalletBean;
import chinapex.com.wallet.global.Constant;
import chinapex.com.wallet.utils.CpLog;

/**
 * Created by SteelCabbage on 2018/3/29 0029.
 */

public class ApexWalletDbDao {

    private static final String TAG = ApexWalletDbDao.class.getSimpleName();

//    private static final String DB_PWD = "";

    private static ApexWalletDbDao sApexWalletDbDao;

    private AtomicInteger mOpenCounter = new AtomicInteger();

    private ApexWalletDbHelper mApexWalletDbHelper;

    private SQLiteDatabase mDatabase;

    private ApexWalletDbDao(Context context) {
        mApexWalletDbHelper = new ApexWalletDbHelper(context);
    }

    public static ApexWalletDbDao getInstance(Context context) {
        if (null == context) {
            CpLog.e(TAG, "context is null!");
            return null;
        }

        if (null == sApexWalletDbDao) {
            synchronized (ApexWalletDbDao.class) {
                if (null == sApexWalletDbDao) {
                    sApexWalletDbDao = new ApexWalletDbDao(context);
                }
            }
        }

        return sApexWalletDbDao;
    }

    public synchronized SQLiteDatabase openDatabase() {
        if (mOpenCounter.incrementAndGet() == 1) {
            mDatabase = mApexWalletDbHelper.getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        if (mOpenCounter.decrementAndGet() == 0) {
            mDatabase.close();
        }
    }


    public synchronized void insert(String tableName, WalletBean walletBean) {
        if (TextUtils.isEmpty(tableName) || null == walletBean) {
            CpLog.e(TAG, "insert() -> tableName or walletBean is null!");
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.FIELD_WALLET_NAME, walletBean.getWalletName());
        contentValues.put(Constant.FIELD_WALLET_ADDRESS, walletBean.getWalletAddr());
        contentValues.put(Constant.FIELD_BACKUP_STATE, walletBean.getBackupState());
        contentValues.put(Constant.FIELD_WALLET_KEYSTORE, walletBean.getKeyStore());
        contentValues.put(Constant.FIELD_WALLET_ASSETS_JSON, walletBean.getAssetsJson());
        contentValues.put(Constant.FIELD_WALLET_ASSETS_NEP5_JSON, walletBean.getAssetsNep5Json());
        contentValues.put(Constant.FIELD_CREATE_TIME, SystemClock.currentThreadTimeMillis());

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.insertOrThrow(tableName, null, contentValues);
            db.setTransactionSuccessful();
            CpLog.i(TAG, "insert() -> insert " + walletBean.getWalletName() + " ok!");
        } catch (SQLException e) {
            CpLog.e(TAG, "insert exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    private static final String WHERE_CLAUSE_WALLET_NAME_EQ_AND_ADDRESS_EQ = Constant
            .FIELD_WALLET_NAME + " = ?" + " and " + Constant.FIELD_WALLET_ADDRESS + " = ?";

    public void deleteByWalletNameAndAddr(String tableName, String walletName, String
            walletAddress) {
        if (TextUtils.isEmpty(walletName) || TextUtils.isEmpty(walletAddress)) {
            CpLog.e(TAG, "deleteByWalletName() -> walletName or walletAddress is null!");
            return;
        }

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.delete(tableName, WHERE_CLAUSE_WALLET_NAME_EQ_AND_ADDRESS_EQ, new
                    String[]{walletName, walletAddress});
            db.setTransactionSuccessful();
            CpLog.i(TAG, "deleteByWalletName() -> delete " + walletName + ":" + walletAddress + "" +
                    " ok!");
        } catch (Exception e) {
            CpLog.e(TAG, "deleteByWalletName exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    public List<WalletBean> queryWalletBeans(String tableName) {
        if (TextUtils.isEmpty(tableName)) {
            CpLog.e(TAG, "queryAll() -> tableName is null!");
            return null;
        }

        ArrayList<WalletBean> walletBeans = new ArrayList<>();

        SQLiteDatabase db = openDatabase();
        Cursor cursor = db.query(tableName, null, null, null, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                int walletNameIndex = cursor.getColumnIndex(Constant.FIELD_WALLET_NAME);
                int walletAddressIndex = cursor.getColumnIndex(Constant.FIELD_WALLET_ADDRESS);
                int backupStateIndex = cursor.getColumnIndex(Constant.FIELD_BACKUP_STATE);
                int walletKeystoreIndex = cursor.getColumnIndex(Constant.FIELD_WALLET_KEYSTORE);
                int walletAssetsJsonIndex = cursor.getColumnIndex(Constant
                        .FIELD_WALLET_ASSETS_JSON);
                int walletAssetsNep5JsonIndex = cursor.getColumnIndex(Constant
                        .FIELD_WALLET_ASSETS_NEP5_JSON);

                String walletName = cursor.getString(walletNameIndex);
                String walletAddress = cursor.getString(walletAddressIndex);
                int backupState = cursor.getInt(backupStateIndex);
                String walletKeystore = cursor.getString(walletKeystoreIndex);
                String walletAssetsJson = cursor.getString(walletAssetsJsonIndex);
                String walletAssetsNep5Json = cursor.getString(walletAssetsNep5JsonIndex);

                WalletBean walletBean = new WalletBean();
                walletBean.setWalletName(walletName);
                walletBean.setWalletAddr(walletAddress);
                walletBean.setBackupState(backupState);
                walletBean.setKeyStore(walletKeystore);
                walletBean.setAssetsJson(walletAssetsJson);
                walletBean.setAssetsNep5Json(walletAssetsNep5Json);

                walletBeans.add(walletBean);
            }
            cursor.close();
        }
        closeDatabase();
        return walletBeans;
    }

    private static final String WHERE_CLAUSE_WALLET_ADDRESS_EQ = Constant.FIELD_WALLET_ADDRESS +
            " = ?";

    public WalletBean queryByWalletAddress(String tableName, String walletAddress) {
        if (TextUtils.isEmpty(tableName)
                || TextUtils.isEmpty(walletAddress)) {
            CpLog.e(TAG, "queryByWalletName() -> tableName or walletAddress is null!");
            return null;
        }

        ArrayList<WalletBean> walletBeans = new ArrayList<>();

        SQLiteDatabase db = openDatabase();
        Cursor cursor = db.query(tableName, null, WHERE_CLAUSE_WALLET_ADDRESS_EQ, new
                String[]{walletAddress}, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                int walletNameIndex = cursor.getColumnIndex(Constant.FIELD_WALLET_NAME);
                int walletAddrIndex = cursor.getColumnIndex(Constant.FIELD_WALLET_ADDRESS);
                int backupStateIndex = cursor.getColumnIndex(Constant.FIELD_BACKUP_STATE);
                int walletKeystoreIndex = cursor.getColumnIndex(Constant.FIELD_WALLET_KEYSTORE);
                int walletAssetsJsonIndex = cursor.getColumnIndex(Constant
                        .FIELD_WALLET_ASSETS_JSON);
                int walletAssetsNep5JsonIndex = cursor.getColumnIndex(Constant
                        .FIELD_WALLET_ASSETS_NEP5_JSON);

                String walletName = cursor.getString(walletNameIndex);
                String walletAddr = cursor.getString(walletAddrIndex);
                int backupState = cursor.getInt(backupStateIndex);
                String walletKeystore = cursor.getString(walletKeystoreIndex);
                String walletAssetsJson = cursor.getString(walletAssetsJsonIndex);
                String walletAssetsNep5Json = cursor.getString(walletAssetsNep5JsonIndex);

                WalletBean walletBean = new WalletBean();
                walletBean.setWalletName(walletName);
                walletBean.setWalletAddr(walletAddr);
                walletBean.setBackupState(backupState);
                walletBean.setKeyStore(walletKeystore);
                walletBean.setAssetsJson(walletAssetsJson);
                walletBean.setAssetsNep5Json(walletAssetsNep5Json);

                walletBeans.add(walletBean);
            }
            cursor.close();
        }
        closeDatabase();
        return walletBeans.isEmpty() ? null : walletBeans.get(0);
    }

    public void updateBackupState(String tableName, String walletAddress, int backupState) {
        if (TextUtils.isEmpty(tableName) || TextUtils.isEmpty(walletAddress)) {
            CpLog.e(TAG, "updateBackupState() -> tableName or walletAddress is null!");
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.FIELD_BACKUP_STATE, backupState);

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.update(tableName, contentValues, WHERE_CLAUSE_WALLET_ADDRESS_EQ, new
                    String[]{walletAddress});
            db.setTransactionSuccessful();
            CpLog.i(TAG, "updateBackupState -> update" + walletAddress + " backupState ok!");
        } catch (SQLException e) {
            CpLog.e(TAG, "updateBackupState exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    public void updateWalletName(String tableName, String walletAddress, String
            walletNameNew) {
        if (TextUtils.isEmpty(tableName)
                || TextUtils.isEmpty(walletAddress)
                || TextUtils.isEmpty(walletNameNew)) {
            CpLog.e(TAG, "tableName or walletAddress or walletNameNew is null!");
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.FIELD_WALLET_NAME, walletNameNew);

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.update(tableName, contentValues, WHERE_CLAUSE_WALLET_ADDRESS_EQ, new
                    String[]{walletAddress});
            db.setTransactionSuccessful();
            CpLog.i(TAG, "updateWalletName: " + walletNameNew + " is ok!");
        } catch (SQLException e) {
            CpLog.e(TAG, "updateBackupStateByWalletName exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    public void updateCheckedAssets(WalletBean walletBean) {
        if (null == walletBean) {
            CpLog.e(TAG, "walletBean is null!");
            return;
        }

        String walletAddress = walletBean.getWalletAddr();
        if (TextUtils.isEmpty(walletAddress)) {
            CpLog.e(TAG, "walletAddress is null!");
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.FIELD_WALLET_ASSETS_NEP5_JSON, walletBean.getAssetsNep5Json());
        contentValues.put(Constant.FIELD_WALLET_ASSETS_JSON, walletBean.getAssetsJson());

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.update(Constant.TABLE_APEX_WALLET, contentValues, WHERE_CLAUSE_WALLET_ADDRESS_EQ, new
                    String[]{walletAddress});
            db.setTransactionSuccessful();
            CpLog.i(TAG, "updateCheckedAssets is ok!");
        } catch (SQLException e) {
            CpLog.e(TAG, "updateCheckedAssets exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    public synchronized void insertTxRecord(String tableName, TransactionRecord transactionRecord) {
        if (TextUtils.isEmpty(tableName) || null == transactionRecord) {
            CpLog.e(TAG, "insertTxRecord() -> tableName or transactionRecord is null!");
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.FIELD_WALLET_ADDRESS, transactionRecord.getWalletAddress());
        contentValues.put(Constant.FIELD_TX_TYPE, transactionRecord.getTxType());
        contentValues.put(Constant.FIELD_TX_ID, transactionRecord.getTxID());
        contentValues.put(Constant.FIELD_TX_AMOUNT, transactionRecord.getTxAmount());
        contentValues.put(Constant.FIELD_TX_STATE, transactionRecord.getTxState());
        contentValues.put(Constant.FIELD_TX_FROM, transactionRecord.getTxFrom());
        contentValues.put(Constant.FIELD_TX_TO, transactionRecord.getTxTo());
        contentValues.put(Constant.FIELD_GAS_CONSUMED, transactionRecord.getGasConsumed());
        contentValues.put(Constant.FIELD_ASSET_ID, transactionRecord.getAssetID());
        contentValues.put(Constant.FIELD_ASSET_SYMBOL, transactionRecord.getAssetSymbol());
        contentValues.put(Constant.FIELD_ASSET_LOGO_URL, transactionRecord.getAssetLogoUrl());
        contentValues.put(Constant.FIELD_ASSET_DECIMAL, transactionRecord.getAssetDecimal());
        contentValues.put(Constant.FIELD_CREATE_TIME, transactionRecord.getTxTime());

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.insertOrThrow(tableName, null, contentValues);
            db.setTransactionSuccessful();
//            CpLog.i(TAG, "insertTxRecord() -> insert " + transactionRecord.getTxID() + " ok!");
        } catch (SQLException e) {
            CpLog.e(TAG, "insertTxRecord exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    public List<TransactionRecord> queryTxByAddress(String tableName, String walletAddress) {
        List<TransactionRecord> transactionRecords = new ArrayList<>();

        if (TextUtils.isEmpty(tableName) || TextUtils.isEmpty(walletAddress)) {
            CpLog.e(TAG, "queryTxByAddress() -> tableName or walletAddress " +
                    "is null!");
            return transactionRecords;
        }

        SQLiteDatabase db = openDatabase();
        Cursor cursor = db.query(tableName, null, WHERE_CLAUSE_WALLET_ADDRESS_EQ, new
                String[]{walletAddress}, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                int walletAddressIndex = cursor.getColumnIndex(Constant.FIELD_WALLET_ADDRESS);
                int txTypeIndex = cursor.getColumnIndex(Constant.FIELD_TX_TYPE);
                int txIdIndex = cursor.getColumnIndex(Constant.FIELD_TX_ID);
                int txAmountIndex = cursor.getColumnIndex(Constant.FIELD_TX_AMOUNT);
                int txStateIndex = cursor.getColumnIndex(Constant.FIELD_TX_STATE);
                int txFromIndex = cursor.getColumnIndex(Constant.FIELD_TX_FROM);
                int txToIndex = cursor.getColumnIndex(Constant.FIELD_TX_TO);
                int gasConsumedIndex = cursor.getColumnIndex(Constant.FIELD_GAS_CONSUMED);
                int assetIdIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_ID);
                int assetSymbolIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_SYMBOL);
                int assetLogoUrlIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_LOGO_URL);
                int assetDecimalIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_DECIMAL);
                int createTimeIndex = cursor.getColumnIndex(Constant.FIELD_CREATE_TIME);


                String walletAddressTmp = cursor.getString(walletAddressIndex);
                String txType = cursor.getString(txTypeIndex);
                String txId = cursor.getString(txIdIndex);
                String txAmount = cursor.getString(txAmountIndex);
                int txState = cursor.getInt(txStateIndex);
                String txFrom = cursor.getString(txFromIndex);
                String txTo = cursor.getString(txToIndex);
                String gasConsumed = cursor.getString(gasConsumedIndex);
                String assetId = cursor.getString(assetIdIndex);
                String assetSymbol = cursor.getString(assetSymbolIndex);
                String assetLogoUrl = cursor.getString(assetLogoUrlIndex);
                int assetDecimal = cursor.getInt(assetDecimalIndex);
                long createTime = cursor.getLong(createTimeIndex);

                TransactionRecord transactionRecord = new TransactionRecord();
                transactionRecord.setWalletAddress(walletAddressTmp);
                transactionRecord.setTxType(txType);
                transactionRecord.setTxID(txId);
                transactionRecord.setTxAmount(txAmount);
                transactionRecord.setTxState(txState);
                transactionRecord.setTxFrom(txFrom);
                transactionRecord.setTxTo(txTo);
                transactionRecord.setGasConsumed(gasConsumed);
                transactionRecord.setAssetID(assetId);
                transactionRecord.setAssetSymbol(assetSymbol);
                transactionRecord.setAssetLogoUrl(assetLogoUrl);
                transactionRecord.setAssetDecimal(assetDecimal);
                transactionRecord.setTxTime(createTime);

                transactionRecords.add(transactionRecord);
            }
            cursor.close();
        }
        closeDatabase();
        return transactionRecords;
    }

    public HashMap<String, TransactionRecord> queryTxCacheByAddress(String tableName, String
            walletAddress) {
        HashMap<String, TransactionRecord> txHashMap = new HashMap<>();

        if (TextUtils.isEmpty(tableName) || TextUtils.isEmpty(walletAddress)) {
            CpLog.e(TAG, "queryTxCacheByAddress() -> tableName or walletAddress is null!");
            return txHashMap;
        }

        SQLiteDatabase db = openDatabase();
        Cursor cursor = db.query(tableName, null, WHERE_CLAUSE_WALLET_ADDRESS_EQ, new
                String[]{walletAddress}, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                int walletAddressIndex = cursor.getColumnIndex(Constant.FIELD_WALLET_ADDRESS);
                int txTypeIndex = cursor.getColumnIndex(Constant.FIELD_TX_TYPE);
                int txIdIndex = cursor.getColumnIndex(Constant.FIELD_TX_ID);
                int txAmountIndex = cursor.getColumnIndex(Constant.FIELD_TX_AMOUNT);
                int txStateIndex = cursor.getColumnIndex(Constant.FIELD_TX_STATE);
                int txFromIndex = cursor.getColumnIndex(Constant.FIELD_TX_FROM);
                int txToIndex = cursor.getColumnIndex(Constant.FIELD_TX_TO);
                int gasConsumedIndex = cursor.getColumnIndex(Constant.FIELD_GAS_CONSUMED);
                int assetIdIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_ID);
                int assetSymbolIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_SYMBOL);
                int assetLogoUrlIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_LOGO_URL);
                int assetDecimalIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_DECIMAL);
                int createTimeIndex = cursor.getColumnIndex(Constant.FIELD_CREATE_TIME);


                String walletAddressTmp = cursor.getString(walletAddressIndex);
                String txType = cursor.getString(txTypeIndex);
                String txId = cursor.getString(txIdIndex);
                String txAmount = cursor.getString(txAmountIndex);
                int txState = cursor.getInt(txStateIndex);
                String txFrom = cursor.getString(txFromIndex);
                String txTo = cursor.getString(txToIndex);
                String gasConsumed = cursor.getString(gasConsumedIndex);
                String assetId = cursor.getString(assetIdIndex);
                String assetSymbol = cursor.getString(assetSymbolIndex);
                String assetLogoUrl = cursor.getString(assetLogoUrlIndex);
                int assetDecimal = cursor.getInt(assetDecimalIndex);
                long createTime = cursor.getLong(createTimeIndex);

                TransactionRecord transactionRecord = new TransactionRecord();
                transactionRecord.setWalletAddress(walletAddressTmp);
                transactionRecord.setTxType(txType);
                transactionRecord.setTxID(txId);
                transactionRecord.setTxAmount(txAmount);
                transactionRecord.setTxState(txState);
                transactionRecord.setTxFrom(txFrom);
                transactionRecord.setTxTo(txTo);
                transactionRecord.setGasConsumed(gasConsumed);
                transactionRecord.setAssetID(assetId);
                transactionRecord.setAssetSymbol(assetSymbol);
                transactionRecord.setAssetLogoUrl(assetLogoUrl);
                transactionRecord.setAssetDecimal(assetDecimal);
                transactionRecord.setTxTime(createTime);

                txHashMap.put(txId, transactionRecord);
            }
            cursor.close();
        }
        closeDatabase();
        return txHashMap;
    }

    private static final String WHERE_CLAUSE_FIELD_TX_ID_EQ = Constant.FIELD_TX_ID + " = ?";

    public void delCacheByTxIDAndAddr(String tableName, String txID, String walletAddress) {
        if (TextUtils.isEmpty(tableName)
                || TextUtils.isEmpty(txID)
                || TextUtils.isEmpty(walletAddress)) {
            CpLog.e(TAG, "delCacheByTxIDAndAddr() -> tableName or txID or walletAddress is null!");
            return;
        }

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.delete(tableName,
                    WHERE_CLAUSE_FIELD_TX_ID_EQ + " and " + WHERE_CLAUSE_WALLET_ADDRESS_EQ,
                    new String[]{txID, walletAddress});
            db.setTransactionSuccessful();
            CpLog.i(TAG, "delCacheByTxIDAndAddr() -> delete:" + txID + " ok!");
        } catch (Exception e) {
            CpLog.e(TAG, "delCacheByTxIDAndAddr exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    private static final String WHERE_CLAUSE_TX_ID_EQ = Constant.FIELD_TX_ID + " = ?";

    public void updateTxState(String txID, int txState) {
        if (TextUtils.isEmpty(txID)) {
            CpLog.e(TAG, "updateTxState() -> txID is null!");
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.FIELD_TX_STATE, txState);

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.update(Constant.TABLE_TRANSACTION_RECORD, contentValues, WHERE_CLAUSE_TX_ID_EQ,
                    new String[]{txID});
            db.setTransactionSuccessful();
            CpLog.i(TAG, "updateTxState -> update: " + txID + " ok!");
        } catch (SQLException e) {
            CpLog.e(TAG, "updateTxState exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    public synchronized void insertAsset(AssetBean assetBean) {
        if (null == assetBean) {
            CpLog.e(TAG, "insertAsset() -> assetBean is null!");
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.FIELD_ASSET_TYPE, assetBean.getType());
        contentValues.put(Constant.FIELD_ASSET_SYMBOL, assetBean.getSymbol());
        contentValues.put(Constant.FIELD_ASSET_PRECISION, assetBean.getPrecision());
        contentValues.put(Constant.FIELD_ASSET_NAME, assetBean.getName());
        contentValues.put(Constant.FIELD_ASSET_IMAGE_URL, assetBean.getImageUrl());
        contentValues.put(Constant.FIELD_ASSET_HEX_HASH, assetBean.getHexHash());
        contentValues.put(Constant.FIELD_ASSET_HASH, assetBean.getHash());
        contentValues.put(Constant.FIELD_CREATE_TIME, SystemClock.currentThreadTimeMillis());

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.insertOrThrow(Constant.TABLE_ASSETS, null, contentValues);
            db.setTransactionSuccessful();
            CpLog.i(TAG, "insert() -> insert " + assetBean.getSymbol() + " ok!");
        } catch (SQLException e) {
            CpLog.e(TAG, "insert exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    private static final String WHERE_CLAUSE_ASSET_TYPE_EQ = Constant.FIELD_ASSET_TYPE + " = ?";

    public List<AssetBean> queryAssetsByType(String assetType) {
        ArrayList<AssetBean> assetBeans = new ArrayList<>();

        if (TextUtils.isEmpty(assetType)) {
            CpLog.e(TAG, "queryAssetsByType() -> assetType is null!");
            return assetBeans;
        }

        SQLiteDatabase db = openDatabase();
        Cursor cursor = db.query(Constant.TABLE_ASSETS, null, WHERE_CLAUSE_ASSET_TYPE_EQ, new
                String[]{assetType}, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                int assetTypeIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_TYPE);
                int assetSymbolIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_SYMBOL);
                int assetPrecisionIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_PRECISION);
                int assetNameIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_NAME);
                int assetImageUrlIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_IMAGE_URL);
                int assetHexHashIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_HEX_HASH);
                int assetHashIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_HASH);

                String assetTypeTmp = cursor.getString(assetTypeIndex);
                String assetSymbol = cursor.getString(assetSymbolIndex);
                String assetPrecision = cursor.getString(assetPrecisionIndex);
                String assetName = cursor.getString(assetNameIndex);
                String assetImageUrl = cursor.getString(assetImageUrlIndex);
                String assetHexHash = cursor.getString(assetHexHashIndex);
                String assetHash = cursor.getString(assetHashIndex);


                AssetBean assetBean = new AssetBean();
                assetBean.setType(assetTypeTmp);
                assetBean.setSymbol(assetSymbol);
                assetBean.setPrecision(assetPrecision);
                assetBean.setName(assetName);
                assetBean.setImageUrl(assetImageUrl);
                assetBean.setHexHash(assetHexHash);
                assetBean.setHash(assetHash);

                assetBeans.add(assetBean);
            }
            cursor.close();
        }
        closeDatabase();
        return assetBeans;
    }

    private static final String WHERE_CLAUSE_ASSET_HEX_HASH_EQ = Constant.FIELD_ASSET_HEX_HASH +
            " = ?";

    public AssetBean queryAssetByHash(String assetHexHash) {
        if (TextUtils.isEmpty(assetHexHash)) {
            CpLog.e(TAG, "queryAssetsByType() -> assetType is null!");
            return null;
        }

        AssetBean assetBean = new AssetBean();
        SQLiteDatabase db = openDatabase();
        Cursor cursor = db.query(Constant.TABLE_ASSETS, null, WHERE_CLAUSE_ASSET_HEX_HASH_EQ, new
                String[]{assetHexHash}, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                int assetTypeIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_TYPE);
                int assetSymbolIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_SYMBOL);
                int assetPrecisionIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_PRECISION);
                int assetNameIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_NAME);
                int assetImageUrlIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_IMAGE_URL);
                int assetHexHashIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_HEX_HASH);
                int assetHashIndex = cursor.getColumnIndex(Constant.FIELD_ASSET_HASH);

                String assetTypeTmp = cursor.getString(assetTypeIndex);
                String assetSymbol = cursor.getString(assetSymbolIndex);
                String assetPrecision = cursor.getString(assetPrecisionIndex);
                String assetName = cursor.getString(assetNameIndex);
                String assetImageUrl = cursor.getString(assetImageUrlIndex);
                String assetHexHashTmp = cursor.getString(assetHexHashIndex);
                String assetHash = cursor.getString(assetHashIndex);

                assetBean.setType(assetTypeTmp);
                assetBean.setSymbol(assetSymbol);
                assetBean.setPrecision(assetPrecision);
                assetBean.setName(assetName);
                assetBean.setImageUrl(assetImageUrl);
                assetBean.setHexHash(assetHexHashTmp);
                assetBean.setHash(assetHash);
            }
            cursor.close();
        }
        closeDatabase();
        return assetBean;
    }

}
