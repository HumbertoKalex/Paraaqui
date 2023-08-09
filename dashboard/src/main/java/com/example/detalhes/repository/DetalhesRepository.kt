package com.example.detalhes.repository


interface DetalhesRepository {

    suspend fun fetchDetalhes(name: String): Any

}