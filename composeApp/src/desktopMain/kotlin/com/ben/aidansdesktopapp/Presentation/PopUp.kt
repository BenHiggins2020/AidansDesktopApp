package com.ben.aidansdesktopapp.Presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.Timer
import kotlin.concurrent.schedule

class PopUp {

    lateinit var popUpTrigger: MutableTransitionState<Boolean>
    lateinit var popUpText: MutableStateFlow<String>

    class PopUpBuilder {
        val popUp = PopUp()

        fun withTransitionState(transitionState: MutableTransitionState<Boolean>): PopUpBuilder {
            popUp.popUpTrigger = transitionState
            return this
        }

        fun withText(text: MutableStateFlow<String>): PopUpBuilder {
            popUp.popUpText = text
            return this
        }

        fun build(): PopUp {
            return popUp
        }
    }


    @Composable
    fun show() {

        LaunchedEffect(popUpTrigger.targetState) {
            if (popUpTrigger.targetState) {
                Timer().schedule(SHORT) {
                    popUpTrigger.targetState = !popUpTrigger.targetState
                }
            }
        }
        AnimatedVisibility(
            visibleState = popUpTrigger,
            enter = slideInVertically(),
            exit = slideOutVertically()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(.3f)
                    .fillMaxHeight(.1f)
                    .border(border = BorderStroke(4.dp, Color.Black), shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = popUpText.value,
                )
            }
        }

    }

    companion object {
        val SHORT = 3000L
        val LONG = 5000L

        fun startTimer(time: Long) = runBlocking {
            launch {
                delay(time)
            }
        }
    }
}

