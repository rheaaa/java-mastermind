import java.util.*;

public class Base6
{
    private int[] digits = new int[4];
    private String[] possFeedback = {"00", "01", "02", "03", "04", "10", "11", "12", "13", "20", "21", "22", "30", "40"};
    private int reds = 0;
    private int whites = 0;

    public Base6(String number)
    {
        for(int z = 0; z < 4; z++)
            digits[z] = intAt(z, number);
    }

    public Base6(Base6 copy, int r, int w)
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

    public boolean equals(Base6 other)
    {
        return Arrays.equals(digits, other.getDigits());
    }

    public Base6 clone()
    {
        Base6 ret = new Base6(this.toString());
        return ret;
    }

    public void add(int val)
    {
        if(val == 0)
            return;
        int highestPow = 0;
        while(true)
        {
            if(val < Math.pow(6, highestPow))
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
            topVal = (int)(Math.pow(6, highestPow)*digit);
        }
        digit--;
        addToPlace(digit, highestPow);
        val -= Math.pow(6, highestPow)*digit;
        add(val);
    }

    public void addToPlace(int val, int power)
    {
        if(digits[3 - power] + val > 5)
        {
            digits[3 - power] = digits[3 - power] + val - 6;
            power++;
            addToPlace(1, power);
        }
        else
        {
            digits[3 - power] = digits[3 - power] + val;
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

    private boolean worksWith(Base6 other)
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

    public boolean works(Base6 tester, ArrayList<Base6> prevs)
    {
        ListIterator<Base6> prev = prevs.listIterator();
        while(prev.hasNext())
        {
            Base6 guess = prev.next();
            if(!tester.worksWith(guess))
                return false;
        }
        return true;
    }

    public int getMinimEl(ArrayList<Base6> context, ArrayList<Base6> eliminateFrom)
    {
        int min = eliminateFrom.size();
        int eliminated = 0;
        for(String sampleFeedback : getPossFeedback())
        {
            int sampleReds = intAt(0, sampleFeedback);
            int sampleWhites = intAt(1, sampleFeedback);
            Base6 sample = new Base6(this, sampleReds, sampleWhites);
            context.add(sample);
            ListIterator<Base6> i = eliminateFrom.listIterator();
            while(i.hasNext())
            {
                Base6 possibility = i.next();
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