import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * chenweijian
 * 2023.2.25
 */
public class Calculator {

    /**
     * 前一个输入值
     */
    private BigDecimal preNum;

    /**
     * 后输入的值
     */
    private BigDecimal nextNum;

    /**
     * 算法
     */
    private OperatorEnum operatorEnum;

    /**
     * 当前下标计数器
     */
    private int count = -1;

    /**
     * 游标
     */
    private int cursor = -1;

    /**
     * 数组存储过往的结果值
     */
    List<BigDecimal> preNumList = new ArrayList<>(10);

    /**
     * 算法map
     */
    private static Map<OperatorEnum, BasicAlgorithmService> basicAlgorithmServiceMap;

    {
        basicAlgorithmServiceMap = new HashMap<>(4);
        basicAlgorithmServiceMap.put(OperatorEnum.ADD, new AddService());
        basicAlgorithmServiceMap.put(OperatorEnum.SUBTRACTION, new SubtractService());
        basicAlgorithmServiceMap.put(OperatorEnum.MULTIPLY, new MultiplyService());
        basicAlgorithmServiceMap.put(OperatorEnum.DIVISION, new DivideService());
    }

    /**
     * 运算符枚举
     */
    public enum OperatorEnum {
        ADD("+"),
        SUBTRACTION("-"),
        MULTIPLY("*"),
        DIVISION("/");

        private String character;

        OperatorEnum(String character) {
            this.character = character;
        }
    }

    /**
     * 计算
     * @return
     */
    public BigDecimal arithmetic() {
        BigDecimal result;
        result = Optional.ofNullable(basicAlgorithmServiceMap.get(operatorEnum)).orElseThrow(() -> new RuntimeException("未实现的计算法则")).apply(this);
        System.out.println(preNum + " " + operatorEnum.character + " " + nextNum + " = " + result);

        preNumList.add(result);
        preNum = result;
        nextNum = null;
        cursor = ++count;
        return result;
    }

    /**
     * 返回上一次计算结果
     * @return
     */
    public void undo() {
        // 计算后重置游标
        if (preNumList.size() > 0 && count != -1) {
            cursor = count;
        }
        int index = --cursor;
        if (index < 0) {
            System.out.println("已经没有更前的计算结果了");
            return;
        }
        BigDecimal undo = preNumList.get(index);
        System.out.println("undo拿到的值是：" + undo);
    }

    /**
     * 获取往前一次计算结果
     * @return
     */
    public void redo() {
        int index = ++cursor;
        // 没进行计算 or 游标>数组下标
        if (preNumList.size() == 0 || index > count) {
            System.out.println("已经没有更后的计算结果了");
            return;
        }
        BigDecimal redo = preNumList.get(index);
        System.out.println("redo拿到的值是：" + redo);
    }

    /**
     * 算法
     */
    public interface BasicAlgorithmService {
        BigDecimal apply(Calculator calculator);
    }

    /**
     * 加法
     */
    public static class AddService implements BasicAlgorithmService {
        @Override
        public BigDecimal apply(Calculator calculator) {
            return calculator.preNum.add(calculator.nextNum);
        }
    }

    /**
     * 减法
     */
    public static class SubtractService implements BasicAlgorithmService {
        @Override
        public BigDecimal apply(Calculator calculator) {
            return calculator.preNum.subtract(calculator.nextNum);
        }
    }

    /**
     * 乘法
     */
    public static class MultiplyService implements BasicAlgorithmService {
        @Override
        public BigDecimal apply(Calculator calculator) {
            return calculator.preNum.multiply(calculator.nextNum);
        }
    }

    /**
     * 除法
     */
    public static class DivideService implements BasicAlgorithmService {
        @Override
        public BigDecimal apply(Calculator calculator) {
            return calculator.preNum.divide(calculator.nextNum, 10, RoundingMode.UP);
        }
    }

    public BigDecimal getPreNum() {
        return preNum;
    }

    public void setPreNum(BigDecimal preNum) {
        this.preNum = preNum;
    }

    public BigDecimal getNextNum() {
        return nextNum;
    }

    public void setNextNum(BigDecimal nextNum) {
        this.nextNum = nextNum;
    }

    public OperatorEnum getOperatorEnum() {
        return operatorEnum;
    }

    public void setOperatorEnum(OperatorEnum operatorEnum) {
        this.operatorEnum = operatorEnum;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCursor() {
        return cursor;
    }

    public void setCursor(int cursor) {
        this.cursor = cursor;
    }

    public List<BigDecimal> getPreNumList() {
        return preNumList;
    }

    public void setPreNumList(List<BigDecimal> preNumList) {
        this.preNumList = preNumList;
    }

    public static void main(String[] args) {

        // 计算器1
        Calculator calculator = new Calculator();
        calculator.undo();
        calculator.redo();

        calculator.setPreNum(new BigDecimal("0.5"));
        calculator.setOperatorEnum(OperatorEnum.ADD);
        calculator.setNextNum(new BigDecimal("2"));
        calculator.arithmetic();

        //计算器2
        Calculator calculator1 = new Calculator();
        calculator1.undo();
        calculator1.redo();

        calculator1.setPreNum(new BigDecimal("2"));
        calculator1.setOperatorEnum(OperatorEnum.SUBTRACTION);
        calculator1.setNextNum(new BigDecimal("4"));
        calculator1.arithmetic();

        calculator.setOperatorEnum(OperatorEnum.SUBTRACTION);
        calculator.setNextNum(new BigDecimal("2"));
        calculator.arithmetic();

        calculator.undo();
        calculator.redo();

        calculator.setOperatorEnum(OperatorEnum.MULTIPLY);
        calculator.setNextNum(new BigDecimal("1.8888888"));
        calculator.arithmetic();
        calculator.undo();

        calculator.setOperatorEnum(OperatorEnum.DIVISION);
        calculator.setNextNum(new BigDecimal("8.8"));
        calculator.arithmetic();

        calculator.redo();
    }

}
