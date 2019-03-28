package net.apptronic.test.commons_sample_app.app

import net.apptronic.core.component.context.CoreComponentContext
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule
import net.apptronic.test.commons_sample_app.app.ApplicationContext.Companion.HttpClientDescriptor
import net.apptronic.test.commons_sample_app.app.ApplicationContext.Companion.HttpClientFactoryDescriptor

class ApplicationContext(
    httpClientFactory: HttpClientFactory,
    platform: Platform
) : CoreComponentContext() {

    companion object {
        val HttpClientFactoryDescriptor = createDescriptor<HttpClientFactory>()
        val HttpClientDescriptor = createDescriptor<HttpClient>()
        val PlatformDescriptor = createDescriptor<Platform>()
    }

    init {
        objects().addInstance(HttpClientFactoryDescriptor, httpClientFactory)
        objects().addInstance(PlatformDescriptor, platform)
        objects().addModule(coreModule)
    }

}

val coreModule = declareModule {

    single(HttpClientDescriptor) {
        this.inject(HttpClientFactoryDescriptor).createHttpClient()
    }

}