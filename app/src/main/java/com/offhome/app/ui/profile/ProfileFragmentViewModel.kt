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
import okhttp3.ResponseBody

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

    private var _usernameSetSuccessfully = MutableLiveData<ResponseBody>()
    var usernameSetSuccessfully: LiveData<ResponseBody> = _usernameSetSuccessfully

    private var _descriptionSetSuccessfully = MutableLiveData<ResponseBody>()
    var descriptionSetSuccessfully : LiveData<ResponseBody> = _descriptionSetSuccessfully

    private var _tagAddedSuccessfully = MutableLiveData<ResponseBody>()
    var tagAddedSuccessfully:LiveData<ResponseBody> = _tagAddedSuccessfully
    private var _tagDeletedSuccessfully = MutableLiveData<ResponseBody>()
    var tagDeletedSuccessfully : LiveData<ResponseBody> = _tagDeletedSuccessfully

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
        myActivities = repository.getUserActivities("victor@gmai.com"/*loggedUserEmail*/)!!      //funciona amb myActivities i no amb _myActivities
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
     * makes the call to the Repository and places the result on the live data.
     *
     * @param newUsername string to change the username to
     */
    fun usernameChangedByUser(newUsername: Editable) {
        //repository.setUsername(loggedUserEmail, newUsername.toString())
        usernameSetSuccessfully = repository.setUsername(loggedUserEmail, newUsername.toString())!!
    }

    /**
     * initiates the edition of the description
     *
     * makes the call to the Repository and places the result on the live data.
     *
     * @param newDescription string to change the description to
     */
    fun descriptionChangedByUser(newDescription: Editable) {
        descriptionSetSuccessfully = repository.setDescription(loggedUserEmail, newDescription.toString())
    }

    /**
     * initiates the deletion of a tag
     *
     * makes the call to the Repository and places the result on the live data.
     *
     * @param tag tag to be deleted
     */
    fun tagDeletedByUser(tag: String) {
        tagDeletedSuccessfully = repository.deleteTag(loggedUserEmail, tag)
    }

    /**
     * initiates the addition of a tag
     *
     * makes the call to the Repository and places the result on the live data.
     *
     * @param tag tag to be added
     */
    fun tagAddedByUser(tag:String) {
        tagAddedSuccessfully =  repository.addTag(loggedUserEmail, tag)
    }

    /**
     * This function calls the Repository to manage the photo uploading of the user profile
     * @param photoPath The path of the photo desired
     */
    fun uploadPhoto(photoPath: String){
        val email = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()
        repository.uploadPhoto(email, photoPath)
    }
}
