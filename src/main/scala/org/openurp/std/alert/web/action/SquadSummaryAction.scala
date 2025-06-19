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
import org.beangle.webmvc.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.model.{Department, Project, Semester}
import org.openurp.base.std.model.Squad
import org.openurp.std.alert.model.{AlertForStdGrade, AlertType}
import org.openurp.starter.web.support.ProjectSupport

class SquadSummaryAction extends RestfulAction[AlertForStdGrade] with ProjectSupport {

  override def index(): View = {
    given project: Project = getProject

    val semester = getSemester
    put("project", project)
    put("semester", semester)

    val a = getInt("departId")
    val departId = getInt("departId").get
    val builder = OqlBuilder.from(classOf[AlertForStdGrade].getName, "gw")
    builder.where("gw.semester=:semester", semester)
    builder.where("gw.std.state.department.id=:id", departId)
    builder.select("gw.std.state.squad.id,gw.alertType.id,count(*)")
    builder.groupBy("gw.std.state.squad.id,gw.alertType.id")
    builder.orderBy("gw.std.state.squad.id,gw.alertType.id")
    val datas: Seq[Array[_]] = entityDao.search(builder)
    val newDatas = datas.groupBy(_(0)).map { e =>
      (e._1, e._2.map(f => (f(1), f(2))).toMap)
    }
    val squads = entityDao.find(classOf[Squad], newDatas.keys.asInstanceOf[Iterable[Long]])
    put("datas", newDatas)
    put("squads", squads)
    put("depart", entityDao.get(classOf[Department], departId))
    put("GREENID", AlertType.green)
    put("REDID", AlertType.red)
    put("YELLOWID", AlertType.yellow)
    forward()
  }

}
