package gurobi;
import common.Instance;
import common.InstanceManager;
public class BPPSolver {
    private static Instance instance;
    
    public static void main(String[] args) {
        InstanceManager instanceManager = new InstanceManager();
        instance = instanceManager.readInstance("instances/instance5.bpp");
        
        try {
            GRBEnv env = new GRBEnv();
            GRBModel model = new GRBModel(env);
            model.set(GRB.DoubleParam.TimeLimit, 1800);

            // Creating bins variables
            GRBVar[] binsVariables = new GRBVar[instance.getQtyItems()];
            for(int i = 0; i < instance.getQtyItems(); i++) {
                binsVariables[i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "y" + Integer.toString(i));
            }
            
            // Creating assignment variables
            GRBVar[][] assignmentVariables = new GRBVar[instance.getQtyItems()][instance.getQtyItems()];
            for(int i = 0; i < instance.getQtyItems(); i++) {
                for(int j = 0; j < instance.getQtyItems(); j++) {
                    assignmentVariables[i][j] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x" + Integer.toString(i) + Integer.toString(j));
                }
            }
            
            // Add object function
            GRBLinExpr objectiveExpr = new GRBLinExpr();
            for(int i = 0; i < instance.getQtyItems(); i++) {
                objectiveExpr.addTerm(1, binsVariables[i]);
            }
            model.setObjective(objectiveExpr, GRB.MINIMIZE);
            
            // Add capacity restriction
            GRBLinExpr itemsCapacityExpr;
            GRBLinExpr capacityExpr;
            for(int i = 0; i < instance.getQtyItems(); i++) {
                itemsCapacityExpr = new GRBLinExpr();
                for(int j = 0; j < instance.getQtyItems(); j++) {
                    itemsCapacityExpr.addTerm(instance.getItems().get(j), assignmentVariables[i][j]);
                }
                capacityExpr = new GRBLinExpr();
                capacityExpr.addTerm(instance.getBinCapacity(), binsVariables[i]);
                model.addConstr(itemsCapacityExpr, GRB.LESS_EQUAL, capacityExpr, "c" + Integer.toString(i));
            }
            
            // Add do not duplicate item restriction
            GRBLinExpr avoidDuplicationExpr;
            for(int i = 0; i < instance.getQtyItems(); i++) {
                avoidDuplicationExpr = new GRBLinExpr();
                for(int j = 0; j < instance.getQtyItems(); j++) {
                    avoidDuplicationExpr.addTerm(1, assignmentVariables[j][i]);
                }
                model.addConstr(avoidDuplicationExpr, GRB.EQUAL, 1, "d" + Integer.toString(i));
            }
            
            model.optimize();
            
            System.out.println("Obj: " + model.get(GRB.DoubleAttr.ObjVal));
            // Dispose of model and environment
            model.dispose();
            env.dispose();

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        
    }
}
