package com.example.vplayer.Doman


import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vplayer.data.Dtomodel.videoFileModel
import com.example.vplayer.data.repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class vplayerViewmodel @Inject constructor(
    var repo: repo,
    var application: Application
) : ViewModel() {
    var grantstate = MutableStateFlow(null as Boolean?)
    var videos = MutableStateFlow<List<videoFileModel>>(emptyList())
    var videosfolder = MutableStateFlow(emptyMap<String, List<videoFileModel>>())


    fun getallvideo() {
        viewModelScope.launch {
            repo.getallVideo(application).collectLatest { videolist ->
                videos.value = videolist
                Log.d("testvideo", "getallvideo: ${videolist}")
            }

        }
    }
    fun getallvideobyFolder() {
        viewModelScope.launch {
          var foldervideo =  repo.getVideosByFolder(application)
            videosfolder.value = foldervideo

        }
    }

}