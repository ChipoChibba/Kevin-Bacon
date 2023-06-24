import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * @author Chikwanda Chisha
 * @author Chipo Chibbamulilo
 * making the actor, movie and movie-Act maps necessary for creating the graph
 */
public class ActorMovie {

    public static Map<Integer, String> actorMap(String pathname) throws IOException {
        // opening the input file
        BufferedReader input1 = null;

        try{
         input1 = new BufferedReader(new FileReader(pathname));
        }
        catch(IOException e){
            System.err.println(e.getMessage());
        }

        //instantiating new map
        Map<Integer, String> actorMap = new HashMap<>();
        try{
            String line;
            while ((line = input1.readLine()) != null) {
                String[] actorsArr = line.split("\\|");

                if (actorsArr.length!=2){
                    System.err.println("bad separation in line "+line);
                }
                else{
                    try{
                        int identity = Integer.parseInt(actorsArr[0]);
                        String actor = actorsArr[1];
                        actorMap.put(identity, actor);

                    }catch(NumberFormatException e){
                        System.err.println("bad number in line "+line);
                    }
                }
            }
        }
        finally{
            try{
                input1.close();
            }catch(IOException e){
                System.err.println(e.getMessage());
            }
        }

        return actorMap;
    }

    public static Map<Integer, String> movie(String pathname) throws IOException {

        BufferedReader input2 = null;
        try{
        input2 = new BufferedReader(new FileReader(pathname));
        }catch(FileNotFoundException e){
            System.err.println(e.getMessage());
        }

        //instantiating new map
        Map<Integer, String> movieMap = new HashMap<>();
        try {
            String line2;

            while ((line2 = input2.readLine()) != null) {
                String[] moviesArr = line2.split("\\|");

                if (moviesArr.length!=2){
                    System.err.println("bad separation in line "+line2);
                }
                else{
                    try {
                        int identity = Integer.parseInt(moviesArr[0]);
                        String movie = moviesArr[1];
                        movieMap.put(identity, movie);
                    }catch(NumberFormatException e){
                        System.err.println("bad number in line "+line2);
                    }
                }


            }
        }
        finally{
            try{
            input2.close();
            } catch(IOException e){
                System.err.println(e.getMessage());
            }
        }
        //System.out.println(movieMap);
        return movieMap;
    }

    public static Map<String, ArrayList<String>> movieAct(String pathname,String actorpathname,String moviepathname) throws IOException {
        BufferedReader input3 = null;
        try{
            input3=new BufferedReader(new FileReader(pathname));

        }catch (FileNotFoundException e){
            System.err.println(e.getMessage());
        }

        Map<Integer,String> actormap= actorMap(actorpathname);
        Map<Integer, String> moviemap=movie(moviepathname);

        Map<String, ArrayList<String>> movieActorMap = new HashMap<>();
        try{
            String line3;
            while ((line3 = input3.readLine())!=null) {
                String[] movieActArr = line3.split("\\|");

                if (movieActArr.length != 2) {
                    System.err.println("bad separation in line " + line3);
                } else {
                    try{

                        //initial occurrence of the key
                        if (!movieActorMap.containsKey(moviemap.get(Integer.parseInt(movieActArr[0])))) {
                            ArrayList<String> actorArr = new ArrayList<>();
                            actorArr.add(actormap.get(Integer.parseInt(movieActArr[1])));
                            movieActorMap.put(moviemap.get(Integer.parseInt(movieActArr[0])), actorArr);

                        }
                        //occurrences after the initial
                        else {
                            ArrayList<String> list = movieActorMap.get(moviemap.get(Integer.parseInt(movieActArr[0])));
                            list.add(actormap.get(Integer.parseInt(movieActArr[1])));
                            movieActorMap.put(moviemap.get(Integer.parseInt(movieActArr[0])), list);
                        }
                    }catch(NumberFormatException e){
                    System.err.println("bad number in line "+line3);
                }
                }
            }
        }
        finally{
            try{
            input3.close();

            }catch(IOException e){
                System.err.println(e.getMessage());
            }
        }
        return movieActorMap;
    }

    public static Graph<String,Set<String>> makeGraph(String actorFile,String movieFile, String movActFile) throws IOException {
        Graph<String, Set<String>> graph=new AdjacencyMapGraph<>();

        Map<Integer, String> map=actorMap(actorFile);
        Map<String, ArrayList<String>> movActmap=movieAct(movActFile,actorFile,movieFile);

        for(int i:map.keySet()){
            //add the actors' name
            graph.insertVertex(map.get(i));
        }

        for (String movie: movActmap.keySet()){
            //get particular movie
            ArrayList<String> actors=movActmap.get(movie);
            //System.out.println(actors);

            //looping through the actors list
            for(int i=0; i< actors.size()-1;i++){
                for(int j=i+1;j<actors.size();j++){
                    String v=actors.get(i);String u=actors.get(j);

                    //initial occurrence of the edge
                    if(!graph.hasEdge(v,u)){
                        Set<String> intersection=new HashSet<>();
                        intersection.add(movie);
                        graph.insertUndirected(v,u,intersection);
                    }

                    //occurrences after initial
                    else{
                        Set<String> common=graph.getLabel(v,u);
                        common.add(movie);
                        graph.insertUndirected(v,u,common);
                    }
                }
            }
        }
        return graph;
    }
    public static void main(String[] args) throws Exception {
        //Step1:Make the graph
        Graph<String, Set<String>> graph=(makeGraph("Input/actorsTest.txt","Input/moviesTest.txt","Input/movie-actorsTest.txt"));

        //Test the code for each of the methods made
        Graph<String, Set<String>> tree=GraphLibPset.bfs(graph,"Kevin Bacon");
        System.out.println(graph);
        GraphLibPset.getPath(tree,"Alice");
        GraphLibPset.missingVertices(graph,tree);
        System.out.println(GraphLibPset.averageSeparation(tree,"Kevin Bacon"));


    }
}

