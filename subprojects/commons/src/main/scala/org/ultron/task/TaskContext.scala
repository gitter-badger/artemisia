package org.ultron.task

import java.nio.file.Path

import com.google.common.io.Files

/**
 * Created by chlr on 3/7/16.
 */

/*
  we are creating a separate task
 */
private[ultron] object TaskContext {

  private var preferred_working_dir: Option[Path] = None

  lazy val getWorkingDir = preferred_working_dir.getOrElse(Files.createTempDir().toPath)

  def setWorkingDir(working_dir: Path) = {
    preferred_working_dir = Some(working_dir)
  }

}
