package com.offhome.app.ui.inviteChoosePerson

import android.net.Uri
import com.google.firebase.dynamiclinks.ktx.androidParameters
import com.google.firebase.dynamiclinks.ktx.dynamicLink
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.dynamiclinks.ktx.socialMetaTagParameters
import com.google.firebase.ktx.Firebase
import com.offhome.app.model.ActivityDataForInvite
import com.offhome.app.model.ActivityFromList

/**
 * Creada per ferran, per a no repetir codi a dues activitats.
 *
 * */
class AuxGenerateDynamicLink {
    fun generateDynamicLink(activityInfo:ActivityDataForInvite): Uri {
        val dynamicLink = Firebase.dynamicLinks.dynamicLink {
            link = Uri.parse("https://offhome.es/activity?creator="+activityInfo.usuariCreador+"&dataHora="+activityInfo.dataHoraIni)  //aquest és el deeplink crec
            domainUriPrefix = "https://offhome.page.link"
            // Open links with this app on Android
            androidParameters {
                //minimumVersion = 23
            }

            socialMetaTagParameters {
                title = activityInfo.titol
                description = activityInfo.descripcio+" (OFF Home Activity)"      //el string extret ja existeix!
            }

            // si fessim la app a iOS: Open links with com.example.ios on iOS
            //iosParameters("com.example.ios") { }
        }

        val dynamicLinkUri = dynamicLink.uri

        return dynamicLinkUri
    }
}
