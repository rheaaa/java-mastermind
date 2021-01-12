import java.util.*;

public class MindServer
{
    private static final String[] colors = {"purple", "green", "orange", "pink", "yellow", "blue"};
    private static ArrayList<Base6> untried = new ArrayList<Base6>();
    private static ArrayList<Base6> possible = new ArrayList<Base6>();
    private static ArrayList<Base6> guesses = new ArrayList<Base6>();
    private static int timeAround = 0;
    private static Scanner sc = new Scanner(System.in);
    private static boolean correct = false;

    public static void main(String[] args)
    {
        untried.add(new Base6("0000"));
        while(!untried.get(untried.size() - 1).toString().equals("5555"))
        {
            Base6 add = untried.get(untried.size() - 1).clone();
            add.add(1);
            untried.add(add);
        }
        for(int iter = 0; iter < 1296; iter++)
            possible.add(untried.get(iter));
        ask(new Base6("0011"));
        if(correct)
            return;
        do
        {
            eliminateImpossible(lastGuess());
            ArrayList<Integer> minElAll = getScores(untried);
            int overallMax = findMax(minElAll);
            ArrayList<Integer> minElPoss = getScores(possible);
            int possMax = findMax(minElPoss);
            int bestIndex;
            if(possible.size() == 0)
            {
                System.out.println("Error: Nothing possible");
                correct = true;
            }
            else if(possMax == overallMax || possible.size() <= 2)
            {
                bestIndex = findIn(overallMax, minElPoss);
                ask(possible.get(bestIndex));
            }
            else
            {
                bestIndex = findIn(overallMax, minElAll);
                ask(untried.get(bestIndex));
            }
        } while(!correct);
    }

    private static int findIn(int findThis, ArrayList<Integer> findItHere)
    {
        ListIterator<Integer> search = findItHere.listIterator();
        while(search.hasNext())
        {
            int currVal = search.next();
            if(findThis == currVal)
                return search.previousIndex();
        }
        return 0;
    }

    private static void ask(Base6 guess)
    {
        timeAround++;
        System.out.print("Guess " + timeAround + ": ");
        for(int count = 0; count < 4; count++)
            System.out.print(colors[intAt(count, guess.toString())]+" ");
        System.out.println("");
        System.out.println("Enter reds:");
        guess.setReds(sc.nextInt());
        if(guess.getReds() == 4)
        {
            correct = true;
            return;
        }
        System.out.println("Enter whites:");
        guess.setWhites(sc.nextInt());
        remove(guess, possible);
        remove(guess, untried);
        guesses.add(guess);
    }

    private static int intAt(int place, String num)
    {
        return Character.getNumericValue(num.charAt(place));
    }

    private static void remove(Base6 removee, ArrayList<Base6> remFrom)
    {
        int test = 0;
        while(!removee.equals(remFrom.get(test)))
        {
            test++;
            if(test >= remFrom.size())
                return;
        }
        remFrom.remove(test);
    }

    private static void eliminateImpossible(Base6 guess)
    {
        Iterator<Base6> itr = possible.iterator();
        while(itr.hasNext())
        {
            Base6 possibility = itr.next();
            if(!lastGuess().works(possibility, guesses))
            {
                itr.remove();
            }
        }
    }

    private static Base6 lastGuess()
    {
        return guesses.get(guesses.size() - 1);
    }

    private static ArrayList<Integer> getScores(ArrayList<Base6> arrl)
    {
        ArrayList<Integer> minimumEl = new ArrayList<Integer>();
        ListIterator<Base6> guess = arrl.listIterator();
        while(guess.hasNext())
        {
            Base6 i = guess.next();
            minimumEl.add(i.getMinimEl(guesses, possible));
        }
        return minimumEl;
    }

    private static int findMax(ArrayList<Integer> numbers)
    {
        ListIterator<Integer> check = numbers.listIterator();
        int max = 0;
        while(check.hasNext())
        {
            int v = check.next();
            if(v > max)
                max = v;
        }
        return max;
    }
}