package watchReminder

import tornadofx.*

// Video is a super class. It contains the name.
open class Video(name: String){
	var name by property(name)
	fun nameProperty() = getProperty(Video::name)
}
