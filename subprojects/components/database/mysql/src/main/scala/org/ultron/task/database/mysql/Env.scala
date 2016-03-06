package org.ultron.task.database.mysql

import org.ultron.util.db.{ConnectionProfile, DBInterface}

/**
 * Created by chlr on 4/15/16.
 */

trait Env {

  def dbInterface(connectionProfile: ConnectionProfile): DBInterface
}


class ProdEnv extends Env
{

  override def dbInterface(connectionProfile: ConnectionProfile): DBInterface = {
    new MysqlDBInterface(connectionProfile)
  }

}