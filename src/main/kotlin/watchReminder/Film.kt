package watchReminder

import tornadofx.*

class Film(name: String, year: Int) : Video(name){

	var year by property(year)
	fun yearProperty() = getProperty(Film::year)

}
