package com.memori.app.data.remote

// Login
data class LoginRequest(val emailUsuario: String, val senhaUsuario: String)
data class LoginResponse(val token: String)

// Pontuação
data class CriarPontuacaoRequest(
    val idUsuario: String,
    val idDesafio: String,
    val dataInicio: String,
    val dataFim: String,
    val pontosTotal: Int
)
data class CriarPontuacaoResponse(val id: String)

// Ranking
data class CriarRankingRequest(
    val idUsuario: String,
    val idPontuacao: String,
    val pontosTotal: Int,
    val posicao: Int
)
data class UsuarioRanking(val nomeUsuario: String)
data class RankingItem(val idUsuario: UsuarioRanking, val pontosTotal: Int, val posicao: Int)
data class RankingResponse(val ranking: List<RankingItem>)