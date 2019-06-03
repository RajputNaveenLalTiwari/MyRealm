package com.realm.myrealm;

import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimeUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

public class MainActivity extends AppCompatActivity {
    private Realm realm = null;
    private StudentModel studentModel;
    List<StudentModel> modelList;
    private final Handler handler = new Handler();
    private final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if (count <= 10) {
                String dateFormat = "dd-MMM-yyyy hh:mm:ss a";
                String time_stamp = new SimpleDateFormat(dateFormat, Locale.US).format(new Date());
                Log.i("Runnable", "Time_Stamp = " + time_stamp);
                studentModel = new StudentModel();
                studentModel.id = "AMCA0" + count;
                studentModel.name = "STUDENT_" + count;
                studentModel.address = "HYDERABAD";
                studentModel.date_of_admission = new Date();
                modelList.add(studentModel);
                if (count == 10)
                    insertDataIntoRealm(modelList);
            }
            count++;
            handler.postDelayed(runnable, TimeUnit.SECONDS.toMillis(10));
            if (count > 10) {
                getDataFromRealm();
                handler.removeCallbacks(runnable);
            }
        }
    };
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Preparing Data to insert*/
        modelList = new ArrayList<>();

        count = 0;
//        getDataFromRealm();
        handler.postDelayed(runnable, TimeUnit.SECONDS.toMillis(0));
    }

    private void getDataFromRealm() {
        /* retrieving data */
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    final RealmResults<StudentModel> modelList = realm
                            .where(StudentModel.class)
                            .findAll();
                    StudentModel model = new StudentModel();
                    Date[] sortedDates = model.sortDatesToLatest(modelList);
                    for (Date date : sortedDates){
                        Log.i("Result", " DOA = "+date);
                    }
                    /*for (StudentModel model : modelList) {
                        *//*if (model.hasLongName())
                            Log.i("Result", "Student Id = " + model.id + " Student Name = " + model.name + " is long");
                        else
                            Log.i("Result", "Student Id = " + model.id + " Student Name = " + model.name + " is small");*//*
                        Log.i("Result", "Student Id = " + model.id
                                + " Student Name = " + model.name
                                + " Address = "+model.address
                                +" DOA = "+model.date_of_admission);
                    }*/
                }
            });
        } finally {
            if (realm != null)
                realm.close();
        }
    }

    private void insertDataIntoRealm(final List<StudentModel> modelList) {
        /*inserting data*/
        try {
            realm = Realm.getDefaultInstance();/*Opens realm file*/
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insertOrUpdate(modelList);
                }
            });
        } finally {
            if (realm != null)
                realm.close();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null)
            handler.removeCallbacks(runnable);
    }
}
