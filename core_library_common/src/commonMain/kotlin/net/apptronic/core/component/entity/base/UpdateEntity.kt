package net.apptronic.core.component.entity.base

import net.apptronic.core.base.observable.subject.Subject
import net.apptronic.core.component.entity.Entity

interface UpdateEntity<T> : Entity<T>, Subject<T>