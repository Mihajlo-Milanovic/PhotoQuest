package com.example.photoquest.ui.screens.signUp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.ui.theme.PhotoQuestTheme
import com.example.photoquest.ui.util.DrawLogo


@Composable
fun SignUpScreen(
    navController: NavController
){

    val vm = SignUpScreenViewModel.getInstance()
    val context = LocalContext.current

    //TODO: this
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
                SignUpInputFields(vm = vm)

                Spacer(modifier = Modifier.height(32.dp))
            }

            item{

                Button(
                    onClick = { vm.onSignInClick(context = context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.signUp),
                        fontSize = 20.sp
                    )
                }
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
        visualTransformation = if (vm.showPassword.value)
            VisualTransformation.None
        else
            PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (vm.showPassword.value)
                Icons.Default.Warning
            else
                Icons.Default.Check
            IconButton(onClick = { vm.onShowPasswordClick() }) {
                Icon(imageVector = image, contentDescription = null)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

    OutlinedTextField(
        value = vm.repPassword.value,
        onValueChange = { vm.onPasswordChange(it) },
        label = { Text(stringResource(id = R.string.confirmPassword)) },
        visualTransformation = if (vm.showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true)
@Composable
fun SignUpScreenPreview(){

    PhotoQuestTheme{
        SignUpScreen(navController = NavController(LocalContext.current))
    }
}