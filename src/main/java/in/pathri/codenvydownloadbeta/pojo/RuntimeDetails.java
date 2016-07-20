package in.pathri.codenvydownloadbeta.pojo;

import java.util.List;

public class RuntimeDetails {
  private List<MachineDetails> machines;
  
  public List<MachineDetails> getMachies(){
    return machines;
  }

public MachineDetails getMachine(int i) {
	return machines.get(i);
}
}
