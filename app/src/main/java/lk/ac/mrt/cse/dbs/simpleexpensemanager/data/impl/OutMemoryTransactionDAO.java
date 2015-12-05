package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.Context;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DataAccessHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by User on 12/3/2015.
 */
public class OutMemoryTransactionDAO implements TransactionDAO {
    private DataAccessHandler dataAccessHandler;

    public OutMemoryTransactionDAO(Context context) {
        dataAccessHandler=new DataAccessHandler(context);
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        Transaction transaction = new Transaction(date, accountNo, expenseType, amount);
        dataAccessHandler.insertTransction(dataAccessHandler,transaction);
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        return dataAccessHandler.getAllTransactions();
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> list=dataAccessHandler.getAllTransactions();
        int size = list.size();
        if (size <= limit) {
            return list;
        }
        // return the last <code>limit</code> number of transaction logs
        return list.subList(size - limit, size);
    }

}
