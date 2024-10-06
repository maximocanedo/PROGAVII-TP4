package frgp.utn.edu.ar.tp4.data.dao

import frgp.utn.edu.ar.tp4.data.models.Category

interface CategoryDao {
    suspend fun deleteCategory(id: Int): Boolean
    suspend fun getCategoryById(id: Int): Category
    suspend fun getAllCategories(): List<Category>
}