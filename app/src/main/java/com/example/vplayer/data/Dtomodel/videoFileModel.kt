package com.example.vplayer.data.Dtomodel

import android.net.Uri

data class videoFileModel(
    var id:String?,
    var path:String,
    var titel:String?,
    var filename:String?,
    var duration:String?,
    var size:String?,
    var dateAdded:String?,
    var ThumbnailUri:Uri?,
)
