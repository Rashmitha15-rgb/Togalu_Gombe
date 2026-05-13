package com.example.togalu_gombe.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plays")
data class Play(
    @PrimaryKey val id: Int,
    val titleEn: String,
    val titleKn: String,
    val descriptionEn: String,
    val descriptionKn: String
)

@Entity(tableName = "scenes")
data class Scene(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val playId: Int,
    val sceneNumber: Int,
    val titleEn: String,
    val titleKn: String,
    val summaryEn: String,
    val summaryKn: String,
    val characters: String // Comma separated character names
)

@Entity(tableName = "puppets")
data class Puppet(
    @PrimaryKey val id: String,
    val nameEn: String,
    val nameKn: String,
    val powersEn: String,
    val powersKn: String,
    val symbolismEn: String,
    val symbolismKn: String,
    val imageResId: Int // Placeholder for drawable resource
)

@Entity(tableName = "history_videos")
data class HistoryVideo(
    @PrimaryKey val id: Int,
    val titleEn: String,
    val titleKn: String,
    val videoUrl: String, // Or raw resource name
    val thumbnailResId: Int
)

@Entity(tableName = "store_items")
data class StoreItem(
    @PrimaryKey val id: Int,
    val nameEn: String,
    val nameKn: String,
    val price: Double,
    val imageUrl: String, // Or drawable res ID
    val type: String // "Miniature" or "Workshop"
)
