package com.realm.myrealm;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by Andriod1 on 3/13/2018.
 */

public class StudentModel extends RealmObject {
    @PrimaryKey
    public String id;
    @Required
    public String name;
    @Required
    public String address;
    @Required
    public Date date_of_admission;

    public boolean hasLongName() {
        return (name.length() > 5);
    }

    public Date[] sortDatesToLatest(List<StudentModel> modelList) {
        Date[] dates_of_admission = new Date[modelList.size()];
        for (StudentModel model : modelList)
            dates_of_admission[modelList.indexOf(model)] = model.date_of_admission;

        Arrays.sort(dates_of_admission, new Comparator<Date>() {
            @Override
            public int compare(Date dateLHS, Date dateRHS) {
                return dateRHS.compareTo(dateLHS);
            }
        });
        return dates_of_admission;
    }
}
