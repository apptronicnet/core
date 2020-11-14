package net.apptronic.core.entity.base

import net.apptronic.core.base.subject.Subject
import net.apptronic.core.entity.Entity

/**
 * [Entity] which supports updates as [Subject]
 */
interface SubjectEntity<T> : Entity<T>, Subject<T>