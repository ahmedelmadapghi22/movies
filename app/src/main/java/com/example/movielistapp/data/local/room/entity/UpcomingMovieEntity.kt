package com.example.movielistapp.data.local.room.entity

import androidx.room.Entity
import com.example.movielistapp.data.local.room.RoomConstants

@Entity(tableName = RoomConstants.TABLE_UP_COMING)
class UpcomingMovieEntity(
    id: Int, posterPath: String, title: String, voteCount: Int, overview: String
) : BaseMovieEntity(id, posterPath, title, voteCount, overview) {


}