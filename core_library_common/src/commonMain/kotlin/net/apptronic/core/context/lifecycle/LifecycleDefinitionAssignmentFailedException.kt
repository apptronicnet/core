package net.apptronic.core.context.lifecycle

class LifecycleDefinitionAssignmentFailedException internal constructor() :
    Exception(
        "Cannot assign multiple [LifecycleDefinition]s to a [Context] [Lifecycle]. " +
                "[Component]s in same [Context] should expect and use same [LifecycleDefinition]."
    )