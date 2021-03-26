package com.offhome.app.ui.infoactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.model.ActivityData

/**
 * View Model for InfoActivity
 * @author Pau Cuesta Arocs
 */
class InfoActivityViewModel : ViewModel() {
    private var repository: ActivitiesRepository = ActivitiesRepository()

    /**
     * This function calls the [ActivitiesRepository] in order to join to an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @param usuariParticipant is the user that wants to join the activity
     * @return the result with a live data string type
     */
    fun joinActivity(usuariCreador: String, dataHoraIni: String, usuariParticipant: String): MutableLiveData<String> {
        return repository.joinActivity(usuariCreador, dataHoraIni, usuariParticipant)
    }
}