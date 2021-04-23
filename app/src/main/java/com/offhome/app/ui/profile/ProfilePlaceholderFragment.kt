package com.offhome.app.ui.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.offhome.app.R
import com.offhome.app.ui.updatePassword.UpdatePasswordActivity

/**
 * A placeholder fragment containing a simple view.
 */
class ProfilePlaceholderFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_placeholder_fragment, container, false)
    }

    /**
     * /The layout view has been created and it is already available
     * @param view The View returned by the method onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state as given here. This value may be null
     */
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
