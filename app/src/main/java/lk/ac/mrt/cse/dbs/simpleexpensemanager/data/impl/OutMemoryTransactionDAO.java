package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by User on 12/3/2015.
 */
public class OutMemoryTransactionDAO implements TransactionDAO {
    private DBManager DBManager;

    public OutMemoryTransactionDAO(Context context) {
        DBManager =new DBManager(context);
    }

    //record the transactions in the database
    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase SQ = DBManager.getWritableDatabase();
        ContentValues Con_values = new ContentValues();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Con_values.put(Database.Table_Transaction.user_id,accountNo);
        Con_values.put(Database.Table_Transaction.date,df.format(date));
        Con_values.put(Database.Table_Transaction.type,expenseType.toString());
        Con_values.put(Database.Table_Transaction.amount,amount);
        SQ.insert(Database.Table_Transaction.transaction_info, null, Con_values);
        SQ.close();
    }

    //Gets the all the transactions from the database
    @Override
    public List<Transaction> getAllTransactionLogs() {
        return getAllTransactions();    //call for private method getAllTransactions()
}

    //get given number of transactions
    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> list=getAllTransactions();     //call for private method getAllTransactions()
        int size = list.size();
        if (size <= limit) {
            return list;
        }
        // return the last <code>limit</code> number of transaction logs
        return list.subList(size - limit, size);
    }

    //private method for getting all transactions from the database
    private List<Transaction> getAllTransactions() {
        List<Transaction> transactionList = new ArrayList<Transaction>();
        String selectQuery = "SELECT * FROM " + Database.Table_Transaction.transaction_info;

        SQLiteDatabase db = DBManager.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        Date date = null;
        if (cursor.moveToFirst()) {
            do {
                String user_id=cursor.getString(cursor.getColumnIndex(Database.Table_Transaction.user_id));
                String date_string=cursor.getString(cursor.getColumnIndex(Database.Table_Transaction.date));
                DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
                try {
                    date = df.parse(date_string);
                    Log.d("Database operation",date_string);
                } catch (ParseException e) {
                    e.printStackTrace();
                    Log.d("Database operation","Come4");
                    Log.d("Database operation",date_string);
                }
                ExpenseType expenseType =   ExpenseType.valueOf(cursor.getString(cursor.getColumnIndex(Database.Table_Transaction.type)));
                double amount=cursor.getDouble(cursor.getColumnIndex(Database.Table_Transaction.amount));
                Transaction transaction = new Transaction(date, user_id,expenseType, amount);
                transactionList.add(transaction);
            } while (cursor.moveToNext());
        }
        return transactionList;
    }


}
