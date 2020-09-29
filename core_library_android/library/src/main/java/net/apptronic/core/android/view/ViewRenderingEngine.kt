package net.apptronic.core.android.view

import android.content.Context
import android.graphics.Color
import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import net.apptronic.core.android.view.adapters.ContentViewAdapter
import net.apptronic.core.android.view.adapters.FrameContainerViewAdapter
import net.apptronic.core.android.view.adapters.RectangleShapeAdapter
import net.apptronic.core.android.view.adapters.TextViewAdapter
import net.apptronic.core.android.view.platform.DimensionEngine
import net.apptronic.core.base.utils.isInstanceOf
import net.apptronic.core.view.ICoreView
import net.apptronic.core.view.base.CoreViewBuilder
import net.apptronic.core.view.base.ViewConfiguration
import net.apptronic.core.view.context.CoreViewContext
import net.apptronic.core.view.properties.LayoutDirection
import kotlin.reflect.KClass

open class ViewRenderingEngine(
    private val activity: AppCompatActivity,
    private val dimensionEngine: DimensionEngine = DisplayMetricsDimensionEngine(activity.resources.displayMetrics)
) {

    private val androidContext: Context = activity
    private val viewConfiguration: ViewConfiguration

    private val factory = ViewFrameFactory()

    private val holders = mutableListOf<ViewTypeAdapterHolder<*, *>>()

    init {
        val layoutDirectionInt = activity.window.decorView.layoutDirection
        viewConfiguration = ViewConfiguration(
            layoutDirection = if (layoutDirectionInt == View.LAYOUT_DIRECTION_RTL) {
                LayoutDirection.LeftToRight
            } else {
                LayoutDirection.RightToLeft
            }
        )
        addAdapter(TextViewAdapter)
        addAdapter(ContentViewAdapter)
        addAdapter(FrameContainerViewAdapter)
        addAdapter(RectangleShapeAdapter)
    }

    private inner class ViewTypeAdapterHolder<CoreView : ICoreView, ContentView : View>(
        val coreType: KClass<CoreView>,
        val androidType: KClass<ContentView>,
        val adapter: ViewTypeAdapter<CoreView, ContentView>
    ) {

        @Suppress("UNCHECKED_CAST")
        fun createContentView(coreView: ICoreView): ContentView? {
            return if (coreView::class == this.coreType) {
                adapter.createView(androidContext, coreView as CoreView)
            } else {
                null
            }
        }

        @Suppress("UNCHECKED_CAST")
        fun proceed(
            engine: IViewRenderingEngine, coreView: ICoreView, frame: View, contentView: View
        ) {
            if (coreView.isInstanceOf(this.coreType) && contentView.isInstanceOf(androidType)) {
                val asCoreView = coreView as? CoreView
                val asContent = contentView as? ContentView
                if (asCoreView != null && asContent != null) {
                    adapter.applyViewAttributes(engine, asCoreView, frame, asContent)
                }
            }
        }

    }

    private class RootBuilder(override val context: CoreViewContext) : CoreViewBuilder

    fun <CoreView : ICoreView, ContentView : View> addAdapter(
        coreType: KClass<CoreView>,
        androidType: KClass<ContentView>,
        adapter: ViewTypeAdapter<CoreView, ContentView>
    ) {
        holders.add(ViewTypeAdapterHolder(coreType, androidType, adapter))
    }

    inline fun <reified CoreView : ICoreView, reified ContentView : View> addAdapter(
        adapter: ViewTypeAdapter<CoreView, ContentView>
    ) {
        addAdapter(CoreView::class, ContentView::class, adapter)
    }

    fun renderRoot(builder: CoreViewBuilder.() -> ICoreView): View {
        val coreView = RootBuilder(CoreViewContext(viewConfiguration)).builder()
        return render(coreView, false).frame
    }

    private fun render(coreView: ICoreView, independentContext: Boolean): ViewHolder {
        val context =
            if (independentContext)
                coreView.context.childRenderingContext()
            else
                coreView.context
        val renderer = Renderer(context)
        val content = createContentView(coreView) ?: createFallbackContent(coreView)
        val frame = factory.createViewFrame(renderer, coreView, content)
        applyAttributes(renderer, coreView, frame, content)
        return RenderViewHolder(context, coreView, frame, content)
    }

    private fun createContentView(coreView: ICoreView): View? {
        for (holder in holders) {
            val view = holder.createContentView(coreView)
            if (view != null) {
                return view
            }
        }
        return null
    }

    private fun createFallbackContent(coreView: ICoreView): View {
        return TextView(androidContext).apply {
            setText(coreView::class.simpleName)
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            setTextColor(Color.BLACK)
            setBackgroundColor(Color.LTGRAY)
        }
    }

    private fun applyAttributes(
        engine: IViewRenderingEngine,
        coreView: ICoreView,
        frame: View,
        content: View
    ) {
        for (holder in holders) {
            holder.proceed(engine, coreView, frame, content)
        }
    }

    private inner class Renderer(override val context: net.apptronic.core.component.context.Context) :
        IViewRenderingEngine {

        override val androidContext: Context = this@ViewRenderingEngine.androidContext

        override val dimensionEngine: DimensionEngine = this@ViewRenderingEngine.dimensionEngine

        override fun render(coreView: ICoreView, independentContext: Boolean): ViewHolder {
            return this@ViewRenderingEngine.render(coreView, independentContext)
        }

    }

}