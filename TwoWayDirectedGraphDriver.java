
public class TwoWayDirectedGraphDriver {

    /**
     * This method recreates the test example from Problem 2, creates an adjacency matrix for the uphill and downhill edges
     * @return
     */
    public static TwoWayDirectedGraph createTestExample(){

        boolean[][] matrixUphill = new boolean[6][6];
        boolean[][] matrixDownhill = new boolean[6][6];

        matrixUphill[1][2] = true;
        matrixUphill[3][4] = true;
        matrixUphill[3][5] = true;

        matrixDownhill[1][3] = true;
        matrixDownhill[2][3] = true;
        matrixDownhill[2][4] = true;
        matrixDownhill[4][5] = true;

        TwoWayDirectedGraph graph = new TwoWayDirectedGraph(matrixUphill, matrixDownhill);

        return graph;
    }//createBinaryTree


    public static void main(String[] args){
        TwoWayDirectedGraph graph = TwoWayDirectedGraphDriver.createTestExample();
        System.out.println(graph.toString());
        System.out.println(graph.isValidUphillDownhillPath(1, 5));
    }//main
}

