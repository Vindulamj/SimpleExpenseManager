/*
 * Copyright 2015 Department of Computer Science and Engineering, University of Moratuwa.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *                  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.DBManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;


public class OutMemoryAccountDAO implements AccountDAO {
    private DBManager DBManager;

    public OutMemoryAccountDAO(Context context) {
        DBManager =new DBManager(context);
    }

    //this method is for returning all the account numbers from the database
    @Override
    public List<String> getAccountNumbersList() {

        ArrayList<String> accountNo_list = new ArrayList<String>();
        String selectQuery = "SELECT id FROM " + Database.Table_Account.account_info;

        SQLiteDatabase db = DBManager.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String id = new String();
                id= cursor.getString(cursor.getColumnIndex(Database.Table_Account.user_id));

                accountNo_list.add(id);
            } while (cursor.moveToNext());
        }
        return accountNo_list;
    }

    //this method is for get all the accounts from the database
    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> accountNo_list = new ArrayList<Account>();
        String selectQuery = "SELECT * FROM " + Database.Table_Account.account_info;

        SQLiteDatabase db = DBManager.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                String id= cursor.getString(cursor.getColumnIndex(Database.Table_Account.user_id));
                String bank= cursor.getString(cursor.getColumnIndex(Database.Table_Account.bank));;
                String holder= cursor.getString(cursor.getColumnIndex(Database.Table_Account.holder));
                Double balance= cursor.getDouble(cursor.getColumnIndex(Database.Table_Account.balance));
                Account account =new Account(id,bank,holder,balance);

                accountNo_list.add(account);
            } while (cursor.moveToNext());
        }
        return accountNo_list;
    }

    //this method returns only one account which has the given account number
    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Account account =getOneAccount(accountNo);  //call for the private method getOneAccount
        if (account!=null) {
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    //Method for a adding a new account
    @Override
    public void addAccount(Account account) {
        SQLiteDatabase SQ = DBManager.getWritableDatabase();
        ContentValues Con_values = new ContentValues();
        Con_values.put(Database.Table_Account.user_id,account.getAccountNo());
        Con_values.put(Database.Table_Account.bank,account.getBankName());
        Con_values.put(Database.Table_Account.holder,account.getAccountHolderName());
        Con_values.put(Database.Table_Account.balance,account.getBalance());
        SQ.insert(Database.Table_Account.account_info, null, Con_values);
        SQ.close();
    }

    //Method for removing a new account
    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        Account account =getOneAccount(accountNo);   //call for the private method getOneAccount
        if (account!=null) {
            SQLiteDatabase db = DBManager.getWritableDatabase();
            db.delete(Database.Table_Account.account_info, Database.Table_Account.user_id + " = ?",
                    new String[] { String.valueOf(accountNo) });
            db.close();
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    //update the balance of the account given by account id
    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account =getOneAccount(accountNo);                   //call for the private method getOneAccount
        if (account!=null) {
            switch (expenseType) {
                case EXPENSE:
                    account.setBalance(account.getBalance() - amount);
                    break;
                case INCOME:
                    account.setBalance(account.getBalance() + amount);
                    break;
            }
            SQLiteDatabase db = DBManager.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(Database.Table_Account.balance, account.getBalance());
            db.update(Database.Table_Account.account_info, values, Database.Table_Account.user_id + " = ?",
                    new String[] { String.valueOf(account.getAccountNo()) });
        }
        else{
            String msg = "Account " + accountNo + " is invalid.";
            throw new InvalidAccountException(msg);
        }
    }
    //this method is privately maintained and used to get a specific account with its account no.
    private Account getOneAccount(String id) {
        SQLiteDatabase db = DBManager.getWritableDatabase();

        Cursor cursor = db.query(Database.Table_Account.account_info, new String[] {Database.Table_Account.user_id,
                        Database.Table_Account.bank, Database.Table_Account.holder, Database.Table_Account.balance}, Database.Table_Account.user_id + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null){
            cursor.moveToFirst();
            String user_id= cursor.getString(cursor.getColumnIndex(Database.Table_Account.user_id));
            String bank= cursor.getString(cursor.getColumnIndex(Database.Table_Account.bank));
            String holder= cursor.getString(cursor.getColumnIndex(Database.Table_Account.holder));
            Double balance= cursor.getDouble(cursor.getColumnIndex(Database.Table_Account.balance));
            Account account =new Account(user_id,bank,holder,balance);
            return account;
        }
        return null;
    }

}
