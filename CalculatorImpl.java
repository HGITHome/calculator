package calue;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;


public class CalculatorImpl extends AbstractCalculator{


    private BigDecimal preTotal;
    private BigDecimal newNum;
    private List<BigDecimal> lastNumList = new ArrayList<>();
    private List<String> lastOptList = new ArrayList<>();
    private List<BigDecimal> lastTotalList = new ArrayList<>();
    private String curOperator;
    private int lastOptIndex = -1;
    private int scale = 2;
    private int validIndexMax = -1;

    public void setNewNum(BigDecimal newNum) {
        if(preTotal == null){
            preTotal = newNum;
        }else{
            this.newNum = newNum;
        }
    }

    public void setCurOperator(String curOperator) {
        this.curOperator = curOperator;
    }
    @Override
    public void calc() throws Exception {
        preTotal = preTotal == null ? BigDecimal.ZERO : preTotal;
        if(curOperator == null){
            System.out.println("请选择操作!");
        }
        if(newNum != null) {
            BigDecimal ret = calcTwoNum(preTotal, curOperator, newNum);
            if(this.lastOptIndex == -1){
                lastTotalList.add(preTotal);
                lastNumList.add(newNum);
                lastOptList.add(curOperator);
            }else{
                this.lastOptIndex++;
                this.validIndexMax = this.lastOptIndex;
                this.lastTotalList.set(this.lastOptIndex, ret);
                this.lastNumList.set(this.lastOptIndex-1, newNum);
                this.lastOptList.set(this.lastOptIndex-1, curOperator);
            }
            preTotal = ret;
            curOperator = null;
            newNum = null;
        }
    }

    @Override
    public void undo(){
        if(preTotal != null && lastOptIndex == -1){
            lastTotalList.add(preTotal);
            curOperator = null;
            newNum = null;
        }

        if(lastTotalList.size() == 0){
            System.out.println("无操作!");
        }else if(lastTotalList.size() == 1){
            System.out.println("undo后值:0,"+"undo前值:"+preTotal);
            preTotal = BigDecimal.ZERO;
        } else {
            if(lastOptIndex == -1){
                lastOptIndex = lastOptList.size()-1;
            }else{
                if(lastOptIndex-1 < 0){
                    System.out.println("无法再undo!");
                    return;
                }
                lastOptIndex--;
            }
            this.undoOperatePrint(lastTotalList.get(lastOptIndex),this.preTotal,lastOptList.get(lastOptIndex), lastNumList.get(lastOptIndex));
        }
    }

    @Override
    public void redo(){
        try{
            if(lastOptIndex > -1){
                if(lastOptIndex + 1 == lastTotalList.size() || lastOptIndex + 1 == this.validIndexMax+1){
                    System.out.println("无法再redo!");
                    return;
                }
                lastOptIndex++;
                redoOperatePrint(lastTotalList.get(lastOptIndex),this.preTotal,lastOptList.get(lastOptIndex-1), lastNumList.get(lastOptIndex-1));
            }
        }catch (Exception e){
            System.out.println("redo异常,lastOptIndex:"+lastOptIndex);
        }
    }

    @Override
    public BigDecimal calcTwoNum(BigDecimal preTotal, String curOperator, BigDecimal newNum) throws Exception {
        BigDecimal ret = BigDecimal.ZERO;
        curOperator = curOperator == null ? "+" : curOperator;
        switch (curOperator){
            case "+":
                ret = preTotal.add(newNum);
                break;
            case "-":
                ret = preTotal.subtract(newNum).setScale(scale, RoundingMode.HALF_UP);
                break;
            case "*":
                ret = preTotal.multiply(newNum).setScale(scale, RoundingMode.HALF_UP);
                break;
            case "/":
                if (newNum.compareTo(BigDecimal.ZERO) == 0) {
                    throw new Exception("分播不能为0");
                }
                ret = preTotal.divide(newNum, RoundingMode.HALF_UP);
                break;
        }
        return ret;
    }

    public static void main(String[] args) {
        try {
            CalculatorImpl calculator = new CalculatorImpl();
            calculator.setNewNum(new BigDecimal(10));
            calculator.setCurOperator("+");
            calculator.setNewNum(new BigDecimal(15));
            calculator.display(calculator.preTotal, calculator.curOperator, calculator.newNum, calculator.scale);
            calculator.calc();
            calculator.display(calculator.preTotal, calculator.curOperator, calculator.newNum, calculator.scale);
            calculator.setCurOperator("*");
            calculator.setNewNum(new BigDecimal(13));
            calculator.display(calculator.preTotal, calculator.curOperator, calculator.newNum, calculator.scale);
            calculator.calc();
            calculator.display(calculator.preTotal, calculator.curOperator, calculator.newNum, calculator.scale);
            calculator.undo();
            calculator.redo();
            calculator.display(calculator.preTotal, calculator.curOperator, calculator.newNum, calculator.scale);
            calculator.undo();
        } catch (Exception e) {
            System.out.println("计算失败:" + e.getMessage());
        }
    }
}
