package view

import watchReminder.Movie
import io.reactivex.Maybe
import io.reactivex.Observable
import javafx.scene.control.ButtonType
import javafx.scene.control.Dialog
import javafx.scene.image.ImageView
import javafx.stage.Stage
import tornadofx.*

class NewMovieDialog: Dialog<Maybe<Int>>() {
    private val root = Form()

    init {
        title = "Add movie"

        with(root) {
            fieldset("Movie Name") {
                textfield {
                    setResultConverter {
                        if (it == ButtonType.OK)
                            Movie.createNew(text).toMaybe()//returns ID for new Customer
                        else
                            Maybe.empty()
                    }
                }
            }
        }

        dialogPane.content = root
        dialogPane.buttonTypes.addAll(ButtonType.OK,ButtonType.CANCEL)
        graphic = ImageView(FX.primaryStage.icons[0])
        (dialogPane.scene.window as Stage).icons += FX.primaryStage.icons[0]
    }
}
