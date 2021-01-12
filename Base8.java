import java.util.*;

public class Base8
{
    private int[] digits = new int[5];
    private String[] possFeedback = {"00", "01", "02", "03", "04", "05", "10", "11", "12", "13", "14", "20", "21", "22", "23", "30", "31", "32", "40", "50"};
    private int reds = 0;
    private int whites = 0;

    public Base8(String number)
    {
        for(int z = 0; z < digits.length; z++)
            digits[z] = intAt(z, number);
    }

    public Base8(Base8 copy, int r, int w)
    {
        digits = copy.getDigits();
        reds = r;
        whites = w;
    }

    private int intAt(int place, String num)
    {
        return Character.getNumericValue(num.charAt(place));
    }

    public String toString()
    {
        String ret = "";
        for(int countDown = 0; countDown < digits.length; countDown++)
        {
            ret += digits[countDown];
        }
        return ret;
    }

    public boolean equals(Base8 other)
    {
        return Arrays.equals(digits, other.getDigits());
    }

    public Base8 clone()
    {
        Base8 ret = new Base8(this.toString());
        return ret;
    }

    public void add(int val)
    {
        if(val == 0)
            return;
        int highestPow = 0;
        while(true)
        {
            if(val < Math.pow(8, highestPow))
            {
                highestPow--;
                break;
            }
            else
                highestPow++;
        }
        int topVal = 0;
        int digit = 0;
        while(topVal <= val)
        {
            digit++;
            topVal = (int)(Math.pow(8, highestPow)*digit);
        }
        digit--;
        addToPlace(digit, highestPow);
        val -= Math.pow(8, highestPow)*digit;
        add(val);
    }

    public void addToPlace(int val, int power)
    {
        if(digits[4 - power] + val > 7)
        {
            digits[4 - power] = digits[4 - power] + val - 8;
            power++;
            addToPlace(1, power);
        }
        else
        {
            digits[4 - power] = digits[4 - power] + val;
        }
    }

    public void setReds(int r)
    {
        reds = r;
    }

    public void setWhites(int w)
    {
        whites = w;
    }

    public int getReds()
    {
        return reds;
    }

    public int getWhites()
    {
        return whites;
    }

    public int[] getDigits()
    {
        return digits;
    }

    private boolean worksWith(Base8 other)
    {
        //returns true if other is a possible previous line given that this is the actual solution
        //so determines what red and white peg counts would be if this were the solution then compares that to what they actually were
        int[] testerCopyThis = digits.clone();
        int[] testerCopyOther = other.getDigits().clone();
        int sampleReds = 0;
        int sampleWhites = 0;
        for(int corrector = 0; corrector < testerCopyOther.length; corrector++)
        {
            if (testerCopyOther[corrector] == testerCopyThis[corrector])
            {
                sampleReds++;
                testerCopyThis[corrector] = (int)(10000000 * Math.random());
                testerCopyOther[corrector] = (int)(10000 * Math.random());
            }
        }
        for(int corrector = 0; corrector < testerCopyOther.length; corrector++)
        {
            for(int whiteCorr = 0; whiteCorr < testerCopyThis.length; whiteCorr++)
            {
                if(testerCopyOther[corrector] == testerCopyThis[whiteCorr])
                {
                    sampleWhites++;
                    testerCopyThis[whiteCorr] = (int)(100 * Math.random());
                    testerCopyOther[corrector] = (int)(100000000 * Math.random());
                    break;
                }
            }
        }

        if(sampleReds == other.getReds() && sampleWhites == other.getWhites())
            return true;
        else
            return false;
    }

    public boolean works(Base8 tester, ArrayList<Base8> prevs)
    {
        ListIterator<Base8> prev = prevs.listIterator();
        while(prev.hasNext())
        {
            Base8 guess = prev.next();
            if(!tester.worksWith(guess))
                return false;
        }
        return true;
    }

    public int getMinimEl(ArrayList<Base8> context, ArrayList<Base8> eliminateFrom)
    {
        int min = eliminateFrom.size();
        int eliminated = 0;
        for(String sampleFeedback : getPossFeedback())
        {
            int sampleReds = intAt(0, sampleFeedback);
            int sampleWhites = intAt(1, sampleFeedback);
            Base8 sample = new Base8(this, sampleReds, sampleWhites);
            context.add(sample);
            ListIterator<Base8> i = eliminateFrom.listIterator();
            while(i.hasNext())
            {
                Base8 possibility = i.next();
                if(!works(possibility, context))
                    eliminated++;
            }
            if(min > eliminated)
                min = eliminated;
            eliminated = 0;
            context.remove(context.size() - 1);
        }
        return min;
    }

    private String[] getPossFeedback()
    {
        return possFeedback;
    }
}