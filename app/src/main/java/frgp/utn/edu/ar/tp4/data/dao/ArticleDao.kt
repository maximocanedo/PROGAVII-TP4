package frgp.utn.edu.ar.tp4.data.dao

import frgp.utn.edu.ar.tp4.data.models.Article

interface ArticleDao {
    suspend fun insertArticle(article: Article)
    suspend fun updateArticle(article: Article)
    suspend fun deleteArticle(article: Article)
    suspend fun getArticleById(id: Int): Article
    suspend fun getAllArticles(): List<Article>
}
