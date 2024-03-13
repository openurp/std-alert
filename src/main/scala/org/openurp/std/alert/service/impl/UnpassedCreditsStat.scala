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

package org.openurp.std.alert.service.impl

import org.openurp.base.model.Semester
import org.openurp.base.std.model.Student

class UnpassedCreditsStat extends AllGradeStat {
  /**
   * 统计自入学起不及格成绩门数总和
   */
  def stat(std: Student, semester: Semester): Float = {
    getUnPassed(std, semester).map(_.course.getCredits(std.level)).sum
  }

}
