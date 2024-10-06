package frgp.utn.edu.ar.tp4

import ArticleDaoImpl
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
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
            try {
                val id = newText.toInt()
                if (articleDaoImpl.getArticleById(id).getId() == id) {
                    create__idMsg = "El id ya existe"
                }
                else {
                    create__idMsg = ""
                }
                create__idText = newText
                create__idError = false
            } catch (e: NumberFormatException) {
                create__idError = true
                create__idMsg = "El id debe ser un numero valido"
            }
        }
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

}
