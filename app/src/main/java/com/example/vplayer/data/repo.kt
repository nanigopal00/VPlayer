package com.example.vplayer.data

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore
import android.util.Log
import com.example.vplayer.Doman.vplayerViewmodel
import com.example.vplayer.data.Dtomodel.videoFileModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import java.io.File

class repo {
    fun getallVideo(application: Application): Flow<ArrayList<videoFileModel>> = flow {
        var projection = arrayOf(
            MediaStore.Video.Media._ID,
            MediaStore.Video.Media.DATA,
            MediaStore.Video.Media.TITLE,
            MediaStore.Video.Media.DISPLAY_NAME,
            MediaStore.Video.Media.DURATION,
            MediaStore.Video.Media.SIZE,
            MediaStore.Video.Media.DATE_ADDED
        )
        var uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        var videolist = mutableListOf<videoFileModel>()
        var mamorycurser = application.contentResolver.query(
            uri,
            projection,
            null,
            null,
            "${MediaStore.Video.Media.DATE_ADDED} DESC"
        )
        if (mamorycurser != null) {
            while (mamorycurser.moveToNext()) {
                var id = mamorycurser.getString(0)
                var path = mamorycurser.getString(1)
                var titel = mamorycurser.getString(2)
                var filename = mamorycurser.getString(3)
                var duration = mamorycurser.getString(4)
                var size = mamorycurser.getString(5)
                var dateAdded = mamorycurser.getString(6)
                val contentUri = ContentUris.withAppendedId(
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                    id.toLong()

                )
                var videomodeldata = videoFileModel(
                    id = id,
                    path = path,
                    titel = titel,
                    filename = filename,
                    duration = duration,
                    size = size,
                    dateAdded = dateAdded,
                    ThumbnailUri = contentUri
                )
                videolist.add(videomodeldata)

            }
            mamorycurser.close()

        }
        emit(videolist as ArrayList<videoFileModel>)


    }
    suspend fun getVideosByFolder(application: Application): Map<String, List<videoFileModel>> {
        val allVideos = getallVideo(application).first()
        return allVideos.groupBy { File(it.path).parentFile?.name ?: "Unknown" }

    }
}