package view

import watchReminder.Serie
import io.reactivex.Maybe
import io.reactivex.Observable
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.image.ImageView
import javafx.stage.Stage
import tornadofx.*
import javafx.scene.control.TextField
import javafx.scene.image.Image


class NewSerieDialog: Dialog<Maybe<Int>>() {
    private val root = Form()
		private var name: TextField by singleAssign()
		private var season: TextField by singleAssign()
		private var episode: TextField by singleAssign()

		init {
        title = "Add Serie"

        with(root) {
            fieldset("Serie") {
                field("Name") {
                    name = textfield()
                }
                field("Season") {
                    season = textfield()
                }
								field("Episode") {
                    episode = textfield()
								}
            }
        }

        setResultConverter {
            if (it == ButtonType.OK)
                Serie.createNew(name.text,season.text.toInt(),episode.text.toInt()).toMaybe() //returns ID for new Customer
            else
                Maybe.empty()
        }

        dialogPane.content = root
        dialogPane.buttonTypes.addAll(ButtonType.OK,ButtonType.CANCEL)
        graphic = ImageView(FX.primaryStage.icons[0])
        (dialogPane.scene.window as Stage).icons += FX.primaryStage.icons[0]
    }
}
