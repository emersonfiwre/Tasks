package com.example.tasks.service.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "priority")
class PriorityModel {
    @SerializedName("Id")
    @ColumnInfo(name = "id")
    var id: Int = 0

    @SerializedName("Description")
    @ColumnInfo(name = "description")
    @PrimaryKey
    var description: String = ""
}