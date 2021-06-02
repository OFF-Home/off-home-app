package com.offhome.app.ui.profile



import android.text.Editable
import androidx.lifecycle.*
import com.offhome.app.common.Constants
import com.offhome.app.common.SharedPreferenceManager
import com.offhome.app.data.ActivitiesRepository
import com.offhome.app.data.ProfileRepository
import com.offhome.app.data.Result
import com.offhome.app.data.model.*

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
    private val activityRepository =ActivitiesRepository()
    private var loggedUserEmail = SharedPreferenceManager.getStringValue(Constants().PREF_EMAIL).toString()

    private var _profileInfo = MutableLiveData<Result<UserInfo>>()
    var profileInfo: LiveData<Result<UserInfo>> = _profileInfo

    private var _tags = MutableLiveData< List<TagData> >()
    var tags: LiveData<List<TagData>> = _tags

    private var _myActivities = MutableLiveData<Result<List<ActivityFromList>>>()
    var myActivities: LiveData<Result<List<ActivityFromList>>> = _myActivities
    var likedActivities = MutableLiveData<Result<List<ActivityFromList>>>()

    var usernameSetSuccessfullyResult= MutableLiveData<Result<String>>()

    var descriptionSetSuccessfully = MutableLiveData<Result<String>>()
    var tagAddedSuccessfullyResult =  MutableLiveData<Result<String>>()
    var tagDeletedSuccessfullyResult =  MutableLiveData<Result<String>>()

    /**
     * obtains ProfileInfo from the lower level and places it on the live data
     *
     * calls the functions that do the same for the user's activities and tags
     */
    fun getProfileInfo() {
        profileInfo = repository.getProfileInfo(loggedUserEmail)

        getMyActivities()
        getTags()
    }

    /**
     * obtains myActivities from the lower level and places them on the live data
     */
    private fun getMyActivities() {
        myActivities =
            repository.getUserActivities(loggedUserEmail) // funciona amb myActivities i no amb _myActivities
    }

    fun getLikedActivitiesList(userEmail: String) {
        likedActivities = activityRepository.getLikedAct(userEmail)
    }

    /**
     * obtains tags from the lower level and places them on the live data
     */
    private fun getTags() {
        tags = repository.getUserTags(loggedUserEmail)
    }

    /**
     * initiates the edition of the username
     *
     * makes the call to the Repository and places the result on the live data.
     *
     * @param newUsername string to change the username to
     */
    fun usernameChangedByUser(newUsername: Editable) {
        usernameSetSuccessfullyResult = repository.setUsernameResult(loggedUserEmail, newUsername.toString())
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
        tagDeletedSuccessfullyResult = repository.deleteTag(loggedUserEmail, tag)
    }

    /**
     * initiates the addition of a tag
     *
     * makes the call to the Repository and places the result on the live data.
     *
     * @param tag tag to be added
     */
    fun tagAddedByUser(tag: String) {
        tagAddedSuccessfullyResult = repository.addTag(loggedUserEmail, tag)
    }

    /**
     * This function calls the Repository to manage the photo uploading of the user profile
     * @param photoPath The path of the photo desired
     */
    fun uploadPhoto(photoPath: String): MutableLiveData<Result<String>> {
        return repository.uploadPhoto(loggedUserEmail, photoPath)
    }

    fun deleteAccount(): MutableLiveData<Result<String>> {
        return repository.deleteAccount(loggedUserEmail)
    }

    fun updateDarkMode(username: String, dm: DarkModeUpdate): MutableLiveData<Result<String>> {
        return repository.updateDarkMode(username,dm)
    }

    fun getAchievements(userEmail: String): MutableLiveData<Result<List<AchievementData>>> {
        return repository.getAchievements(userEmail)
    }

    fun updateNotifications(username: String, notif: NotificationData): MutableLiveData<Result<String>>{
        return repository.updateNotifications(username, notif)
    }
}
