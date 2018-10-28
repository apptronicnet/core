package net.apptronic.shoppinglist.uisample

/**
 * Sample presenter represents behavior for sample screen
 */
class SamplePresenter(val view: SampleViewModel) {

    /**
     * Sample screen state variable
     */
    var count = 1

    // initialize behavior
    init {

        // Set view title
        view.title.set("Initial title")

        // perform some actions on view is created
        view.onViewCreated {
            // handle clicks on button "Refresh title"
            view.onClickRefreshTitle.subscribe {
                // set new title value
                view.title.set("Title changes ${count++}")
            }
            // all subscriptions will be automatically unsubscribed when view destoryed
        }

        // perform some actions on view is resumed
        view.onResume {
            // when user changes view property userInputValue
            view.userInputValue.subscribe {
                // then set view property [currentInputText] to it
                view.currentInputText.set("Input: $it")
            }
            // when user clicks on button "Confirm input"
            view.onClickConfirmInputEvent.subscribe {
                // then set view property [confirmedInputText] to value of [userInputValue]
                view.confirmedInputText.text.set(view.userInputValue)
            }
            // all subscriptions will be automatically unsubscribed when view paused
        }

    }

}