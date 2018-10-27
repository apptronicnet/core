package net.apptronic.shoppinglist.uisample

class SamplePresenter(val viewModel: SampleViewModel) {

    var count = 1

    init {

        viewModel.title.set("Initial title")

        viewModel.onViewCreated {
            viewModel.onClickRefreshTitle.subscribe {
                viewModel.title.set("Title changes ${count++}")
            }

        }

        viewModel.onResume {
            viewModel.userInput.subscribe {
                viewModel.userInputText.set(it)
            }
            viewModel.onClickConfirmInput.subscribe {
                viewModel.userConfirmedInputText.set(viewModel.userInput)
            }
        }

    }

}