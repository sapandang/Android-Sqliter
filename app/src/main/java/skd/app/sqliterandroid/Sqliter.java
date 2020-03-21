package skd.app.sqliterandroid;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.File;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author sapan.dang
 */
public class Sqliter {

    public static ArrayList<Sqliter> sqliterTracker = new ArrayList<>();
    static String TAG = "SKDINFO";
    public SQLiteDatabase db = null;
    String dbName = "";


    public Sqliter(String dbName) {
        this.dbName=dbName;
        openDb();
        sqliterTracker.add(this);
    }

    public void deleteDb() {
        try {


            //FileUtils.forceDelete(new File(dbName)); //apache commons io
            File dbfile = new File(dbName); //delete file
            if (dbfile.exists()) {
                dbfile.delete();
                Log.wtf(TAG, "db deleted success " + dbName);
            }
        } catch (Exception e) {
            Log.wtf(TAG, "ERROR DELETING DB " + dbName + " - " + e.getMessage());
            e.printStackTrace();
        }
    }


    public void openDb() {
        try {
            db = SQLiteDatabase.openOrCreateDatabase(dbName, null);
            Log.wtf(TAG, "DB opened successfully " + dbName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void closeDb() {
        try {
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void createTable(String tableName, HashMap<String, String> tabledata) {

        try {

            //generate the table query
            //String format : " NAME           TEXT, "
            String query = "";
            Object tableDataKeyset[] = tabledata.keySet().toArray();
            for (int i = 0; i < tabledata.size(); i++) {

                String columnName = tableDataKeyset[i].toString();
                String columnValue = tabledata.get(columnName);

                query += " " + columnName + " " + columnValue;
                if (i < (tabledata.size() - 1)) {
                    query += ",";
                }

            }

            String sql = "CREATE TABLE " + tableName +
                    "(" +
                    "_ID INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + query +
                    ")";

            SQLiteStatement stmt = db.compileStatement(sql);
            Log.wtf(TAG, "table created " + sql);
            stmt.execute();
            stmt.close();
        } catch (Exception e) {
            Log.wtf(TAG, "Error While creating table " + e.getMessage());
            e.printStackTrace();
        }

    }

    public long insertData(String tableName, HashMap<String, String> tabledata) {

        try {

            String columnNamesString = "";
            String columnValuesString = "";
            Object tableDataKeyset[] =  tabledata.keySet().toArray();

            for (int i = 0; i < tabledata.size(); i++) {

                String columnName = tableDataKeyset[i].toString();
                String columnValue = tabledata.get(columnName);

                columnNamesString += "" + columnName;
                columnValuesString += "'" + columnValue + "'";

                if (i < (tabledata.size() - 1)) {
                    columnNamesString += ",";
                    columnValuesString += ",";
                }

            }


            String sql = "INSERT INTO " + tableName +
                    "(" +
                    columnNamesString
                    + ") VALUES (" +
                    columnValuesString
                    + ");";


            SQLiteStatement stmt = db.compileStatement(sql); //performace check:2
            long result = stmt.executeInsert();

            Log.wtf(TAG, "Data inserted " + sql);
            stmt.close();
            return result;

        } catch (Exception e) {
            Log.wtf(TAG, "Error inserting data " + e.getMessage());
            e.printStackTrace();
        }


        return 0;
    }

    public int updateData(String tableName, HashMap<String, String> tabledata) {
        try {
            String coulmnwithValues = "";
            String conditions = "";
            Object tableDataKeyset[] =  tabledata.keySet().toArray();

            for (int i = 0; i < tabledata.size(); i++) {

                String columnName = tableDataKeyset[i].toString();
                String columnValue = tabledata.get(columnName);

                if (columnName.toLowerCase().equals("_id")) {
                    conditions += "_ID=" + columnValue + " ";


                } else {

                    coulmnwithValues += columnName + "='" + columnValue + "' ";
                    if (i < (tabledata.size() - 2)) {

                        coulmnwithValues += ",";
                    }
                }

            }

            //sql query
            String query = "UPDATE " + tableName + " SET " + coulmnwithValues + " WHERE " + conditions + ";";
            Log.wtf(TAG, "" + query);
            SQLiteStatement stmt = db.compileStatement(query);
            int result = stmt.executeUpdateDelete();
            stmt.close();
            return result;

        } catch (Exception e) {

            Log.wtf(TAG, "Error while updating data " + e.getMessage());
            e.printStackTrace();
        }

        return 0;

    }


    public int deleteData(String tableName, HashMap<String, String> tabledata) {
        try {

            //sql query
            String query = "DELETE from " + tableName + " where ID=" + tabledata.get("_ID") + ";";
            Log.wtf(TAG, "" + query);
            SQLiteStatement stmt = db.compileStatement(query);
            int result = stmt.executeUpdateDelete();
            stmt.close();
            return result;

        } catch (Exception e) {

            Log.wtf(TAG, "Error while deleting data " + e.getMessage());
            e.printStackTrace();
        }

        return 0;

    }


    public int runUpdateOrDelete(String query) {

        try {

            SQLiteStatement stmt = db.compileStatement(query);
            int result = stmt.executeUpdateDelete();
            stmt.close();
            return result;

        } catch (Exception e) {
            Log.wtf(TAG, "ERROR UPDATE " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public ArrayList<HashMap<String, String>> runQuery(String query) {
        ArrayList<HashMap<String, String>> resultList = new ArrayList<>();
        Cursor cursor=null;
        try {
            //check if it a update statement or not
            String checkquery = query.trim().toLowerCase();

            if (checkquery.toLowerCase().startsWith("update") || checkquery.toLowerCase().startsWith("delete")) {

                Log.wtf(TAG, "Do not use runquery method to execute update or delete");
                return null;
            }


            cursor = db.rawQueryWithFactory(null,query,null,null);
            String[] columnNames = cursor.getColumnNames();
            while (cursor.moveToNext()) {
                HashMap<String, String> resultData = new HashMap<>();

                for (int i = 0; i < columnNames.length; i++) {

                    resultData.put(columnNames[i], cursor.getString(cursor.getColumnIndex(columnNames[i])));

                }
                resultList.add(resultData);
            }

        } catch (Exception e) {
            Log.wtf(TAG, "ERROR WHILE RUNNING Query " + e.getMessage());
            e.printStackTrace();
        }finally {
            if(cursor!=null) {
                cursor.close();
            }
        }
        return resultList;

    }


}