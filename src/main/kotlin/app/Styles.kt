package app

import javafx.scene.paint.Color
import javafx.scene.text.FontWeight
import javafx.geometry.Pos
import tornadofx.*

class Styles : Stylesheet() {
	companion object {
		val heading by cssclass()
	}

	init {
		heading {
			//padding = box(0.px,0.px,0.px,5.px)
			fontSize = 12.px
			fontWeight = FontWeight.BOLD
			textFill = Color.BLACK
			alignment = Pos.CENTER
		}
	}
}
