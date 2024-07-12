package ru.ndevelop.yandexhomework.presentation


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Configuration
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import ru.ndevelop.yandexhomework.App
import ru.ndevelop.yandexhomework.appComponent
import ru.ndevelop.yandexhomework.core.FetchDataWorker
import ru.ndevelop.yandexhomework.core.FetchDataWorkerFactory
import ru.ndevelop.yandexhomework.databinding.ActivityMainBinding
import java.util.concurrent.TimeUnit
import javax.inject.Inject


class MainActivity : AppCompatActivity(), Configuration.Provider {

    private lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var workerFactory: FetchDataWorkerFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        appComponent().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (savedInstanceState == null) {
            scheduleFetchDataWorker()
        }
    }

    private fun scheduleFetchDataWorker() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val fetchDataWorkRequest = PeriodicWorkRequestBuilder<FetchDataWorker>(8, TimeUnit.HOURS)
            .setConstraints(constraints)
            .build()

        WorkManager.initialize(this, workManagerConfiguration)
        WorkManager
            .getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "FetchDataWork",
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
                fetchDataWorkRequest
            )
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
