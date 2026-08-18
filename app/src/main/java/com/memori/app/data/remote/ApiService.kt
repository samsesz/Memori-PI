package com.memori.app.data.remote

import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @POST("api/usuario/login")
    suspend fun login(@Body body: LoginRequest): Response<LoginResponse>

    @POST("api/pontuacao")
    suspend fun criarPontuacao(@Body body: CriarPontuacaoRequest): Response<CriarPontuacaoResponse>

    @POST("api/ranking")
    suspend fun criarRanking(@Body body: CriarRankingRequest): Response<Unit>

    @GET("api/ranking/app/top")
    suspend fun getRanking(): Response<RankingResponse>
}