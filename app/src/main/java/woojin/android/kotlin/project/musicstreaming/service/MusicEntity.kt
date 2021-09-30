package woojin.android.kotlin.project.musicstreaming.service

import com.google.gson.annotations.SerializedName

data class MusicEntity(
        @SerializedName("track")
        val track:String,
        @SerializedName("streamUrl")
        val streamUrl:String,
        @SerializedName("artist")
        val artist:String,
        @SerializedName("cover")
        val coverUrl:String
)