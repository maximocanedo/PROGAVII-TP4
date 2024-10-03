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

    override fun deleteCategory(category: Category) {
        TODO("Not yet implemented")
    }

    override fun getCategoryById(id: Int): Category {
        TODO("Not yet implemented")
    }

    override fun getAllCategories(): List<Category> {
        val categories : MutableList<Category> = mutableListOf<Category>().toMutableList()
        val dataDB = DataDB()
        try {
            val connection: Connection = DriverManager.getConnection(dataDB.urlMySQL, dataDB.user, dataDB.pass)
            val statement: Statement = connection.createStatement()
            val resulSet: ResultSet = statement.executeQuery("SELECT * FROM cliente")

            while (resulSet.next()) {
                val category = Category()
                category.setId(resulSet.getInt("id"))
                category.setDescription(resulSet.getString("description"))
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