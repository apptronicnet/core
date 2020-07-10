package net.apptronic.test.commons_sample_app.app

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.coreContext
import net.apptronic.core.component.di.declareModule
import net.apptronic.core.component.di.dependencyDescriptor

val HttpClientFactoryDescriptor = dependencyDescriptor<HttpClientFactory>()
val HttpClientDescriptor = dependencyDescriptor<HttpClient>()
val PlatformDescriptor = dependencyDescriptor<PlatformDefinition>()

fun applicationContext(
    httpClientFactory: HttpClientFactory,
    platformDefinition: PlatformDefinition
): Context {
    return coreContext {
        dependencyDispatcher.addInstance(HttpClientFactoryDescriptor, httpClientFactory)
        dependencyDispatcher.addInstance(PlatformDescriptor, platformDefinition)
        dependencyDispatcher.addModule(coreModule)
    }
}

private val coreModule = declareModule {

    factory(HttpClientDescriptor) {
        inject(HttpClientFactoryDescriptor).createHttpClient()
    }

}