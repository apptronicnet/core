package net.apptronic.test.commons_sample_compat_app.fragments.enterdata

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import net.apptronic.core.android.compat.CoreCompatContextHolder
import net.apptronic.core.android.compat.componentContext
import net.apptronic.core.context.dependencyModule
import net.apptronic.core.context.di.inject
import net.apptronic.test.commons_sample_compat_app.R
import net.apptronic.test.commons_sample_compat_app.Router
import net.apptronic.test.commons_sample_compat_app.fragments.enterdata.location.EnterLocationFragment
import net.apptronic.test.commons_sample_compat_app.fragments.enterdata.name.EnterNameFragment

class EnterDataFragment : Fragment(), CoreCompatContextHolder {

    lateinit var router: Router

    lateinit var repository: DataRepository

    override val componentContext by componentContext {
        dependencyModule {
            single {
                DataRepository()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        router = componentContext.dependencyProvider.inject()
        repository = componentContext.dependencyProvider.inject()
        componentContext.dependencyDispatcher.addInstance<EnterDataRouter>(dataRouter)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_enter_data, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (savedInstanceState == null) {
            childFragmentManager.beginTransaction()
                .add(R.id.enterDataFragmentContainer, EnterNameFragment())
                .commit()
        }
    }

    private val dataRouter = object : EnterDataRouter {

        override fun submitName() {
            childFragmentManager.beginTransaction()
                .replace(R.id.enterDataFragmentContainer, EnterLocationFragment())
                .commit()
        }

        override fun submitLocation() {
            val data = repository.getData()
            router.goToShowUserData(data)
        }

    }

}