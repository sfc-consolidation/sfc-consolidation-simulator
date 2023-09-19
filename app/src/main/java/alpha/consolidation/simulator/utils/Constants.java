package alpha.consolidation.simulator.utils;

public class Constants {
  public static final int MAX_SRV_NUM = 100;
  public static final int MAX_VNF_NUM = 200;
  public static final int MAX_SFC_NUM = 100;
  public static final int MAX_EPISODE_LEN = 100;
  public static final int MAX_SRV_VCPU_NUM = 1000;
  public static final int MIN_SRV_VCPU_NUM = 1;
  public static final int MAX_SRV_VMEM_MB = 1000 * 1024; // MB
  public static final int MIN_SRV_VMEM_MB = 1 * 1024; // MB
  public static final int MAX_VNF_VCPU_NUM = 10;
  public static final int MIN_VNF_VCPU_NUM = 1;
  public static final int MAX_VNF_VMEM_MB = 10 * 1024; // MB
  public static final int MIN_VNF_VMEM_MB = (int) 0.5f * 1024; // MB
  public static final float VNF_MOVABLE_PROB = 0.8f;
  public static final float SRV_SLEEPABLE_PROB = 0.8f;

  public static final int HOST_BW = 1000 * 1024 * 1024; // 1 Gbps (TODO: check 필요)
  public static final int HOST_STORAGE = 1000 * 1024 * 1024; // 1 GB (TODO: check 필요)
  public static final int HOST_MIPS = 1000; // 1 GHz (TODO: check 필요)
  public static final int VM_MIPS = 1000; // 1 GHz (TODO: check 필요)
  public static final int HOST_MAX_POWER = 1000; // 1 kW (TODO: check 필요)
  public static final int HOST_IDLE_POWER = 100; // 0.1 kW (TODO: check 필요)
  public static final int HOST_SLEEP_POWER = 20; // 0.02 kW (TODO: check 필요) <-- 사용하지 않고 있는데 이것도 넣고 싶긴함.
}
