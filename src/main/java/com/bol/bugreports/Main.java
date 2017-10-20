package com.bol.bugreports;

import org.apache.flink.addons.hbase.AbstractTableInputFormat;
import org.apache.flink.addons.hbase.TableInputFormat;
import org.apache.flink.api.java.tuple.Tuple1;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.nio.charset.StandardCharsets.UTF_8;

public class Main {
  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    printZookeeperConfig();
    final StreamExecutionEnvironment env = StreamExecutionEnvironment
      .getExecutionEnvironment()
      .setParallelism(1);
    env.createInput(new HBaseSource()).print();
    env.execute("HBase config problem");
  }

  public static void printZookeeperConfig() {
    String zookeeper = HBaseConfiguration.create().get("hbase.zookeeper.quorum");
    LOG.info("----> Loading HBaseConfiguration: Zookeeper = {}", zookeeper);
  }

  public static class HBaseSource extends AbstractTableInputFormat<String> {
    @Override
    public void configure(org.apache.flink.configuration.Configuration parameters) {
      table = createTable();
      if (table != null) {
        scan = getScanner();
      }
    }

    private HTable createTable() {
      LOG.info("Initializing HBaseConfiguration");
      // Uses files found in the classpath
      org.apache.hadoop.conf.Configuration hConf = HBaseConfiguration.create();
      printZookeeperConfig();

      try {
        return new HTable(hConf, getTableName());
      } catch (Exception e) {
        LOG.error("Error instantiating a new HTable instance", e);
      }
      return null;
    }

    @Override
    public String getTableName() {
      return "bugs:flink";
    }

    @Override
    protected String mapResultToOutType(Result result) {
      return new String(result.getFamilyMap("v".getBytes(UTF_8)).get("column".getBytes(UTF_8)));
    }

    @Override
    protected Scan getScanner() {
      return new Scan();
    }
  }

}
