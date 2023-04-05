import java.util.ArrayList;
import java.util.Scanner;

public class Calculator {

    public static void main(String[] args) {
        // Main Function that Calls Calculate function and prints answer
        Scanner myScan = new Scanner(System.in);
        String inputs = myScan.nextLine();
        float answer = calculate(inputs);
        System.out.println(answer);
    }

    public static float calculate(String inputs) {
//Initializing variables and arrays
        int counter = 0;
        String[] operators = {"^", "/", "*", "+", "-"};
        ArrayList<Float> nums = new ArrayList<>();
        ArrayList<String> ops = new ArrayList<>();
        ArrayList<Float> bracket = new ArrayList<>();
        float currentNum;
        int point = 0;
        String input = "";
        for (int p = 0; p < inputs.length() - 4; p++) { // This algorithm replaces occurrences of "sqrt" with "^0.5 so that the logic can be handled
            if (inputs.startsWith("sqrt", p)) {
                int start = p + 4; //beginning of number to be rooted by a square
                int end = start + 1; // end on number to be square rooted
                while (end < inputs.length() && Character.isDigit(inputs.charAt(end))) { // searching for the full number after the sqrt
                    end++;
                }
                input += inputs.substring(point, p) + inputs.substring(start, end) + "^0.5";
                point = end;
            }
        }
        input += inputs.substring(point); //Add remaining part of input string
        for (int i = 0; i < input.length(); i++) { //Bracket code that doesn't work.
            if (input.charAt(i) == '(') {
                int bracketCounter = 1;
                int j = i + 1;
                while (j < input.length() && bracketCounter > 0) {
                    if (input.charAt(j) == '(') {
                        bracketCounter++;
                    } else if (input.charAt(j) == ')') {
                        bracketCounter--;
                    }
                    j++;
                }
                bracket.add(calculate(input.substring(i + 1, j - 1)));
                i = j - 1;
            }
        }

        point = 0;
        //Loop to extract numbers and operators from input string
        for (int i = 0; i < input.length(); i++) {
            if (!Character.isDigit(input.charAt(i)) && i != input.length() - 1) { //Check if character is an operator
                try {
                    if (input.charAt(i) == '.') { //searches for a decimal number
                        int j = i;
                        while (Character.isDigit(input.charAt(j + 1))) {
                            j++;
                            counter++;
                        }
                    } else {
                        currentNum = Float.parseFloat(input.substring(point, i)); //adds digits before operator to number arraylist
                        point = i + 1;
                        nums.add(currentNum);
                    }
                    for (String op : operators) { // adds operators to ops array list
                        if (input.startsWith(op, i)) {
                            ops.add(op);
                        }
                    }
                } catch (Exception ignored) {
                }
            } else if (i == input.length() - 1) { // if its end of string add everything from the last operator to number arraylist
                try {
                    currentNum = Float.parseFloat(input.substring(point));
                    nums.add(currentNum);
                } catch (Exception e) {
                    System.out.println("Error" + input.substring(point));
                }
            }
        }
        //System.out.println(ops);
        nums.add(Float.parseFloat(input.substring(point)));
        //bedmas is done using a nested for loop, comparing the operators in the input string to the operators in the operators array.
        // The code uses pointers to calculate and remove values until the last value left in the array is the answer and then returns the first value in the array
        for (String operator : operators) { // if the current operator is of the current precedence, calculate the result
            for (int j = 0; j < ops.size(); j++) {
                String op = ops.get(j);
                if (op.equals(operator)) { // apply the operation based on the operator symbol
                    float num1 = nums.get(j);
                    float num2 = nums.get(j + 1);
                    float result = switch (op) {
                        case "^" -> (float) Math.pow(num1, num2);
                        case "/" -> num1 / num2;
                        case "*" -> num1 * num2;
                        case "+" -> num1 + num2;
                        case "-" -> num1 - num2;
                        default -> 0;
                    };
                    // update the numbers and operators lists to remove the calculated values and add the result
                    nums.set(j, result);
                    nums.remove(j + 1);
                    ops.remove(j);
                    j--;
                }
            }
        }
        /* More Bracket code that doesn't work.
        if (bracket.size() > 0) {
            int bracketIndex = 0;
            for (int i = 0; i < ops.size(); i++) {
                if (ops.get(i).equals("(")) {
                    nums.set(i, bracket.get(bracketIndex));
                    bracketIndex++;
                }
            }
            while (ops.contains("(")) {
                int index = ops.indexOf("(");
                ops.remove(index);
                nums.remove(index + 1);
            }
            bracket.clear();
        }

         */
        return nums.get(0);
    }
}
