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

import org.beangle.commons.collection.Collections
import org.beangle.data.model.LongId
import org.beangle.data.model.pojo.{Named, Updated}
import org.openurp.base.model.Semester

import scala.collection.mutable.Buffer

/**
 * 帮扶记录
 */
class AlertReliefRecord extends LongId with Updated with Named {

  var semester: Semester = _

  var file: AlertStudent = _

  var description: String = _

  var attachments: Buffer[AlertReliefFile] = Collections.newBuffer[AlertReliefFile]

  var alertType: AlertType = _

  /**
   * 具体不及格学分情况描述
   */
  var detail: String = _

}
