package frgp.utn.edu.ar.tp4.activity.Article

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import frgp.utn.edu.ar.tp4.data.dao.ArticleDao
import frgp.utn.edu.ar.tp4.data.models.Article
import kotlinx.coroutines.launch

class ArticleViewModel(private val articleDao: ArticleDao) : ViewModel() {

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
