package frgp.utn.edu.ar.tp4.activity.Article

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import frgp.utn.edu.ar.tp4.data.dao.ArticleDao
import frgp.utn.edu.ar.tp4.data.models.Article
import kotlinx.coroutines.launch

class ArticleViewModel(private val articleDao: ArticleDao) : ViewModel() {
    var create__nameText by mutableStateOf("")
    var create__stockText by mutableStateOf("")
    var create__categoryText by mutableStateOf("")

    var create__nameMsg by mutableStateOf("")
    var create__stockMsg by mutableStateOf("")
    var create__nameError by mutableStateOf(false)
    var create__stockError by mutableStateOf(false)

    fun updateFieldsFromArticle(article: Article) {
        create__nameText = article.getName()
        create__stockText = article.getStock().toString()
        create__categoryText = article.getCategory().getDescription()
    }

    fun insertArticle(article: Article) {
        viewModelScope.launch {
            articleDao.insertArticle(article)
        }
    }

    fun updateArticle(article: Article) {
        viewModelScope.launch {
            articleDao.updateArticle(article)
        }
    }

    fun deleteArticle(article: Article) {
        viewModelScope.launch {
            articleDao.deleteArticle(article)
        }
    }

}
