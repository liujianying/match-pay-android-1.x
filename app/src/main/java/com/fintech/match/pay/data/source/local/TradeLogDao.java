package com.fintech.match.pay.data.source.local;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.fintech.match.pay.data.source.TradeLog;

import java.util.List;

@Dao
public interface TradeLogDao {

    @Query("SELECT * FROM trade_log order by time desc")
    List<TradeLog> getAll();

    @Query("SELECT * FROM trade_log where post = :post")
    List<TradeLog> loadByPost(boolean post);

    @Query("SELECT * FROM trade_log where uuid = :uuid")
    TradeLog loadAllById(boolean uuid);

    @Insert
    void insertAll(TradeLog... logs);

    @Update(onConflict = OnConflictStrategy.ABORT)
    int update(TradeLog logs);


}
