package cz.mendelu.souvenirbox.ui.screens.dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import cz.mendelu.souvenirbox.R
import cz.mendelu.souvenirbox.database.SouvenirEntity
import cz.mendelu.souvenirbox.navigation.INavigationRouter
import cz.mendelu.souvenirbox.ui.elements.BaseScreen
import cz.mendelu.souvenirbox.testTags.TestTagNoSouvenirs
import cz.mendelu.souvenirbox.testTags.TestTagSouvenirCarousel

@Composable
fun DashboardScreen(
    navigation: INavigationRouter,
    paddingValues: PaddingValues,
    viewModel: DashboardViewModel = hiltViewModel<DashboardViewModel>(),
    testSouvenirs: List<SouvenirEntity>? = null
) {

    val state = viewModel.uiState.collectAsStateWithLifecycle()

    BaseScreen(
        topBarText = "Dashboard",
        showLoading = state.value.loading
    ) {
        DashboardScreenContent(
            paddingValuesTop = it,
            state = state.value,
            navigation = navigation
        )
    }
}

@Composable
fun DashboardScreenContent(
    paddingValuesTop: PaddingValues,
    state: DashboardUIState,
    navigation: INavigationRouter
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValuesTop)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        if (state.recentSouvenirs.isEmpty()) {
            // no images
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            ) {
                Text(
                    "No souvenirs yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.testTag(TestTagNoSouvenirs)
                )
            }
        } else {
            Text("Recently added souvenirs", style = MaterialTheme.typography.titleMedium)

            Carousel(
                souvenirs = state.recentSouvenirs,
                navigation = navigation,
                carouselNum = "1"
            )

            Text("Favourite souvenirs", style = MaterialTheme.typography.titleMedium)

            Carousel(
                souvenirs = state.favouriteSouvenirs,
                navigation = navigation,
                carouselNum = "2"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Carousel(
    souvenirs: List<SouvenirEntity>,
    navigation: INavigationRouter,
    carouselNum: String
) {

    // images
    HorizontalMultiBrowseCarousel(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .testTag(TestTagSouvenirCarousel + carouselNum),
        state = rememberCarouselState { souvenirs.size },
        preferredItemWidth = 240.dp,
        itemSpacing = 12.dp
    ) { index ->

        Box(
            modifier = Modifier
                .fillMaxHeight()
                .maskClip(RoundedCornerShape(24.dp))
        ) {
            val uri = souvenirs[index].imageUri

            Image(
                painter = if (uri.isNullOrEmpty())
                    painterResource(R.drawable.undraw_image_folder)
                else
                    rememberAsyncImagePainter(uri),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp)
                    .clickable {
                        navigation.navigateToSouvenirDetail(souvenirs[index].id!!)
                    }
            )
        }
    }
}
