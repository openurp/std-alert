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

package org.openurp.std.alert.web.action

import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.model.Project
import org.openurp.std.alert.model.AlertStudent
import org.openurp.starter.web.support.ProjectSupport

class FileAction extends RestfulAction[AlertStudent], ProjectSupport {

  override def indexSetting(): Unit = {
    given project: Project = getProject

    put("departments", getDeparts)
    super.indexSetting()
  }
}
