package woojin.android.kotlin.project.musicstreaming

import woojin.android.kotlin.project.musicstreaming.service.MusicEntity

fun MusicEntity.mapper(id: Long): MusicModel =
        MusicModel(
                id = id,
                streamUrl = streamUrl,
                coverUrl = coverUrl,
                track = track,
                artist = artist
        )