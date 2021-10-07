package woojin.android.kotlin.project.musicstreaming

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.NonNull
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
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
    private var isWatchingPlayListView = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentPlayerBinding = FragmentPlayerBinding.bind(view)
        binding = fragmentPlayerBinding

        initPlayListButton(fragmentPlayerBinding)
        getVideoListFromServer()
    }

    private fun initPlayListButton(fragmentPlayerBinding: FragmentPlayerBinding) {
        fragmentPlayerBinding.playListImageView.setOnClickListener {
            //todo 서버에서 데이터가 다 불려오지 않은 상태일 때
            fragmentPlayerBinding.playerViewGroup.isVisible = isWatchingPlayListView
            fragmentPlayerBinding.playListViewGroup.isVisible =  isWatchingPlayListView.not()

            isWatchingPlayListView = !isWatchingPlayListView
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


                    }
                }

                override fun onFailure(call: Call<MusicDto>, t: Throwable) {
                    TODO("Not yet implemented")
                }

            })
        }
    }

    companion object {
        fun newInstance(): PlayerFragment {
            return PlayerFragment()
        }
    }
}