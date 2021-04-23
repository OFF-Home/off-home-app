package com.offhome.app.ui.profile

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import com.offhome.app.R
import org.w3c.dom.Text

/**
 * A placeholder fragment containing a simple view.
 */
class ProfilePlaceholderFragment : Fragment() {

    private lateinit var btnChangePwd : Button
    private lateinit var fragmentViewModel: ProfileFragmentViewModel
    private lateinit var username: String

    private lateinit var actualPassword: EditText
    private lateinit var newPassword: EditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.profile_placeholder_fragment, container, false)

        fragmentViewModel = ViewModelProvider(this).get(ProfileFragmentViewModel::class.java)
        fragmentViewModel.getProfileInfo()
        fragmentViewModel.profileInfo.observe(
            viewLifecycleOwner,
            Observer { // aquest observer no arriba a executar-se però el de AboutMeFragment sí. NO ENTENC PERQUÈ
                val profileInfoVM = it ?: return@Observer
                username = profileInfoVM.username
            }
        )
        btnChangePwd = root.findViewById(R.id.changePassword)

        val view = layoutInflater.inflate(R.layout.activity_recover_password,null)
/*        actualPassword = view.findViewById(R.id.textView)
        newPassword = view.findViewById(R.id.editTextEmailToRecover)
        actualPassword.setText("holiiis")
*/

        btnChangePwd.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("Change password")
            builder.setView(view)
            changePassword(username)
        }

        return root
    }

    private fun changePassword(username:String){

    }
}
