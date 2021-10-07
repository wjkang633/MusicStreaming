package woojin.android.kotlin.project.musicstreaming

data class PlayerModel(
        val playMusicList: List<MusicModel> = emptyList(),
        var currentPosition: Int = -1,
        var isWatchingPlayListView: Boolean = true
){

}
