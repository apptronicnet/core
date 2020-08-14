package net.apptronic.core.mvvm.viewmodel.navigation

class ItemAdded(val index: Int)

class ItemRemoved(val index: Int)

class ItemMoved(val fromIndex: Int, val toIndex: Int)

class RangeInserted(val range: IntRange)

class RangeRemoved(val range: IntRange)