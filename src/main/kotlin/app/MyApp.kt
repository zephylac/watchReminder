package app

import javafx.scene.image.Image
import tornadofx.App
import tornadofx.addStageIcon
import view.MainView
import tornadofx.Stylesheet

import tornadofx.*

class MyApp: App(MainView::class, Styles::class) {
	init {
		addStageIcon(Image("/app/tornado-fx-logo.png"))
		importStylesheet("/css/styles.css")
	}
}
