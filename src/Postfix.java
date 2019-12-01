import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import java.util.Stack;

public class Postfix
{

    static final HashMap<String, Integer> prec;

    static
    {
        prec = new HashMap<>();
        prec.put("^", 3);
        prec.put("%", 2);
        prec.put("*", 2);
        prec.put("/", 2);
        prec.put("+", 1);
        prec.put("-", 1);
    }

    public static void main(String[] args)
    {

        Queue<String> infixQueue = new LinkedList<>(); 
        Scanner sc = new Scanner(System.in);
        Double number = 0.0;
        Character c, cNext = ' ';
        String input;
        String multiDigit = "";
        do
        {
            System.out.println("Insira sua expressão: ");
            input = sc.nextLine();
            input = input.replaceAll(" ", ""); 
            if (input.equals("sair"))
            {
                System.exit(0);
            }

            for (int i = 0; i < input.length(); i++)
            {
                c = input.charAt(i);
                if (i + 1 < input.length())
                {
                    cNext = input.charAt(i + 1);
                }

                if (c.equals('(') || c.equals(')'))
                {
                    if (c.equals('(') && cNext.equals('-'))
                    {
                        System.out.println("Não sei fazer com numero negativo :)");
                        main(args);
                    } else
                    {
                        infixQueue.add(c.toString());
                    }
                } else if (!Character.isDigit(c))
                {
                    if (infixQueue.isEmpty() && c.equals('-'))
                    {
                        System.out.println("Não sei fazer com numero negativo :)");
                        main(args);
                    } else if (cNext.equals('-'))
                    {
                        System.out.println("Não sei fazer com numero negativo :)");
                        main(args);
                    } else
                    {
                        infixQueue.add(c.toString());
                    }
                } else if (Character.isDigit(c))
                {
                    if (i + 1 < input.length() && input.charAt(i + 1) == '.') //to handle decimal
                    {
                        int j = i + 1;
                        multiDigit = c.toString() + input.charAt(j); 
                        while (j + 1 <= input.length() - 1 && Character.isDigit(input.charAt(j + 1)))
                        {
                            multiDigit = multiDigit + input.charAt(j + 1);
                            j++;
                        }
                        i = j;
                        infixQueue.add(multiDigit);
                        multiDigit = "";
                    } else if (i + 1 <= input.length() - 1 && Character.isDigit(input.charAt(i + 1)))
                    {
                        int j = i;
                
                        while (j <= input.length() - 1 && Character.isDigit(input.charAt(j)))
                        {
                            multiDigit = multiDigit + input.charAt(j);
                            j++;
                        }
                        i = j - 1;
                        infixQueue.add(multiDigit);
                        multiDigit = "";
                    } else
                    {
                        infixQueue.add(c.toString());
                    }

                }
            }

            infixToPostfix(infixQueue);
        } while (!input.equals("sair"));
    }

    public static void infixToPostfix(Queue<String> infixQueue)
    {
        Stack operatorStack = new Stack();
        Queue<String> postQueue = new LinkedList<>();
        String t;
        while (!infixQueue.isEmpty())
        {
            t = infixQueue.poll();
            try
            {
                double num = Double.parseDouble(t);
                postQueue.add(t);
            } catch (NumberFormatException nfe)
            {
                if (operatorStack.isEmpty())
                {
                    operatorStack.add(t);
                } else if (t.equals("("))
                {
                    operatorStack.add(t);
                } else if (t.equals(")"))
                {
                    while (!operatorStack.peek().toString().equals("("))
                    {
                        postQueue.add(operatorStack.peek().toString());
                        operatorStack.pop();
                    }
                    operatorStack.pop();
                } else
                {
                    while (!operatorStack.empty() && !operatorStack.peek().toString().equals("(") && prec.get(t) <= prec.get(operatorStack.peek().toString()))
                    {
                        postQueue.add(operatorStack.peek().toString());
                        operatorStack.pop();
                    }
                    operatorStack.push(t);
                }
            }
        }
        while (!operatorStack.empty())
        {
            postQueue.add(operatorStack.peek().toString());
            operatorStack.pop();
        }
        System.out.println();
        System.out.println("Sua expressão posfixa é: ");
        for (String val : postQueue)
        {
            System.out.print(val + " ");
        }
        postfixResolucao(postQueue);
    }

    public static void postfixResolucao(Queue<String> postQueue)
    {
        Stack<String> eval = new Stack<>(); 
        String t;
        Double headNumber, nextNumber, result = 0.0;
        while (!postQueue.isEmpty())
        {
            t = postQueue.poll();
            try
            {
                double num = Double.parseDouble(t);
                eval.add(t);
            } catch (NumberFormatException nfe)
            {
                headNumber = Double.parseDouble(eval.peek());
                eval.pop();
                nextNumber = Double.parseDouble(eval.peek());
                eval.pop();

                switch (t)
                {
                    case "+":
                        result = nextNumber + headNumber;
                        break;
                    case "-":
                        result = nextNumber - headNumber;
                        break;
                    case "*":
                        result = nextNumber * headNumber;
                        break;
                    case "/":
                        
                
                        if (headNumber == 0)
                        {
                            System.out.println("\nERROR: Não pode dividir por zero!\n");
                            return;
                        } else
                        {
                            result = nextNumber / headNumber;
                            break;
                        }
                    case "%":
                        result = nextNumber % headNumber;
                        break;
                    case "^":
                        result = Math.pow(nextNumber, headNumber);
                        break;

                }

                eval.push(result.toString());

            }

        }
        System.out.println("\nResultado é: ");
        DecimalFormat df = new DecimalFormat("0.000");
        for (String val : eval)
        {
            System.out.println(df.format(Double.parseDouble(val)) + "\n");
        }
    }

}