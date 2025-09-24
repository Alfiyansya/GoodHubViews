package com.alfiansyah.goodhubviews.core.di

import android.content.Context
import androidx.core.app.NotificationCompat
import com.alfiansyah.goodhubviews.core.utils.Constant.NOTIFICATION_DOWNLOAD_CHANNEL_ID
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationModule {

    @Provides
    @Singleton
    fun provideBaseNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, NOTIFICATION_DOWNLOAD_CHANNEL_ID)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setForegroundServiceBehavior(NotificationCompat.FOREGROUND_SERVICE_IMMEDIATE)
    }
}
