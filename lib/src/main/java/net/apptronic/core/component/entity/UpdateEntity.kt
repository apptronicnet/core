package net.apptronic.core.component.entity

import net.apptronic.core.base.observable.subject.Subject

interface UpdateEntity<T> : Entity<T>, Subject<T>