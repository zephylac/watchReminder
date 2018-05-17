package view

import app.Styles
import app.currentSelections
import app.toSet
import com.github.thomasnield.rxkotlinfx.actionEvents
import com.github.thomasnield.rxkotlinfx.events
import com.github.thomasnield.rxkotlinfx.onChangedObservable
import com.github.thomasnield.rxkotlinfx.toMaybe
import watchReminder.Serie
import io.reactivex.Observable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.rxkotlin.toObservable
import javafx.geometry.Orientation
import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import javafx.scene.control.SelectionMode
import javafx.scene.control.TableView
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.paint.Color
import org.controlsfx.glyphfont.FontAwesome
import org.controlsfx.glyphfont.GlyphFontRegistry
import tornadofx.*

class SerieView : View() {
	private val controller: EventController by inject()
	private var table: TableView<Serie> by singleAssign()

	private val fontAwesome = GlyphFontRegistry.font("FontAwesome")
	private val addGlyph = fontAwesome.create(FontAwesome.Glyph.PLUS).color(Color.BLUE)
	private val removeGlyph = fontAwesome.create(FontAwesome.Glyph.TIMES).color(Color.RED)

	private var b1 : Int = 0

	override val root = borderpane {
		top = label("SERIE") {
			addClass(Styles.heading)
			useMaxWidth = true
		}

		center = tableview<Serie> {
			readonlyColumn("NAME", Serie::name)
			readonlyColumn("SEASON", Serie::season)
			readonlyColumn("EPISODE", Serie::episode)


			columnResizePolicy = SmartResize.POLICY
			selectionModel.selectionMode = SelectionMode.MULTIPLE

			//broadcast selections
			selectionModel.selectedItems.onChangedObservable()
			.map { it.filterNotNull().toSet() }
			.subscribe(controller.selectedSeries )

			//Import data and refresh event handling
			controller.refreshSeries.startWith(Unit)
			.flatMapSingle {
				Serie.all.toList()
			}.subscribeBy(
				onNext = { items.setAll(it) },
				onError = { alert(Alert.AlertType.ERROR, "PROBLEM!", it.message ?: "").show() }
			)

			//handle search request
			controller.searchSeries
			.subscribeBy(
				onNext = { ids ->
					moveToTopWhere { it.id in ids }
					requestFocus()
				},
				onError	={ it.printStackTrace() }
			)

			table = this
		}

		left = toolbar {
			orientation = Orientation.VERTICAL

			//add Serie button
			button("", addGlyph) {
				tooltip("Create a new Serie")
				useMaxWidth = true
				textFill = Color.GREEN
				actionEvents().map { Unit }.subscribe(controller.createNewSerie)
			}

			//remove Serie button
			button("", removeGlyph) {
				tooltip("Remove selected Series")
				useMaxWidth = true
				textFill = Color.RED
				actionEvents().map {
					table.selectionModel.selectedItems
					.asSequence()
					.filterNotNull()
					.map { it.id }
					.toSet()
				}.subscribe(controller.deleteSeries)
			}

			//inc Serie button
			button("Season +") {
				tooltip("Increment season")
				useMaxWidth = true
				textFill = Color.GREEN
				actionEvents().map {
					table.selectionModel.selectedItems
					.asSequence()
					.filterNotNull()
					.map { it.id }
					.toSet()
				}.subscribe(controller.IncSeasonSeries)
			}

			//inc Serie button
			button("Season -") {
				tooltip("Increment season")
				useMaxWidth = true
				textFill = Color.GREEN
				actionEvents().map {
					table.selectionModel.selectedItems
					.asSequence()
					.filterNotNull()
					.map { it.id }
					.toSet()
				}.subscribe(controller.DecSeasonSeries)
			}

			//inc Serie button
			button("Episode +") {
				tooltip("Increment season")
				useMaxWidth = true
				textFill = Color.GREEN
				actionEvents().map {
					table.selectionModel.selectedItems
					.asSequence()
					.filterNotNull()
					.map { it.id }
					.toSet()
				}.subscribe(controller.IncEpisodeSeries)
			}

			//inc Serie button
			button("Episode -") {
				tooltip("Increment season")
				useMaxWidth = true
				textFill = Color.GREEN
				actionEvents().map {
					table.selectionModel.selectedItems
					.asSequence()
					.filterNotNull()
					.map { it.id }
					.toSet()
				}.subscribe(controller.DecEpisodeSeries)
			}
		}

		//create new Serie requests
		controller.createNewSerie
		.flatMapMaybe { NewSerieDialog().toMaybe() }
		.flatMapMaybe { it }
		.flatMapSingle { Serie.forId(it) }
		.subscribe {
			table.items.add(it)
			table.selectionModel.clearSelection()
			table.selectionModel.select(it)
			table.requestFocus()
		}


		//increment by one season
		val incrementS = controller.IncSeasonSeries
		.flatMapSingle {
			table.currentSelections.toList()
			.flatMapObservable { it.toObservable() }
			.flatMapSingle { it.incSeason() }
			.toSet()
		}
		incrementS.subscribe(controller.refreshedSeries)

		//increment by one episode
		val incrementE = controller.IncEpisodeSeries
		.flatMapSingle {
			table.currentSelections.toList()
			.flatMapObservable { it.toObservable() }
			.flatMapSingle { it.incEpisode() }
			.toSet()
		}
		incrementE.subscribe(controller.refreshedSeries)

		//decrement by one season
		val decrementS = controller.DecSeasonSeries
		.flatMapSingle {
			table.currentSelections.toList()
			.flatMapObservable { it.toObservable() }
			.flatMapSingle { it.decSeason() }
			.toSet()
		}
		decrementS.subscribe(controller.refreshedSeries)

		//decrement by one episode
		val decrementE = controller.DecEpisodeSeries
		.flatMapSingle {
			table.currentSelections.toList()
			.flatMapObservable { it.toObservable() }
			.flatMapSingle { it.decEpisode() }
			.toSet()
		}
		decrementE.subscribe(controller.refreshedSeries)

		//handle Serie deletions
		val deletions = controller.deleteSeries
		.flatMapSingle {
			table.currentSelections.toList()
		}.flatMapSingle { deleteItems ->
			Alert(Alert.AlertType.WARNING, "Are you sure you want to delete these ${deleteItems.size} Series?", ButtonType.YES, ButtonType.NO)
			.toMaybe().filter { it == ButtonType.YES }
			.map { deleteItems }
			.flatMapObservable { it.toObservable() }
			.flatMapSingle { it.delete() }
			.toSet()
		}.publish() //publish() to prevent multiple subscriptions triggering alert multiple times

		deletions.subscribe(controller.refreshedSeries)

		//refresh on deletion
		controller.refreshedSeries
		.map { Unit }
		.subscribe(controller.refreshSeries) //push this refresh Series

		//trigger the publish
		deletions.connect()
	}
}
