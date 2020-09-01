package com.example.callofdutymw_stats.view

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.callofdutymw_stats.R
import com.example.callofdutymw_stats.model.multiplayer.lifetime.UserLifeTimeMultiplayer
import com.example.callofdutymw_stats.model.multiplayer.lifetime.all.properties.UserInformationMultiplayer
import com.example.callofdutymw_stats.model.warzone.all.UserAllWarzone
import com.example.callofdutymw_stats.model.warzone.dto.UserDtoWarzone
import com.example.callofdutymw_stats.view.util.Status
import com.example.callofdutymw_stats.viewmodel.MainActivityViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Response

@Suppress("IMPLICIT_CAST_TO_ANY", "ControlFlowWithEmptyBody")
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar!!.hide()

        setAutoCompletePlatforms()
        buttonSearchClickListener()
    }

    private fun buttonSearchClickListener() {
        buttonSearch.setOnClickListener {
            //TODO: Refactor this, this is just a test.
            if (
                editTextNickname.text.toString().isNotEmpty() &&
                autoCompleteTextViewPlatforms.text.toString().isNotEmpty()
            ) {
                getMultiplayerUser(it)

                //Clear error fields
                editTextNickname.error = null
                autoCompleteTextViewPlatforms.error = null
            } else {
                if (editTextNickname.text.toString().isEmpty()) {
                    editTextNickname.error = "Campo vazio"
                }
                if (autoCompleteTextViewPlatforms.text.toString().isEmpty()) {
                    autoCompleteTextViewPlatforms.error = "Campo vazio"
                }
            }
        }
    }

    private fun setAutoCompletePlatforms() {
        val platforms = arrayOf("psn", "steam", "xbl", "battle")

        autoCompleteTextViewPlatforms.setAdapter(
            ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                platforms
            )
        )
        autoCompleteTextViewPlatforms.setOnClickListener {
            autoCompleteTextViewPlatforms.showDropDown()
        }
        Log.d("AutocompleteItem ", autoCompleteTextViewPlatforms.text.toString())
    }

    private fun getMultiplayerUser(v: View) {
        val selectedPlatform = autoCompleteTextViewPlatforms.text.toString()
        val gamertag = editTextNickname.text.toString()
        val progressDialog = ProgressDialog(this, R.style.myAlertDialogStyle)

        val mainActivityViewModel = MainActivityViewModel()
        mainActivityViewModel.getMultiplayerUser(gamertag = gamertag, platform = selectedPlatform)
            .observe(this, androidx.lifecycle.Observer {
                it?.let { resource ->
                    when (resource.status) {
                        Status.LOADING -> {
                            setMessageAndShowProgressDialog(progressDialog)
                        }
                        Status.SUCCESS -> {
                            progressDialog.dismiss()
                            //Even if an incorrect user is passed, it will continue to fall into "SUCCESS", however the data will come null
                            resource.data?.let {
                                handlesUserSituation(
                                    userIsIncorrect = resource.data.error,
                                    view = v
                                )
                            }!!
                        }
                        Status.ERROR -> {
                            Log.e("ERROR", "ERROR")
                            showErrorSnackbar(v)
                            progressDialog.dismiss()
                        }
                    }
                }
            }
            )
    }

    private fun handlesUserSituation(userIsIncorrect: Boolean, view: View) {
        if (userIsIncorrect) {
            showSnackbarErrorUser(view)
        } else {
            //TODO: CHANGE SCREEN
        }
    }

    //Snackbar
    private fun showSnackbarErrorUser(v: View) {
        Snackbar.make(v, R.string.user_dont_exists, Snackbar.LENGTH_LONG)
            .setAction(R.string.help_snackbar) {
                //TODO: Show dialog informing to check nickname or/and platform
            }.show()
    }

    private fun setMessageAndShowProgressDialog(progressDialog: ProgressDialog) {
        progressDialog.setMessage("Aguarde...")
        progressDialog.show()
    }

    private fun showErrorSnackbar(v: View) {
        Snackbar.make(v, R.string.ops_something_gone_wrong, Snackbar.LENGTH_LONG)
            .setAction(R.string.help_snackbar) {
                //TODO: Show dialog informing to check internet connection or warn that server is off
            }.show()
    }

    //Create user
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

    private fun createNewMultiplayerUser(response: Response<UserLifeTimeMultiplayer>): UserInformationMultiplayer {
        Log.d("Status code from M user", response.toString())
        val recordWinStreak =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.recordLongestWinStreak.toString()
        val recordXP =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.recordXP.toString()
        val accuracy =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.accuracy.toString()
        val losses =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.losses.toString()
        val totalGamesPlayed =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.totalGamesPlayed.toString()
        val score =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.score.toString()
        val deaths =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.deaths.toString()
        val wins =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.wins.toString()
        val kdRatio =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.kdRatio.toString()
        val bestAssists =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.bestAssists.toString()
        val bestScore =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.bestScore.toString()
        val recordDeathsInMatch =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.recordDeathsInMatch.toString()
        val recordKillsInMatch =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.recordKillsInMatch.toString()
        val suicides =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.suicides.toString()
        val totalKills =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.totalKills.toString()
        val headshots =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.headshots.toString()
        val assists =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.assists.toString()
        val recordKillStreak =
            response.body()?.userAllMultiplayer?.userPropertiesMultiplayer?.userInformationMultiplayer?.recordKillStreak.toString()
        return UserInformationMultiplayer(
            recordWinStreak,
            recordXP,
            accuracy,
            losses,
            totalGamesPlayed,
            score,
            deaths,
            wins,
            kdRatio,
            bestAssists,
            bestScore,
            recordDeathsInMatch,
            recordKillsInMatch,
            suicides,
            totalKills,
            headshots,
            assists,
            recordKillStreak
        )
    }
}