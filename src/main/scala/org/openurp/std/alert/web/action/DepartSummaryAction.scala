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

import org.beangle.data.dao.OqlBuilder
import org.beangle.web.action.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.model.{Department, Project, Semester}
import org.openurp.std.alert.model.{AlertForStdGrade, AlertType}
import org.openurp.starter.web.support.ProjectSupport

class DepartSummaryAction extends RestfulAction[AlertForStdGrade] with ProjectSupport {

  override def index(): View = {
    given project: Project = getProject

    val semester = getSemester
    put("semester", semester)
    put("project", project)
    val builder = OqlBuilder.from(classOf[AlertForStdGrade].getName, "gw")
    builder.where("gw.semester=:semester", semester)
    builder.select("gw.std.state.department.id,gw.alertType.id,count(*)")
    builder.groupBy("gw.std.state.department.id,gw.alertType.id")
    builder.orderBy("gw.std.state.department.id,gw.alertType.id")
    val datas: Seq[Array[_]] = entityDao.search(builder)
    val newDatas = datas.groupBy(_(0)).map { e =>
      (e._1, e._2.map(f => (f(1), f(2))).toMap)
    }
    val departs = entityDao.find(classOf[Department], newDatas.keys.asInstanceOf[Iterable[Int]])
    put("datas", newDatas)
    put("departs", departs)
    put("GREENID", AlertType.green)
    put("REDID", AlertType.red)
    put("YELLOWID", AlertType.yellow)
    forward()
  }

  def alerts(): View = {
    val departId = getIntId("department")
    val semesterId = getIntId("semester")
    val typeId = getIntId("alertType")
    val builder = OqlBuilder.from(classOf[AlertForStdGrade].getName, "gw")
    builder.where("gw.semester.id=:semesterId", semesterId)
    builder.where("gw.std.state.department.id=:departId", departId)
    builder.where("gw.alertType.id=:typeId", typeId)
    put("alerts", entityDao.search(builder))
    forward()
  }

}
