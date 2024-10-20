package com.example.photoquest.ui.screens.logIn

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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.photoquest.R
import com.example.photoquest.ui.theme.PhotoQuestTheme

@Composable
fun LogInScreen(
    vm:LogInScreenViewModel = LogInScreenViewModel.getInstance()
) {

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
                Text(
                    text = stringResource(id = R.string.photoQuest),
                    fontSize = 64.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontStyle = FontStyle.Italic,
                    fontFamily = FontFamily.Cursive,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(64.dp))
            }
            /*
            item {
                Text(
                    text = stringResource(id = R.string.logIn),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(32.dp))
            }
            */
            item {

                OutlinedTextField(
                    value = vm.email.value,
                    onValueChange = {
                        vm.onEmailChange(it)
                    },
                    label = { Text(stringResource(id = R.string.email)) },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }


            item {

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

                Spacer(modifier = Modifier.height(16.dp))
            }


            item {

                Button(
                    onClick = { vm.onLoginClick(context = context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.logIn))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Text(
                    text = stringResource(id = R.string.dontHaveAcc),
                    //fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier,

                )
                Button(
                    onClick = { vm.onGoToSignInScreen(context = context) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = stringResource(id = R.string.register))
                }
            }
        }
    }
}

@Preview(showBackground = false)
@Composable
fun LoginScreenPreview() {
    PhotoQuestTheme {
        LogInScreen()
    }
}