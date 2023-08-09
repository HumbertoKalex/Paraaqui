package com.example.detalhes.repository

class DetalhesRepositoryImpl(
    private val detalhesService: DetalhesApi
) : DetalhesRepository {

    override suspend fun fetchDetalhes(name: String) = Any()

}