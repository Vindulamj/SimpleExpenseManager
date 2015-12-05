package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by User on 12/3/2015.
 */
public class DataAccessHandler extends SQLiteOpenHelper {

    static final int DB_VERSION = 1;
    private static final String CREATE_TABLE_Account = "CREATE TABLE " + Database.Table_Account.account_info+ "(" + Database.Table_Account.user_id
            + " TEXT PRIMARY KEY," + Database.Table_Account.bank + " TEXT NOT NULL,"+ Database.Table_Account.holder + " TEXT NOT NULL," + Database.Table_Account.balance + " NUMERIC(10,2) NOT NULL);";

    private static final String CREATE_TABLE_Transaction = "CREATE TABLE " + Database.Table_Transaction.transaction_info+ "("+ Database.Table_Transaction.id+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Database.Table_Transaction.user_id
            + " TEXT," + Database.Table_Transaction.date + " TEXT NOT NULL,"+ Database.Table_Transaction.type + " TEXT NOT NULL," + Database.Table_Transaction.amount
            + " NUMERIC(10,2) NOT NULL, FOREIGN KEY (" + Database.Table_Transaction.user_id+") REFERENCES "+ Database.Table_Account.account_info + ");";

    public DataAccessHandler(Context context){
        super(context, Database.DB_NAME,null,DB_VERSION);
        //Log.d("Database Operation","Database Created");
    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_TABLE_Account);
       // Log.d("Database Operation","Table1 Created");
        sdb.execSQL(CREATE_TABLE_Transaction);
        //Log.d("Database Operation","Table2 Created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Database.Table_Account.account_info);
        db.execSQL("DROP TABLE IF EXISTS " + Database.Table_Transaction.transaction_info);
        onCreate(db);
    }

    public void insertAccount(DataAccessHandler DHandler,String id,String bank,String holder, double balance){
        SQLiteDatabase SQ = DHandler.getWritableDatabase();
        ContentValues Con_values = new ContentValues();
        Con_values.put(Database.Table_Account.user_id,id);
        Con_values.put(Database.Table_Account.bank,bank);
        Con_values.put(Database.Table_Account.holder,holder);
        Con_values.put(Database.Table_Account.balance,balance);
        SQ.insert(Database.Table_Account.account_info, null, Con_values);
        SQ.close();
       // Log.d("Database Operation","Row Inserted to Account");
    }

    public ArrayList<String> getAllAccountsNo() {
        ArrayList<String> accountNo_list = new ArrayList<String>();
        String selectQuery = "SELECT id FROM " + Database.Table_Account.account_info;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String id = new String();
                id= cursor.getString(0);

                accountNo_list.add(id);
            } while (cursor.moveToNext());
        }
        return accountNo_list;
    }

    public ArrayList<Account> getAllAccounts() {
        ArrayList<Account> accountNo_list = new ArrayList<Account>();
        String selectQuery = "SELECT * FROM " + Database.Table_Account.account_info;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String id= cursor.getString(0);
                String bank= cursor.getString(1);
                String holder= cursor.getString(2);
                Double balance= cursor.getDouble(3);
                Account account =new Account(id,bank,holder,balance);

                accountNo_list.add(account);
            } while (cursor.moveToNext());
        }
        return accountNo_list;
    }

    public Account getAccount(String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(Database.Table_Account.account_info, new String[] {Database.Table_Account.user_id,
                        Database.Table_Account.bank, Database.Table_Account.holder, Database.Table_Account.balance}, Database.Table_Account.user_id + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            String user_id= cursor.getString(0);
            String bank= cursor.getString(1);
            String holder= cursor.getString(2);
            Double balance= cursor.getDouble(3);
            Account account =new Account(user_id,bank,holder,balance);
            return account;
        }
        return null;
    }

    public int updateAccountBalance(Account account) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(Database.Table_Account.balance, account.getBalance());

        return db.update(Database.Table_Account.account_info, values, Database.Table_Account.user_id + " = ?",
                new String[] { String.valueOf(account.getAccountNo()) });
    }

    public void deleteAccount(String account_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Database.Table_Account.account_info, Database.Table_Account.user_id + " = ?",
                new String[] { String.valueOf(account_id) });
        db.close();
    }

    public void insertTransction(DataAccessHandler DHandler,Transaction transaction) {
        SQLiteDatabase SQ = DHandler.getWritableDatabase();

        ContentValues Con_values = new ContentValues();
        Con_values.put(Database.Table_Transaction.user_id,transaction.getAccountNo());
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String date=df.format(transaction.getDate());
        Con_values.put(Database.Table_Transaction.date,date);
        Con_values.put(Database.Table_Transaction.type,transaction.getExpenseType().toString());
        Con_values.put(Database.Table_Transaction.amount,transaction.getAmount());
        SQ.insert(Database.Table_Transaction.transaction_info, null, Con_values);
        SQ.close();
        //Log.d("Database Operation","Row Inserted to Transaction");
    }

    public List<Transaction> getAllTransactions() {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        String selectQuery = "SELECT * FROM " + Database.Table_Transaction.transaction_info;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String user_id=cursor.getString(1);
                String date_string=cursor.getString(2);
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                Date date = null;
                try {
                    date = df.parse(date_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                ExpenseType expenseType =   ExpenseType.valueOf(cursor.getString(3));
                double amount=cursor.getDouble(4);
                Transaction transaction = new Transaction(date, user_id,expenseType, amount);
                transactionList.add(transaction);

            } while (cursor.moveToNext());
        }
        return transactionList;
    }



}
