package com.example.photoquest.ui.screens.logIn

import android.content.Context
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.photoquest.R
import com.example.photoquest.Screens
import com.example.photoquest.ui.theme.PhotoQuestTheme
import com.example.photoquest.ui.util.DrawLogo

@Composable
fun LogInScreen(
    navController: NavController
) {
    val vm = LogInScreenViewModel.getInstance()
    val context = LocalContext.current
    Surface {


        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            item{

                DrawLogo()

                Spacer(modifier = Modifier.height(64.dp))
            }

            item{
                if(vm.validationDone.value){

                    if (vm.userLoggedIn.value) {
                        if (!navController.popBackStack(Screens.Profile.name, false))
                            navController.navigate(Screens.Profile.name)
                    } else {

                            LogInInputFields(vm = vm)

                            Spacer(modifier = Modifier.height(16.dp))

                            LogInButtons(vm = vm, context = context, navController = navController)
                    }

                }else {

                    LaunchedEffect(Unit) {
                        vm.validateUser()
                    }
                    CircularProgressIndicator( color = MaterialTheme.colorScheme.primary, )
                }
            }
        }
    }
}

@Composable
fun LogInInputFields(vm: LogInScreenViewModel){

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
        visualTransformation = if (vm.showPassword.value) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            val image = if (vm.showPassword.value) Icons.Default.Warning else Icons.Default.Check

            IconButton(onClick = { vm.onShowPasswordClick() }) {
                Icon(imageVector = image, contentDescription = null)
            }
        },
        modifier = Modifier.fillMaxWidth()
    )

}

@Composable
fun LogInButtons(vm: LogInScreenViewModel, context: Context, navController: NavController){

    Button(
        onClick = { vm.onLogInClick(context = context, navController = navController) },
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.logIn),
            fontSize = 20.sp
        )
    }
    Spacer(modifier = Modifier.height(32.dp))

    Text(
        text = AnnotatedString(
            stringResource(id = R.string.dontHaveAcc),
        ),
        style = TextStyle(
            color = MaterialTheme.colorScheme.primary,
            fontSize = 18.sp
        ),
        modifier = Modifier.clickable {
            if(!navController.popBackStack(Screens.SignUp.name, false))
                navController.navigate(Screens.SignUp.name)
        }
    )
}


@Preview(showBackground = false)
@Composable
fun LoginScreenPreview() {
    PhotoQuestTheme {
        LogInScreen(navController = NavController(context = LocalContext.current))
    }
}