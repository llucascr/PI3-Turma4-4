package com.example.cameraapp2.permissions

import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext

//Arquivo para gerenciar as permissões do usuário

@Composable
fun RequestPermissionScreen(
    modifier: Modifier = Modifier,
    permission: String,
    onPermissionGranted: () -> Unit
){
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if(granted){
            onPermissionGranted()
        }
    }

    LaunchedEffect(permission) {
        launcher.launch(permission)
    }
}

@Composable
fun WithPermission(
    modifier: Modifier = Modifier,
    permission: String,
    content: @Composable () -> Unit
){
    val context = LocalContext.current
    var permissionGranted by remember {
        mutableStateOf(context.checkSelfPermission(permission) ==
                PackageManager.PERMISSION_GRANTED)
    }

    if(!permissionGranted){
        // fun do composable para pedir permissão
        RequestPermissionScreen(
            modifier = modifier,
            permission = permission
        ){
            permissionGranted = true
        }
    }else{
        Surface(modifier = modifier) {
            content()
        }
    }
}