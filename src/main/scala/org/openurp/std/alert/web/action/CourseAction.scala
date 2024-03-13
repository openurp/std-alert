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
import org.beangle.webmvc.support.action.{ExportSupport, RestfulAction}
import org.openurp.base.edu.model.Course
import org.openurp.base.model.{Project, Semester}
import org.openurp.edu.grade.model.CourseGrade
import org.openurp.std.alert.model.AlertForCourse
import org.openurp.starter.web.support.ProjectSupport

import java.time.Instant

/**
 * 课程预警
 */
class CourseAction extends RestfulAction[AlertForCourse], ProjectSupport, ExportSupport[AlertForCourse] {

  override def simpleEntityName: String = "alert"

  override def indexSetting(): Unit = {
    given project: Project = getProject

    put("semester", getSemester)
    put("project", project)
    put("departments", getDeparts)
    super.indexSetting()
  }

  def stat(): View = {
    given project: Project = getProject

    val semester = getSemester
    get("count").foreach(count => {
      val builder = OqlBuilder.from(classOf[AlertForCourse], "cw")
      builder.where("cw.semester=:semester", semester)
      val alerts = entityDao.search(builder)
      entityDao.remove(alerts)
      val query = OqlBuilder.from(classOf[CourseGrade].getName, "grade")
      query.where("grade.semester.beginOn <:beginOn", semester.beginOn)
      query.where(":beginOn between grade.std.state.beginOn and grade.std.state.endOn", semester.beginOn.plusDays(60))
      query.where("grade.passed=false")
      query.select("grade.course.id,count(distinct std.id)")
      query.groupBy("grade.course.id")
      query.having("count(distinct std.id) > " + count)
      val datas: Seq[Array[_]] = entityDao.search(query)
      datas.foreach(data => {
        val alert = new AlertForCourse
        alert.updatedAt = Instant.now
        alert.count = data(1).toString.toInt
        alert.course = entityDao.get(classOf[Course], data(0).toString.toLong)
        alert.semester = semester
        entityDao.saveOrUpdate(alert)
      })
    })
    redirect("index", s"&alert.semester.id=${semester.id}", "")
  }

}
