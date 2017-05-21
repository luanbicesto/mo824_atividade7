package gurobi;
import common.Instance;
import common.InstanceManager;
public class BPPSolver {
    private static Instance instance;
    
    public static void main(String[] args) {
        InstanceManager instanceManager = new InstanceManager();
        instance = instanceManager.readInstance("instances/instance1.bpp");
    }
}
