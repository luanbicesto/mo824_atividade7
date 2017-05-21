package gurobi;
import java.io.IOException;
import java.util.List;

import common.Instance;
import common.InstanceManager;
public class BPPSolver2 {
  private static int C;  //tamanho do bin
  private static int _nItens;  //qtd de itens
  private static List<Integer> peso;  //vetor de peso dos itens
  

  public static void main(String[] args) {

    //int n=3;
    /*Double peso[] = new Double[n];
    peso[0] = 1000.0;
    peso[1] = 1000.0;
    peso[2] = 1000.0;
    */
    
    try {
        readInput("instances/instance4.bpp");
        
        GRBEnv env = new GRBEnv();
        GRBModel model = new GRBModel(env);
        model.set(GRB.DoubleParam.TimeLimit, 1800);//Limite de tempo de execução de 30 minutos
        
        //variáveis x_ij binárias para dizer se o item j está ou não no bin i.
        GRBVar[][] x = new GRBVar[_nItens][_nItens];
        for (int i = 0; i < _nItens; i++) {
            for (int j = 0; j < _nItens; j++) {
                x[i][j] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "x_"+i+"_"+j);
            }
            
        }
        
        // Minimizar a quantidade de y's (bins)
        GRBVar[] y = new GRBVar[_nItens];
        for (int i = 0; i < _nItens; i++) {
            y[i] = model.addVar(0.0, 1.0, 0.0, GRB.BINARY, "y"+i);
        }
                
        GRBLinExpr objetivo =new GRBLinExpr();
        for (int i = 0; i < _nItens; i++) {
            objetivo.addTerm(1, y[i]);
        }
        
        model.setObjective(objetivo, GRB.MINIMIZE);
        
        
       //retrição que garante que a soma dos pesos dos itens do bin não ultrapasse sua capacidade.
        for (int i = 0; i < _nItens; i++) {
            GRBLinExpr pesoSomatorio = new GRBLinExpr();
            GRBLinExpr capacidadeBin = new GRBLinExpr();
            for (int j = 0; j < _nItens; j++) {
                 pesoSomatorio.addTerm(peso.get(j), x[i][j]);
            }
            capacidadeBin.addTerm(C, y[i]);
            model.addConstr(pesoSomatorio, GRB.LESS_EQUAL, capacidadeBin, "c"+i);
        }
        //Restrição que melhora o desempenho do Gurobi
        for (int i = 0; i < _nItens-1; i++) {
            model.addConstr(y[i], GRB.GREATER_EQUAL, y[i+1], "binMaior"+i);
        }
        
        
        // restrição que garante que um item j só pode estar em um bin i.
        for (int i = 0; i < _nItens; i++) {
            GRBLinExpr itemBinUnico = new GRBLinExpr();
            for (int j = 0; j < _nItens; j++) {
                itemBinUnico.addTerm(1.0, x[j][i]);
            }
            model.addConstr(itemBinUnico, GRB.EQUAL, 1, "unico"+i);
        }
        
       
        //model.write("teste.lp");
       
        // Optimize model        
        model.optimize();

        System.out.println("Obj: " + model.get(GRB.DoubleAttr.ObjVal) + " bins");        

        // Dispose of model and environment
        model.dispose();
        env.dispose();
    }catch(GRBException | IOException ex) {
        ex.printStackTrace();
    }

  }
  
  protected static void readInput(String filename) throws IOException {

      InstanceManager instanceManager = new InstanceManager();
      Instance instance = instanceManager.readInstance(filename);
      
      C = instance.getBinCapacity();
      _nItens = instance.getQtyItems();
      peso = instance.getItems();
    }
}