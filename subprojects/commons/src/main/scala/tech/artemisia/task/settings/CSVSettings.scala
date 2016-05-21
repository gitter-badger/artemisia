package tech.artemisia.task.settings

/**
 * Created by chlr on 5/2/16.
 */

abstract class CSVSettings(val delimiter: Char = ',', val quoting: Boolean = false, val quotechar: Char = '"',
                          val escapechar: Char = '\\')
