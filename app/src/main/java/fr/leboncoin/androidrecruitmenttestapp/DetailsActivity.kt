package fr.leboncoin.androidrecruitmenttestapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.components.image.Illustration
import fr.leboncoin.androidrecruitmenttestapp.di.AppDependenciesProvider
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper

class DetailsActivity : ComponentActivity() {

    private val analyticsHelper: AnalyticsHelper by lazy {
        val dependencies = (application as AppDependenciesProvider).dependencies
        dependencies.analyticsHelper
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (savedInstanceState == null) {
            analyticsHelper.trackScreenView("Details")
        }

        setContent {
            SparkTheme {
                Illustration(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(id = R.drawable.work_in_progress),
                    contentDescription = null,
                    contentScale = ContentScale.Inside,
                )
            }
        }
    }
}

