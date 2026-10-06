package myfirst.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import myfirst.app.ui.theme.*
import kotlin.math.floor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        ClickButton(
                            modifier = Modifier
                                .size(250.dp)
                                .border(1.5.dp, Color.Black)
                                .padding(0.dp, 175.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ClickButton(modifier: Modifier?) {
    var clicks by remember { mutableFloatStateOf(0f) }
    var text by remember { mutableStateOf("Клики: $clicks") }
    var addPerClick by remember { mutableFloatStateOf(1f) }

    Button(
        onClick = {
            clicks += addPerClick
            text = if (clicks - floor(clicks) == 0f) {
                "Клики: ${clicks.toInt()}"
            } else {
                "Клики: $clicks"
            }
        },
        modifier = modifier ?: Modifier.size(250.dp).border(1.5.dp, Color.Black),
        colors = ButtonColors(
            Color.Blue, Color.DarkBlue,
            Color.Red, Color.DarkRed
        )
    ) {
        Text(text)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MainTheme {
        Surface {
            ClickButton(null)
        }
    }
}