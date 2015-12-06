package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by User on 12/3/2015.
 */
public class DBManager extends SQLiteOpenHelper {

    static final int DB_VERSION = 1;

    //query for creating table account
    private static final String CREATE_TABLE_Account = "CREATE TABLE " + Database.Table_Account.account_info+ "(" + Database.Table_Account.user_id
            + " TEXT PRIMARY KEY," + Database.Table_Account.bank + " TEXT NOT NULL,"+ Database.Table_Account.holder + " TEXT NOT NULL," + Database.Table_Account.balance + " NUMERIC(10,2) NOT NULL);";

    //query for creating table transaction
    private static final String CREATE_TABLE_Transaction = "CREATE TABLE " + Database.Table_Transaction.transaction_info+ "("+ Database.Table_Transaction.id+ " INTEGER PRIMARY KEY AUTOINCREMENT," + Database.Table_Transaction.user_id
            + " TEXT NOT NULL," + Database.Table_Transaction.date + " DATE NOT NULL,"+ Database.Table_Transaction.type + " TEXT NOT NULL," + Database.Table_Transaction.amount
            + " NUMERIC(10,2) NOT NULL, FOREIGN KEY (" + Database.Table_Transaction.user_id+") REFERENCES "+ Database.Table_Account.account_info + " ON DELETE CASCADE ON UPDATE CASCADE);";

    public DBManager(Context context){
        super(context, Database.DB_NAME,null,DB_VERSION);
    }

    //creates the database tables on the first time app runs
    @Override
    public void onCreate(SQLiteDatabase sdb) {
        sdb.execSQL(CREATE_TABLE_Account);
        sdb.execSQL(CREATE_TABLE_Transaction);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + Database.Table_Account.account_info);
        db.execSQL("DROP TABLE IF EXISTS " + Database.Table_Transaction.transaction_info);
        onCreate(db);
    }

}
