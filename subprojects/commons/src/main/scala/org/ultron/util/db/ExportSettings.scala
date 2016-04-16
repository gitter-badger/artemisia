package org.ultron.util.db

import java.io.File

/**
 * Created by chlr on 4/13/16.
 */

case class ExportSettings(file: File, includeHeader: Boolean, separator: Char ,quoting: QuotingOption.Value, quotechar: Char,
                          escapechar: Char)
