package skd.app.sqliterandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.greendao.database.Database;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    public static final boolean ENCRYPTED = false;

    String  ROOT_DIR_NAME = "appdirs";
    File rootdir;
    SQLiteDatabase db;
    String bench="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File root = android.os.Environment.getExternalStorageDirectory(); //for sdcard
        rootdir = new File (root.getAbsolutePath() + "/"+ROOT_DIR_NAME+"/"); //it is my root directory
        if(rootdir.exists()==false)
        {
            rootdir.mkdirs();
            Log.wtf("SKDINFO","folder created");
        }

        sqliterTest();
        sqliteTest();
        testGreenDao();

        TextView tx = findViewById(R.id.tx);
        tx.setText(bench);

    }


    public void sqliteTest(){
        db = SQLiteDatabase.openOrCreateDatabase(rootdir.getAbsoluteFile()+"/dxdb1", null);
        SQLiteDatabase db2 = SQLiteDatabase.openOrCreateDatabase(rootdir.getAbsoluteFile()+"/dxdb2", null);

        Toast.makeText(this,""+db.getPath(),Toast.LENGTH_LONG).show();

    }

    public void sqliterTest(){

        Sqliter sqliter = new Sqliter(rootdir.getAbsoluteFile()+"/dxdb");
        Sqliter sqliter2= new Sqliter(rootdir.getAbsoluteFile()+"/rwedb");
        Toast.makeText(this,"2-> "+sqliter2.db.getPath(),Toast.LENGTH_LONG).show();

        //create table
        HashMap<String,String> tableMap = new HashMap<>();
        tableMap.put("column1","'TEXT'");
        tableMap.put("column2","'TEXT'");
        tableMap.put("column3","'TEXT'");
        sqliter.createTable("table1",tableMap);

        long timeStart = System.currentTimeMillis();
        for(int i=0;i<12000;i++) {
            //insert data
            HashMap<String, String> dataMap = new HashMap<>();
            dataMap.put("column1", "hii");
            dataMap.put("column2", "bye");
            dataMap.put("column3", "database");
            sqliter.insertData("table1", dataMap);
        }
        long end = System.currentTimeMillis();
        Toast.makeText(this,"Insert time "+(end-timeStart),Toast.LENGTH_LONG).show();

        bench+="sqliter -> "+(end-timeStart)+"\r\n";

        //query data
        ArrayList<HashMap<String,String>> records = sqliter.runQuery("select * from table1;");
        for(int i =0 ;i<records.size();i++)
        {

            HashMap<String,String> record = records.get(i);
           // Toast.makeText(this,""+record.toString(),Toast.LENGTH_LONG).show();
        }



    }


    public void testGreenDao(){

        //setup green doa
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, ENCRYPTED ? "indusform-encrypted" : "formx");
        Database db = ENCRYPTED ? helper.getEncryptedWritableDb("super-secret") : helper.getWritableDb();
        DaoSession daoSession = new DaoMaster(db).newSession();

        long timeStart = System.currentTimeMillis();

        EntityxDao entityxDao = daoSession.getEntityxDao();
        for(int i=0;i<12000;i++) {
            //insert data
            Entityx entityx = new Entityx();
            entityxDao.insert(entityx);
        }
        long end = System.currentTimeMillis();
        bench+="doa -> "+(end-timeStart)+"\r\n";



    }


}
