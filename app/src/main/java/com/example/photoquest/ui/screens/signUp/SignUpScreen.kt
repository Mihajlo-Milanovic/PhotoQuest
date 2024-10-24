package com.example.photoquest.ui.screens.signUp

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.ui.theme.PhotoQuestTheme
import com.example.photoquest.ui.util.DrawLogo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun SignUpScreen(
    navController: NavController
){

    val vm = SignUpScreenViewModel.getInstance()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Surface {

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            item {

                DrawLogo()

                Spacer(modifier = Modifier.height(64.dp))
            }

            item{

                if (!vm.signUpInProgress.value) {

                    SignUpInputFields(vm = vm)

                    Spacer(modifier = Modifier.height(32.dp))

                    SignUpScreenButton(
                        vm = vm,
                        context = context,
                        coroutineScope = coroutineScope,
                        navController = navController
                    )
                }
                else CircularProgressIndicator( color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}

@Composable
fun SignUpInputFields(vm: SignUpScreenViewModel){

    OutlinedTextField(
        value = vm.username.value,
        onValueChange = {
            vm.onUsernameChange(it)
        },
        label = { Text(stringResource(id = R.string.username)) },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = vm.email.value,
        onValueChange = {
            vm.onEmailChange(it)
        },
        label = { Text(stringResource(id = R.string.email)) },
        modifier = Modifier.fillMaxWidth()
    )

    Spacer(modifier = Modifier.height(16.dp))

    OutlinedTextField(
        value = vm.password.value,
        onValueChange = { vm.onPasswordChange(it) },
        label = { Text(stringResource(id = R.string.password)) },
        visualTransformation = vm.passwordTransformation.value,
        trailingIcon = {
            IconButton(onClick = { vm.onShowPasswordClick() }) {
                Icon(imageVector = ImageVector.vectorResource(id = vm.passwordIcon.intValue), contentDescription = null)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = vm.repPassword.value,
        onValueChange = { vm.onRepPasswordChange(it) },
        label = { Text(stringResource(id = R.string.confirmPassword)) },
        visualTransformation = vm.passwordTransformation.value,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SignUpScreenButton(
    vm: SignUpScreenViewModel,
    context: Context,
    coroutineScope: CoroutineScope,
    navController: NavController
){
    Button(
        onClick = {
            vm.signUpInProgress.value = true
            coroutineScope.launch (Dispatchers.Default){
                vm.onSignInClick(context = context, navController = navController) }
        },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.signUp),
            fontSize = 20.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview(){

    PhotoQuestTheme{
        SignUpScreen(navController = NavController(LocalContext.current))
    }
}