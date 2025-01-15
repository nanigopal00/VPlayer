package com.example.vplayer.UI_layer

import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vplayer.Doman.vplayerViewmodel
import com.example.vplayer.ui.theme.myCustomFontFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun singelvideobyFolderScreen(
    foldename: String,
    navcontroll: NavHostController,
    viewmodel: vplayerViewmodel
) {
    val textColor = if (isSystemInDarkTheme()) {
        Color.White.copy(0.9f)
    } else {
        Color.Black.copy(0.9f)
    }
    var video = viewmodel.videosfolder.collectAsState().value
    var videolist = video[foldename]
  Scaffold(
      modifier = Modifier.fillMaxSize(),
      topBar = {
          TopAppBar(
              title = {
                  Text(
                      text = foldename,
                      color = textColor,
                      modifier = Modifier.padding(start = 6.dp),
                      fontWeight = FontWeight.Normal,
                      fontFamily = myCustomFontFamily,
                      fontSize = 20.sp
                  )
              },
              navigationIcon = {
                  IconButton(onClick = {
                      navcontroll.popBackStack()
                  },
                      Modifier
                          .size(37.dp)
                          .padding(start = 8.dp), colors = IconButtonDefaults.iconButtonColors(
                          containerColor = Color.Transparent
                      )) {
                      Icon(Icons.Filled.ArrowBack, contentDescription = "",Modifier.size(31.dp),tint =textColor)

                  }

              }
          )
      },


  ) { it->
      Column(Modifier.fillMaxSize().padding(it)) {
          if (videolist != null) {
              if (videolist.isEmpty()) {
                  Text("E ..")
              } else {
                  LazyColumn(
                      Modifier.fillMaxSize(),
                      contentPadding = PaddingValues(0.dp)
                  ) {
                      items(videolist.toList()) {

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
                                  VideoItemF(
                                      thumbnailUri = it.ThumbnailUri!!

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

}
@Composable
fun VideoItemF(thumbnailUri: Uri) {
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

        } catch (e: Exception) {
            thumbnailBitmap=null
        }
    }

    if (thumbnailBitmap != null) {
        Image(
            bitmap = thumbnailBitmap!!.asImageBitmap(),
            contentDescription = null,

            modifier = Modifier.clip(RoundedCornerShape(4.dp)).size(height = 70.dp, width = 100.dp),
            contentScale = ContentScale.Crop
        )
    } else {
        Box(
            modifier = Modifier
                .size(height = 70.dp, width = 100.dp)
                .background(Color.Gray)
        )
    }
}