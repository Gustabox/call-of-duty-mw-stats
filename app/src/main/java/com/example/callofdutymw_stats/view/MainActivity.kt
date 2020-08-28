package com.example.callofdutymw_stats.view

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.callofdutymw_stats.R
import com.example.callofdutymw_stats.model.multiplayer.dto.UserDtoMultiplayer
import com.example.callofdutymw_stats.model.multiplayer.lifetime.all.properties.UserPropertiesMultiplayer
import com.example.callofdutymw_stats.model.warzone.all.UserAllWarzone
import com.example.callofdutymw_stats.model.warzone.dto.UserDtoWarzone
import com.example.callofdutymw_stats.viewmodel.MainActivityViewModel
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buttonFindUserClick()
    }

    private fun buttonFindUserClick() {
        buttonFindUser.setOnClickListener {
            getMultiplayerUser()
            getWarzoneUser()
        }
    }

    private fun getMultiplayerUser() {
        val mainActivityViewModel = MainActivityViewModel()
        mainActivityViewModel.getMultiplayerUser(
            editTextUser.text.toString(),
            editTextPlatform.text.toString()
        ).enqueue(object : retrofit2.Callback<UserDtoMultiplayer> {
            override fun onResponse(
                call: Call<UserDtoMultiplayer>,
                response: Response<UserDtoMultiplayer>
            ) {

                val userPropertiesMultiplayer = createNewMultiplayerUser(response)
                Log.d("M - Recorde de win ", userPropertiesMultiplayer.recordLongestWinStreak)
                Log.d("M - Recorde XP ", userPropertiesMultiplayer.recordXP)
                Log.d("M - Precisão ", userPropertiesMultiplayer.accuracy)
                Log.d("M - Perdas ", userPropertiesMultiplayer.losses)
                Log.d("M - Total de jogos ", userPropertiesMultiplayer.totalGamesPlayed)
                Log.d("M - Pontuação ", userPropertiesMultiplayer.score)
                Log.d("M - Mortes ", userPropertiesMultiplayer.deaths)
                Log.d("M - Vitórias ", userPropertiesMultiplayer.wins)
                Log.d("M - KD Ratio ", userPropertiesMultiplayer.kdRatio)
                Log.d("M - Melhores assistên. ", userPropertiesMultiplayer.bestAssists)
                Log.d("M - Melhor pont. ", userPropertiesMultiplayer.bestScore)
                Log.d("M - Recorde de mortes ", userPropertiesMultiplayer.recordDeathsInMatch)
                Log.d("M - Recorde de baixas ", userPropertiesMultiplayer.recordKillsInMatch)
                Log.d("M - Suicídios ", userPropertiesMultiplayer.suicides)
                Log.d("M - Total de baixas ", userPropertiesMultiplayer.totalKills)
                Log.d("M - Tiros na cabeça ", userPropertiesMultiplayer.headshots)
                Log.d("M - Assistências ", userPropertiesMultiplayer.assists)
                Log.d("M - Maior seq. baixas ", userPropertiesMultiplayer.recordKillStreak)
                Log.e("-", "-----------------------------------------------------------------")
            }

            override fun onFailure(call: Call<UserDtoMultiplayer>, t: Throwable) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun getWarzoneUser() {
        val mainActivityViewModel = MainActivityViewModel()
        mainActivityViewModel.getWarzoneUser(
            editTextUser.text.toString(),
            editTextPlatform.text.toString()
        )
            .enqueue(object : retrofit2.Callback<UserDtoWarzone> {
                override fun onResponse(
                    call: Call<UserDtoWarzone>,
                    response: Response<UserDtoWarzone>
                ) {
                    val userAllWarzone = createNewWarzoneUser(response)
                    Log.d("W - Vitórias ", userAllWarzone.wins)
                    Log.d("W - Baixas ", userAllWarzone.kills)
                    Log.d("W - Mortes ", userAllWarzone.deaths)
                    Log.d("W - KD ", userAllWarzone.kd)
                    Log.d("W - Derrubados ", userAllWarzone.downs)
                    Log.d("W - Top 25 ", userAllWarzone.topTwentyFive)
                    Log.d("W - Top 10 ", userAllWarzone.topTen)
                    Log.d("W - Top 5 ", userAllWarzone.topFive)
                    Log.d("W - Contratos pegos ", userAllWarzone.contracts)
                    Log.d("W - Ressurgimentos ", userAllWarzone.revives)
                    Log.d("W - Pontuação ", userAllWarzone.score)
                    Log.d("W - Jogos jogados ", userAllWarzone.gamesPlayed)
                    textInputLayoutUser.error = ""
                }

                override fun onFailure(call: Call<UserDtoWarzone>, t: Throwable) {
                    Log.e("API error ", t.toString())
                }
            })
    }

    private fun warzoneUserDontExists(response: Response<UserDtoWarzone>): Boolean {
        //Será pego um valor aleatório do objeto para ser comparado.
        return response.body()?.userAllWarzone?.deaths == null
    }

    private fun createNewWarzoneUser(response: Response<UserDtoWarzone>): UserAllWarzone {
        Log.d("Status code from W user", response.toString())

        val wins: String = response.body()?.userAllWarzone?.wins.toString()
        val kills: String = response.body()?.userAllWarzone?.kills.toString()
        val deaths: String = response.body()?.userAllWarzone?.deaths.toString()
        val kd: String = response.body()?.userAllWarzone?.kd.toString()
        val downs: String = response.body()?.userAllWarzone?.downs.toString()
        val topTwentyFive: String = response.body()?.userAllWarzone?.topTwentyFive.toString()
        val topTen: String = response.body()?.userAllWarzone?.topTen.toString()
        val topFive: String = response.body()?.userAllWarzone?.topFive.toString()
        val contracts: String = response.body()?.userAllWarzone?.contracts.toString()
        val revives: String = response.body()?.userAllWarzone?.revives.toString()
        val score: String = response.body()?.userAllWarzone?.score.toString()
        val gamesPlayed: String = response.body()?.userAllWarzone?.gamesPlayed.toString()
        return UserAllWarzone(
            wins,
            kills,
            deaths,
            kd,
            downs,
            topTwentyFive,
            topTen,
            topFive,
            contracts,
            revives,
            score,
            gamesPlayed
        )
    }

    private fun createNewMultiplayerUser(response: Response<UserDtoMultiplayer>): UserPropertiesMultiplayer {
        Log.d("Status code from M user", response.toString())

        val recordLongestWinStreak =
            response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.recordLongestWinStreak.toString()
        val kdRatio =
            response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.kdRatio.toString()
        val recordXP = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.recordXP.toString()
        val accuracy = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.accuracy.toString()
        val losses = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.losses.toString()
        val totalGamesPlayed= response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.totalGamesPlayed.toString()
        val score = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.score.toString()
        val deaths = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.deaths.toString()
        val wins = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.wins.toString()
        val bestAssists = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.bestAssists.toString()
        val bestScore = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.bestScore.toString()
        val recordDeathsInMatch = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.recordDeathsInMatch.toString()
        val recordKillsInMatch = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.recordKillsInMatch.toString()
        val suicides = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.suicides.toString()
        val totalKills = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.totalKills.toString()
        val headshots = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.headshots.toString()
        val assists = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.assists.toString()
        val recordKillStreak = response.body()?.userLifeTimeMultiplayer?.userAllMultiplayer?.userPropertiesMultiplayer?.recordKillStreak.toString()
        return UserPropertiesMultiplayer(
            recordLongestWinStreak, recordXP, accuracy, losses, totalGamesPlayed, score, deaths, wins, kdRatio, bestAssists, bestScore,
            recordDeathsInMatch, recordKillsInMatch, suicides, totalKills, headshots, assists, recordKillStreak
        )
    }
}