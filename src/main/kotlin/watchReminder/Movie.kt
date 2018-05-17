package watchReminder

import org.nield.rxkotlinjdbc.execute
import org.nield.rxkotlinjdbc.insert
import org.nield.rxkotlinjdbc.select


class Movie(val id: Int, val name: String) {

	/**
	* Deletes this `Movie` instance
	*/
	fun delete() = db.execute("DELETE FROM MOVIE WHERE ID = ?")
	.parameter(id)
	.toSingle()


	companion object {

		/**
		* Retrieves all Movies
		*/
		val all = db.select("SELECT * FROM MOVIE")
		.toObservable { Movie(it.getInt("ID"), it.getString("NAME")) }

		/**
		* Retrieves `Movie` for a given `id`
		*/
		fun forId(id: Int) = db.select("SELECT * FROM MOVIE WHERE ID = ?")
		.parameter(id)
		.toSingle { Movie(it.getInt("ID"), it.getString("NAME")) }

		/**
		* Creates a new `Movie` with the given `name`
		*/
		fun createNew(name: String) = db.insert("INSERT INTO MOVIE (NAME) VALUES (:name)")
		.parameter("name",name)
		.toSingle { it.getInt(1) }
	}
}
