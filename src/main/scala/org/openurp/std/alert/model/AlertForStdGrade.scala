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

package org.openurp.std.alert.model

import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.Updated
import org.openurp.base.model.{ProjectBased, Semester}
import org.openurp.base.std.model.Student

/**
 * 成绩学业预警情况
 */
class AlertForStdGrade extends LongId, Updated, ProjectBased {
  /**
   * 需要查看各学期预警变化情况
   */
  var semester: Semester = _

  var std: Student = _

  var alertType: AlertType = _

  /**
   * 具体不及格学分情况描述
   */
  var detail: String = _

}
