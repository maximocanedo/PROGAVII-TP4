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
    var create__nameText by mutableStateOf("")
    var create__stockText by mutableStateOf("")
    var create__category by mutableStateOf(Category())

    var create__nameMsg by mutableStateOf("")
    var create__stockMsg by mutableStateOf("")
    var create__nameError by mutableStateOf(false)
    var create__stockError by mutableStateOf(false)

    fun updateFieldsFromArticle(article: Article) {
        create__nameText = article.getName()
        create__stockText = article.getStock().toString()
        create__category = article.getCategory()
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
