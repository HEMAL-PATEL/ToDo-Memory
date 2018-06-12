package com.aar.app.todomemory.data;

import android.arch.persistence.room.TypeConverter;

import java.util.Date;

public class RoomDateConverter {

    @TypeConverter
    public Long convertFromDate(Date date) {
        return date == null ? 0 : date.getTime();
    }

    @TypeConverter
    public Date convertFromLong(Long date) {
        return new Date(date);
    }

}
