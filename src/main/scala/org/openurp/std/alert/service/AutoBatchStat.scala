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

import org.beangle.commons.collection.Collections
import org.beangle.data.dao.{EntityDao, OqlBuilder}
import org.openurp.base.model.{Project, Semester}
import org.openurp.base.std.model.Student
import org.openurp.std.alert.model.AlertForStdGrade

import java.time.LocalDate

class AutoBatchStat extends AbstractJob {

  var alertservice: AlertForGradeService = _

  val bulkSize = 100

  override def execute(): Unit = {
    val format = new java.text.SimpleDateFormat("yyyy-MM-dd")
    val today = format.parse(LocalDate.now().toString).toInstant
    val query = OqlBuilder.from(classOf[Student], "s")
    query.where("not exists(from " + classOf[AlertForStdGrade].getName + " r where r.std=s and r.updatedAt > :updatedAt)", today)
    query.where("s.state.inschool=true")
    query.where(":today between s.state.beginOn and s.state.endOn", LocalDate.now())
    query.orderBy("s.code")
    query.limit(1, bulkSize)
    val stds = entityDao.search(query)
    val startAt = System.currentTimeMillis()
    val semesters = Collections.newMap[Project, Semester]

    stds.foreach(std => {
      val semster = semesters.getOrElseUpdate(std.project, getCurrentSemester(std.project))
      alertservice.autoStat(std, semster)
    })

    if (stds.size > 0) {
      logger.info("auto gws: " + stds(0).code + "~" + stds(stds.size - 1).code + "["
        + stds.size + "] using " + (System.currentTimeMillis() - startAt) / 1000.0 + "s")
    } else {
      logger.info("auto gws: all alertStat is updated today!")
    }
  }

  def getCurrentSemester(project: Project): Semester = {
    val builder = OqlBuilder.from(classOf[Semester], "semester")
      .where("semester.calendar =:calendar", project.calendar)
    builder.where(":date between semester.beginOn and  semester.endOn", LocalDate.now)
    builder.cacheable()
    val rs = entityDao.search(builder)
    if (rs.isEmpty) {
      val builder2 = OqlBuilder.from(classOf[Semester], "semester")
        .where("semester.calendar =:calendars", project.calendar)
      builder2.orderBy("abs(semester.beginOn - current_date() + semester.endOn - current_date())")
      builder2.cacheable()
      builder2.limit(1, 1)
      val rs2 = entityDao.search(builder2)
      if (rs2.nonEmpty) {
        rs2.head
      } else {
        null
      }
    } else {
      rs.head
    }
  }

}
