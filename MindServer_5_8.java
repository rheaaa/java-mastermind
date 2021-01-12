import java.util.*;

public class MindServer_5_8
{
    private static final String[] colors = {"pink", "green", "purple", "white", "black", "orange", "yellow", "blue"};
    private static ArrayList<Base8> untried = new ArrayList<Base8>();
    private static ArrayList<Base8> possible = new ArrayList<Base8>();
    private static ArrayList<Base8> guesses = new ArrayList<Base8>();
    private static int timeAround = 0;
    private static Scanner sc = new Scanner(System.in);
    private static boolean correct = false;

    public static void main(String[] args)
    {
        untried.add(new Base8("00000"));
        while(!untried.get(untried.size() - 1).toString().equals("77777"))
        {
            Base8 add = untried.get(untried.size() - 1).clone();
            add.add(1);
            untried.add(add);
        }
        for(int iter = 0; iter < untried.size(); iter++)
            possible.add(untried.get(iter));
        ask(new Base8("01234"));
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
            if(possMax == overallMax || possible.size() <= 2)
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

    private static void ask(Base8 guess)
    {
        timeAround++;
        System.out.print("Guess " + timeAround + ": ");
        for(int count = 0; count < guess.getDigits().length; count++)
            System.out.print(colors[intAt(count, guess.toString())]+" ");
        System.out.println("");
        System.out.println("Enter reds:");
        guess.setReds(sc.nextInt());
        if(guess.getReds() == guess.getDigits().length)
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

    private static void remove(Base8 removee, ArrayList<Base8> remFrom)
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

    private static void eliminateImpossible(Base8 guess)
    {
        Iterator<Base8> itr = possible.iterator();
        while(itr.hasNext())
        {
            Base8 possibility = itr.next();
            if(!lastGuess().works(possibility, guesses))
            {
                itr.remove();
            }
        }
    }

    private static Base8 lastGuess()
    {
        return guesses.get(guesses.size() - 1);
    }

    private static ArrayList<Integer> getScores(ArrayList<Base8> arrl)
    {
        ArrayList<Integer> minimumEl = new ArrayList<Integer>();
        ListIterator<Base8> guess = arrl.listIterator();
        while(guess.hasNext())
        {
            Base8 i = guess.next();
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