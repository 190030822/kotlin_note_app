@file:OptIn(ExperimentalMaterial3Api::class)

package com.ad_coding.noteappcourse.ui.screen.note

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.*
import androidx.compose.material.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

@Composable
fun NoteScreen(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit
) {
    val scaffoldState = rememberScaffoldState()
    val scope = rememberCoroutineScope()
    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Add/ Edit TODO", color = Color.White) },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            onEvent(NoteEvent.NavigateBack)
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "navigate back",
                            tint = Color.White,
                        )
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color(0xFF0B6885)),
                actions = {
                    IconButton(
                        onClick = {
                            onEvent(NoteEvent.DeleteNote)
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Note ${state.title} deleted successfully")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            tint = Color.White,
                            contentDescription = "delete"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    horizontal = 20.dp,
                    vertical = 15.dp
                ),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            var showTitleError by remember { mutableStateOf(false) }
            var showContentError by remember { mutableStateOf(false) }
            OutlinedTextField(
                textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
                value = state.title,
                onValueChange = {
                    onEvent(NoteEvent.TitleChange(it))
                    showTitleError = it.isBlank()
                },
                placeholder = {
                    Text(text = "Title")
                },
                isError = showTitleError && state.title.equals(""),
            )
            if (showTitleError) {
                Text(text = "title can't be empty", color = Color.Red)
            }
            OutlinedTextField(
                textStyle = TextStyle(color = Color.Black, fontSize = 18.sp),
                value = state.content,
                onValueChange = {
                    onEvent(NoteEvent.ContentChange(it))
                    showContentError = it.isBlank()
                },
                placeholder = {
                    Text(text = "Content")
                },
                isError = showContentError && state.content.equals("")
            )
            if (showContentError) {
                Text(text = "content can't be empty", color = Color.Red)
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        if (!showContentError && !showTitleError && !state.content.equals("") && !state.title.equals("")) {
                            scope.launch {
                                scaffoldState.snackbarHostState.showSnackbar("Note ${state.title} added successfully", actionLabel = null, SnackbarDuration.Long)
                            }
                            onEvent(NoteEvent.Save)
                        } else {
                            if (state.content.equals("")) {
                                showContentError = true
                            }
                            if (state.title.equals("")) {
                                showTitleError = true
                            }


                        }
                    },
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(text = "Save", color = Color.White, fontSize = 20.sp)
                }
            }
        }
    }
}