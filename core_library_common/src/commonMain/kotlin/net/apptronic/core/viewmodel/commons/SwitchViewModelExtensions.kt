package net.apptronic.core.viewmodel.commons

import net.apptronic.core.UnderDevelopment
import net.apptronic.core.entity.behavior.filter

@UnderDevelopment
fun groupSwitches(vararg switches: SwitchModel) {
    groupSwitches(switches.toList())
}

@UnderDevelopment
fun groupSwitches(switches: List<SwitchModel>) {
    switches.forEach { switch ->
        switch.filter { it }.subscribe {
            switches.forEach { item ->
                if (item != switch) {
                    item.update(false)
                }
            }
        }
    }
}