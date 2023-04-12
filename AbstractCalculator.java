package calue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public abstract class AbstractCalculator implements CalculatorService{



    public void redoOperatePrint(BigDecimal redoTotal, BigDecimal preTotal,String redoOpt, BigDecimal redoNum) {
        System.out.println("redo后值:"+redoTotal+",redo前值:"+preTotal+",redo的操作:"+redoOpt+",redo操作的值:"+redoNum);

    }

    public void undoOperatePrint(BigDecimal lastTotal,BigDecimal preTotal, String lastOpt, BigDecimal lastNum) {
        System.out.println("undo后值:"+lastTotal+",undo前值:"+preTotal+",undo的操作:"+lastOpt+",undo操作的值:"+lastNum);

    }

    public String display(BigDecimal preTotal, String curOperator, BigDecimal newNum, int scale){
        StringBuilder sb = new StringBuilder();
        if(preTotal != null){
            sb.append(preTotal.setScale(scale, BigDecimal.ROUND_HALF_DOWN).toString());
        }
        if(curOperator != null){
            sb.append(curOperator);
        }
        if(newNum != null){
            sb.append(newNum);
        }
        System.out.println(sb);
        return sb.toString();
    }
}
