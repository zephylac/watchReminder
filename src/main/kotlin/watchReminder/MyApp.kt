package watchReminder

import javafx.stage.Stage
import tornadofx.*

class MyApp: App(MyView::class){}


fun main(args: Array<String>){
	launch<MyApp>(args)
}
