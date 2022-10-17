package pl.rubajticos.pepfirebase.di

import com.google.firebase.database.FirebaseDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import pl.rubajticos.pepfirebase.data.PeopleRepository
import pl.rubajticos.pepfirebase.data.RealtimePeopleRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    @Singleton
    abstract fun bindPeopleRepository(repo: RealtimePeopleRepository): PeopleRepository

    companion object {
        @Singleton
        @Provides
        fun provideRealtimeDatabase() = FirebaseDatabase.getInstance()
    }
}