package tech.artemesia.task.settings

/**
 * Created by chlr on 5/2/16.
 */

abstract case class CSVSettings(headerLines: Int = 0 ,delimiter: Char = ',', quoting: Boolean = false, quotechar: Char = '"',
                          escapechar: Char = '\\')
