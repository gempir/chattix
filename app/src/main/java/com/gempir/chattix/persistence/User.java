package com.gempir.chattix.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {
    
    @PrimaryKey
    public int id;

    public String displayName;

    public String name;

    public String accessToken;
}
