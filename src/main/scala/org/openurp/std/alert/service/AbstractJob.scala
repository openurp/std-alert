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

import org.beangle.commons.logging.Logging
import org.beangle.data.dao.EntityDao
import org.beangle.data.orm.hibernate.SessionHelper
import org.hibernate.SessionFactory
import org.springframework.transaction.support.TransactionSynchronizationManager

abstract class AbstractJob extends Runnable, Logging {

  var entityDao: EntityDao = _
  var sessionFactory: SessionFactory = _

  override def run(): Unit = {
    val session = SessionHelper.openSession(sessionFactory)
    try {
      execute()
    } finally {
      SessionHelper.closeSession(session.session)
    }
  }

  def execute(): Unit

}
