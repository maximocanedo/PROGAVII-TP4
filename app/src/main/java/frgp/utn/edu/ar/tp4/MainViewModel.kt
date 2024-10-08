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
import frgp.utn.edu.ar.tp4.data.models.Category

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
    var selectedCategoryIndex by mutableStateOf(0)
        private set
    var create__selectedCategoryText by mutableStateOf("")
        private set
    var create__categories by mutableStateOf(listOf<Category>())
        private set


    suspend fun onCreate__idTextChange(newText: String) {
        val articleDaoImpl = ArticleDaoImpl()
        if (newText.isEmpty()) {
            create__idText = ""
        } else {
            create__idText = newText
            try {
                val id = newText.toInt()
                if (articleDaoImpl.getArticleById(id).getId() == id) {
                    create__idError = true
                    create__idMsg = "El id ya existe"
                }
                else {
                    create__idError = false
                    create__idMsg = ""
                }
            } catch (e: NumberFormatException) {
                create__idError = true
                create__idMsg = "El id debe ser un numero valido"
            }
        }
    }
    fun onCreate__nameTextChange(newText: String) {
        if (newText.isEmpty()) {
            create__nameText = ""
        } else {
            create__nameText = newText
            if (newText.contains("\\d".toRegex())) {
                create__nameError = true
                create__nameMsg = "El nombre no puede contener numeros"
            } else {
                create__nameError = false
                create__nameMsg = ""
            }
        }
    }
    fun onCreate__stockTextChange(newText: String) {
        if (newText.isEmpty()) {
            create__stockText = ""
        } else {
            create__stockText = newText
        }
    }

    fun create__StockErrorChange(newError: Boolean) {
        create__stockError = newError
    }

    fun create__StockMsgChange(newMsg: String) {
        create__stockMsg = newMsg
    }

    fun onCreate__selectedCategoryIndexChange(newIndex: Int) {
        selectedCategoryIndex = newIndex
    }

    fun onCreate__selectedCategoryTextChange(newText: String) {
        create__selectedCategoryText = newText
    }

    fun onCreate__categoriesChange(newCategories: List<Category>) {
        create__categories = newCategories
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
            val dao = ArticleDaoImpl()
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
