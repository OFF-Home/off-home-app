package com.offhome.app.ui.profile

import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
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
    private var loggedUserEmail = "victorfer@gmai.com" // stub

    private var _profileInfo = MutableLiveData<UserInfo>()
    var profileInfo: LiveData<UserInfo> = _profileInfo

    private var _tags = MutableLiveData< List<TagData> >()
    var tags: LiveData<List<TagData>> = _tags


    private var _myActivities = MutableLiveData<List<ActivityFromList>>()
    var myActivities: LiveData<List<ActivityFromList>> = _myActivities

    private var _usernameSetSuccessfully = MutableLiveData<Boolean>()
    var usernameSetSuccessfully: LiveData<Boolean> = _usernameSetSuccessfully

    private var _descriptionSetSuccessfully = MutableLiveData<Boolean>()
    var descriptionSetSuccessfully : LiveData<Boolean> = _descriptionSetSuccessfully

    private var _tagAddedSuccessfully = MutableLiveData<Boolean>()
    var tagAddedSuccessfully:LiveData<Boolean> = _tagAddedSuccessfully
    private var _tagDeletedSuccessfully = MutableLiveData<Boolean>()
    var tagDeletedSuccessfully : LiveData<Boolean> = _tagDeletedSuccessfully

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

    fun usernameChangedByUser(newUsername: Editable, activity: AppCompatActivity) {
        /*_setUsernameSuccessfully = */
        repository.setUsername(loggedUserEmail, newUsername.toString())
        repository.usernameSetSuccessfully.observe(
            activity,
            Observer {
                val resultRepo = it ?: return@Observer
                Log.d("setUsername", "salta el observer de VM")
                _usernameSetSuccessfully.value = resultRepo
            }
        )
    }

    fun descriptionChangedByUser(newDescription: Editable, activity: AppCompatActivity) {
        /*_setDescriptionSuccessfully=*/
        repository.setDescription(loggedUserEmail, newDescription.toString())
        repository.descriptionSetSuccessfully.observe(
            activity,
            Observer {
                val resultRepo = it ?: return@Observer
                Log.d("setDescription", "salta el observer de VM")
                _descriptionSetSuccessfully.value = resultRepo
            }
        )
    }

    //inutil, intentant que salti el observer de setUsernameSuccessfully
    fun simularResposta() {
        Log.d("simular resposta", "SIMULA RESPOSTA")

        var setUsernameSuccessfully2 : MutableLiveData<Boolean>? = null
        setUsernameSuccessfully2 = MutableLiveData<Boolean>()

        _usernameSetSuccessfully = setUsernameSuccessfully2 as MutableLiveData<Boolean>
    }

    //aquests encara no els faig pq total em donarà el mateix error q description i username
    fun tagDeletedByUser(tag: String, activity: AppCompatActivity) {
        repository.deleteTag(loggedUserEmail, tag)
        repository.tagDeletedSuccessfully.observe(
            activity,
            Observer {
                val resultRepo = it ?: return@Observer
                Log.d("deleteTag", "salta el observer de VM")
                _tagDeletedSuccessfully.value = resultRepo
            }
        )
    }

    fun tagAddedByUser(tag:String, activity: AppCompatActivity) {
        repository.addTag(loggedUserEmail, tag)
        repository.tagAddedSuccessfully.observe(
            activity,
            Observer {
                val resultRepo = it ?: return@Observer
                Log.d("addTag", "salta el observer de VM")
                _tagAddedSuccessfully.value = resultRepo
            }
        )
    }
}
