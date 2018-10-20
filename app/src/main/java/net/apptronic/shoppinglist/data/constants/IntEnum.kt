package net.apptronic.shoppinglist.data.constants

fun <T : IntEnum> valueFromId(clazz: Class<T>, id: Int, defaultValue: T): T {
    clazz.enumConstants.forEach {
        if (it.id() == id) {
            return it
        }
    }
    return defaultValue
}

interface IntEnum {

    fun id(): Int

}