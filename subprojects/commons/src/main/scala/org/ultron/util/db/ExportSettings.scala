package org.ultron.util.db

import java.io.FileWriter

/**
 * Created by chlr on 4/13/16.
 */

case class ExportSettings(file: FileWriter, includeHeader: Boolean, separator: Char ,quoting: QuotingOption.Value, quotechar: Char,
                          escapechar: Char)
