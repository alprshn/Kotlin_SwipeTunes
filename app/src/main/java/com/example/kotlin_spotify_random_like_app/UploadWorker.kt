package com.example.kotlin_spotify_random_like_app

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadWorker(appContext: Context, workerParams: WorkerParameters)
    : Worker(appContext, workerParams) {




    override fun doWork(): Result {
        SpotifyApiManager.getRefreshToken()
        val sharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("prefToken",
            Context.MODE_PRIVATE)
        val refreshToken: SharedPreferences.Editor = sharedPreferences.edit()
        refreshToken.putString("refresh_token", SpotifyApiManager.refreshToken).apply()

        val accessSharedPreferences: SharedPreferences = applicationContext.getSharedPreferences("prefAccessToken",
            Context.MODE_PRIVATE)
        val accessToken: SharedPreferences.Editor = sharedPreferences.edit()
        refreshToken.putString("access_token", SpotifyApiManager.refreshToken).apply()

        Log.e("work", SpotifyApiManager.refreshToken.toString())
        return Result.success()
    }


}