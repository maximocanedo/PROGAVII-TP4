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
        val sql = "INSERT INTO articles (id, nombre, stock, category) VALUES (?, ?, ?, ?)"
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
        val sql = "UPDATE articulo SET nombre = ?, stock = ?, category = ? WHERE id = ?"
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
        val sql = "DELETE FROM articulo WHERE id = ?"
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
                val connection: Connection = DriverManager.getConnection("${dataDB.urlMySQL}?characterEncoding=UTF-8", dataDB.user, dataDB.pass)
                val statement: Statement = connection.createStatement()
                val sql = "SELECT a.id, a.nombre, a.stock, c.id AS category, c.descripcion " +
                        "FROM articulo a " +
                        "JOIN categoria c ON a.idCategoria = c.id " +
                        "WHERE a.id = $id"
                val resultSet: ResultSet = statement.executeQuery(sql)

                if (resultSet.next()) {
                    val category = Category(
                        id = resultSet.getInt("category"),
                        description = resultSet.getString("descripcion")
                    )
                    article.setId( resultSet.getInt("id"))
                    article.setName(resultSet.getString("nombre"))
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
                val sql = "SELECT a.id, a.nombre, a.stock, c.id AS category, c.descripcion " +
                        "FROM articulo a JOIN category c ON a.category = c.id"
                val resultSet: ResultSet = statement.executeQuery(sql)

                while (resultSet.next()) {
                    val category = Category(
                        id = resultSet.getInt("category"),
                        description = resultSet.getString("descripcion")
                    )
                    val article = Article(
                        id = resultSet.getInt("id"),
                        name = resultSet.getString("nombre"),
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
