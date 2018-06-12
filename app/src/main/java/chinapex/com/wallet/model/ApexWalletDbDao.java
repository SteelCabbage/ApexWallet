package chinapex.com.wallet.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.SystemClock;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

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

                String walletName = cursor.getString(walletNameIndex);
                String walletAddress = cursor.getString(walletAddressIndex);
                int backupState = cursor.getInt(backupStateIndex);
                String walletKeystore = cursor.getString(walletKeystoreIndex);
                String walletAssetsJson = cursor.getString(walletAssetsJsonIndex);

                WalletBean walletBean = new WalletBean();
                walletBean.setWalletName(walletName);
                walletBean.setWalletAddr(walletAddress);
                walletBean.setBackupState(backupState);
                walletBean.setKeyStore(walletKeystore);
                walletBean.setAssetsJson(walletAssetsJson);

                walletBeans.add(walletBean);
            }
            cursor.close();
        }
        closeDatabase();
        return walletBeans;
    }

    private static final String WHERE_CLAUSE_WALLET_NAME_EQ = Constant.FIELD_WALLET_NAME + " = ?";

    public WalletBean queryByWalletName(String tableName, String walletName) {
        if (TextUtils.isEmpty(tableName)
                || TextUtils.isEmpty(walletName)) {
            CpLog.e(TAG, "queryByWalletName() -> tableName or walletName is null!");
            return null;
        }

        ArrayList<WalletBean> walletBeans = new ArrayList<>();

        SQLiteDatabase db = openDatabase();
        Cursor cursor = db.query(tableName, null, WHERE_CLAUSE_WALLET_NAME_EQ, new
                String[]{walletName}, null, null, null);
        if (null != cursor) {
            while (cursor.moveToNext()) {
                int walletNameIndex = cursor.getColumnIndex(Constant.FIELD_WALLET_NAME);
                int walletAddressIndex = cursor.getColumnIndex(Constant.FIELD_WALLET_ADDRESS);
                int backupStateIndex = cursor.getColumnIndex(Constant.FIELD_BACKUP_STATE);
                int walletKeystoreIndex = cursor.getColumnIndex(Constant.FIELD_WALLET_KEYSTORE);
                int walletAssetsJsonIndex = cursor.getColumnIndex(Constant
                        .FIELD_WALLET_ASSETS_JSON);

                String walletNameTmp = cursor.getString(walletNameIndex);
                String walletAddress = cursor.getString(walletAddressIndex);
                int backupState = cursor.getInt(backupStateIndex);
                String walletKeystore = cursor.getString(walletKeystoreIndex);
                String walletAssetsJson = cursor.getString(walletAssetsJsonIndex);

                WalletBean walletBean = new WalletBean();
                walletBean.setWalletName(walletNameTmp);
                walletBean.setWalletAddr(walletAddress);
                walletBean.setBackupState(backupState);
                walletBean.setKeyStore(walletKeystore);
                walletBean.setAssetsJson(walletAssetsJson);

                walletBeans.add(walletBean);
            }
            cursor.close();
        }
        closeDatabase();
        return walletBeans.isEmpty() ? null : walletBeans.get(0);
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

                String walletName = cursor.getString(walletNameIndex);
                String walletAddr = cursor.getString(walletAddrIndex);
                int backupState = cursor.getInt(backupStateIndex);
                String walletKeystore = cursor.getString(walletKeystoreIndex);
                String walletAssetsJson = cursor.getString(walletAssetsJsonIndex);

                WalletBean walletBean = new WalletBean();
                walletBean.setWalletName(walletName);
                walletBean.setWalletAddr(walletAddr);
                walletBean.setBackupState(backupState);
                walletBean.setKeyStore(walletKeystore);
                walletBean.setAssetsJson(walletAssetsJson);

                walletBeans.add(walletBean);
            }
            cursor.close();
        }
        closeDatabase();
        return walletBeans.isEmpty() ? null : walletBeans.get(0);
    }

    public void updateBackupState(String tableName, String walletAddress, int backupState) {
        if (TextUtils.isEmpty(tableName) || TextUtils.isEmpty(walletAddress)) {
            CpLog.e(TAG, "insert() -> tableName or walletAddress is null!");
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.FIELD_BACKUP_STATE, backupState);

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.update(tableName, contentValues, WHERE_CLAUSE_WALLET_ADDRESS_EQ, new
                    String[]{walletAddress + ""});
            db.setTransactionSuccessful();
            CpLog.i(TAG, "updateBackupState -> update" + backupState + " ok!");
        } catch (SQLException e) {
            CpLog.e(TAG, "updateBackupState exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }

    public void updateWalletName(String tableName, String walletNameOld, String
            walletNameNew) {
        if (TextUtils.isEmpty(tableName)
                || TextUtils.isEmpty(walletNameOld)
                || TextUtils.isEmpty(walletNameNew)) {
            CpLog.e(TAG, "insert() -> tableName or walletName or walletNameNew is null!");
            return;
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(Constant.FIELD_WALLET_NAME, walletNameNew);

        SQLiteDatabase db = openDatabase();
        try {
            db.beginTransaction();
            db.update(tableName, contentValues, WHERE_CLAUSE_WALLET_NAME_EQ, new
                    String[]{walletNameOld + ""});
            db.setTransactionSuccessful();
            CpLog.i(TAG, "updateBackupStateByWalletName() -> update " + walletNameOld + " ok!");
        } catch (SQLException e) {
            CpLog.e(TAG, "updateBackupStateByWalletName exception:" + e.getMessage());
        } finally {
            db.endTransaction();
        }
        closeDatabase();
    }
}
