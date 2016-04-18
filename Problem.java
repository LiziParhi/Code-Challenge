
/**
 *  Input:
 >
 > i. Set of integers [A, B, C, ..., D], the size of the set will be from 2 to 5 integers. For example [5, 5, 5, 1]
 >
 > ii. Target number: one integer, for example 24
 >
 > b. Output: arithmetical expression, that can use four arithmetic operands '+', '-', '*', '/' , any number of nested parenthesis and uses each integer from the input set exactly once. The result of the expression should be equal to the target number. If no such expression exists, then output will be 'none'
 >
 > c. Samples of inputs and outputs:
 >
 > i. [1, 2, 3, 4], target = 3, output: (2 + 4) * 1 – 3 = 3
 >
 > ii. [3, 3, 2, 8], target = 2, output: (3 – 3) * 8 + 2 = 2
 >
 > iii. [2, 1, 7], target = 12, output: 2 * (7 – 1) = 12
 >
 > iv. [3, 5, 5, 3], target = 1, output: (5 - (5 – 3)) / 3 = 1
 */

package Arithematics;

import java.util.*;

public class Program {
    
    private static void SolveAndPrint(int[] numbers, int targetValue) {
        int targetKey = (targetValue << numbers.length) + (1 << numbers.length) - 1;
        
        HashSet<Integer> solvedKeys = new HashSet<Integer>();
        
        HashMap<Integer, Integer> keyToLeftParent = new HashMap<Integer, Integer>();
        
        HashMap<Integer, Integer> keyToRightParent = new HashMap<Integer, Integer>();
        
        HashMap<Integer, Character> keyToOperator = new HashMap<Integer, Character>();
        
        LinkedList<Integer> queue = new LinkedList<Integer>();
        
        for (int i = 0; i < numbers.length; i++) {
            
            int key = (numbers[i] << numbers.length) + (1 << i);
            
            solvedKeys.add(key);
            queue.offer(key);
            
        }
        
        while (queue.size() > 0 && !solvedKeys.contains(targetKey)) {
            
            int curKey = queue.poll();
            
            int curMask = curKey & ((1 << numbers.length) - 1);
            int curValue = curKey >> numbers.length;
            
            int[] keys = new int[solvedKeys.size()];
            
            int j = 0;
            for (Integer val : solvedKeys) {
                keys[j++] = val;
            }
            
            for (int i = 0; i < keys.length; i++) {
                
                int mask = keys[i] & ((1 << numbers.length) - 1);
                int value = keys[i] >> numbers.length;
                
                if ((mask & curMask) == 0) {
                    for (int op = 0; op < 6; op++) {
                        
                        char opSign = '\0';
                        int newValue = 0;
                        
                        switch (op) {
                            case 0: // Addition
                                newValue = curValue + value;
                                opSign = '+';
                                break;
                            case 1: // Subtraction - another value subtracted from current
                                newValue = curValue - value;
                                opSign = '-';
                                break;
                            case 2: // Subtraction - current value subtracted from another
                                newValue = value - curValue;
                                opSign = '-';
                                break;
                            case 3: // Multiplication
                                newValue = curValue * value;
                                opSign = '*';
                                break;
                            case 4: // Division - current divided by another
                                newValue = -1; // Indicates failure to divide
                                if (value != 0 && curValue % value == 0) {
                                    newValue = curValue / value;
                                }
                                opSign = '/';
                                break;
                            case 5:
                                newValue = -1;
                                if (curValue != 0 && value % curValue == 0) {
                                    newValue = value / curValue;
                                }
                                opSign = '/';
                                break;
                        }
                        
                        if (newValue >= 0) {
                            int newMask = (curMask | mask);
                            int newKey = (newValue << numbers.length) + newMask;
                            
                            if (!solvedKeys.contains(newKey)) {
                                solvedKeys.add(newKey);
                                
                                if (op == 2 || op == 5) {
                                    keyToLeftParent.put(newKey, keys[i]);
                                    
                                    keyToRightParent.put(newKey, curKey);
                                } else {
                                    keyToLeftParent.put(newKey, curKey);
                                    
                                    keyToRightParent.put(newKey, keys[i]);
                                }
                                
                                keyToOperator.put(newKey, opSign);
                                
                                solvedKeys.add(newKey);
                                queue.offer(newKey);
                            }
                        }
                    }
                }
            }
        }
        
        if (!solvedKeys.contains(targetKey)) {
            System.out.print("none");
        } else {
            PrintExpression(keyToLeftParent, keyToRightParent, keyToOperator, targetKey, numbers.length);
            
        }
    }
    
    static void PrintExpression(HashMap<Integer, Integer> keyToLeftParent, HashMap<Integer, Integer> keyToRightParent, HashMap<Integer, Character> keyToOperator, int key, int numbersCount) {
        if (!keyToOperator.containsKey(key)) {
            System.out.print(key >> numbersCount);
        } else {
            System.out.print("(");
            PrintExpression(keyToLeftParent, keyToRightParent, keyToOperator, keyToLeftParent.get(key), numbersCount);
            
            System.out.print(keyToOperator.get(key));
            PrintExpression(keyToLeftParent, keyToRightParent, keyToOperator, keyToRightParent.get(key), numbersCount);
            
            System.out.print(")");
        }
    }
    
    public static void main(String[] args) {
        
        Scanner s = new Scanner(System.in);
        
        System.out.println("enter number of elements ");
        
        int n = s.nextInt();
        if(n<2){
            System.out.print("Minimum size of array is 2 . Run the program again and enter atleast 2 array elements");
            
            System.exit(0);
        }
        else if(n>5){
            System.out.print("Maximum size of array is 5 . Run the program again and enter atmost 5 array elements");
            System.exit(0);
        }
        else{
            
            int arr[] = new int[n];
            
            System.out.println("enter elements");
            
            for (int i = 0; i < n; i++) {
                arr[i] = s.nextInt();
                
            }
            Scanner s1 = new Scanner(System.in);
            
            System.out.println("enter target");
            
            int value = s1.nextInt();
            
            SolveAndPrint(arr, value);
            
        }
    }
}
