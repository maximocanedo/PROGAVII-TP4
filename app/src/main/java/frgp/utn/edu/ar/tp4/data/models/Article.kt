package frgp.utn.edu.ar.tp4.data.models

class Article {
    private var id: Int = 0
    private var name: String = ""
    private var stock: Int = 0
    private var category: Category = Category()

    constructor()

    constructor(id: Int, name: String, stock: Int, category: Category) {
        this.id = id
        this.name = name
        this.stock = stock
        this.category = category
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getStock(): Int {
        return stock
    }

    fun setStock(stock: Int) {
        this.stock = stock
    }

    fun getCategory(): Category {
        return category
    }

    fun setCategoryId(category: Category) {
        this.category = category
    }

    override fun toString(): String {
        return "Article(id=$id, name='$name', stock=$stock, category=${category})"
    }
}