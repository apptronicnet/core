package net.apptronic.core.view.container

import net.apptronic.core.view.properties.*

fun CoreContainerView.alignContentToCenter() {
    contentAlignmentHorizontal = ToCenter
    contentAlignmentVertical = ToCenter
}

fun CoreContainerView.alignContentToLeft() {
    contentAlignmentHorizontal = ToLeft
}

fun CoreContainerView.alignContentToStart() {
    contentAlignmentHorizontal = ToStart
}

fun CoreContainerView.alignContentToRight() {
    contentAlignmentHorizontal = ToRight
}

fun CoreContainerView.alignContentToEnd() {
    contentAlignmentHorizontal = ToEnd
}

fun CoreContainerView.alignContentToCenterHorizontal() {
    contentAlignmentHorizontal = ToCenter
}

fun CoreContainerView.alignContentToTop() {
    contentAlignmentVertical = ToTop
}

fun CoreContainerView.alignContentToBottom() {
    contentAlignmentVertical = ToBottom
}

fun CoreContainerView.alignContentToCenterVertical() {
    contentAlignmentVertical = ToCenter
}