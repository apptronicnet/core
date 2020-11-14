package net.apptronic.core.entity.base

import net.apptronic.core.base.subject.Subject
import net.apptronic.core.entity.Entity

/**
 * [Entity] which supports notification
 */
interface SubjectEntity<T> : Entity<T>, Subject<T>