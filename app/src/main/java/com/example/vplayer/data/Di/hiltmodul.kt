package com.example.vplayer.data.Di

import android.app.Application
import com.example.vplayer.data.repo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object hiltmodul {
    @Provides
    @Singleton
    fun providevideorepo():repo{
        return repo()
    }


}