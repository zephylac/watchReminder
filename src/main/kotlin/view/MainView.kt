package view

import com.github.thomasnield.rxkotlinfx.actionEvents
import javafx.geometry.Orientation
import javafx.scene.layout.BorderPane
import tornadofx.*

class MainView : View() {
	override val root = BorderPane()

	private val movieV: MovieView by inject()
	private val serieV: SerieView by inject()

	private val controller: EventController by inject()

	init {
		title = "Movie / Serie watch list"

		with(root) {
			setPrefSize(940.0,610.0)
			top = menubar {
				menu("File") {
					item("Refresh").apply {
						actionEvents().map { Unit }.subscribe(controller.refreshMovies)
						actionEvents().map { Unit }.subscribe(controller.refreshSeries)
					}
				}
				menu("Edit") {
					item("Add new movie").actionEvents().map { Unit }.subscribe(controller.createNewMovie)
					item("Add new serie").actionEvents().map { Unit }.subscribe(controller.createNewSerie)
				}
			}
			center = splitpane {
				orientation = Orientation.HORIZONTAL
				this += movieV
				this += serieV
			}
		}
	}
}
