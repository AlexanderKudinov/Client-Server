import java.io.UnsupportedEncodingException;
import java.util.Stack;

public class Calculator {

    public static class ExceptionZero extends Exception {
        public String getError() {
            String exception = "";
            try {
                exception = new String("Деление на 0!".getBytes("Cp1251"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return exception;
        }
    }

    public static class ExceptionLessNumbers extends Exception {
        public String getError() {
            String exception = "";
            try {
                exception = new String("Не хватает чисел для совершения операции!".getBytes("Cp1251"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            return exception;
        }
    }

    // структура, содержащая оператор и его приоритет
    private static class Operation {
        char name;
        int priority;
        public Operation(char name, int priority) {
            this.name = name;
            this.priority = priority;
        }
    }

    public static String calculate(String input) {
        //приоритеты: 10) (, )
        //            1) ^
        //            2) *, /
        //            3) +, -
        Stack<Double> numbers = new Stack<>();  // стэк чисел
        Stack<Operation> symbols = new Stack<>();  // стэк операторов
        StringBuilder temp = new StringBuilder();
        double tempNumber = 0;
        for (int i = 0; i < input.length(); ++i) {
            char symbInStr = input.charAt(i);
            // если это цифра или "." или "-", относящийся к числу
            if (Character.isDigit(symbInStr) || symbInStr == '.' || (temp.toString().equals("") && symbInStr == '-')) {
                temp.append(symbInStr);
            }
            // если это символ или число, последнее в строке
            if ((!Character.isDigit(symbInStr) || i == input.length()-1) && !(temp.toString().equals("") && symbInStr == '-') && symbInStr != '.')  {
                if (!temp.toString().equals(""))
                    numbers.push(Double.valueOf(temp.toString()));
                //System.out.println(temp);
                temp = new StringBuilder();
                int priority = getPriority(symbInStr);
                // если открывающая скобка, помещаем в стэк
                if (symbInStr == '(') {
                    symbols.push(new Operation(symbInStr, priority));
                }
                // если закрывающая строка, вычисляем выражение в стэке после открывающей скобки до конца
                else if (symbInStr == ')') {
                    //вычисление операций в скобках
                    while (symbols.peek().name != '(') {
                        try {
                            //выполнение единичной операции (в случае неудачи - выход)
                            doOperation(symbols, numbers, tempNumber);
                        } catch (ExceptionLessNumbers e) {
                            return e.getError();
                        } catch (ExceptionZero e) {
                            return e.getError();
                        }
                    }

                    symbols.pop(); //удаление '('
                }
                //если символов в стэке нет или приоритет прошлого символа выше этого и это не последний эелемент, вставляем его
                else if ((symbols.empty() || symbols.peek().priority > priority) && i != input.length()-1) {
                    symbols.push(new Operation(symbInStr, priority));
                }
                //иначе считаем все предыдущие действие, приоритет которых ниже (в реальности выше)
                else if (i != input.length()-1) {
                    while(!symbols.empty() && symbols.peek().priority <= priority) {
                        try {
                            doOperation(symbols, numbers, tempNumber); //выполнение единичной операции (в случае неудачи - выход)
                        } catch (ExceptionLessNumbers e) {
                            return e.getError();
                        } catch (ExceptionZero e) {
                            return e.getError();
                        }
                    }

                    symbols.push(new Operation(symbInStr, priority)); //вставка нового символа
                }
            }
        }

        tempNumber = 0;
        //если ещё остались действия
        while (!symbols.empty()) {
            try {
                doOperation(symbols, numbers, tempNumber); //выполнение единичной операции (в случае неудачи - выход)
            } catch (ExceptionLessNumbers e) {
                return e.getError();
            } catch (ExceptionZero e) {
                return e.getError();
            }
        }


        Double answer = numbers.pop();
        // если нет чисел после запятой возвращаем не как double, а как Long
        if (answer - Math.round(answer) == 0)
            return Long.toString(Math.round(answer));
        return answer.toString();
    }

    // 1 действие
    private static double calc(double numb1, char symb, double numb2) throws ExceptionZero {
        ExceptionZero exceptionZero = new ExceptionZero();
        switch (symb) {
            case '+':
                return  numb1 + numb2;
            case '-':
                return numb1 - numb2;
            case '*':
                return numb1 * numb2;
            case '/':
                if (numb2 == 0) throw exceptionZero;
                return numb1 / numb2;
            case '^':
                return Math.pow(numb1, numb2);
        }
        return -1;
    }

    // получение приоритета оператора
    private static int getPriority(char symb) {
        switch (symb) {
            case '+':
            case '-':
                return 3;
            case '*':
            case '/':
                return 2;
            case '^':
                return 1;
            case '(':
            case ')':
                return 10;
        }
        return -1;
    }


    private static boolean doOperation(Stack<Operation> symbols, Stack<Double> numbers, double tempNumber)
                                        throws ExceptionLessNumbers, ExceptionZero {
        ExceptionLessNumbers exceptionLessNumbers = new ExceptionLessNumbers();
        if (numbers.size() < 2) throw exceptionLessNumbers;
        double numb2 = numbers.pop();
        double numb1 = numbers.pop();
        tempNumber = calc(numb1, symbols.pop().name, numb2);
        numbers.push(tempNumber);
        return true;
    }
}