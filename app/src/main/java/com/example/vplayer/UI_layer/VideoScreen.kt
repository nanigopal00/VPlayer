package com.example.vplayer.UI_layer

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vplayer.Doman.vplayerViewmodel

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun videoscreen(navcontroll: NavHostController, viewmodel: vplayerViewmodel) {
    // Collect the video list as a state
    val video by viewmodel.videos.collectAsState()
    val context = LocalContext.current
    var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }

    Scaffold(Modifier.fillMaxSize()) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center

        ) {
            if (video.isEmpty()) {
                Text("Video loading ..")
            } else {
                LazyColumn(
                    Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    items(video.toList()) {

                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                                .padding(7.dp)
                                .clickable {
                                    navcontroll.navigate(
                                        exoplayer(
                                            videoUri = it.path,
                                            title = it.titel
                                        )
                                    )
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (it.ThumbnailUri != null) {
                                VideoItem(
                                    thumbnailUri = it.ThumbnailUri!!,
                                    onThumbnailLoaded = { bitmap ->
                                        thumbnailBitmap = bitmap
                                    }
                                )
                            } else {
                                Box(
                                    Modifier
                                        .size(80.dp)
                                        .background(Color.Black)
                                )
                            }
                            Spacer(Modifier.width(15.dp))
                            Column(Modifier.fillMaxSize()) {
                                Text(
                                    text = it.titel.takeIf { it!!.isNotBlank() }
                                        ?: "Untitled",
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    fontSize = 15.sp,

                                    )
                                Text(
                                    text = formatFileSize(
                                        it.size?.toLongOrNull() ?: 0
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 12.sp,
                                )

                                Text(
                                    text = formatDuration(
                                        it.duration?.toLongOrNull() ?: 0
                                    ),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    fontSize = 14.sp,
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VideoItem(thumbnailUri: Uri, onThumbnailLoaded: (Bitmap) -> Unit) {
    val context = LocalContext.current
    var thumbnailBitmap by remember { mutableStateOf<Bitmap?>(null) }

    LaunchedEffect(thumbnailUri) {
        try {
            val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                context.contentResolver.loadThumbnail(
                    thumbnailUri,
                    android.util.Size(640, 480),
                    null
                )
            } else {
                null
            }
            thumbnailBitmap = bitmap
            if (bitmap != null) {
                onThumbnailLoaded(bitmap)
            }
        } catch (e: Exception) {
           thumbnailBitmap=null
        }
    }

    if (thumbnailBitmap != null) {
        Image(
            bitmap = thumbnailBitmap!!.asImageBitmap(),
            contentDescription = null,

            modifier = Modifier.clip(RoundedCornerShape(4.dp)).size(height = 80.dp, width = 110.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .size(height = 80.dp, width = 110.dp)
                .background(Color.Gray)
        )
    }
}

fun formatDuration(durationInMillis: Long): String {
    val seconds = (durationInMillis / 1000).toInt()
    val minutes = seconds / 60
    val hours = minutes / 60
    return when {
        hours > 0 -> String.format("%d:%02d:%02d", hours, minutes % 60, seconds % 60)
        else -> String.format("%d:%02d", minutes, seconds % 60)
    }
}

fun formatFileSize(sizeInBytes: Long): String {
    val kb = sizeInBytes / 1024.0
    val mb = kb / 1024.0
    val gb = mb / 1024.0
    return when {
        gb >= 1 -> "%.2f GB".format(gb)
        mb >= 1 -> "%.2f MB".format(mb)
        kb >= 1 -> "%.2f KB".format(kb)
        else -> "$sizeInBytes bytes"
    }
}