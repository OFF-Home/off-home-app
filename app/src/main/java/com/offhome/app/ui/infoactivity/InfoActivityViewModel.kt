package com.offhome.app.ui.infoactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.UserInfo
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.Rating

/**
 * View Model for InfoActivity
 * @author Pau Cuesta Arocs
 */
class InfoActivityViewModel : ViewModel() {
    private var repository: ActivitiesRepository = ActivitiesRepository()
    private var participants: MutableLiveData<List<String>> = MutableLiveData<List<String>>()
    private var valoracio: MutableLiveData<Rating> = MutableLiveData<Rating>()

    /**
     * This function calls the [ActivitiesRepository] in order to join to an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @return the result with a live data string type
     */
    fun joinActivity(usuariCreador: String, dataHoraIni: String): MutableLiveData<String> {
        return repository.joinActivity(usuariCreador, dataHoraIni,
            SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()
        )
    }

    /**
     * This function calls the [ActivitiesRepository] in order to join to an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @return the result with a live data string type
     */
    fun deleteUsuari(usuariCreador: String, dataHoraIni: String): MutableLiveData<String> {
        return repository.deleteUsuari(usuariCreador, dataHoraIni,
            SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()
        )
    }

    /**
     * gets the participants from the repository
     */
    fun getParticipants(usuariCreador: String, dataHoraIni: String): MutableLiveData<List<String>> {
        participants = repository.getNamesParticipants(usuariCreador, dataHoraIni)
        return participants
    }

    /**
     * gets the user's rating of the activity from the repository
     */
    fun getValoracioUsuari(usuariCreador: String, dataHoraIni: String, usuariParticipant: String): MutableLiveData<Rating> {
        valoracio = repository.getValoracio(usuariCreador, dataHoraIni, usuariParticipant)
        return valoracio
    }

    /**
     * gets the user's rating of the activity from the repository
     */
    fun putValoracio(usuariParticipant: String, usuariCreador: String, dataHoraIni: String, valoracio: Int, comentari: String): MutableLiveData<String> {
        return repository.valorarActivitat(usuariParticipant, usuariCreador, dataHoraIni, valoracio, comentari)
    }
}
