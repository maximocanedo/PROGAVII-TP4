package frgp.utn.edu.ar.tp4

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    var selectedTabIndex by mutableStateOf(0)
        private set

    fun onTabSelected(index: Int) {
        selectedTabIndex = index
    }

}
