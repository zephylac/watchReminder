package view

import app.Styles
import app.currentSelections
import app.toSet
import com.github.thomasnield.rxkotlinfx.actionEvents
import com.github.thomasnield.rxkotlinfx.events
import com.github.thomasnield.rxkotlinfx.onChangedObservable
import com.github.thomasnield.rxkotlinfx.toMaybe
import watchReminder.Movie
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

import javafx.geometry.Pos



class MovieView : View() {
	private val controller: EventController by inject()
	private var table: TableView<Movie> by singleAssign()

	private val fontAwesome = GlyphFontRegistry.font("FontAwesome")
	private val addGlyph = fontAwesome.create(FontAwesome.Glyph.PLUS).color(Color.BLUE)
	private val removeGlyph = fontAwesome.create(FontAwesome.Glyph.TIMES).color(Color.RED)

	override val root = borderpane {
		top = label("MOVIE") {
			addClass(Styles.heading)
			useMaxWidth = true
		}

		center = tableview<Movie> {
			columnResizePolicy = SmartResize.POLICY
			readonlyColumn("NAME", Movie::name)

			selectionModel.selectionMode = SelectionMode.MULTIPLE

			//broadcast selections
			selectionModel.selectedItems.onChangedObservable()
			.map { it.filterNotNull().toSet() }
			.subscribe(controller.selectedMovies )

			//Import data and refresh event handling
			controller.refreshMovies.startWith(Unit)
			.flatMapSingle {
				Movie.all.toList()
			}.subscribeBy(
			onNext = { items.setAll(it) },
			onError = { alert(Alert.AlertType.ERROR, "PROBLEM!", it.message ?: "").show() }
			)

			//handle search request
			controller.searchMovies
			.subscribeBy(
			onNext = { ids ->
			moveToTopWhere { it.id in ids }
			requestFocus()
			},
			onError	={ it.printStackTrace() }
			)

			this.getStyleClass().add("tree-table-view")

			table = this
		}

		left = toolbar {
			orientation = Orientation.VERTICAL

			//add movie button
			button("", addGlyph) {
				tooltip("Create a new Movie")
				useMaxWidth = true
				textFill = Color.BLUE

				actionEvents().map { Unit }.subscribe(controller.createNewMovie)
			}

			//remove movie button
			button("", removeGlyph) {
				tooltip("Remove selected Movies")
				useMaxWidth = true
				textFill = Color.RED

				actionEvents().map {
					table.selectionModel.selectedItems
					.asSequence()
					.filterNotNull()
					.map { it.id }
					.toSet()
				}.subscribe(controller.deleteMovies)
			}
		}

		//create new Movie requests
		controller.createNewMovie
		.flatMapMaybe { NewMovieDialog().toMaybe() }
		.flatMapMaybe { it }
		.flatMapSingle { Movie.forId(it) }
		.subscribe {
			table.items.add(it)
			table.selectionModel.clearSelection()
			table.selectionModel.select(it)
			table.requestFocus()
		}


		//handle Movie deletions
		val deletions = controller.deleteMovies
		.flatMapSingle {table.currentSelections.toList()}
		.flatMapSingle { deleteItems ->
			Alert(Alert.AlertType.WARNING, "Are you sure you want to delete these ${deleteItems.size} movies?", ButtonType.YES, ButtonType.NO)
			.toMaybe().filter { it == ButtonType.YES }
			.map { deleteItems }
			.flatMapObservable { it.toObservable() }
			.flatMapSingle { it.delete() }
			.toSet()
		}.publish() //publish() to prevent multiple subscriptions triggering alert multiple times

		deletions.subscribe(controller.deletedMovies)

		//refresh on deletion
		controller.deletedMovies
		.map { Unit }
		.subscribe(controller.refreshMovies) //push this refresh movies

		//trigger the publish
		deletions.connect()
	}
}
