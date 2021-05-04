package com.offhome.app.ui.infoactivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.UserInfo
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.model.ActivityFromList

/**
 * View Model for InfoActivity
 * @author Pau Cuesta Arocs
 */
class InfoActivityViewModel : ViewModel() {
    private var repository: ActivitiesRepository = ActivitiesRepository()
    private lateinit var participants: LiveData<List<UserInfo>>

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
    fun getParticipants(categoryName: String): LiveData<List<UserInfo>> {
        //participants = repository.getAll(categoryName)
        return participants
    }
}
