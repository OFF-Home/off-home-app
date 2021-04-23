package com.offhome.app.ui.profile

import android.text.Editable
import android.util.Log
import androidx.lifecycle.*
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.TagData
import com.offhome.app.model.profile.UserInfo

/**
 * Class *ProfileFragmentViewModel*
 *
 * ViewModel for the Profile screen.
 *
 * @author Ferran
 * @property repository reference to the Repository (Model) object
 * @property _profileInfo private mutable live data of the profile info displayed at the top, obtained from the server
 * @property profileInfo public live data for the topProfileInfo
 */
class ProfileFragmentViewModel : ViewModel() {

    private var repository = ProfileRepository()
    private var loggedUserEmail = "victor@gmai.com" // stub

    private var _profileInfo = MutableLiveData<UserInfo>()
    var profileInfo: LiveData<UserInfo> = _profileInfo

    private var _tags = MutableLiveData< List<TagData> >()
    var tags: LiveData<List<TagData>> = _tags


    private var _myActivities = MutableLiveData<List<ActivityFromList>>()
    var myActivities: LiveData<List<ActivityFromList>> = _myActivities

    private var _setUsernameSuccessfully = MutableLiveData<Boolean>()
    var setUsernameSuccessfully: LiveData<Boolean> = _setUsernameSuccessfully

    private var _setDescriptionSuccessfully = MutableLiveData<Boolean>()
    var setDescriptionSuccessfully : LiveData<Boolean> = _setDescriptionSuccessfully

    /**
     * obtains topProfileInfo from the lower level and places it on the live data
     */
    fun getProfileInfo() {
        val username = "victorfer" // stub

        profileInfo = repository.getProfileInfo(username)!!

        getMyActivities()
        getTags()
    }

    private fun getMyActivities() {
        //_myActivities = repository.getUserActivities(loggedUserEmail)!!
        myActivities = repository.getUserActivities(loggedUserEmail)!!      //funciona amb aquest i no amb el _myActivities
        //TODO nomes vull les futures
    }

    private fun getTags() {
        tags = repository.getUserTags(loggedUserEmail)!!
    }

    fun usernameChangedByUser(newUsername: Editable) {
        _setUsernameSuccessfully = repository.setUsername(loggedUserEmail, newUsername.toString())!!
    }

    fun descriptionChangedByUser(newDescription: Editable) {
        _setDescriptionSuccessfully= repository.setDescription(loggedUserEmail, newDescription.toString())!!
    }

    //inutil, intentant que salti el observer de setUsernameSuccessfully
    fun simularResposta() {
        Log.d("simular resposta", "SIMULA RESPOSTA")

        var setUsernameSuccessfully2 : MutableLiveData<Boolean>? = null
        setUsernameSuccessfully2 = MutableLiveData<Boolean>()

        _setUsernameSuccessfully = setUsernameSuccessfully2 as MutableLiveData<Boolean>
    }

    //aquests encara no els faig pq total em donarà el mateix error q description i username
    fun tagDeletedByUser(tag: String) {
        repository.deleteTag(loggedUserEmail, tag)
    }

    fun tagAddedByUser(tag:String) {
        repository.addTag(loggedUserEmail, tag)
    }
}
