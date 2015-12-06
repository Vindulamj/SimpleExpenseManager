package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.provider.BaseColumns;

/**
 * Created by User on 12/3/2015.
 */
public class Database {

    //This class elaborates the structure of the database of the application
    public Database(){}

    public static final String DB_NAME = "130247P";   //database name

    //Table Account structure
    public static abstract class Table_Account implements BaseColumns{
        public static final String user_id ="id";
        public static final String bank ="bank";
        public static final String holder ="holder";
        public static final String balance ="balance";
        public static final String account_info ="Account";

    }

    //Table Transaction structure
    public static abstract class Table_Transaction implements BaseColumns{
        public static final String id ="trans_id";
        public static final String user_id ="id";
        public static final String date ="date";
        public static final String type ="type";
        public static final String amount ="amount";
        public static final String transaction_info ="Transactions";

    }

}
