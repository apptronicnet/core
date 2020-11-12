package net.apptronic.core.viewmodel.commons

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.entity.behavior.filter

@UnderDevelopment
fun groupSwitches(vararg switches: SwitchViewModel) {
    groupSwitches(switches.toList())
}

@UnderDevelopment
fun groupSwitches(switches: List<SwitchViewModel>) {
    switches.forEach { switch ->
        switch.observeState().filter { it }.subscribe {
            switches.forEach { item ->
                if (item != switch) {
                    item.updateState(false)
                }
            }
        }
    }
}