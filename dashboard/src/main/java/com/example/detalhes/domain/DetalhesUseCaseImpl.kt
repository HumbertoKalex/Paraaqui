package com.example.detalhes.domain

import com.example.detalhes.repository.DetalhesRepository

class DetalhesUseCaseImpl(
    private val DetalhesRepository: DetalhesRepository
) : DetalhesUseCase {

    override suspend fun fetchDetalhes(name: String) = Any()

}