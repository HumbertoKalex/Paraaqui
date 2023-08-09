package com.example.detalhes.domain

interface DetalhesUseCase {

    suspend fun fetchDetalhes(name: String): Any

}