package watchReminder

import org.nield.rxkotlinjdbc.execute
import org.nield.rxkotlinjdbc.insert
import org.nield.rxkotlinjdbc.select

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxjavafx.subscriptions.CompositeBinding

import app.flatCollect
import io.reactivex.rxkotlin.*


class Serie(val id: Int, val name: String, val season: Int, val episode: Int) {

	//We maintain a collection of bindings and disposables to unsubscribe them later
	private val bindings = CompositeBinding()
	private val disposables = CompositeDisposable()

	/** Overried toString Method
	*
	*/
	override fun toString() : String = "$id - $name - $season - $episode"

	/**
	* Deletes this `Serie` instance
	*/
	fun delete() = db.execute("DELETE FROM SERIE WHERE ID = ?")
	.parameter(id)
	.toSingle()

	/**
	* Increment this `Serie` season
	*/
	fun incSeason() = db.execute("UPDATE SERIE SET SEASON = SEASON + 1 WHERE ID = ?")
	.parameter(id)
	.toSingle()

	/**
	* Increment this `Serie` season
	*/
	fun decSeason() = db.execute("UPDATE SERIE SET SEASON = SEASON - 1 WHERE ID = ?")
	.parameter(id)
	.toSingle()

	/**
	* Increment this `Serie` season
	*/
	fun incEpisode() = db.execute("UPDATE SERIE SET EPISODE = EPISODE + 1 WHERE ID = ?")
	.parameter(id)
	.toSingle()

	/**
	* Increment this `Serie` season
	*/
	fun decEpisode() = db.execute("UPDATE SERIE SET EPISODE = EPISODE - 1 WHERE ID = ?")
	.parameter(id)
	.toSingle()

	/**Releases any reactive disposables associated with this SalesPerson.
	 * This is very critical to prevent memory leaks with infinite hot Observables
	 * because they do not know when they are complete
	 */
	fun dispose() {
			bindings.dispose()
			disposables.dispose()
	}

	companion object {

		/**
		* Retrieves all Movies
		*/
		val all = db.select("SELECT * FROM SERIE")
		.toObservable { Serie(it.getInt("ID"), it.getString("NAME"), it.getInt("SEASON"), it.getInt("EPISODE")) }

		/**
		* Retrieves `Serie` for a given `id`
		*/
		fun forId(id: Int) = db.select("SELECT * FROM SERIE WHERE ID = ?")
		.parameter(id)
		.toSingle { Serie(it.getInt("ID"), it.getString("NAME"), it.getInt("SEASON"), it.getInt("EPISODE")) }

		/**
		* Creates a new `Serie` with the given `name`
		*/
		fun createNew(name: String, season: Int, episode: Int) = db.insert("INSERT INTO SERIE (NAME,SEASON,EPISODE) VALUES (:name,:season,:episode)")
		.parameter("name",name)
		.parameter("season",season)
		.parameter("episode",episode)
		.toSingle { it.getInt(1) }
	}
}
