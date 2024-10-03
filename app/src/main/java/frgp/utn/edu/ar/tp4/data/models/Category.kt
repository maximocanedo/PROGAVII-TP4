package frgp.utn.edu.ar.tp4.data.models

class Category {
    private var id: Int = 0
    private var description: String = ""

    constructor()

    constructor(id: Int, description: String) {
        this.id = id
        this.description = description
    }

    fun getId(): Int {
        return id
    }

    fun setId(id: Int) {
        this.id = id
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    override fun toString(): String {
        return "Category(id=$id, description='$description')"
    }
}