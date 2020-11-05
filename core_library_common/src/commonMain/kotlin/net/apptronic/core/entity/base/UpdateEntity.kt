package net.apptronic.core.entity.base

import net.apptronic.core.base.subject.Subject
import net.apptronic.core.entity.Entity

interface UpdateEntity<T> : Entity<T>, Subject<T>