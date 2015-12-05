package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.OutMemoryAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.OutMemoryTransactionDAO;

/**
 * Created by User on 12/4/2015.
 */
public class PersistentExpenseManager extends ExpenseManager{

    public PersistentExpenseManager(Context context) {
        setup(context);
    }

    @Override
    public void setup(Context context) {

        TransactionDAO outMemoryTransactionDAO = new OutMemoryTransactionDAO(context);
        setTransactionsDAO(outMemoryTransactionDAO);

        AccountDAO outMemoryAccountDAO = new OutMemoryAccountDAO(context);
        setAccountsDAO(outMemoryAccountDAO);

    }

}
