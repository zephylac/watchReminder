package view

import watchReminder.Movie
import watchReminder.Serie
import io.reactivex.subjects.BehaviorSubject
import tornadofx.Controller

class EventController: Controller() {
		val searchMovies = BehaviorSubject.create<Set<Int>>()
		val searchMoviesUsages = BehaviorSubject.create<Set<Int>>()

		val applyMovies = BehaviorSubject.create<Set<Int>>()

		val refreshMovies = BehaviorSubject.create<Unit>()

		val selectedMovies = BehaviorSubject.create<Set<Movie>>()

		val moveMovieUp = BehaviorSubject.create<Int>()
		val moveMovieDown = BehaviorSubject.create<Int>()

		val createNewMovie = BehaviorSubject.create<Unit>()
		val deleteMovies = BehaviorSubject.create<Set<Int>>()
		val deletedMovies = BehaviorSubject.create<Set<Int>>()

		val searchSeries = BehaviorSubject.create<Set<Int>>()
		val searchSeriesUsages = BehaviorSubject.create<Set<Int>>()

		val applySeries = BehaviorSubject.create<Set<Int>>()

		val refreshSeries = BehaviorSubject.create<Unit>()

		val selectedSeries = BehaviorSubject.create<Set<Serie>>()

		val moveSerieUp = BehaviorSubject.create<Int>()
		val moveSerieDown = BehaviorSubject.create<Int>()

		val createNewSerie = BehaviorSubject.create<Unit>()
		val deleteSeries = BehaviorSubject.create<Set<Int>>()
		val deletedSeries = BehaviorSubject.create<Set<Int>>()

		val refreshedSeries1 = BehaviorSubject.create<Set<Unit>>()

		val refreshedSeries = BehaviorSubject.create<Set<Int>>()
		val IncSeasonSeries = BehaviorSubject.create<Set<Int>>()
		val DecSeasonSeries = BehaviorSubject.create<Set<Int>>()

		val IncSeasonSeries1 = BehaviorSubject.create<Serie>()

		val IncEpisodeSeries = BehaviorSubject.create<Set<Int>>()
		val DecEpisodeSeries = BehaviorSubject.create<Set<Int>>()
}
