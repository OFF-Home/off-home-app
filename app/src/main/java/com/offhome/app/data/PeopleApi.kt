package com.offhome.app.data

import android.R
import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.people.v1.PeopleService
import com.google.api.services.people.v1.model.Birthday
import com.google.api.services.people.v1.model.Person
import java.lang.Enum
import java.util.*

object PeopleApi {
    const val CONTACT_SCOPE = "https://www.googleapis.com/auth/contacts.readonly"
    const val BIRTHDAY_SCOPE = "https://www.googleapis.com/auth/user.birthday.read"
    private var mInstance: PeopleService? = null
    private val service: PeopleService?
        private get() {
            if (mInstance == null) mInstance = initializeService()
            return mInstance
        }

    private fun initializeService(): PeopleService {
        val context: Context = BHApp.getContext()
        val credential = GoogleAccountCredential.usingOAuth2(
            context,
            Arrays.asList(CONTACT_SCOPE, BIRTHDAY_SCOPE)
        )
        credential.selectedAccount = GoogleSignIn.getLastSignedInAccount(context)!!.account
        return PeopleService.Builder(
            AndroidHttp.newCompatibleTransport(),
            JacksonFactory.getDefaultInstance(),
            credential
        )
            .setApplicationName(context.getString(R.string.app_name)).build()
    }

    val profile: Person?
        get() = try {
            service!!.people()["people/me"]
                .setPersonFields("genders,birthdays,addresses")
                .execute()
        } catch (e: Exception) {

            null
        }

    fun getBirthday(person: Person): String {
        return try {
            val birthdayList: List<Birthday> = person.getBirthdays()
                ?: return Utils.EMPTY_STRING
            var date: Date? = null
            for (birthday in birthdayList) {
                date = birthday.date
                date = if (date != null && date.size() >= 3) break else null
            }
            if (date == null) return Utils.EMPTY_STRING
            val calendar: Calendar = Calendar.getInstance()
            calendar.set(date.getYear(), date.getMonth() - 1, date.getDay())
            Utils.convertDateToString(calendar)
        } catch (e: Exception) {
            Utils.handleException(e)
            Utils.EMPTY_STRING
        }
    }

    private const val CITY_SUFFIX = " city"
    fun getLocation(person: Person): Address? {
        return try {
            val addressList: List<Address> = person.getAddresses() ?: return null
            var city: String? = null
            for (add in addressList) {
                city = add.getCity()
                if (!TextUtils.isEmpty(city)) break
            }
            if (TextUtils.isEmpty(city)) return null
            val geocoder = Geocoder(BHApp.getContext())
            val addresses = geocoder.getFromLocationName(city + CITY_SUFFIX, 1)
            if (addresses == null || addresses.isEmpty()) null else addresses[0]
        } catch (e: Exception) {
            Utils.handleException(e)
            null
        }
    }

    fun getGender(person: Person): String? {
        val genders: List<Gender> = person.getGenders()
        if (genders == null || genders.isEmpty()) return null
        val gender: Gender = genders[0]
        return java.lang.String.valueOf(Enum.Gender.getEnumByValue(gender.getValue()).getId())
    }
}
