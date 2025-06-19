/*
 * Copyright (C) 2014, The OpenURP Software.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.openurp.std.alert.service

import org.beangle.commons.cdi.BindModule
import org.openurp.edu.grade.domain.DefaultCourseGradeProvider
import org.openurp.edu.grade.service.filters.BestGradeFilter
import org.openurp.edu.program.domain.DefaultAlternativeCourseProvider
import org.openurp.std.alert.service.impl.{AlertForGradeServiceImpl, UnpassedCountStat, UnpassedCreditsStat, UnpassedCreditsStatBySemester}

class ServiceModule extends BindModule {

  protected override def binding(): Unit = {
    bind("unpassedCreditsStat", classOf[UnpassedCreditsStat])
    bind("unpassedCountStat", classOf[UnpassedCountStat])
    bind("unpassedCreditsStatBySemester", classOf[UnpassedCreditsStatBySemester])
    bind(classOf[AlertForGradeServiceImpl])

    bind(classOf[StatJobStarter]).lazyInit(false)

    bind(classOf[AutoBatchStat])

    bind("courseGradeProvider", classOf[DefaultCourseGradeProvider])

    bind("bestGradeFilter", classOf[BestGradeFilter])

    bind("alternativeCourseProvider", classOf[DefaultAlternativeCourseProvider])

  }

}
