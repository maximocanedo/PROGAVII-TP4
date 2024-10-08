package frgp.utn.edu.ar.tp4.activity.Article

import ArticleDaoImpl
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import frgp.utn.edu.ar.tp4.data.models.Article
import frgp.utn.edu.ar.tp4.data.models.Category
import kotlinx.coroutines.launch

class ArticleViewModel() : ViewModel() {
    val articleDaoImpl = ArticleDaoImpl()
    var create__idText by mutableStateOf("")
    var create__nameText by mutableStateOf("")
    var create__stockText by mutableStateOf("")

    var create__idMsg by mutableStateOf("")
    var create__idError by mutableStateOf(false)
    var create__nameMsg by mutableStateOf("")
    var create__stockMsg by mutableStateOf("")
    var create__nameError by mutableStateOf(false)
    var create__stockError by mutableStateOf(false)
    var create__selectedCategoryText by mutableStateOf("")
    var create__selectedCategoryIndex by mutableStateOf(0)

    fun onCreate__idTextChange(newText: String) {
        val articleDaoImpl = ArticleDaoImpl()
        if (newText.isEmpty()) {
            create__idText = ""
        } else {
            create__idText = newText
            try {
                val id = newText.toInt()
            } catch (e: NumberFormatException) {
                create__idError = true
                create__idMsg = "El id debe ser un número"
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
            try {
                val stock = newText.toInt()
                if (newText.toIntOrNull() == null || newText.toInt() < 0) {
                    create__stockError = true
                    create__stockMsg = "El stock debe ser un numero valido"
                } else {
                    create__stockError = false
                    create__stockMsg = ""
                }
            } catch (e: NumberFormatException) {
                create__stockError = true
                create__stockMsg = "El stock debe ser un número"
            }
        }
    }

    fun create__StockErrorChange(newError: Boolean) {
        create__stockError = newError
    }

    fun create__StockMsgChange(newMsg: String) {
        create__stockMsg = newMsg
    }

    fun create__CategoryChange(newCategory: Category) {
        create__selectedCategoryText = newCategory.getDescription()
    }

    fun create__CategoryIndexChange(newIndex: Int) {
        create__selectedCategoryIndex = newIndex
    }

    fun updateFieldsFromArticle(article: Article) {
        create__nameText = article.getName()
        create__stockText = article.getStock().toString()
    }

    fun isInvalid(): Boolean {
        return create__idError || create__nameError || create__stockError
    }

    fun insertArticle(article: Article) {
        viewModelScope.launch {
            articleDaoImpl.insertArticle(article)
        }
    }

    fun updateArticle(article: Article) {
        viewModelScope.launch {
            articleDaoImpl.updateArticle(article)
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            articleDaoImpl.deleteArticle(article)
        }
    }

}
