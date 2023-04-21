package com.example.listassist.db.typeConverters;

import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;


@TypeConverters
public class DateTypeConverter {
    @TypeConverter
    public Long convertDateToLong(Date date){
        return date.getTime();
    }

    @TypeConverter
    public Date convertLongToDate(long time){
        return new Date(time);
    }
}
