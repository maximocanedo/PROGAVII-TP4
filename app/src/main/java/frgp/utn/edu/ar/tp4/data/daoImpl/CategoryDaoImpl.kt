package frgp.utn.edu.ar.tp4.data.daoImpl

import frgp.utn.edu.ar.tp4.data.dao.CategoryDao
import frgp.utn.edu.ar.tp4.data.models.Category
import java.sql.Connection
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.Statement

class CategoryDaoImpl : CategoryDao {
    override fun insertCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override fun updateCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override fun deleteCategory(id: Int): Boolean {
        val dataDB = DataDB()
        var deleted = false
        try {
            val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
            val statement: Statement = connection.createStatement()

            val rowCount = statement.executeUpdate("DELETE FROM categoria WHERE id = ${id}")

            deleted = rowCount > 0

            statement.close()
            connection.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return deleted
    }

    override fun getCategoryById(id: Int): Category {
        val category = Category();
        val dataDB = DataDB()
        try {
            val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
            val statement: Statement = connection.createStatement()
            val resulSet: ResultSet = statement.executeQuery("SELECT * FROM categoria")

            category.setId(resulSet.getInt("id"))
            category.setDescription(resulSet.getString("descripcion"))

            resulSet.close()
            statement.close()
            connection.close()
        }
        catch (e: Exception) {
            e.printStackTrace()
        }
        return category;
    }

    override suspend fun getAllCategories(): List<Category> {
        val categories : MutableList<Category> = mutableListOf<Category>().toMutableList()
        val dataDB = DataDB()
        try {
            val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
            val statement: Statement = connection.createStatement()
            val resulSet: ResultSet = statement.executeQuery("SELECT * FROM categoria")

            while (resulSet.next()) {
                val category = Category()
                category.setId(resulSet.getInt("id"))
                category.setDescription(resulSet.getString("descripcion"))
                categories += category
            }
            resulSet.close()
            statement.close()
            connection.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return categories
    }

}