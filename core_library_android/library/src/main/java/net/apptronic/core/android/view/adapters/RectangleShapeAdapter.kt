//package net.apptronic.core.android.view.adapters
//
//import android.content.Context
//import android.graphics.Canvas
//import android.graphics.Paint
//import android.view.View
//import androidx.appcompat.widget.AppCompatImageView
//import net.apptronic.core.android.view.IViewRenderingEngine
//import net.apptronic.core.android.view.ViewTypeAdapter
//import net.apptronic.core.android.view.platform.getAndroidColor
//import net.apptronic.core.view.shape.CoreRectangleShapeDrawable
//
//object RectangleShapeAdapter :
//    ViewTypeAdapter<CoreRectangleShapeDrawable, RectangleShapeAdapter.RectangleShapeView> {
//
//    override fun createView(
//        context: Context,
//        source: CoreRectangleShapeDrawable
//    ): RectangleShapeView {
//        return RectangleShapeView(context)
//    }
//
//    override fun applyViewAttributes(
//        engine: IViewRenderingEngine,
//        coreView: CoreRectangleShapeDrawable,
//        frame: View,
//        content: RectangleShapeView
//    ) {
//        with(engine) {
//            coreView.fillColor.subscribe {
//                content.fillColor = it?.getAndroidColor()
//            }
//            coreView.strokeColor.subscribe {
//                content.strokeColor = it?.getAndroidColor()
//            }
//            coreView.strokeWidth.subscribe {
//                content.strokeWidth = it?.let { dimensionEngine.getDimensionPixelSizeFloat(it) }
//            }
//        }
//    }
//
//    class RectangleShapeView(context: Context) : AppCompatImageView(context) {
//
//        private val fillPaint = Paint().apply {
//            isAntiAlias = true
//            style = Paint.Style.FILL
//        }
//
//        private val strokePaint = Paint().apply {
//            isAntiAlias = true
//            style = Paint.Style.STROKE
//        }
//
//        var fillColor: Int? = null
//            set(value) {
//                field = value
//                invalidate()
//                fillPaint.color = value ?: 0
//            }
//
//        var strokeColor: Int? = null
//            set(value) {
//                field = value
//                invalidate()
//                strokePaint.color = value ?: 0
//            }
//
//        var strokeWidth: Float? = null
//            set(value) {
//                field = value
//                invalidate()
//                strokePaint.strokeWidth = value ?: 0f
//            }
//
//        override fun onDraw(canvas: Canvas) {
//            if (fillColor != null) {
//                canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), fillPaint)
//            }
//            if (strokeColor != null && strokeWidth != null) {
//                val halfStroke = strokeWidth!! / 2f
//                canvas.drawRect(
//                    0f + halfStroke,
//                    0f + halfStroke,
//                    width.toFloat() - halfStroke,
//                    height.toFloat() - halfStroke,
//                    strokePaint
//                )
//            }
//        }
//
//    }
//
//}