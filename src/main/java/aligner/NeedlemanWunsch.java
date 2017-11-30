package aligner;

import java.io.CharArrayReader;
import java.util.Arrays;

public class NeedlemanWunsch {

    private String query;
    private String reference;
    private String score;
    private int[][] points;

    public String getQuery() {
        return query;
    }

    public String getReference() {
        return reference;
    }

    public String getScore() {
        return score;
    }


    /*public int[][] getPoints() {
        return points;
    }*/

    public int[][] getPoints(String seq1, String seq2)
    {
        //assume seq 1 is along the x axis

        points = new int[seq1.length()+1][2];
        points[0][0] = 0;
        points[0][1] = 0;
        int x=0, y=0;
        for(int i=0;i<seq1.length();i++)
        {
            if(seq1.charAt(i)=='_')
            {
                points[i+1][0] = x;
                points[i+1][1] = ++y;
            }
            else if(seq2.charAt(i)=='_')
            {
                points[i+1][0] = ++x;
                points[i+1][1] = y;
            }
            else
            {
                points[i+1][0] = ++x;
                points[i+1][1] = ++y;
            }
        }
        return points;
    }

    public void align(String sequence1, String sequence2)
    {
        int gapPenalty = -2;
        int fMatrix[][] = new int[sequence1.length()+1][sequence2.length()+1];
        int fiMatrix[][] = new int[sequence1.length()+1][sequence2.length()+1];
        int fjMatrix[][] = new int[sequence1.length()+1][sequence2.length()+1];
        fMatrix[0][0] = 0;
        fiMatrix[0][0] = 0;
        fjMatrix[0][0] = 0;
        int xy[][] = new int[sequence1.length()+sequence2.length()+3][2];
        int xyPointer = 0;
        int x=0, y=0;
        xy[xyPointer][0] = x;
        xy[xyPointer][1] = y;
        xyPointer++;
        for(int i=1; i<sequence1.length()+1; i++)
        {
            fMatrix[i][0] = fMatrix[i-1][0] + gapPenalty;
            fiMatrix[i][0] = i-1;
            fjMatrix[i][0] = 0;
        }
        for(int i=1; i<sequence2.length()+1; i++)
        {
            fMatrix[0][i] = fMatrix[0][i-1] + gapPenalty;
            fiMatrix[0][i] = 0;
            fjMatrix[0][i] = i-1;
        }

        for(int i=1; i<sequence1.length()+1; i++)
        {
            for(int j=1; j<sequence2.length()+1; j++)
            {
                int align = 0; //Along diagonal
                int seq1Gap = 0; //Along row
                int seq2Gap = 0; //Along column

                align = fMatrix[i-1][j-1] + match(sequence1.charAt(i-1), sequence2.charAt(j-1));
                seq1Gap = fMatrix[i][j-1] + gapPenalty;
                seq2Gap = fMatrix[i-1][j] + gapPenalty;

                fMatrix[i][j] = (align>=seq1Gap)?(align>=seq2Gap?align:seq2Gap):(seq1Gap>=seq2Gap?seq1Gap:seq2Gap);
                if(fMatrix[i][j]==align)
                {
                    //System.out.println("align");
                    fiMatrix[i][j] = i-1;
                    fjMatrix[i][j] = j-1;
                }
                else if(fMatrix[i][j] == seq1Gap)
                {
                    //System.out.println("gap in seq1");
                    fiMatrix[i][j] = i;
                    fjMatrix[i][j] = j-1;
                }
                else if(fMatrix[i][j] == seq2Gap)
                {
                    //System.out.println("gap in seq2");
                    fiMatrix[i][j] = i-1;
                    fjMatrix[i][j] = j;
                }
            }
        }


        String resultSequence1 = "";
        String resultSequence2 = "";

        int i = sequence1.length();
        int j = sequence2.length();
        while(i>=0 && j>=0)
        {
            int iTemp = fiMatrix[i][j];
            int jTemp = fjMatrix[i][j];
            if(i!=iTemp && j!=jTemp)
            {
                //take from both sequences
                resultSequence1 += Character.toString(sequence1.charAt(i-1));
                resultSequence2 += Character.toString(sequence2.charAt(j-1));
                i = i-1;
                j = j-1;

                xy[xyPointer][0] = x++;
                xy[xyPointer][1] = y++;
                xyPointer++;
            }
            else if(i!=iTemp && j==jTemp)
            {
                //gap in sequence 2
                resultSequence1 += Character.toString(sequence1.charAt(i-1));
                resultSequence2 += "_";
                i = i-1;

                xy[xyPointer][1] = y;
                xy[xyPointer][0] = x++;
                xyPointer++;


            }
            else if(i==iTemp && j!=jTemp)
            {
                //gap in sequence 1
                resultSequence1 += "_";
                resultSequence2 += Character.toString(sequence2.charAt(j-1));
                j = j-1;

                //Assuming sequence 1 plot along y-axis
                xy[xyPointer][1] = y++;
                xy[xyPointer][0] = x;
                xyPointer++;


            }
            if(i==0 && j==0)
            {
                break;
            }
        }

        xyPointer--;
        int shiftValue = xy[xyPointer][0];
        System.out.println("Shift value: " + shiftValue);

        for(int p=0;p<xy.length;p++)
        {
            System.out.println(xy[p][0] + "\t" + xy[p][1]);
        }

        StringBuffer sb1 = new StringBuffer(resultSequence1);
        sb1.reverse();
        StringBuffer sb2 = new StringBuffer(resultSequence2);
        sb2.reverse();
        resultSequence1 = sb1.toString();
        resultSequence2 = sb2.toString();

        //create new array from xy pointer
        int[][] copy = new int[xyPointer+1][2];
        for(int k=0;k<copy.length;k++)
        {
            copy[k][0] = xy[k][0];
            copy[k][1] = xy[k][1];
        }

        //assign resultseq1 to query
        query = resultSequence1;
        reference = resultSequence2;
        score = String.valueOf(fMatrix[sequence1.length()][sequence2.length()]);
        points = copy;

    }

    public int match(char a, char b)
    {
        int mismatchCost = -3;
        int matchCost = 1;
        if(a==b)
        {
            return matchCost;
        }
        else
        {
            return mismatchCost;
        }
    }

}
