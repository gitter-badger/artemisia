package org.ultron.util.db

import java.sql.ResultSet

import au.com.bytecode.opencsv.CSVWriter

import scala.collection.convert.wrapAsJava


/**
 * Created by chlr on 4/13/16.
 */


trait DBInterface {


  type DBConnection = java.sql.Connection

  def connection: DBConnection

  def query(sql: String): ResultSet = {
    val stmt = connection.prepareStatement(sql)
    stmt.executeQuery()
  }

  def execute(sql: String): Int = {
    val stmt = connection.prepareStatement(sql)
    val recordCnt = stmt.executeUpdate()
    stmt.close()
    recordCnt
  }

  def queryOne(sql: String, column: String): String = {
    val stmt = connection.prepareStatement(sql)
    val rs = stmt.executeQuery()
    rs.
  }

  def terminate(): Unit = {
    connection.close()
  }

  /**
   *
   * @param resultSet ResultSet to be exported
   * @param exportSettings
   */
  def exportCursorToFile(resultSet: ResultSet, exportSettings: ExportSettings) = {
    val csvWriter = new CSVWriter(exportSettings.file, exportSettings.separator, exportSettings.quotechar, exportSettings.escapechar)
    csvWriter.writeAll(resultSet,exportSettings.includeHeader)
  }

  def exportCursorToFile(result: List[Array[String]], exportSettings: ExportSettings) = {
    val csvWriter = new CSVWriter(exportSettings.file, exportSettings.separator, exportSettings.quotechar, exportSettings.escapechar)
    val newlist: java.util.List[Array[String]] = wrapAsJava.seqAsJavaList(result)
    csvWriter.writeAll(newlist)
  }


}



