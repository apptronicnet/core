package net.apptronic.test.commons_sample_app.onboarding

import net.apptronic.core.context.di.declareModule
import net.apptronic.core.context.di.dependencyDescriptor
import net.apptronic.core.viewmodel.defineViewModelContext

val OnboardingDataDescriptor = dependencyDescriptor<OnboadingData>()

val OnboardingContext = defineViewModelContext("Onboarding") {
    dependencyDispatcher.addModule(OnboardingModule)
}

private val OnboardingModule = declareModule {

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