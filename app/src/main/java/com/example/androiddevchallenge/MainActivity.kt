/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.text.format.DateUtils
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                MyApp()
            }
        }
    }
}

// Start building your app here!
@Composable
fun MyApp() {
    Surface(color = MaterialTheme.colors.background) {
        val context = LocalContext.current
        val durationState = remember { mutableStateOf(0) }
        val targetState = remember { mutableStateOf(0) }
        val startState = remember { mutableStateOf(false) }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CountDownView(start = startState.value, target = targetState.value, duration = durationState.value) {
//                Log.i("zander", "finish: $it, start: ${startState.value}")
                if (startState.value && it != 0) {
                    durationState.value = it * 1000
                    targetState.value = 0
                } else if (startState.value && it == 0) {
                    startState.value = false
                    Toast.makeText(context, "Times up !!", Toast.LENGTH_SHORT).show()
                }
            }
            InputView(startState.value) {
                startState.value = !startState.value
                targetState.value = if (startState.value) it else 0
                durationState.value = 0
            }
        }
    }
}

@Composable
fun InputView(start: Boolean, onClick: (Int) -> Unit) {
    val hourState = remember { mutableStateOf("") }
    val minuteState = remember { mutableStateOf("") }
    val secondState = remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        OutlinedTextField(
            value = hourState.value,
            onValueChange = {
                hourState.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text("H")
            }
        )
        OutlinedTextField(
            value = minuteState.value,
            onValueChange = {
                minuteState.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text("M")
            }
        )
        OutlinedTextField(
            value = secondState.value,
            onValueChange = {
                secondState.value = it
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            label = {
                Text("S")
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = {
                var seconds = 0
                if (hourState.value.isNotEmpty()) {
                    seconds += hourState.value.toInt() * 3600
                }
                if (minuteState.value.isNotEmpty()) {
                    seconds += minuteState.value.toInt() * 60
                }
                if (secondState.value.isNotEmpty()) {
                    seconds += secondState.value.toInt()
                }
                onClick.invoke(seconds)
            }
        ) {
            Text(if (start) "Stop !!" else "Start !!")
        }
    }
}

@Composable
fun CountDownView(start: Boolean, target: Int, duration: Int, finish: (Int) -> Unit) {
//    Log.i("zander", "start: $start, target: ${target}")
    val current = animateIntAsState(
        targetValue = target,
        animationSpec = tween(
            durationMillis = duration,
            easing = LinearEasing
        ),
        finishedListener = finish
    )
    Text(
        text = DateUtils.formatElapsedTime(current.value.toLong()),
        color = Color.Black,
        fontSize = 50.sp,
        modifier = Modifier.padding(0.dp, 30.dp)
    )
}
