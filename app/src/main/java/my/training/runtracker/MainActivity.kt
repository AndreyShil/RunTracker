package my.training.runtracker

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_host_fragment) as NavHostFragment


        val graphInflater = navHostFragment.navController.navInflater

        val navGraph = graphInflater.inflate(R.navigation.main_nav_graph)
        val navController = navHostFragment.navController

        val destinationId = my.training.feature.auth.R.id.auth_graph

        navGraph.setStartDestination(destinationId)
        navController.setGraph(navGraph, bundleOf())
//        navController.navigate(my.training.feature.auth.R.id.auth_graph)

//        if (savedInstanceState == null) {
//            supportFragmentManager.commit {
//                add(R.id.container, MainFragment())
//            }
//        }
    }
}