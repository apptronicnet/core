package net.apptronic.core.view

import net.apptronic.core.component.entity.Entity
import net.apptronic.core.view.properties.CoreColor
import net.apptronic.core.view.shape.rectangleShape

/**
 * Specifies base properties for multiplatform view.
 *
 * Used by target platforms to generate native views and layouts and update according to it's properties.
 */
class CoreFrameView internal constructor() : CoreParentView() {

    val content: ViewProperty<ICoreView?> = viewProperty(null)
    val background: ViewProperty<ICoreView?> = viewProperty(null)
    val foreground: ViewProperty<ICoreView?> = viewProperty(null)

    fun content(layerBuilder: ICoreViewBuilder.() -> ICoreView) {
        content.set(StandaloneCoreViewBuilder(this).layerBuilder())
    }

    fun background(layerBuilder: ICoreViewBuilder.() -> ICoreView) {
        background.set(StandaloneCoreViewBuilder(this).layerBuilder())
    }

    fun foreground(layerBuilder: ICoreViewBuilder.() -> ICoreView) {
        foreground.set(StandaloneCoreViewBuilder(this).layerBuilder())
    }

    fun backgroundColor(value: CoreColor) {
        background {
            rectangleShape {
                fillColor(value)
            }
        }
    }

    fun backgroundColor(source: Entity<CoreColor?>) {
        background {
            rectangleShape {
                fillColor(source)
            }
        }
    }

    fun foregroundColor(value: CoreColor) {
        foreground {
            rectangleShape {
                fillColor(value)
            }
        }
    }

    fun foregroundColor(source: Entity<CoreColor?>) {
        foreground {
            rectangleShape {
                fillColor(source)
            }
        }
    }

}

fun ICoreViewBuilder.frameView(builder: CoreFrameView.() -> Unit = {}): CoreFrameView {
    return onNextView(CoreFrameView(), builder)
}