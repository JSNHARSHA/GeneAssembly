package assembler;

import java.util.ArrayList;
import java.util.List;

/**
 * Denovo sequence assembler class
 */
public class DenoSequenceAssembler{

    private DeBruijnGraph graph;

    public DenoSequenceAssembler(DeBruijnGraph graph) {
        this.graph = graph;
    }

    public int findStart(int edges[][], List<String> vertices)
    {
        int[][] degree = new int[vertices.size()][2];
        for(int i=0; i<edges.length; i++)
        {
            //out edges count
            degree[edges[i][0]][0]++;
            //in edges count
            degree[edges[i][1]][1]++;
        }

        int startIndex = -1;
        int count = 0;
        int[] oddCount = new int[vertices.size()];
        boolean areAllZero = true;
        for(int i=0; i<vertices.size(); i++)
        {
            oddCount[i] = degree[i][0] - degree[i][1];
            if(oddCount[i]!=0)
            {
                areAllZero = false;
                count++;
                if(oddCount[i]>0 && startIndex==-1)
                {
                    startIndex = i;
                }
            }
        }

        if(count != 2)
        {
            if(areAllZero)
            {
                //Eulerian Cycle. We don't care where we start
                startIndex = 0;
            }
            else
            {
                //No Euler Path
                startIndex = -1;
            }
        }
        return startIndex;
    }

    public DeBruijnGraph getGraph() {
        return graph;
    }

    public String EulerianPath(int edges[][], List<String> vertices)
    {
        int startIndex = findStart(edges, vertices);

        if(startIndex == -1)
            return "no";

        int visitHistory[] = new int[edges.length];
        int visitCount = 0;

        String path = "";

        ArrayList<Integer> nodesInPath = new ArrayList<Integer>();
        nodesInPath.add(startIndex);
        int tracker = 0;

        int elementsAdded = 0;

        while(true)
        {
            boolean nodeFound = false;
            for(int i=0; i<edges.length; i++)
            {
                if(edges[i][0] == startIndex && visitHistory[i] == 0)
                {
                    startIndex = edges[i][1];
                    if(tracker == 0)
                    {
                        nodesInPath.add(startIndex);
                    }
                    else
                    {
                        //Insert new element at tracker+elementsAdded
                        nodesInPath.add((tracker+elementsAdded), startIndex);
                        elementsAdded++;
                    }
                    visitHistory[i] = 1; //Mark as visited
                    visitCount++; //Increase count of visited nodes
                    nodeFound = true;
                    break;
                }
            }
            if(visitCount == edges.length) //All edges traversed
            {
                break;
            }
            else
            {
                if(!nodeFound) //No non-visited out edge found from given node
                {
                    startIndex = nodesInPath.get(tracker++); //Re-initialize start index to look for edges missed from any nodes already visited
                    elementsAdded = 0; //Reset elements added to list for current start to 0
                }
            }
        }

        //Compute string from list of node traversals that give Euler path
        for(int i=0; i<nodesInPath.size(); i++)
        {
            if(i==0)
            {
                path += vertices.get(nodesInPath.get(i));
            }
            else
            {
                String temp = vertices.get(nodesInPath.get(i));
                path += Character.toString(temp.charAt(temp.length()-1));
            }
        }
        return path;
    }

}
