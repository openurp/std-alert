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

import jakarta.servlet.http.Part
import org.beangle.commons.codec.digest.Digests
import org.beangle.commons.io.{Dirs, IOs}
import org.beangle.commons.lang.Strings
import org.beangle.data.dao.OqlBuilder
import org.beangle.webmvc.annotation.mapping
import org.beangle.webmvc.context.Params
import org.beangle.webmvc.view.{Stream, View}
import org.beangle.webmvc.support.action.RestfulAction
import org.openurp.base.model.{Project, Semester}
import org.openurp.std.alert.Constants
import org.openurp.std.alert.model.{AlertReliefFile, AlertStudent, AlertForStdGrade, AlertReliefRecord}
import org.openurp.starter.web.support.ProjectSupport

import java.io.{File, FileOutputStream}
import java.time.Instant

class RecordAction extends RestfulAction[AlertReliefRecord] with ProjectSupport {

  override def simpleEntityName: String = "record"

  override def indexSetting(): Unit = {
    given project: Project = getProject

    put("semester", getSemester)
    put("project", project)
    put("departments", getDeparts)
    super.indexSetting()
  }

  override def search(): View = {
    val alertIds = getLongIds("alert")
    val fileIds = getLongIds("file")
    if (alertIds.nonEmpty) {
      val alerts = entityDao.find(classOf[AlertForStdGrade], alertIds)
      put("std", alerts.head.std)
      val builder = OqlBuilder.from(classOf[AlertReliefRecord], "record")
      builder.where("record.file.std=:std", alerts.head.std)
      val records = entityDao.search(builder)
      put("records", records)
      forward()
    } else if (fileIds.nonEmpty) {
      val builder = OqlBuilder.from(classOf[AlertReliefRecord], "record")
      builder.where("record.file.id=:fileId", fileIds)
      val records = entityDao.search(builder)
      put("records", records)
      forward()
    }
    else {
      super.search()
    }
  }

  override def editSetting(record: AlertReliefRecord): Unit = {
    given project: Project = getProject

    put("semester", getSemester)
    put("project", project)
    val alerts = entityDao.find(classOf[AlertForStdGrade], getLongIds("alert"))
    put("alert", alerts.head)
  }

  override def saveAndRedirect(record: AlertReliefRecord): View = {
    get("alert.id").foreach(alertId => {
      val alerts = entityDao.find(classOf[AlertForStdGrade], alertId.toLong)
      val builder = OqlBuilder.from(classOf[AlertStudent], "file")
      builder.where("file.std=:std", alerts.head.std)
      record.alertType = alerts.head.alertType
      record.detail = alerts.head.detail
      val files = entityDao.search(builder)
      if (files.isEmpty) {
        val file = new AlertStudent
        file.std = alerts.head.std
        file.updatedAt = Instant.now
        record.file = file
        entityDao.saveOrUpdate(file)
      } else {
        record.file = files.head
      }
    })
    val base = Constants.AttachmentBase + "record/"
    Dirs.on(base).mkdirs()
    val aParts = Params.getAll("attachment").asInstanceOf[List[Part]]
    aParts foreach { part =>
      if (part.getSize.toInt > 0) {
        val attachment = new AlertReliefFile()
        attachment.fileSize = part.getSize.toInt
        val ext = Strings.substringAfterLast(part.getSubmittedFileName, ".")
        attachment.path = Digests.md5Hex(part.getSubmittedFileName) + (if (Strings.isEmpty(ext)) "" else "." + ext)
        attachment.name = part.getSubmittedFileName
        attachment.updatedAt = Instant.now()
        IOs.copy(part.getInputStream, new FileOutputStream(base + attachment.path))
        entityDao.saveOrUpdate(attachment)
        record.attachments += attachment
      }
    }
    super.saveAndRedirect(record)
  }

  @mapping("attachment/{attachmentId}")
  def attachment(attachmentId: Long): View = {
    val attach = entityDao.get(classOf[AlertReliefFile], attachmentId)
    val base = Constants.AttachmentBase + "record/"
    Stream(new File(base + attach.path), attach.name)
  }

  def deleteAttach(): View = {
    val base = Constants.AttachmentBase + "record/"
    val recordId = get("recordId").orNull
    get("attachmentId").foreach { attachmentId =>
      val attachment = entityDao.get(classOf[AlertReliefFile], attachmentId.toLong)
      get("recordId").foreach { recordId =>
        val record = entityDao.get(classOf[AlertReliefRecord], recordId.toLong)
        record.attachments -= attachment
        entityDao.saveOrUpdate(record)
      }
      entityDao.remove(attachment)
      val file = new File(base + attachment.path)
      file.delete()
    }
    redirect("info", s"&id=${recordId}", "info.save.success")
  }

}
