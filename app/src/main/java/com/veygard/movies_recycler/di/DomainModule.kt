package com.veygard.movies_recycler.di

import com.veygard.movies_recycler.data.remote.api.MoviesApi
import com.veygard.movies_recycler.data.remote.supports.MovieApiTimeoutControl
import com.veygard.movies_recycler.domain.repository.MoviesRepository
import com.veygard.movies_recycler.domain.repository.MoviesRepositoryImpl
import com.veygard.movies_recycler.domain.use_cases.GetMoviesUseCase
import com.veygard.movies_recycler.domain.use_cases.MoviesUseCases
import com.veygard.movies_recycler.domain.use_cases.SearchMovieUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.DelicateCoroutinesApi
import javax.inject.Singleton

@DelicateCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    @Singleton
    fun provideMoviesRepository(
        timeout: MovieApiTimeoutControl,
        usersApiService: MoviesApi
    ): MoviesRepository = MoviesRepositoryImpl(usersApiService, timeout)

    @Provides
    @Singleton
    fun provideUseCases(
        moviesRepository: MoviesRepository,
    ): MoviesUseCases = MoviesUseCases(
        getMoviesUseCase = GetMoviesUseCase(moviesRepository = moviesRepository),
        searchMovieUseCase = SearchMovieUseCase(moviesRepository = moviesRepository)
    )

}