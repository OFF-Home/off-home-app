package com.offhome.app.ui.profile

import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.model.ActivityFromList
import com.offhome.app.model.profile.ProfileRepository
import com.offhome.app.model.profile.TagData
import com.offhome.app.model.profile.UserInfo

/**
 * Class *ProfileFragmentViewModel*
 *
 * ViewModel for the entire Profile screen. (includes "my activities" and "about me")
 *
 * @author Ferran
 * @property repository reference to the Repository (Model) object
 * @property _profileInfo private mutable live data of the profile info, obtained from the server
 * @property profileInfo public live data for the ProfileInfo
 * @property _tags private mutable live data of the tags to be obtained from the server
 * @property tags public live data for the tags
 * @property _myActivities private mutable live data of the user's activities to be obtained from the server
 * @property myActivities public live data for the user's activities
 * @property _usernameSetSuccessfully private mutable live data of whether the username was successfully set
 * @property usernameSetSuccessfully public live data for usernameSetSuccessfully
 * @property _descriptionSetSuccessfully private mutable live data of whether the description was successfully set
 * @property descriptionSetSuccessfully public live data for descriptionSetSuccessfully
 * @property _tagAddedSuccessfully private mutable live data of whether the tag was successfully added
 * @property tagAddedSuccessfully public live data for tagAddedSuccessfully
 * @property _tagDeletedSuccessfully private mutable live data of whether the tag was successfully deleted
 * @property tagDeletedSuccessfully public live data for tagDeletedSuccessfully
 */
class ProfileFragmentViewModel : ViewModel() {

    private var repository = ProfileRepository()
    private var loggedUserEmail = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()

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
     * obtains ProfileInfo from the lower level and places it on the live data
     *
     * calls the functions that do the same for the user's activities and tags
     */
    fun getProfileInfo() {
        val username = "victorfer" // stub

        profileInfo = repository.getProfileInfo(username)!!

        getMyActivities()
        getTags()
    }

    /**
     * obtains myActivities from the lower level and places them on the live data
     */
    private fun getMyActivities() {
        //_myActivities = repository.getUserActivities(loggedUserEmail)!!
        myActivities = repository.getUserActivities(loggedUserEmail)!!      //funciona amb aquest i no amb el _myActivities
    }

    /**
     * obtains tags from the lower level and places them on the live data
     */
    private fun getTags() {
        tags = repository.getUserTags(loggedUserEmail)!!
    }

    /**
     * initiates the edition of the username
     *
     * makes the call to the Repository and observes its live data for the result.
     * Sets the ViewModel's live data according to that of the Repository when it is ready
     *
     * @param newUsername string to change the username to
     * @param activity pointer to the activity, used by the observers
     */
    fun usernameChangedByUser(newUsername: Editable, activity: AppCompatActivity) {
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

    /**
     * initiates the edition of the description
     *
     * makes the call to the Repository and observes its live data for the result.
     * Sets the ViewModel's live data according to that of the Repository when it is ready
     *
     * @param newDescription string to change the description to
     * @param activity pointer to the activity, used by the observers
     */
    fun descriptionChangedByUser(newDescription: Editable, activity: AppCompatActivity) {
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


    /**
     * initiates the deletion of a tag
     *
     * makes the call to the Repository and observes its live data for the result.
     * Sets the ViewModel's live data according to that of the Repository when it is ready
     *
     * @param tag tag to be deleted
     * @param activity pointer to the activity, used by the observers
     */
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

    /**
     * initiates the addition of a tag
     *
     * makes the call to the Repository and observes its live data for the result.
     * Sets the ViewModel's live data according to that of the Repository when it is ready
     *
     * @param tag tag to be added
     * @param activity pointer to the activity, used by the observers
     */
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
