package com.example.vplayer.UI_layer

import android.app.Activity
import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.VideoFile
import androidx.compose.material.icons.outlined.Folder
import androidx.compose.material.icons.outlined.VideoFile
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.vplayer.Doman.vplayerViewmodel
import com.example.vplayer.common.navitemmodel
import com.example.vplayer.ui.theme.myColor
import com.example.vplayer.ui.theme.myCustomFontFamily
import com.example.vplayer.ui.theme.myTealColor

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun homescreen(navcontroll: NavHostController, viewmodel: vplayerViewmodel) {
    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val activity = context as? Activity
    val textColor = if (isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black.copy(0.9f)
    }
  var  containerColor = if (isSystemInDarkTheme()) {
        Color.Transparent
    } else {
        Color.White
    }
    val unselectedIconColor = if (isSystemInDarkTheme()) {
        Color.White.copy(0.7f)
    } else {
        Color.Black
    }
    val unselectedTextColor = if (isSystemInDarkTheme()) {
        Color.White.copy(0.7f)
    } else {
        Color.Black
    }

    BackHandler(enabled = true) {
        showDialog = true
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Confirm Exit") },
            text = { Text("Are you sure you want to exit?") },
            confirmButton = {
                Button(onClick = {
                    showDialog = false
                    activity?.finish()
                },
                   colors = ButtonDefaults.buttonColors(
                       containerColor = myTealColor
                   ) ) {
                    Text("Yes")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = myTealColor
                    )) {
                    Text("No")
                }
            }
        )
    }

    var selectedindex = rememberSaveable { mutableStateOf(0) }
    val naviremlist = arrayOf(
        navitemmodel(
            iconSelected = Icons.Filled.Folder,
            iconUnselected = Icons.Outlined.Folder,
            titel = "Folder"
        ),
        navitemmodel(
            iconSelected = Icons.Filled.VideoFile,
            iconUnselected = Icons.Outlined.VideoFile,
            titel = "Video"
        )
    )
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp

    val bottomBarHeight = if (screenWidth.dp > 600.dp) {
        72.dp
    } else {
        62.dp
    }

    Scaffold(
        Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = containerColor
            ) {
                naviremlist.forEachIndexed { index, navitemmodel ->
                    NavigationBarItem(
                        onClick = {
                            selectedindex.value = index
                        },
                        selected = selectedindex.value == index,
                        icon = {
                            Icon(
                                imageVector = if (selectedindex.value == index) navitemmodel.iconSelected else navitemmodel.iconUnselected,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp)
                            )
                        },
                        colors = NavigationBarItemColors(
                            selectedIconColor = myColor,
                            unselectedIconColor = unselectedIconColor,
                            selectedIndicatorColor = Color.Transparent,
                            selectedTextColor = myColor,
                            unselectedTextColor = unselectedTextColor,
                            disabledIconColor = unselectedIconColor,
                            disabledTextColor = unselectedTextColor
                        ),
                        label = {
                            Text(navitemmodel.titel, fontWeight = FontWeight.Bold)
                        }
                    )
                }
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (selectedindex.value == 0) "Folder" else "Videos",
                        color = textColor,
                        modifier = Modifier.padding(start = 6.dp),
                        fontWeight = FontWeight.Bold,
                        fontFamily = myCustomFontFamily,
                        fontSize = 29.sp
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                )
            )
        }
    ) { it ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(it)
        ) {
            when (selectedindex.value) {
                0 -> {
                    folderScreen(navcontroll,viewmodel)
                }

                1 -> {
                    videoscreen(navcontroll, viewmodel)
                }
            }
        }
    }
}