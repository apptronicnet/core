package net.apptronic.test.commons_sample_app.onboarding

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.mvvm.viewmodel.ViewModelContext
import net.apptronic.test.commons_sample_app.onboarding.OnboardingContext.Companion.OnboardingDataDescriptor

class OnboardingContext(parent: Context) : ViewModelContext(parent) {

    companion object {
        val OnboardingDataDescriptor = createDescriptor<OnboadingData>()
    }

    init {
        getProvider().addModule(OnboardingModule)
    }

}

val OnboardingModule = declareModule {

    single(OnboardingDataDescriptor) {
        OnboadingData()
    }

    factory {
        DataSender(
            data = inject(OnboardingDataDescriptor)
        )
    }

}

class DataSender(data: OnboadingData) {

    fun sendData() {

    }

}