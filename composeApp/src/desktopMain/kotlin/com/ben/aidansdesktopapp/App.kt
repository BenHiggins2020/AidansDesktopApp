package com.ben.aidansdesktopapp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.onClick
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material.icons.sharp.Menu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ben.aidansdesktopapp.Model.AppViewModel
import com.ben.aidansdesktopapp.Presentation.PopUp
import com.ben.aidansdesktopapp.Presentation.pages.main.Home
import com.ben.aidansdesktopapp.Presentation.pages.sharpe.Sharpe
import kotlinx.coroutines.flow.MutableStateFlow
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalFoundationApi::class)
@Composable
@Preview
fun App() {
//    System.setProperty("webdriver.chrome.driver", "C:\\Program Files\\Google\\Chrome\\Application\\chromedriver.exe")

    val viewModel = AppViewModel()

    val popUpText = MutableStateFlow<String>("Practice")
    val popUpTrigger = MutableTransitionState<Boolean>(false)
    val popUp = PopUp.PopUpBuilder().withTransitionState(popUpTrigger).withText(popUpText).build()

    val navigationPanelTrigger = MutableTransitionState<Boolean>(false)
    /*val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { Home(navController) }
        composable("sharpe") { Sharpe(navController) }
    }*/

    val tabs = listOf("Home", "Sharpe")
    var selectedTab by mutableStateOf("Home")

    MaterialTheme {

        Scaffold {

            when (selectedTab) {
                "Home" -> Home(viewModel)
                "Sharpe" -> Sharpe()
            }

            //Top Menu Bar
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight(.07f)
                    .background(Color.LightGray)
            ) {
                IconButton(
                    modifier = Modifier.size(20.dp),
                    onClick = {
                        navigationPanelTrigger.targetState = !navigationPanelTrigger.targetState
                    }
                ) {
                    Icon(imageVector = Icons.Default.Menu, contentDescription = "Menu")
                }
            }

            //Slide Out Navigation Bar
            AnimatedVisibility(
                visibleState = navigationPanelTrigger,
                enter = slideInHorizontally { -it } + fadeIn(),
                exit = slideOutHorizontally { -it * 2 } + fadeOut()
            ) {

                Box(
                    modifier = Modifier
                        .fillMaxWidth(.25f)
                        .fillMaxHeight()
                        .background(Color.LightGray, shape = RoundedCornerShape(8.dp))
                ) {
                    Column {

                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .padding(16.dp), // Remove fillMaxWidth()
                            horizontalArrangement = Arrangement.Start
                        ) {
                            IconButton(
                                modifier = Modifier.size(20.dp),
                                onClick = {
                                    navigationPanelTrigger.targetState =
                                        !navigationPanelTrigger.targetState
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }
                        }

                        Column(
                            verticalArrangement = Arrangement.Top,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f).fillMaxWidth()
                        ) {
                            tabs.forEach { tab ->
                                TextButton(
                                    onClick = {
                                        selectedTab = tab
                                    },
                                    modifier = Modifier.background(
                                        color = Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    ).fillMaxWidth()
                                ) {
                                    Text(
                                        text = tab,
                                        textAlign = TextAlign.Center,
                                    )
                                }
                            }
                        }


                    }


                }
            }
        }

        popUp.show()
    }


}



