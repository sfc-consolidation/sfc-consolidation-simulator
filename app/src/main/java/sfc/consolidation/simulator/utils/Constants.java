package sfc.consolidation.simulator.utils;

public class Constants {
  // * Data center constants
  public static final int MIN_VNF_NUM = 1;
  public static final int MAX_VNF_NUM = 1000;
  public static final int MIN_SFC_NUM = 1;
  public static final int MAX_SFC_NUM = 1000;
  public static final int MAX_EPISODE_LEN = 100;

  // * Rack constants
  public static final int RACK_BW = 20 * 1024 * 1024; // 20 Gbps
  public static final int MIN_RACK_NUM = 1;
  public static final int MAX_RACK_NUM = 10;

  // * Server constants
  public static final int MIN_SRV_NUM_IN_SINGLE_RACK = 1;
  public static final int MAX_SRV_NUM_IN_SINGLE_RACK = 10;
  public static final int MAX_SRV_VCPU_NUM = 1000;
  public static final int MIN_SRV_VCPU_NUM = 1;
  public static final int MAX_SRV_VMEM_MB = 100 * 1024 * 1024; // 100 TB
  public static final int MIN_SRV_VMEM_MB = 1 * 1024; // 1 GB
  public static final int SRV_BW = 10 * 1024 * 1024; // 10 Gbps
  public static final float SRV_SLEEPABLE_PROB = 0.8f;

  // * VNF constants
  public static final int MAX_VNF_VCPU_NUM = 200;
  public static final int MIN_VNF_VCPU_NUM = 1;
  public static final int MAX_VNF_VMEM_MB = 10 * 1024 * 1024; // 10 TB
  public static final int MIN_VNF_VMEM_MB = 1; // 1 MB
  public static final int VM_BW = 1 * 1024 * 1024; // 1 Gbps
  public static final float VNF_MOVABLE_PROB = 0.8f;

  // * Asumption
  public static final int VCPU_MIPS = 1000;

  public static final String DEFAULT_API_SERVER = "http://localhost:8000";
}
