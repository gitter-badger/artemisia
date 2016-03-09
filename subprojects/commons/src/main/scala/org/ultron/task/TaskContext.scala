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

  private var preferredWorkingDir: Option[Path] = None

  lazy val workingDir = preferredWorkingDir.getOrElse(Files.createTempDir().toPath)

  def setworkingDir(working_dir: Path) = {
    preferredWorkingDir = Some(working_dir)
  }

}


