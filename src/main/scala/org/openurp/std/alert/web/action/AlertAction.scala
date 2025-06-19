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

import org.beangle.commons.collection.Order
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.view.View
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.model.Project
import org.openurp.starter.web.support.ProjectSupport
import org.openurp.std.alert.model.{AlertForStdGrade, AlertType}
import org.openurp.std.alert.service.AlertForGradeService
import org.openurp.std.alert.service.impl.UnpassedCreditsStat

class AlertAction extends RestfulAction[AlertForStdGrade] with ProjectSupport {

  var alertForGradeService: AlertForGradeService = _
  var unpassedCreditsStat: UnpassedCreditsStat = _

  override def simpleEntityName: String = "alert"

  override def indexSetting(): Unit = {
    given project: Project = getProject

    put("project", project)
    put("semester", getSemester)
    put("alertTypes", entityDao.getAll(classOf[AlertType]))
    put("departments", getDeparts)
    super.indexSetting()
  }

  override def getQueryBuilder: OqlBuilder[AlertForStdGrade] = {
    val builder = OqlBuilder.from(classOf[AlertForStdGrade], "alert")
    get("isGreen").foreach {
      case "0" => builder.where("alert.alertType.level>0")
      case "1" => builder.where("alert.alertType.level=0")
      case _ =>
    }
    populateConditions(builder)
    builder.orderBy(get(Order.OrderStr).orNull).limit(getPageLimit)
  }

  def autoStat(): View = {
    val alerts = entityDao.find(classOf[AlertForStdGrade], getLongIds("alert"))
    alerts.foreach(alert => {
      alertForGradeService.autoStat(alert.std, alert.semester)
    })
    redirect("search", s"&alert.semester.id=${alerts.head.semester.id}&orderBy=alert.std.code&isGreen=0", "统计完成")
  }

  def grades(): View = {
    val alerts = entityDao.find(classOf[AlertForStdGrade], getLongIds("alert"))
    val gw = alerts.head
    val grades = unpassedCreditsStat.getUnPassed(gw.std, gw.semester)
    put("grades", grades)
    forward()
  }

}
