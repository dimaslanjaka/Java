package com.dimaslanjaka.tools.Service.Netspeed;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dimaslanjaka.tools.Helpers.core.DateTime;
import com.dimaslanjaka.tools.Helpers.database.DataBaseHelper;
import com.dimaslanjaka.tools.Helpers.firebase.SharedPref;
import com.dimaslanjaka.tools.R;
import com.dimaslanjaka.tools.Service.Netspeed.v2.Database;
import com.dimaslanjaka.tools.Service.Netspeed.v2.NetspeedDataBinding;

import java.util.ArrayList;
import java.util.Objects;

public class NetspeedActivity extends AppCompatActivity {

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.netspeed);
    new Database(getApplicationContext());
    try {
      String today = Database.parseDateToday();
      SharedPreferences pref = Objects.requireNonNull(SharedPref.getPref());
      String type = Database.getNetworkType();
      String typeKey = today + "-" + type;
      String uploadKey = today + "-upload";
      String downloadKey = today + "-download";

      DataBaseHelper dbhelper = new DataBaseHelper(getApplicationContext(), "netspeed.db");
      SQLiteDatabase db = dbhelper.getWritableDatabase();
      Cursor cursor = db.rawQuery("SELECT * FROM traffic", null
              , null);
      ArrayList<NetspeedDataBinding> results = new ArrayList<>();
      if (cursor.moveToFirst()) {
        while (!cursor.isAfterLast()) {
          long upload = cursor.getLong(cursor.getColumnIndex("upload"));
          long download = cursor.getLong(cursor.getColumnIndex("download"));
          long wifi = cursor.getLong(cursor.getColumnIndex("wifi"));
          long mobile = cursor.getLong(cursor.getColumnIndex("mobile"));
          String date =
                  DateTime.getDate(cursor.getString(cursor.getColumnIndex("date")), "dd/MM/yyyy");
          NetspeedDataBinding result = new NetspeedDataBinding(date, String.valueOf(wifi), String.valueOf(mobile),
                  cursor.getString(cursor.getColumnIndex("total")));
          results.add(result);
          cursor.moveToNext();
        }
      }
      cursor.close();
      //Log.out(results);

      RecyclerView recyclerView = findViewById(R.id.n_table);
      // use this setting to improve performance if you know that changes
      // in content do not change the layout size of the RecyclerView
      recyclerView.setHasFixedSize(true);

      // use a linear layout manager
      RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
      recyclerView.setLayoutManager(layoutManager);
      //List<Map<String, String>> datas = Database.getDatas();

      // specify an adapter (see also next example)
      RecyclerView.Adapter mAdapter = new NetspeedAdapter(results);
      recyclerView.setAdapter(mAdapter);

    } catch (Throwable e) {
      e.printStackTrace();
    }

  }
}
