package com.example.togalu_gombe.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TogaluDao {
    @Query("SELECT * FROM plays")
    fun getAllPlays(): Flow<List<Play>>

    @Query("SELECT * FROM scenes WHERE playId = :playId ORDER BY sceneNumber ASC")
    fun getScenesForPlay(playId: Int): Flow<List<Scene>>

    @Query("SELECT * FROM puppets")
    fun getAllPuppets(): Flow<List<Puppet>>

    @Query("SELECT * FROM puppets WHERE id = :id")
    suspend fun getPuppetById(id: String): Puppet?

    @Query("SELECT * FROM history_videos")
    fun getAllHistoryVideos(): Flow<List<HistoryVideo>>

    @Query("SELECT * FROM store_items")
    fun getAllStoreItems(): Flow<List<StoreItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlays(plays: List<Play>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScenes(scenes: List<Scene>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPuppets(puppets: List<Puppet>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHistoryVideos(videos: List<HistoryVideo>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStoreItems(items: List<StoreItem>): List<Long>
}
