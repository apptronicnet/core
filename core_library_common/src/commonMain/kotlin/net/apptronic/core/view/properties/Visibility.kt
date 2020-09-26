package net.apptronic.core.view.properties

enum class Visibility {

    /**
     * View is rendering on screen
     */
    Visible,

    /**
     * View is not rendering on screen but consumes space according to it's content
     */
    Invisible,

    /**
     * View is not rendering on screen and not consumes space
     */
    Gone
}