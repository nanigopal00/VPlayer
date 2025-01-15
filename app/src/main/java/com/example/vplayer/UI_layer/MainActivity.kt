package com.example.vplayer.UI_layer

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vplayer.Doman.vplayerViewmodel
import com.example.vplayer.R
import com.example.vplayer.ui.theme.VPlayerTheme
import com.example.vplayer.ui.theme.myCustomFontFamily
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import dagger.hilt.android.AndroidEntryPoint
import android.net.Uri

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var viewmodel = hiltViewModel<vplayerViewmodel>()
            var grantstate = viewmodel.grantstate.collectAsState()
            VPlayerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    var permitionState =
                        rememberPermissionState(permission = Manifest.permission.READ_MEDIA_VIDEO)
                    var lunchpermition = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission(),
                        onResult = { result ->
                            if (result == true) {
                                viewmodel.grantstate.value = true
                            } else {
                                viewmodel.grantstate.value = false
                            }
                        }
                    )

                    val amazonGradient = Brush.horizontalGradient(
                        colors = listOf(
                            Color(0xFFA9B9CB),
                            Color(0xFF3793E3),
                        )
                    )

                    LaunchedEffect(key1 = permitionState) {
                        if (permitionState.status.isGranted) {
                            viewmodel.grantstate.value = true
                        } else {
                            lunchpermition.launch(Manifest.permission.READ_MEDIA_VIDEO)
                        }
                    }

                    if (grantstate.value == true) {
                        navigation(viewmodel)
                    } else if(grantstate.value==false) {
                        Column(
                            modifier = Modifier
                                .padding(innerPadding)
                                .fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.emogi),
                                contentDescription = null,
                                Modifier.size(200.dp),
                                contentScale = androidx.compose.ui.layout.ContentScale.Crop
                            )
                            Text(
                                text = "Permission Denied",
                                fontFamily = myCustomFontFamily,
                                color = Color.Black
                            )
                            Button(
                                onClick = {
                                    val intent =
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                            val uri = Uri.fromParts("package", packageName, null)
                                            data = uri
                                        }
                                    startActivity(intent)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 50.dp)
                                    .background(amazonGradient),
                                colors = ButtonDefaults.elevatedButtonColors(
                                    containerColor = Color.Transparent
                                )
                            ) {
                                Text(
                                    text = "Grant Permission",
                                    fontFamily = myCustomFontFamily,
                                    color = Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}