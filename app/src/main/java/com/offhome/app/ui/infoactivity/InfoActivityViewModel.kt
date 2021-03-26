package com.offhome.app.ui.infoactivity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.model.ActivityData

class InfoActivityViewModel : ViewModel() {
    private var repository: ActivitiesRepository = ActivitiesRepository()

    fun joinActivity(usuariCreador: String, dataHoraIni: String, usuariParticipant: String): MutableLiveData<String> {
        return repository.joinActivity(usuariCreador, dataHoraIni, usuariParticipant)
    }
}