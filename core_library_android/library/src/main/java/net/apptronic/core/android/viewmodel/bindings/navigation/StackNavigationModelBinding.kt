//package net.apptronic.core.android.viewmodel.bindings.navigation
//
//import android.view.View
//import android.view.ViewGroup
//import net.apptronic.core.android.plugins.getViewBinderFactoryFromExtension
//import net.apptronic.core.android.viewmodel.Binding
//import net.apptronic.core.android.viewmodel.BindingContainer
//import net.apptronic.core.android.viewmodel.ViewBinder
//import net.apptronic.core.android.viewmodel.ViewBinderAdapter
//import net.apptronic.core.android.viewmodel.navigation.ViewBinderListAdapter
//import net.apptronic.core.android.viewmodel.style.list.emptyViewStyleAdapter
//import net.apptronic.core.viewmodel.IViewModel
//import net.apptronic.core.viewmodel.navigation.BackNavigationStatus
//import net.apptronic.core.viewmodel.navigation.StackNavigationModel
//import net.apptronic.core.viewmodel.navigation.ViewModelItem
//
//@Deprecated("Will be replaced")
//fun BindingContainer.bindStackNavigator(
//    viewGroup: ViewGroup,
//    navigationModel: StackNavigationModel,
//    adapter: ViewBinderAdapter? = null,
//    transitionBuilder: TransitionBuilder? = null,
//    defaultAnimationTime: Long = viewGroup.resources.getInteger(android.R.integer.config_mediumAnimTime)
//        .toLong(),
//    gestureDetector: NavigationGestureDetector? = BackwardTransitionGestureDetector()
//) {
//    val resultFactory = adapter
//        ?: navigationModel.parent.getViewBinderFactoryFromExtension()
//        ?: throw IllegalArgumentException("ViewBinderFactory should be provided by parameters or Context.installViewFactoryPlugin()")
//    val resultTransitionBuilder = transitionBuilder
//        ?: navigationModel.parent.getTransitionBuilderFromExtension()
//        ?: TransitionBuilder()
//    +StackNavigationModelBinding(
//        viewGroup,
//        navigationModel,
//        resultFactory,
//        resultTransitionBuilder,
//        defaultAnimationTime,
//        gestureDetector
//    )
//}
//
//@Deprecated("Will be replaced")
//private class StackNavigationModelBinding(
//    private val viewGroup: ViewGroup,
//    private val navigationModel: StackNavigationModel,
//    private val adapter: ViewBinderAdapter,
//    private val transitionBuilder: TransitionBuilder,
//    private val defaultAnimationTime: Long,
//    private val gestureDetector: NavigationGestureDetector?
//) : Binding() {
//
//    override fun onBind(viewModel: IViewModel, viewBinder: ViewBinder<*>) {
//        val listAdapter = ViewBinderListAdapter(
//            viewGroup,
//            adapter,
//            emptyViewStyleAdapter()
//        )
//        val adapter = StackNavigationFrameAdapter(
//            viewGroup, transitionBuilder, defaultAnimationTime, listAdapter = listAdapter
//        )
//        adapter.bind(navigationModel)
//        if (gestureDetector != null) {
//            val gestureDispatcher =
//                GestureDispatcher(
//                    gestureDetector
//                )
//            gestureDispatcher.attach(viewGroup, GestureTargetImpl(navigationModel, adapter))
//            listAdapter.addListener(object : ViewBinderListAdapter.UpdateListener {
//                override fun onDataChanged(items: List<ViewModelItem>, changeInfo: Any?) {
//                    gestureDispatcher.reset()
//                }
//            })
//            onUnbind {
//                adapter.unbind()
//                gestureDispatcher.detach()
//            }
//        }
//    }
//
//    private class GestureTargetImpl(
//        private val navigationModel: StackNavigationModel,
//        private val adapter: StackNavigationFrameAdapter
//    ) : GestureTarget {
//
//        override fun getBackNavigationStatus(): BackNavigationStatus {
//            if (navigationModel.size <= 1) {
//                return BackNavigationStatus.Restricted
//            }
//            return adapter.getBackNavigationStatus()
//        }
//
//        override fun onGestureStarted() {
//            getBackView()?.visibility = View.VISIBLE
//        }
//
//        override fun getBackView(): View? {
//            val previousItem = navigationModel.size - 2
//            return if (previousItem >= 0) {
//                adapter.getViewAt(previousItem)
//            } else null
//        }
//
//        override fun getFrontView(): View? {
//            val currentItem = navigationModel.size - 1
//            return if (currentItem >= 0) {
//                adapter.getViewAt(currentItem)
//            } else null
//        }
//
//        override fun onGestureConfirmedPopBackStack() {
//            getFrontView()?.visibility = View.GONE
//            adapter.onConfirmBackNavigationFromGesture()
//            navigationModel.popBackStack()
//        }
//
//        override fun onGestureCancelled(becauseOfRestricted: Boolean) {
//            getBackView()?.visibility = View.GONE
//            if (becauseOfRestricted) {
//                adapter.onRestrictedBackNavigationFromGesture()
//            }
//        }
//
//    }
//
//}