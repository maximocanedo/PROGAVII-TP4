package frgp.utn.edu.ar.tp4

import ArticleDaoImpl
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import frgp.utn.edu.ar.tp4.data.models.Article
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    var create__stockText by mutableStateOf("")
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
    fun onCreate__stockTextChange(newText: String) {

        if (newText.isEmpty()) {
            create__stockText = ""
        } else {
            try {
                val stock = newText.toInt()
                create__stockText = newText
                create__stockError = false
                create__stockMsg = ""
            } catch (e: NumberFormatException) {
                create__stockError = true
                create__stockMsg = "El stock debe ser un numero valido"
            }
        }
    }

    fun onTabSelected(index: Int) {
        selectedTabIndex = index
    }


    /**
     * Pestaña LISTAR
     */
    var items by mutableStateOf(listOf<Article>())
        private set

    var emptyListMessage by mutableStateOf("")
        private set

    var listError by mutableStateOf(false)
        private set


    fun loadItems() {
        CoroutineScope(Dispatchers.IO).launch {
            var dao = ArticleDaoImpl()
            try {
                items = dao.getAllArticles()
                listError = false
                if (items.isEmpty()) {
                    emptyListMessage = "No hay elementos para mostrar"
                }
            } catch (e: Exception) {
                listError = true
                emptyListMessage = "Error al cargar los elementos"
            }
        }
    }

}
