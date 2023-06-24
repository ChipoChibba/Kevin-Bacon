import java.io.IOException;
import java.util.*;

/**
 * @author Chipo Chibbamulilo
 * @author Chikwanda Chisha
 * making the game interface for the Kevin bacon game
 */
public class GameInterface {

    Graph<String, Set<String>> graph=(ActorMovie.makeGraph("Input/actors.txt","Input/movies.txt","Input/movie-actors.txt"));
    String centerOfUni = "Kevin Bacon";
    Graph<String,Set<String>> tree;
    String v;

    public void GraphInterface() throws IOException {

    }

    public GameInterface() throws IOException {
    }

    public static<V,E> double totalSep(Graph<V,E> tree, V root,List<String> list,int high, int low, int num){
        if(!tree.inNeighbors(root).iterator().hasNext()) return num;
        double total = num;
        if (total <= high && total >= low){
            list.add((String) root);
        }

        for(V v: tree.inNeighbors(root)){
            total = totalSep(tree,v, list, high, low, num+1);
        }
        return total;
    }


    // possible thoughts, probably calling drawing gui's
    public void handleKeypress(char c) throws Exception {
        Set<String> actors = (Set<String>) graph.vertices();


        if (c == 'u') {
            //centre of universe input from user
            Scanner object = new Scanner(System.in);
            centerOfUni = object.nextLine();

            if (!actors.contains(centerOfUni)){
                throw new Exception("Check your name");
            }

            //changing of pathTree
            tree = GraphLibPset.bfs(graph, centerOfUni);

            System.out.println(centerOfUni+" is now the center of the acting universe, connected to "+graph.numVertices() +" actors with average separation " + GraphLibPset.averageSeparation(tree,centerOfUni));//debugging statement
            System.out.println("\n" + centerOfUni + " game >" + "\n");

        }

        else if (c == 'p') {
            //input from user
            Scanner object1 = new Scanner(System.in);
            v = object1.nextLine();

            if (!actors.contains(v)){
                throw new Exception("Check your name");
            }

            List<String> path = GraphLibPset.getPath(tree, v);
            int vNumber = path.size() - 1;
            System.out.println(v + "'s number is " + vNumber);

            int j = 0;
            for(int i = 0; j < vNumber; i++){
                j = i + 1;
                //initializing string msg
                String msg1 = ""; String msg2 = "";

                msg1 += path.get(i) + " appeared in ";
                msg2 +=  " with " + path.get(j);

                System.out.println(msg1 +  graph.getLabel(path.get(i), path.get(j)) + msg2);

            }

        }  else if (c == 's') {
            //low
            Scanner object2 = new Scanner(System.in);
            String low = object2.nextLine();
            int lowNum = Integer.parseInt(low);

            //high
            Scanner object3 = new Scanner(System.in);
            String high = object3.nextLine();
            int highNum = Integer.parseInt(high);

            //calling method from SA7
            ArrayList<String> list = (ArrayList<String>) GraphLibPset.verticesByInDegree(graph);

            ArrayList<String> listWithCond = new ArrayList<>();

            //ensuring that inDegrees are in between bounds given
            for (String i : list) {
                if (graph.inDegree(i) >= lowNum && graph.inDegree(i) <= highNum) {
                    listWithCond.add(i);
                }
            }

            System.out.println(listWithCond);

        } else if (c == 'c') {
            //input from user
            Scanner object3 = new Scanner(System.in);
            String bottomTop = object3.nextLine();

            int number = Integer.parseInt(bottomTop);

            System.out.println(actorNumberList(number));
        }

       else if (c=='i') {

            System.out.println(GraphLibPset.missingVertices(graph,tree));

       } else if (c=='d') {
            //low
            Scanner object4 = new Scanner(System.in);
            String low =object4.nextLine();
            int lowNum = Integer.parseInt(low);

            //high
            Scanner object5 = new Scanner(System.in);
            String high = object5.nextLine();
            int highNum = Integer.parseInt(high);

            ArrayList<String> listWithCond = new ArrayList<>();

            totalSep(tree,centerOfUni,listWithCond,highNum,lowNum,0);

            System.out.println(listWithCond);
       }
    }

    public List<String> actorNumberList(int k){
        int ind = 0;
        List<String> actorList = new ArrayList<>();
        List<String> queue = new ArrayList<>();

        Comparator<String> listComparator = (v1, v2) -> {

            if (GraphLibPset.averageSeparation(tree, v1) < GraphLibPset.averageSeparation(tree, v2)) {
                return -1;
            } else if (GraphLibPset.averageSeparation(tree, v1) == GraphLibPset.averageSeparation(tree, v2)) {
                return 0;
            } else {
                return 1;
            }
        };

        for (String v : tree.vertices()) {
            queue.add(v);
        }
        queue.sort(listComparator);

        if (k < 0){
            ind = tree.numVertices()- 1 + k;
            k = Math.abs(k);
        }

        while (k > 0){
            actorList.add(queue.get(ind));
            ind++;
            k--;
        }
        return actorList;
    }

    public static void main(String[] args) throws Exception {
        GameInterface game=new GameInterface();
        ArrayList<String > commands= new ArrayList<>();
        commands.add("c");
        commands.add("u");
        commands.add("d");
        commands.add("i");
        commands.add("p");
        commands.add("s");
        commands.add("q");

        Scanner in = new Scanner(System.in);
        String c = in.nextLine().toLowerCase();
        char cha = c.charAt(0);

        while (cha != 'q') {
            game.handleKeypress(cha);
            System.out.println("Current Character: " + cha);
            System.out.print(">");
            c = in.nextLine().toLowerCase();
            if (commands.contains(c))
                cha=c.charAt(0);
        }
        System.out.println("GAME OVER");

    }
}
