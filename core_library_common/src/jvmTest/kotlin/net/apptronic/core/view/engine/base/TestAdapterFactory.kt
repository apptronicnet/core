package net.apptronic.core.view.engine.base

import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.engine.ViewAdapterFactory

interface TestAdapterFactory<CoreView : ICoreView, View : TestView> : ViewAdapterFactory<TestRenderingContext, CoreView, TestView, View>