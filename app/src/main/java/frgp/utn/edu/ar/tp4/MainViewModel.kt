package frgp.utn.edu.ar.tp4

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var selectedTabIndex by mutableStateOf(0)
        private set

    var create__idText by mutableStateOf("")
        private set
    var create__idMsg by mutableStateOf("")
        private set
    var create__idError by mutableStateOf(false)
        private set

    var create__nameText by mutableStateOf("")
        private set
    var create__nameMsg by mutableStateOf("")
        private set
    var create__nameError by mutableStateOf(false)
        private set

    var create__stockText by mutableStateOf(0)
        private set
    var create__stockMsg by mutableStateOf("")
        private set
    var create__stockError by mutableStateOf(false)
        private set



    fun onCreate__idTextChange(newText: String) {
        create__idText = newText
    }
    fun onCreate__nameTextChange(newText: String) {
        create__nameText = newText
    }
    fun onCreate__stockTextChange(newText: Int) {
        create__stockText = newText
    }



    fun onTabSelected(index: Int) {
        selectedTabIndex = index
    }

}
