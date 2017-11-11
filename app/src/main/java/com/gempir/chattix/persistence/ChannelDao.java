package com.gempir.chattix.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ChannelDao {
    @Insert(onConflict = REPLACE)
    void insertChannel(Channel channel);

    @Query("SELECT * FROM Channel")
    List<Channel> getAllChannels();

    @Query("SELECT * FROM Channel")
    LiveData<List<Channel>> getAllChannelsSync();
}
