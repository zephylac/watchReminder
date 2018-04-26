package watchReminder

import tornadofx.*

class MyView : View() {

	private val persons = listOf(
	Film("Taxi 1", 2010),
	Film("Taxi 2", 2011),
	Film("Taxi 3", 2012),
	Film("Taxi 4", 2013)
	).observable()

	override val root = tableview(persons) {
		column("Nom", Film::name)
		column("Annee", Film::year)

	}
}
