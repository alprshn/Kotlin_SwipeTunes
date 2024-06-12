package com.example.kotlin_spotify_random_like_app

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {


    private fun refreshToken() {

    }

    override fun doWork(): Result {
        TODO("Not yet implemented")
    }


}