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

import org.beangle.commons.cdi.{Container, ContainerAware}
import org.beangle.commons.collection.Collections
import org.beangle.data.dao.OqlBuilder
import org.openurp.base.model.Semester
import org.openurp.base.std.model.Student
import org.openurp.edu.grade.BaseServiceImpl
import org.openurp.std.alert.model.{AlertForStdGrade, AlertStatMethod, AlertType}
import org.openurp.std.alert.service.{AlertForGradeService, UnpassedCreditsStatService}

import java.time.Instant

class AlertForGradeServiceImpl extends BaseServiceImpl, AlertForGradeService, ContainerAware {

  var container: Container = _

  def autoStat(std: Student, semester: Semester): Unit = {
    val builder = OqlBuilder.from(classOf[AlertForStdGrade], "gw")
    builder.where("gw.std=:std", std).where("gw.semester=:semester", semester)
    val alerts = entityDao.search(builder)

    val alert = if (alerts.isEmpty) new AlertForStdGrade else alerts.head
    val methodQuery = OqlBuilder.from(classOf[AlertStatMethod], "sm")
    methodQuery.where("sm.enabled=true")
    val methods = entityDao.search(methodQuery)
    val ruleMap = methods.map { x => (x, x.rules) }.toMap
    val typeList = Collections.newBuffer[AlertType]
    var typeName = ""
    val detailString = new StringBuilder
    var brString = ""
    methods.foreach(method => {
      val methodBean = container.getBeans(classOf[UnpassedCreditsStatService])(method.serviceName)
      val data = methodBean.stat(std, semester)
      var warned = false
      ruleMap(method).foreach(statRule => {
        if (data >= statRule.minValue && data <= statRule.maxValue) {
          if (statRule.alertType.level > 0) {
            warned = true
            typeName = statRule.alertType.name
            typeList += statRule.alertType
          }
        }
      })
      if (warned) {
        detailString.append(brString)
        detailString.append(method.name).append(" ").append(data)
        brString = ";"
      }
    })
    alert.semester = semester
    alert.std = std
    alert.project = std.project
    alert.updatedAt = Instant.now()
    if (typeList.isEmpty) {
      alert.alertType = new AlertType(AlertType.green)
    } else {
      alert.alertType = typeList.sortBy(f => f.level).reverse.head
    }
    alert.detail = detailString.toString()
    entityDao.saveOrUpdate(alert)
  }

}
