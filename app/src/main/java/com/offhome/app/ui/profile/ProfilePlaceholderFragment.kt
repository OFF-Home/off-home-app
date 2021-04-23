package com.offhome.app.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.offhome.app.R
import com.offhome.app.ui.updatePassword.UpdatePasswordActivity as UpdatePassword
import com.offhome.app.ui.updatePassword.UpdatePasswordActivity as UpdatePasswordActivity

/**
 * A placeholder fragment containing a simple view.
 */
class ProfilePlaceholderFragment : Fragment() {

    private lateinit var fragmentViewModel: ProfileFragmentViewModel
    private lateinit var username: String

    private lateinit var actualPassword: TextView
    private lateinit var newPassword: TextView

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

      /*  val view = layoutInflater.inflate(R.layout.activity_recover_password,null)
        actualPassword = view.findViewById(R.id.textView)
        newPassword = view.findViewById(R.id.editTextEmailToRecover)
        actualPassword.setText("holiiis")

        val builder = AlertDialog.Builder(context)
        builder.setTitle("Change password")
        builder.setView(view)
        changePassword(username)*/
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnChangePwd = view.findViewById<Button>(R.id.changePassword)

        btnChangePwd.setOnClickListener {
            requireActivity().run {
                startActivity(Intent(this, UpdatePasswordActivity::class.java))
                finish()
            }
        }
    }
}
