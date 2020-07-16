package net.apptronic.test.commons_sample_compat_app.fragments.showdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_show_data.*
import net.apptronic.test.commons_sample_compat_app.R
import net.apptronic.test.commons_sample_compat_app.data.UserData

const val KEY_USER_DATA = "user_data"

class ShowDataFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_show_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val data = arguments!!.getSerializable(KEY_USER_DATA) as UserData
        txtFirstName.text = data.firstName
        txtLastName.text = data.lastName
        txtCountry.text = data.country
        txtCity.text = data.city
    }

}