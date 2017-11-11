package com.gempir.chattix.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface UserDao {

    @Insert(onConflict = REPLACE)
    void setUser(User user);

    @Query("SELECT * FROM User LIMIT 1")
    User getUser();

    @Query("DELETE FROM User")
    void deleteAll();
}
