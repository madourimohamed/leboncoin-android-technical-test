package fr.leboncoin.androidrecruitmenttestapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.ViewModelProvider
import com.adevinta.spark.SparkTheme
import fr.leboncoin.androidrecruitmenttestapp.di.AppDependenciesProvider
import fr.leboncoin.androidrecruitmenttestapp.ui.AlbumsScreen
import fr.leboncoin.androidrecruitmenttestapp.utils.AnalyticsHelper

class MainActivity : ComponentActivity() {

    private val viewModel: AlbumsViewModel by lazy {
        val dependencies = (application as AppDependenciesProvider).dependencies
        val factory = AlbumsViewModel.Factory(dependencies.dataDependencies.albumsRepository)
        ViewModelProvider(this, factory)[AlbumsViewModel::class.java]
    }

    private val analyticsHelper: AnalyticsHelper by lazy {
        val dependencies = (application as AppDependenciesProvider).dependencies
        dependencies.analyticsHelper
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            SparkTheme {
                AlbumsScreen(
                    viewModel = viewModel,
                    onItemSelected = {
                        analyticsHelper.trackSelection(it.id.toString())
                        startActivity(Intent(this, DetailsActivity::class.java))
                    }
                )
            }
        }
    }
}