package net.apptronic.test.commons_sample_compat_app.fragments.showdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.apptronic.test.commons_sample_compat_app.data.UserData
import net.apptronic.test.commons_sample_compat_app.databinding.FragmentShowDataBinding

const val KEY_USER_DATA = "user_data"

class ShowDataFragment : Fragment() {

    private lateinit var viewBinding: FragmentShowDataBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentShowDataBinding.inflate(inflater, container, false)
        return viewBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding) {
            val data = arguments!!.getSerializable(KEY_USER_DATA) as UserData
            txtFirstName.text = data.firstName
            txtLastName.text = data.lastName
            txtCountry.text = data.country
            txtCity.text = data.city
        }
    }

}