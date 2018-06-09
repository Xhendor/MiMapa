package com.uabc.edu.mapa

class Puntos {

    var fecha: String? = null
    var latitud: String? = null
    var longitud: String? = null

    constructor() {}

    constructor(fecha: String, latitud: String, longitud: String) {
        this.fecha = fecha
        this.latitud = latitud
        this.longitud = longitud
    }
}
