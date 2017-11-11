package com.gempir.chattix.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Channel {

    @NonNull
    @PrimaryKey
    public String name;
}
