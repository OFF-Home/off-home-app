package com.offhome.app.model.profile

import androidx.lifecycle.MutableLiveData
import com.offhome.app.data.ProfileDataSource

/**
 * Class *ProfileRepository*
 *
 * Repository for the Profile screen. Plays the "Model" role in this screen's MVVM
 * Encapsulates the access to data from the ViewModel
 *
 * @author Ferran
 * @property dataSource reference to the DataSource object
 */
class ProfileRepository {

    private var dataSource = ProfileDataSource()

    /**
     * obtains topProfileInfo from the lower level and returns it
     */
    /*fun getTopProfileInfo(): MutableLiveData<TopProfileInfo>? {
        //TODO obtenir el username del logged in user

        dataSource.getProfileInfo2("Maria")

        //return TopProfileInfo(profilePic = , username = , starRating = )
    }*/
}
