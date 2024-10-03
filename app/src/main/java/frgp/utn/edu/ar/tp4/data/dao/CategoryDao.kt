package frgp.utn.edu.ar.tp4.data.dao

import frgp.utn.edu.ar.tp4.data.models.Category

interface CategoryDao {
    fun insertCategory(category: Category)
    fun updateCategory(category: Category)
    fun deleteCategory(category: Category)
    fun getCategoryById(id: Int): Category
    fun getAllCategories(): List<Category>
}