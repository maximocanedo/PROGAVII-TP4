package frgp.utn.edu.ar.tp4.data.dao

import frgp.utn.edu.ar.tp4.data.models.Category

interface CategoryDao {
    fun insertCategory(category: Category)
    fun updateCategory(category: Category)
    fun deleteCategory(id: Int): Boolean
    fun getCategoryById(id: Int): Category
    suspend fun getAllCategories(): List<Category>
}