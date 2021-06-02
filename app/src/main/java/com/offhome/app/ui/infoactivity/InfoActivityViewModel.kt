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
import com.offhome.app.data.profilejson.UserUsername

/**
 * View Model for InfoActivity
 * @author Pau Cuesta Arocs
 */
class InfoActivityViewModel : ViewModel() {
    private var repository: ActivitiesRepository = ActivitiesRepository()
    var participants: MutableLiveData<List<UserUsername>> = MutableLiveData<List<UserUsername>>()
    private var reviews: MutableLiveData<List<ReviewOfParticipant>> = MutableLiveData<List<ReviewOfParticipant>>()
    private var valoracio: MutableLiveData<Rating> = MutableLiveData<Rating>()

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
    fun joinActivity(usuariCreador: String, dataHoraIni: String): MutableLiveData<Result<AchievementList>> {
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

    fun getProfileInfo(email: String) {
        profileInfo = repositoryProfile.getProfileInfo(email)
    }
    // gets a single activity identified by its creator and date
    fun getActivityResult(activityCreator: String, activityDateTime: String) {
        infoActivitatResult = repository.getActivityResult(activityCreator, activityDateTime)
    }

    fun getInviteAchievements() {
        inviteAchievements = repository.getInviteAchievements(SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString())
    }
}
