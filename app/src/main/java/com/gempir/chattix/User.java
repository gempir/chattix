package com.gempir.chattix;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class User {

    public @PrimaryKey int id;

    public String displayName;

    public String name;

    public String accessToken;
}
