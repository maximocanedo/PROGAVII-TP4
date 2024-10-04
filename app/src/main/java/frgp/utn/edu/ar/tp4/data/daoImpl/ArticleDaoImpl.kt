import frgp.utn.edu.ar.tp4.data.dao.ArticleDao
import frgp.utn.edu.ar.tp4.data.daoImpl.DataDB
import frgp.utn.edu.ar.tp4.data.models.Article
import frgp.utn.edu.ar.tp4.data.models.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class ArticleDaoImpl : ArticleDao {

    override suspend fun insertArticle(article: Article) {
        val dataDB = DataDB()
        val sql = "INSERT INTO articles (id, name, stock, category) VALUES (?, ?, ?, ?)"
        withContext(Dispatchers.IO) {
            try {
                Class.forName(dataDB.driver)
                val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
                val preparedStatement = connection.prepareStatement(sql)
                preparedStatement.setInt(1, article.getId())
                preparedStatement.setString(2, article.getName())
                preparedStatement.setInt(3, article.getStock())
                preparedStatement.setInt(4, article.getCategory().getId())
                preparedStatement.executeUpdate()
                preparedStatement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun updateArticle(article: Article) {
        val dataDB = DataDB()
        val sql = "UPDATE articles SET name = ?, stock = ?, category = ? WHERE id = ?"
        withContext(Dispatchers.IO) {
            try {
                Class.forName(dataDB.driver)
                val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
                val preparedStatement = connection.prepareStatement(sql)
                preparedStatement.setString(1, article.getName())
                preparedStatement.setInt(2, article.getStock())
                preparedStatement.setInt(3, article.getCategory().getId())
                preparedStatement.setInt(4, article.getId())
                preparedStatement.executeUpdate()
                preparedStatement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun deleteArticle(article: Article) {
        val dataDB = DataDB()
        val sql = "DELETE FROM articles WHERE id = ?"
        withContext(Dispatchers.IO) {
            try {
                Class.forName(dataDB.driver)
                val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
                val preparedStatement = connection.prepareStatement(sql)
                preparedStatement.setInt(1, article.getId())
                preparedStatement.executeUpdate()
                preparedStatement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun getArticleById(id: Int): Article {
        val dataDB = DataDB()
        val article = Article()
        withContext(Dispatchers.IO) {
            try {
                Class.forName(dataDB.driver)
                val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
                val statement: Statement = connection.createStatement()
                val sql = "SELECT a.id, a.name, a.stock, c.id AS category, c.description " +
                        "FROM articles a JOIN categories c ON a.category = c.id WHERE a.id = $id"
                val resultSet: ResultSet = statement.executeQuery(sql)

                if (resultSet.next()) {
                    val category = Category(
                        id = resultSet.getInt("category"),
                        description = resultSet.getString("description")
                    )
                    article.setId( resultSet.getInt("id"))
                    article.setName(resultSet.getString("name"))
                    article.setStock(resultSet.getInt("stock"))
                    article.setCategoryId(category)
                }
                resultSet.close()
                statement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return article
    }

    override suspend fun getAllArticles(): List<Article> {
        val articles: MutableList<Article> = mutableListOf()
        val dataDB = DataDB()
        withContext(Dispatchers.IO) {
            try {
                Class.forName(dataDB.driver)
                val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
                val statement: Statement = connection.createStatement()
                val sql = "SELECT a.id, a.name, a.stock, c.id AS category, c.description " +
                        "FROM articles a JOIN category c ON a.category = c.id"
                val resultSet: ResultSet = statement.executeQuery(sql)

                while (resultSet.next()) {
                    val category = Category(
                        id = resultSet.getInt("category"),
                        description = resultSet.getString("description")
                    )
                    val article = Article(
                        id = resultSet.getInt("id"),
                        name = resultSet.getString("name"),
                        stock = resultSet.getInt("stock"),
                        category = category
                    )
                    articles += article
                }
                resultSet.close()
                statement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return articles
    }
}
