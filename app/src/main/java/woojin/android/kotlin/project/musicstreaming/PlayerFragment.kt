package woojin.android.kotlin.project.musicstreaming

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import woojin.android.kotlin.project.musicstreaming.databinding.FragmentPlayerBinding
import woojin.android.kotlin.project.musicstreaming.service.MusicDto
import woojin.android.kotlin.project.musicstreaming.service.MusicService

class PlayerFragment : Fragment(R.layout.fragment_player) {

    private var binding: FragmentPlayerBinding? = null
    private var player: SimpleExoPlayer? = null
//    private var isWatchingPlayListView = true
    private var model:PlayerModel = PlayerModel()
    private lateinit var playListAdapter: PlayListAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding

        initPlayView(fragmentPlayerBinding)
        initPlayListButton(fragmentPlayerBinding)
        initPlayControlButtons(fragmentPlayerBinding)
        initRecyclerView(fragmentPlayerBinding)

        getVideoListFromServer()
    }

    private fun initPlayView(fragmentPlayerBinding: FragmentPlayerBinding) {

        context?.let {
            player = SimpleExoPlayer.Builder(it).build()
        }

        fragmentPlayerBinding.playerView.player = player

        binding?.let { binding ->
            player?.addListener(object: Player.EventListener{
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    super.onIsPlayingChanged(isPlaying)

                    if(isPlaying){
                        binding.playControlImageView.setImageResource(R.drawable.ic_baseline_pause_48)
                    }
                    else{
                        binding.playControlImageView.setImageResource(R.drawable.ic_baseline_play_arrow_48)
                    }
                }
            })
        }
    }

    private fun initPlayListButton(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playListImageView.setOnClickListener {
            //todo 서버에서 데이터가 다 불려오지 않은 상태일 때
            fragmentPlayerBinding.playerViewGroup.isVisible = isWatchingPlayListView
            fragmentPlayerBinding.playListViewGroup.isVisible = isWatchingPlayListView.not()

            isWatchingPlayListView = !isWatchingPlayListView
        }
    }

    private fun initPlayControlButtons(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playControlImageView.setOnClickListener {
            val player = this.player?:return@setOnClickListener

            if (player.isPlaying){
                player.pause()
            }
            else{
                player.play()
            }
        }

        fragmentPlayerBinding.skipNextImageView.setOnClickListener {

        }

        fragmentPlayerBinding.skipPrevImageVie.setOnClickListener {

        }
    }

    private fun initRecyclerView(fragmentPlayerBinding: FragmentPlayerBinding) {
        playListAdapter = PlayListAdapter {
            //todo 음악 재생
        }

        fragmentPlayerBinding.playListRecyclerView.apply {
            adapter = playListAdapter
            layoutManager = LinearLayoutManager(context)
        }
    }

    private fun getVideoListFromServer() {
        val retrofit = Retrofit.Builder()
                .baseUrl("https://run.mocky.io")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        retrofit.create(MusicService::class.java).also {
            it.listMusics().enqueue(object : Callback<MusicDto> {
                override fun onResponse(call: Call<MusicDto>, response: Response<MusicDto>) {
                    Log.e("우진", "${response.body()}")

                    response.body()?.let {
                        val modelList = it.musics.mapIndexed { index, musicEntity ->
                            musicEntity.mapper(index.toLong())
                        }

                        setMusicList(modelList)
                        playListAdapter.submitList(modelList)
                    }
                }

                override fun onFailure(call: Call<MusicDto>, t: Throwable) {
                }

            })
        }
    }

    private fun setMusicList(modelList: List<MusicModel>) {
        context?.let {
            player?.addMediaItems(modelList.map {
                MediaItem.Builder()
                        .setMediaId(it.id.toString())
                        .setUri(it.streamUrl)
                        .build()
            })

            player?.prepare()
            player?.play()
        }
    }

    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}