package frgp.utn.edu.ar.tp4.data.daoImpl

class DataDB {
    val host: String = "canedo.com.ar"
    val port: String = "3306"
    val nameBD: String = "tp4"
    val user: String = "tp4client"
    val pass: String = "UTN.TUSI.2024"
    val urlMySQL: String = "jdbc:mysql://$host:$port/$nameBD"
    val driver = "com.mysql.jdbc.Driver"
}