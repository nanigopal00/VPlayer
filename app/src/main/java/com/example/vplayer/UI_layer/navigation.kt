package com.example.vplayer.UI_layer

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.vplayer.Doman.vplayerViewmodel
import exoplayerScreen


import kotlinx.serialization.Serializable

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun navigation(viewmodel: vplayerViewmodel) {
    LaunchedEffect(key1 = true) {
        viewmodel.getallvideobyFolder()
        viewmodel.getallvideo()

    }
    var navcontroll = rememberNavController()
    NavHost(navController = navcontroll, startDestination = flashscreen) {
        composable<homescreen> {
            homescreen(navcontroll, viewmodel)
        }
        composable<flashscreen> {
            flashscreen(navcontroll)
        }
        composable<exoplayer> {
            var data = it.toRoute<exoplayer>()
            exoplayerScreen(data.videoUri, data.title)
        }
        composable<VideoFolderVideoScreen> {
            var data = it.toRoute<VideoFolderVideoScreen>()
            singelvideobyFolderScreen(data.foldename,navcontroll,viewmodel)
        }

    }

}

@Serializable
object homescreen

@Serializable
object flashscreen

@Serializable

data class exoplayer(val videoUri: String, val title: String? = null)

@Serializable
data class VideoFolderVideoScreen(
    val foldename:String
)