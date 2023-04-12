package calue;

import java.math.BigDecimal;

public interface CalculatorService {

     void calc() throws Exception ;
     void undo();
     void redo();
     BigDecimal calcTwoNum(BigDecimal preTotal, String curOperator, BigDecimal newNum) throws Exception ;

}
