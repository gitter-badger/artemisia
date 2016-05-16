package tech.artemesia.task.database

import tech.artemesia.TestSpec

/**
 * Created by chlr on 4/27/16.
 */
class DBInterfaceSpec extends TestSpec {

  val table = "db_interface"
  val dbInterface = TestDBInterFactory.withDefaultDataLoader(table)

  "DBInterface" must "execute queries correctly" in {
    val rs = dbInterface.query(s"SELECT * FROM $table")
    rs.next() must be (true)
    rs.getInt(1) -> rs.getString(2) must be (1 -> "foo")
    rs.next() must be (true)
    rs.getInt(1) -> rs.getString(2) must be (2 -> "bar")
  }

  it must "run query one and return config object" in {
    val config = dbInterface.queryOne(s"SELECT * FROM $table where col1 = 1")
    //investigate why column names are capitalized
    config.getInt("COL1") -> config.getString("COL2") must be (1 -> "foo")
  }


}
