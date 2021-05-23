package com.offhome.app.ui.infoactivity



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.data.profilejson.UserUsername
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.Rating
import com.offhome.app.model.ReviewOfParticipant
import com.offhome.app.model.profile.UserInfo

/**
 * View Model for InfoActivity
 * @author Pau Cuesta Arocs
 */
class InfoActivityViewModel : ViewModel() {
    private var repository: ActivitiesRepository = ActivitiesRepository()
    var participants: MutableLiveData<List<UserUsername>> = MutableLiveData<List<UserUsername>>()
    private var reviews: MutableLiveData<List<ReviewOfParticipant>> = MutableLiveData<List<ReviewOfParticipant>>()
    private var valoracio: MutableLiveData<Rating> = MutableLiveData<Rating>()

    //private var infoActivitat: MutableLiveData<ActivityFromList> = MutableLiveData<ActivityFromList>()
    private var _infoActivitat = MutableLiveData<ActivityFromList>()
    var infoActivitat: LiveData<ActivityFromList> = _infoActivitat

    /**
     * This function calls the [ActivitiesRepository] in order to join to an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @return the result with a live data string type
     */
    fun joinActivity(usuariCreador: String, dataHoraIni: String): MutableLiveData<String> {
        return repository.joinActivity(
            usuariCreador, dataHoraIni,
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
        return repository.deleteUsuari(
            usuariCreador, dataHoraIni,
            SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()
        )
    }

    /**
     * gets the participants from the repository
     */
    fun getParticipants(usuariCreador: String, dataHoraIni: String): MutableLiveData<List<UserUsername>> {
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

    /**
     * gets the reviews of the activity from the repository
     */
    fun getReviews(usuariCreador: String, dataHoraIni: String): MutableLiveData<List<ReviewOfParticipant>> {
        reviews = repository.getCommentsParticipants(usuariCreador, dataHoraIni)
        return reviews
    }

    //gets a single activity identified by its creator and date
    fun getActivity(activityCreator: String, activityDateTime: String)/*: MutableLiveData<ActivityFromList>*/ {
        /*infoActivitat = repository.getActivity(activityCreator, activityDateTime)
        return infoActivitat*/

        _infoActivitat = repository.getActivity(activityCreator, activityDateTime)
        infoActivitat = _infoActivitat
    }
}
