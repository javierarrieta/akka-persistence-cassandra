package akka.persistence.journal.cassandra

import java.io.File

import akka.testkit.TestKit

import com.datastax.driver.core.Cluster

import org.apache.commons.io.FileUtils
import org.scalatest._

trait CassandraCleanup extends BeforeAndAfterAll { this: TestKit with Suite =>
  val journalConfig = system.settings.config.getConfig("cassandra-journal")
  val snapshotConfig = system.settings.config.getConfig("akka.persistence.snapshot-store.local")

  override protected def afterAll(): Unit = {
    val cluster = Cluster.builder.addContactPoint("127.0.0.1").build
    val session = cluster.connect()
    session.execute(s"DROP KEYSPACE ${journalConfig.getString("keyspace")}")
    FileUtils.deleteDirectory(new File(snapshotConfig.getString("dir")))
    system.shutdown()
  }

}
