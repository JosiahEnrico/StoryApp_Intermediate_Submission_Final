package com.example.mysubmission2.data.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.mysubmission2.data.response.ListStoryItem

@Dao
interface StoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStory(quote: List<ListStoryItem>)

    @Query("SELECT * FROM ListStoryItem")
    fun getAllStory(): PagingSource<Int, ListStoryItem>

    @Query("DELETE FROM ListStoryItem")
    suspend fun deleteAll()
}