package net.apptronic.test.commons_sample_app.app

import net.apptronic.core.component.context.Context
import net.apptronic.core.component.context.coreContext
import net.apptronic.core.component.di.createDescriptor
import net.apptronic.core.component.di.declareModule

val HttpClientFactoryDescriptor = createDescriptor<HttpClientFactory>()
val HttpClientDescriptor = createDescriptor<HttpClient>()
val PlatformDescriptor = createDescriptor<PlatformDefinition>()

fun applicationContext(
    httpClientFactory: HttpClientFactory,
    platformDefinition: PlatformDefinition
): Context {
    return coreContext {
        dependencyDispatcher().addInstance(HttpClientFactoryDescriptor, httpClientFactory)
        dependencyDispatcher().addInstance(PlatformDescriptor, platformDefinition)
        dependencyDispatcher().addModule(coreModule)
    }
}

private val coreModule = declareModule {

    factory(HttpClientDescriptor) {
        inject(HttpClientFactoryDescriptor).createHttpClient()
    }

}