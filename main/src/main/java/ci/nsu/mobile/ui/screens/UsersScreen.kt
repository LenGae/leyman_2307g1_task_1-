package ci.nsu.mobile.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ci.nsu.mobile.ui.viewmodel.AuthViewModel

@Composable
fun UsersScreen(
    vm: AuthViewModel
) {

    LaunchedEffect(vm.isLoggedIn) {

        if (vm.isLoggedIn) {
            vm.loadUsers()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Text(
                text = "Пользователи",
                style = MaterialTheme.typography.headlineMedium
            )

            Button(
                onClick = {
                    vm.logout()
                }
            ) {
                Text("Выйти")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (vm.isLoading) {
            CircularProgressIndicator()
        }

        vm.error?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error
            )
        }

        if (vm.users.isEmpty()) {

            Text("Нет пользователей")

        } else {

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {

                items(vm.users) { user ->

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(4.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text("👤 ${user.login}")

                            Spacer(modifier = Modifier.height(4.dp))

                            Text("📧 ${user.email}")

                            Spacer(modifier = Modifier.height(4.dp))

                            Text("📱 ${user.phoneNumber}")

                            Spacer(modifier = Modifier.height(4.dp))

                            Text("🆔 ID: ${user.userId}")
                        }
                    }
                }
            }
        }
    }
}