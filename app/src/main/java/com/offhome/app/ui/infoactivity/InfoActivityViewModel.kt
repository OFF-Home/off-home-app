package com.offhome.app.ui.infoactivity



import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.data.ProfileRepository
import com.offhome.app.data.Result
import com.offhome.app.data.model.ActivityFromList
import com.offhome.app.data.model.Rating
import com.offhome.app.data.model.ReviewOfParticipant
import com.offhome.app.data.model.UserInfo
import com.offhome.app.data.profilejson.AchievementList
import com.offhome.app.data.model.*
import com.offhome.app.data.profilejson.UserUsername

/**
 * View Model for InfoActivity
 * @author Pau Cuesta Arocs
 */
class InfoActivityViewModel : ViewModel() {
    private var repository: ActivitiesRepository = ActivitiesRepository()
    var reviews = MutableLiveData<Result<List<ReviewOfParticipant>>>()
    private lateinit var valoracio: MutableLiveData<Result<Rating>>
    var participants = MutableLiveData<Result<List<UserUsername>>>()


    var profileInfo = MutableLiveData<Result<UserInfo>>()
    private var repositoryProfile: ProfileRepository = ProfileRepository()
    var infoActivitatResult = MutableLiveData<Result<ActivityFromList>>()
    var inviteAchievements = MutableLiveData<Result<String>>()

    /**
     * This function calls the [ActivitiesRepository] in order to join to an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @return the result with a live data string type
     */
    fun joinActivity(usuariCreador: String, dataHoraIni: String, uidCreador: String): MutableLiveData<Result<AchievementList>> {
        return repository.joinActivity(
            usuariCreador, dataHoraIni,
            SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString(), uidCreador,
            SharedPreferenceManager.getStringValue(Constants().PREF_UID).toString()
        )
    }

    /**
     * This function calls the [ActivitiesRepository] in order to join to an activity
     * @param usuariCreador is the creator of the activity
     * @param dataHoraIni is the date and hour of the activity
     * @return the result with a live data string type
     */
    fun deleteUsuari(usuariCreador: String, dataHoraIni: String, uidCreador: String): MutableLiveData<Result<String>> {
        return repository.deleteUsuari(
            usuariCreador, dataHoraIni,
            SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString(), uidCreador,
            SharedPreferenceManager.getStringValue(Constants().PREF_UID).toString()
        )
    }

    /**
     * gets the participants from the repository
     */
    fun getParticipants(usuariCreador: String, dataHoraIni: String): MutableLiveData<Result<List<UserUsername>>> {
        participants = repository.getNamesParticipants(usuariCreador, dataHoraIni)
        return participants
    }

    /**
     * gets the user's rating of the activity from the repository
     */
    fun getValoracioUsuari(usuariCreador: String, dataHoraIni: String, usuariParticipant: String): MutableLiveData<Result<Rating>> {
        valoracio = repository.getValoracio(usuariCreador, dataHoraIni, usuariParticipant)
        return valoracio
    }

    /**
     * gets the user's rating of the activity from the repository
     */
    fun putValoracio(usuariParticipant: String, usuariCreador: String, dataHoraIni: String, valoracio: Int, comentari: String): MutableLiveData<Result<AchievementList>> {
        return repository.valorarActivitat(usuariParticipant, usuariCreador, dataHoraIni, valoracio, comentari)
    }

    /**
     * gets the reviews of the activity from the repository
     */
    fun getReviews(usuariCreador: String, dataHoraIni: String): MutableLiveData<Result<List<ReviewOfParticipant>>> {
        reviews = repository.getCommentsParticipants(usuariCreador, dataHoraIni)
        return reviews
    }

    fun getProfileInfo(email: String) {
        profileInfo = repositoryProfile.getProfileInfo(email)
    }

    fun getCreatorInfo(email: String): MutableLiveData<Result<UserInfo>> {
        return repositoryProfile.getProfileInfo(email)
    }

    // gets a single activity identified by its creator and date
    fun getActivityResult(activityCreator: String, activityDateTime: String) {
        infoActivitatResult = repository.getActivityResult(activityCreator, activityDateTime)
    }

    /**
     * gets the weather
     */
    fun getWeather(): MutableLiveData<Result<Tiempo>> {
        return repository.getWeather()
    }
}
