package net.apptronic.core.ios.anim.animations

import net.apptronic.core.ios.anim.ViewAnimationDefinition
import net.apptronic.core.ios.anim.ViewTransformation
import platform.UIKit.UIView
import platform.UIKit.UIViewAnimationCurve
import platform.UIKit.alpha
import platform.UIKit.animateWithDuration

object ViewAnimation_Empty : ViewAnimationDefinition() {

    override fun createTransformation(): ViewTransformation {
        return object : ViewTransformation() {

            override fun animate(target: UIView, container: UIView, duration: Double) {
                UIView.animateWithDuration(duration, animations = {}, completion = this::completed)
            }

            override fun cancel(target: UIView, container: UIView) {
                // do nothing
            }

        }
    }
}

object ViewAnimation_FadeIn : BasicViewAnimationDefinition() {

    override fun createBasicViewTransformation(): BasicViewTransformation {
        return object : BasicViewTransformation() {
            override fun animate(target: UIView, container: UIView, duration: Double) {
                target.prepareAnimation {
                    alpha = 0.0
                }
                startAnimation(duration) {
                    target.alpha = 1.0
                }
            }

            override fun cancel(target: UIView, container: UIView) {
                target.alpha = 1.0
            }

        }
    }

}

object ViewAnimation_FadeOut : BasicViewAnimationDefinition() {

    override fun createBasicViewTransformation(): BasicViewTransformation {
        return object : BasicViewTransformation() {

            override fun animate(target: UIView, container: UIView, duration: Double) {
                target.prepareAnimation {
                    alpha = 1.0
                }
                startAnimation(duration) {
                    target.alpha = 0.0
                }
            }

            override fun cancel(target: UIView, container: UIView) {
                target.alpha = 1.0
            }

        }
    }

}

class ViewAnimation_FromTop(
        val amount: Float = 1f,
        animationCurve: UIViewAnimationCurve = UIViewAnimationCurve.UIViewAnimationCurveLinear
) : BasicViewAnimationDefinition(animationCurve) {

    override fun createBasicViewTransformation(): BasicViewTransformation {
        return object : BasicViewTransformation() {

            override fun animate(target: UIView, container: UIView, duration: Double) {
                target.prepareAnimation {
                    translateToParent(target, container, y = -amount)
                }
                startAnimation(duration) {
                    translateToParent(target, container, y = 0f)
                }
            }

            override fun cancel(target: UIView, container: UIView) {
                translateToParent(target, container, y = 0f)
            }

        }
    }

}

class ViewAnimation_ToTop(
        val amount: Float = 1f,
        animationCurve: UIViewAnimationCurve = UIViewAnimationCurve.UIViewAnimationCurveLinear
) : BasicViewAnimationDefinition(animationCurve) {

    override fun createBasicViewTransformation(): BasicViewTransformation {
        return object : BasicViewTransformation() {

            override fun animate(target: UIView, container: UIView, duration: Double) {
                target.prepareAnimation {
                    translateToParent(target, container, y = 0f)
                }
                startAnimation(duration) {
                    translateToParent(target, container, y = -amount)
                }
            }

            override fun cancel(target: UIView, container: UIView) {
                translateToParent(target, container, y = 0f)
            }

        }
    }

}

class ViewAnimation_FromBottom(
        val amount: Float = 1f,
        animationCurve: UIViewAnimationCurve = UIViewAnimationCurve.UIViewAnimationCurveLinear
) : BasicViewAnimationDefinition(animationCurve) {

    override fun createBasicViewTransformation(): BasicViewTransformation {
        return object : BasicViewTransformation() {

            override fun animate(target: UIView, container: UIView, duration: Double) {
                target.prepareAnimation {
                    translateToParent(target, container, y = amount)
                }
                startAnimation(duration) {
                    translateToParent(target, container, y = 0f)
                }
            }

            override fun cancel(target: UIView, container: UIView) {
                translateToParent(target, container, y = 0f)
            }

        }
    }

}


class ViewAnimation_ToBottom(
        val amount: Float = 1f,
        animationCurve: UIViewAnimationCurve = UIViewAnimationCurve.UIViewAnimationCurveLinear
) : BasicViewAnimationDefinition(animationCurve) {

    override fun createBasicViewTransformation(): BasicViewTransformation {
        return object : BasicViewTransformation() {

            override fun animate(target: UIView, container: UIView, duration: Double) {
                target.prepareAnimation {
                    translateToParent(target, container, y = 0f)
                }
                startAnimation(duration) {
                    translateToParent(target, container, y = amount)
                }
            }

            override fun cancel(target: UIView, container: UIView) {
                translateToParent(target, container, y = 0f)
            }

        }
    }

}

class ViewAnimation_FromLeft(
        val amount: Float = 1f,
        animationCurve: UIViewAnimationCurve = UIViewAnimationCurve.UIViewAnimationCurveLinear
) : BasicViewAnimationDefinition(animationCurve) {

    override fun createBasicViewTransformation(): BasicViewTransformation {
        return object : BasicViewTransformation() {

            override fun animate(target: UIView, container: UIView, duration: Double) {
                target.prepareAnimation {
                    translateToParent(target, container, x = -amount)
                }
                startAnimation(duration) {
                    translateToParent(target, container, x = 0f)
                }
            }

            override fun cancel(target: UIView, container: UIView) {
                translateToParent(target, container, x = 0f)
            }

        }
    }

}

class ViewAnimation_ToLeft(
        val amount: Float = 1f,
        animationCurve: UIViewAnimationCurve = UIViewAnimationCurve.UIViewAnimationCurveLinear
) : BasicViewAnimationDefinition(animationCurve) {

    override fun createBasicViewTransformation(): BasicViewTransformation {
        return object : BasicViewTransformation() {

            override fun animate(target: UIView, container: UIView, duration: Double) {
                target.prepareAnimation {
                    translateToParent(target, container, x = 0f)
                }
                startAnimation(duration) {
                    translateToParent(target, container, x = -amount)
                }
            }

            override fun cancel(target: UIView, container: UIView) {
                translateToParent(target, container, x = 0f)
            }

        }
    }

}

class ViewAnimation_FromRight(
        val amount: Float = 1f,
        animationCurve: UIViewAnimationCurve = UIViewAnimationCurve.UIViewAnimationCurveLinear
) : BasicViewAnimationDefinition(animationCurve) {

    override fun createBasicViewTransformation(): BasicViewTransformation {
        return object : BasicViewTransformation() {

            override fun animate(target: UIView, container: UIView, duration: Double) {
                target.prepareAnimation {
                    translateToParent(target, container, x = amount)
                }
                startAnimation(duration) {
                    translateToParent(target, container, x = 0f)
                }
            }

            override fun cancel(target: UIView, container: UIView) {
                translateToParent(target, container, x = 0f)
            }

        }
    }

}

class ViewAnimation_ToRight(
        val amount: Float = 1f,
        animationCurve: UIViewAnimationCurve = UIViewAnimationCurve.UIViewAnimationCurveLinear
) : BasicViewAnimationDefinition(animationCurve) {

    override fun createBasicViewTransformation(): BasicViewTransformation {
        return object : BasicViewTransformation() {

            override fun animate(target: UIView, container: UIView, duration: Double) {
                target.prepareAnimation {
                    translateToParent(target, container, x = 0f)
                }
                startAnimation(duration) {
                    translateToParent(target, container, x = amount)
                }
            }

            override fun cancel(target: UIView, container: UIView) {
                translateToParent(target, container, x = 0f)
            }

        }
    }

}