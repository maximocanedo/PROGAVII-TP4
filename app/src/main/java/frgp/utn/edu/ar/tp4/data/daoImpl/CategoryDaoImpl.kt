package frgp.utn.edu.ar.tp4.data.daoImpl

import frgp.utn.edu.ar.tp4.data.dao.CategoryDao
import frgp.utn.edu.ar.tp4.data.models.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class CategoryDaoImpl : CategoryDao {

    override suspend fun deleteCategory(id: Int): Boolean {
        val dataDB = DataDB()
        var deleted = false
        withContext(Dispatchers.IO) {
            try {
                Class.forName(dataDB.driver)
                val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
                val statement: Statement = connection.createStatement()
                val result = statement.executeUpdate("DELETE FROM categoria WHERE id = ${id}")
                if (result > 0) {
                    deleted = true
                }
                statement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return deleted
    }

    override suspend fun getCategoryById(id: Int): Category {
        val category = Category()
        val dataDB = DataDB()
        withContext(Dispatchers.IO) {
            try {
                Class.forName(dataDB.driver)
                val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
                val statement: Statement = connection.createStatement()
                val resulSet: ResultSet = statement.executeQuery("SELECT * FROM categoria WHERE id = ${id}")

                if (resulSet.next()) {
                    category.setId(resulSet.getInt("id"))
                    category.setDescription(resulSet.getString("descripcion"))
                }
                resulSet.close()
                statement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return category
    }

    override suspend fun getAllCategories(): List<Category> {
        val categories : MutableList<Category> = mutableListOf<Category>().toMutableList()
        val dataDB = DataDB()
        withContext(Dispatchers.IO) {
            try {
                Class.forName(dataDB.driver)
                val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
                val statement: Statement = connection.createStatement()
                val resulSet: ResultSet = statement.executeQuery("SELECT * FROM categoria")

                while (resulSet.next()) {
                    val category = Category()
                    category.setId(resulSet.getInt("id"))
                    category.setDescription(resulSet.getString("descripcion"))
                    categories.add(category)
                }
                resulSet.close()
                statement.close()
                connection.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return categories
    }

}