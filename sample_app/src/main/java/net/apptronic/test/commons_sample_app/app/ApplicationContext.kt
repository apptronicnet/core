package net.apptronic.test.commons_sample_app.app

import net.apptronic.core.component.context.CoreContext
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule
import net.apptronic.test.commons_sample_app.app.ApplicationContext.Companion.HttpClientDescriptor
import net.apptronic.test.commons_sample_app.app.ApplicationContext.Companion.HttpClientFactoryDescriptor

class ApplicationContext(
    httpClientFactory: HttpClientFactory,
    platform: Platform
) : CoreContext() {

    companion object {
        val HttpClientFactoryDescriptor = createDescriptor<HttpClientFactory>()
        val HttpClientDescriptor = createDescriptor<HttpClient>()
        val PlatformDescriptor = createDescriptor<Platform>()
    }

    init {
        getProvider().addInstance(HttpClientFactoryDescriptor, httpClientFactory)
        getProvider().addInstance(PlatformDescriptor, platform)
        getProvider().addModule(coreModule)
    }

}

val coreModule = declareModule {

    factory(HttpClientDescriptor) {
        inject(HttpClientFactoryDescriptor).createHttpClient()
    }

}