package watchReminder

import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import org.nield.rxkotlinjdbc.execute
import org.nield.rxkotlinjdbc.insert
import java.sql.DriverManager


/**
 * An in-memory `Database` using SQLite holding three tables. This `Database` performs reactive querying
 * and writing via [RxJava-JDBC](https://github.com/davidmoten/rxjava-jdbc)
 */
val db = DriverManager.getConnection("jdbc:sqlite::db:").apply {

	//create MOVIE table
	execute("CREATE TABLE IF NOT EXISTS MOVIE (ID INTEGER PRIMARY KEY, NAME VARCHAR, YEAR INTEGER)").toSingle().subscribe()

	/* listOf(
            "Alpha Analytics",
            "Rexon Solutions",
            "Travis Technologies",
            "Anex Applications",
            "Edvin Enterprises",
            "T-Boom Consulting",
            "Nield Industrial",
            "Dash Inc"
    ).toObservable()
    .flatMap {
        insert("INSERT INTO MOVIE (NAME) VALUES (:name)")
                .parameter("name", it)
                .toObservable { it.getInt(1) }
    }
    .toList()
    .subscribeBy(
        onSuccess = { println("Movie table created, KEYS: $it")},
        onError = { throw RuntimeException(it) }
    ) */


	// create SERIE table
	execute("CREATE TABLE IF NOT EXISTS SERIE (ID INTEGER PRIMARY KEY, NAME VARCHAR, SEASON INTEGER, EPISODE INTEGER)").toSingle().subscribe()
}
