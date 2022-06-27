package com.example.mystopwatch.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mystopwatch.databinding.ActivityMainBinding
import com.example.mystopwatch.model.*
import com.example.mystopwatch.viewModel.StopwatchListOrchestrator
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {
    private val timestampProvider = object : TimestampProvider {
        override fun getMilliseconds(): Long {
            return System.currentTimeMillis()
        }
    }

    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private var job: Job? = null


    private val stopwatchListOrchestratorFirst = StopwatchListOrchestrator(
        StopwatchStateHolder(
            StopwatchStateCalculator(
                timestampProvider,
                ElapsedTimeCalculator(timestampProvider)
            ),
            ElapsedTimeCalculator(timestampProvider),
            TimestampMillisecondsFormatter()
        ),
        CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        )
    )

    private val stopwatchListOrchestratorSecond = StopwatchListOrchestrator(
        StopwatchStateHolder(
            StopwatchStateCalculator(
                timestampProvider,
                ElapsedTimeCalculator(timestampProvider)
            ),
            ElapsedTimeCalculator(timestampProvider),
            TimestampMillisecondsFormatter()
        ),
        CoroutineScope(
            Dispatchers.Main
                    + SupervisorJob()
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        with(binding) {
            job = scope.launch {
                stopwatchListOrchestratorFirst
                    .ticker
                    .collect { textTime.text = it }
            }
            job = scope.launch {
                stopwatchListOrchestratorSecond
                    .ticker
                    .collect {
                        textTime2.text = it
                    }
            }

            with(binding) {
                buttonStart.setOnClickListener { stopwatchListOrchestratorFirst.start() }
                buttonPause.setOnClickListener { stopwatchListOrchestratorFirst.pause() }
                buttonStop.setOnClickListener { stopwatchListOrchestratorFirst.stop() }

                buttonStart2.setOnClickListener { stopwatchListOrchestratorSecond.start() }
                buttonPause2.setOnClickListener { stopwatchListOrchestratorSecond.pause() }
                buttonStop2.setOnClickListener { stopwatchListOrchestratorSecond.stop() }
            }
        }
    }

    override fun onDestroy() {
        scope.cancel()
        super.onDestroy()
    }
}