package stores;

import structures.*;

import interfaces.ICredits;

public class Credits implements ICredits {

    //------------------------------nested credit class
    public class Credit {

        private HashMap<Integer, Cast> cast_hashmap;
        private HashMap<Integer, Crew> crew_hashmap;

        /*
         * Using a hashmap rather than an array for the storing of the crew and the cast members aquires a similar 
         * amount of memory, however is much more time efficient in implementation, since searches in a hashmap are 
         * completed in O(1) rather than O(n) in an array.
         */
        public Credit(Cast[] castarray, Crew[] crewarray) {

            this.cast_hashmap = new HashMap<>(castarray.length);
            for (int i = 0; i < castarray.length; i++) {
                cast_hashmap.put(castarray[i].getID(), castarray[i]);
            }

            this.crew_hashmap = new HashMap<>(crewarray.length);
            for (int i = 0; i < crewarray.length; i++) {
                crew_hashmap.put(crewarray[i].getID(), crewarray[i]);
            }
        }

        public HashMap<Integer, Cast> getCastHashmap() {
            return cast_hashmap;
        }

        public HashMap<Integer, Crew> getCrewHashmap() {
            return crew_hashmap;
        }

        public boolean containsCrew(int id) {
            return getCrewHashmap().containsKey(id);
        }

        public boolean containsCast(int id) {
            return getCastHashmap().containsKey(id);
        }

        public Crew getCrew(int id) {
            return getCrewHashmap().get(id);
        }

        public Cast getCast(int id) {
            return getCastHashmap().get(id);
        }

    }

    //------------------------------end of nested credit class
    
    //credits hashmap to store each credit object along with the movie id for that credit as a key
    HashMap<Integer, Credit> credit_hashmap;


    public Credits() {
        credit_hashmap = new HashMap<>(9999);
    }

    /**
     * Adds data about the people who worked on a given film
     * 
     * @param cast An array of all cast members that starred in the given film
     * @param crew An array of all crew members that worked on a given film
     * @param id   The movie ID
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean add(Cast[] cast, Crew[] crew, int id) {
        Credit credit = new Credit(cast, crew);
        return credit_hashmap.put(id, credit);
    }

    /**
     * Remove a given films data from the data structure
     * 
     * @param id The movie ID
     * @return TRUE if the data was removed, FALSE otherwise
     */
    @Override
    public boolean remove(int id) {
        return credit_hashmap.remove(id);
    }

    /**
     * Gets all the IDs for all films
     * 
     * @return An array of all film IDs
     */
    @Override
    public int[] getFilmIDs() {
        if (credit_hashmap.isEmpty()) {
            return new int[0];
        }
        //gets all the avaliable keys since key represents the movie id and free from duplicates 
        int[] filmids = new int[credit_hashmap.getKeySet().size()];
        int i = 0;
        for (Integer key : credit_hashmap.getKeySet()) {
            filmids[i] = key;
            i++;
        }
        return filmids;
    }


    /**
     * Gets all the films worked on by a given cast ID (not cast element ID)
     * 
     * @param castID The ID of the cast member to be found
     * @return An array of film IDs relating to all films worked on by the requested
     *         cast member. If the cast member cannot be found, then return null
     */
    @Override
    public int[] getFilmIDsFromCastID(int castID) {
        if (credit_hashmap.isEmpty()) {
            return new int[0];
        }
        CustomList<Integer> filmids = new CustomList<>();
        for (Integer filmid : credit_hashmap.getKeySet()) {
            if (credit_hashmap.get(filmid).containsCast(castID)) {
                filmids.add(filmid);
            }
        }

        //replaces the linked list with an array; although this requires O(n) time to iterate through,
        //this widely more memory efficient than predicting the size of the array required.
        int[] answer = new int[filmids.size()];
        int i = 0;
        for (Integer filmid : filmids) {
            answer[i] = filmid;
            i++;
        }

        return answer;
    }

    /**
     * Gets all the films worked on by a given crew ID (not crew element ID)
     * 
     * @param crewID The ID of the cast member to be found
     * @return An array of film IDs relating to all films worked on by the requested
     *         crew member. If the crew member cannot be found, then return null
     */
    @Override
    public int[] getFilmIDsFromCrewID(int crewID) {
        if (credit_hashmap.isEmpty()) {
            return new int[0];
        }
        //using a hashmap for the crew data greatly increases implementation efficiency 
        CustomList<Integer> filmids = new CustomList<>();
        for (Integer filmid : credit_hashmap.getKeySet()) {
            if (credit_hashmap.get(filmid).containsCrew(crewID)) {
                filmids.add(filmid);
            }
        }

        int[] answer = new int[filmids.size()];
        int i = 0;
        for (Integer filmid : filmids) {
            answer[i] = filmid;
            i++;
        }

        return answer;
    }

    /**
     * Gets all the cast that worked on a given film
     * 
     * @param filmID The movie ID
     * @return An array of Cast objects for all people that worked on a requested
     *         film. If the film cannot be found, then return null
     */
    @Override
    public Cast[] getCast(int filmID) {
        if (!credit_hashmap.containsKey(filmID)) {return null;}
        
        CustomList<Cast> cast = new CustomList<>();
        HashMap<Integer, Cast> cast_hashmap = credit_hashmap.get(filmID).getCastHashmap();
        for (Integer element : cast_hashmap.getKeySet()) {
            cast.add(cast_hashmap.get(element));
        }
         // as  a result of hashmap  implementation an element can be retrived in O(1) time on average
        Cast[] answer = new Cast[cast.size()];
        int i = 0;
        for (Cast c : cast) {
            answer[i] = c;
            i++;
        }
        cast.clear();
        return answer;
    }

    /**
     * Gets all the cast that worked on a given film
     * 
     * @param filmID The movie ID
     * @return An array of Cast objects for all people that worked on a requested
     *         film. If the film cannot be found, then return null
     */
    @Override
    public Crew[] getCrew(int filmID) {
        if (!credit_hashmap.containsKey(filmID)) {return null;}
        
        CustomList<Crew> crew = new CustomList<>();
        HashMap<Integer, Crew> crew_hashmap = credit_hashmap.get(filmID).getCrewHashmap();
        for (Integer element : crew_hashmap.getKeySet()) {
            crew.add(crew_hashmap.get(element));
        }
        // as  a result of hashmap  implementation an element can be retrived in O(1) time on average 
        Crew[] answer = new Crew[crew.size()];
        int i = 0;
        for (Crew c : crew) {
            answer[i] = c;
            i++;
        }
        crew.clear();
        return answer;
    }

    /**
     * Gets the number of cast that worked on a given film
     * 
     * @param filmID The movie ID
     * @return The number of cast member that worked on a given film. If the film
     *         cannot be found, then return -1
     */
    @Override
    public int sizeOfCast(int filmID) {
        if (!credit_hashmap.containsKey(filmID)) {return -1;}
        HashMap<Integer, Cast> cast_hashmap = credit_hashmap.get(filmID).getCastHashmap();
        return cast_hashmap.getKeySet().size(); //as a result of hashmap implementation
    }

    /**
     * Gets the number of crew that worked on a given film
     * 
     * @param filmID The movie ID
     * @return The number of crew member that worked on a given film. If the film
     *         cannot be found, then return -1
     */
    @Override
    public int sizeofCrew(int filmID) {
        if (!credit_hashmap.containsKey(filmID)) {return -1;}
        HashMap<Integer, Crew> crew_hashmap = credit_hashmap.get(filmID).getCrewHashmap();
        return crew_hashmap.getKeySet().size(); //as a result of hashmap implementation
    }

    /**
     * Gets the number of films stored in this data structure
     * 
     * @return The number of films in the data structure
     */
    @Override
    public int size() {
        return credit_hashmap.getKeySet().size();
    }

    /**
     * Gets the cast name for a given cast ID
     * 
     * @param castID The ID of the cast member to be found
     * @return The name of the cast member for the given ID. If the ID is invalid,
     *         then null should be returned
     */
    @Override
    public String getCastName(int castID) {
        if (credit_hashmap.isEmpty()) {return null;}
       
        for (Integer filmid : credit_hashmap.getKeySet()) {
            if (credit_hashmap.get(filmid).containsCast(castID)) {
                return credit_hashmap.get(filmid).getCast(castID).getName();
            }
        }

        return null;
    }

    /*
     * Gets the crew name for a given crew ID
     * 
     * @param crewID The ID of the crew member to be found
     * @return The name of the crew member for the given ID. If the ID is invalid,
     *         then null should be returned
     */
    @Override
    public String getCrewName(int crewID) {
        if (credit_hashmap.isEmpty()) {return null;}
       
        for (Integer filmid : credit_hashmap.getKeySet()) {
            if (credit_hashmap.get(filmid).containsCrew(crewID)) {
                return credit_hashmap.get(filmid).getCrew(crewID).getName();
            }
        }

        return null;
    }

    /**
     * Gets a list of all unique cast IDs present in the data structure
     * 
     * @return An array of all unique cast IDs. If there are no cast IDs, then
     *         return an empty array
     */
    @Override
    public int[] getUniqueCastIDs() {
        if (credit_hashmap.isEmpty()) {return new int[0]; }

        CustomList<Integer> unique_cast = new CustomList<>();

        for (Integer filmid : credit_hashmap.getKeySet()) {
            CustomList<Integer> cast_hashmap = credit_hashmap.get(filmid).getCastHashmap().getKeySet();
            if (cast_hashmap.isEmpty()) { continue; }
            for (Integer castid : cast_hashmap) {
                //accesses all the castids and adds them if doesnt already exist in list
                if (!unique_cast.contains(castid)) {
                    unique_cast.add(castid);
                }
            }
        }

        int[] answer = new int[unique_cast.size()];
        int i = 0;
        for (Integer castid : unique_cast) {
            answer[i] = castid;
            i++;
        }

        if (answer == null || answer.length == 0) {return new int[0];}
      
        return answer;
    }

    /**
     * Gets a list of all unique crew IDs present in the data structure
     * 
     * @return An array of all unique crew IDs. If there are no crew IDs, then
     *         return an empty array
     */
    @Override
    public int[] getUniqueCrewIDs() {
        if (credit_hashmap.isEmpty()) {return new int[0]; }

        CustomList<Integer> unique_crew = new CustomList<>();

        for (Integer filmid : credit_hashmap.getKeySet()) {
            CustomList<Integer> crew_hashmap = credit_hashmap.get(filmid).getCrewHashmap().getKeySet();
            if (crew_hashmap.isEmpty()) { continue; }
            for (Integer crewid : crew_hashmap) {
                //accesses all the crewids and adds them if doesnt already exist in list
                if (!unique_crew.contains(crewid)) {
                    unique_crew.add(crewid);
                }
            }
        }

        int[] answer = new int[unique_crew.size()];
        int i = 0;
        for (Integer crewid : unique_crew) {
            answer[i] = crewid;
            i++;
        }

        //in the instance where no crews exist and the list is empty or null
        if (answer == null || answer.length == 0) {return new int[0];}
      
        return answer;
    }

    /**
     * Get all the cast members that have the given string within their name
     * 
     * @param cast The string that needs to be found
     * @return An array of Cast objects of all cast members that have the requested
     *         string in their name
     */
    @Override
    public Cast[] findCast(String cast) {
        if (credit_hashmap.isEmpty()) {return new Cast[0];}
       
        CustomList<Cast> list = new CustomList<>();

        //using the .contains method built into java to check if a string is contained within another 
        for (Integer filmid : credit_hashmap.getKeySet()) {
            CustomList<Integer> cast_hashmap = credit_hashmap.get(filmid).getCastHashmap().getKeySet();
            if (cast_hashmap.isEmpty()) { continue; }
            for (Integer castid : cast_hashmap) {
                if (credit_hashmap.get(filmid).getCast(castid).getName().contains(cast) && !list.contains(credit_hashmap.get(filmid).getCast(castid))) {
                    list.add(credit_hashmap.get(filmid).getCast(castid));
                }
            }
        }

        Cast[] answer = new Cast[list.size()];
        int i = 0;
        for (Cast element : list) {
            answer[i] = element;
            i++;
        }

        if (answer == null || answer.length == 0) {return new Cast[0];}
      
        return answer;

    }

    /**
     * Get all the crew members that have the given string within their name
     * 
     * @param crew The string that needs to be found
     * @return An array of Crew objects of all crew members that have the requested
     *         string in their name
     */
    @Override
    public Crew[] findCrew(String crew) {
        if (credit_hashmap.isEmpty()) {return new Crew[0];}
       
        CustomList<Crew> list = new CustomList<>();
        //using the .contains method built into java to check if a string is contained within another 
        for (Integer filmid : credit_hashmap.getKeySet()) {
            CustomList<Integer> crew_hashmap = credit_hashmap.get(filmid).getCrewHashmap().getKeySet();
            if (crew_hashmap.isEmpty()) { continue; }
            for (Integer crewid : crew_hashmap) {
                if (credit_hashmap.get(filmid).getCrew(crewid).getName().contains(crew) && !list.contains(credit_hashmap.get(filmid).getCrew(crewid))) {
                    list.add(credit_hashmap.get(filmid).getCrew(crewid));
                }
            }
        }

        Crew[] answer = new Crew[list.size()];
        int i = 0;
        for (Crew element : list) {
            answer[i] = element;
            i++;
        }

        if (answer == null || answer.length == 0) {return new Crew[0];}
      
        return answer;
    }

    /**
     * Finds all stars. A star is the following person: a star actor is
     * a cast member who have appeared in 3 or more movies, where each movie
     * has an average score of 4 or higher.
     * 
     * @param ratings The ratings for all films
     * @return An array of Cast IDs that are stars
     */
    @Override
    public int[] findStarCastID(Ratings ratings) {
        
        if (ratings.size() == 0) {
            return new int[0];
        }

        CustomList<Cast[]> cast = new CustomList<>();
        //collect all the movies that have a rating greater than 4.0
        for (Integer filmid : ratings.getAllMovies()) {
            if (ratings.getMovieAverageRatings(filmid) >= 4.0) {
                cast.add(getCast(filmid));
            }
        }
        
        CustomList<Integer> castids = new CustomList<>();
        //collect all the cast members who have starred in at least three films
        for (Cast[] cast_array : cast) {
            if (cast_array == null) { continue; }
            for (Cast cast_object : cast_array ) {
                if (getFilmIDsFromCastID(cast_object.getID()).length >= 3 && !castids.contains(cast_object.getID())) {
                    castids.add(cast_object.getID());
                }
            }
        }

        int[] answer = new int[castids.size()];
        int i = 0;
        for (Integer castid : castids) {
            answer[i] = castid;
            i++;
        }
        //check for if the list is empty or the length is 0
        if (answer == null || answer.length == 0) {return new int[0];}
      
        return answer;
    }

    public static int getIndex(int[] arr, int target) {
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] == target) {
                return i;
            }
        }
        return -1; 
    }

    /**
     * Finds all superstars. A superstar is the following person: a star actor is
     * also a superstar if they have played in at least two movies with another star
     * actor.
     * 
     * @param ratings The ratings for all films
     * @return An array of Cast IDs that are super stars
     */
    @Override
    public int[] findSuperStarCastID(Ratings ratings) {
      
        /*
         * An adjacency matrix / graph is used here to show the relationship between the different cast members that are already 
         * star casts. The nodes represent the star casts from the list of the star cast found above, by index.
         * They are placed by index into the matrix, then the loops iterate through the structure to find all the movies in which 
         * the star casts star in; if another starcast is found, then an edge is created between the two node. The Graph is then iterated 
         * through so that all the nodes that have more than two connections are added to the list. A list is used to increase memort, 
         * since the size of the array cannot be predicted; the list is converted to an array that is then returned.
         */


        int[] starCastIDs = findStarCastID(ratings); //taking this out greatly increases the time efficiency without having to be constantly retrieved 
        AdjacencyMatrix graph = new AdjacencyMatrix(starCastIDs);
        //add connection for all nodes that belong to the same movie
        for (Integer castid : starCastIDs) {
            for(Integer filmid : getFilmIDsFromCastID(castid)) {
                for (Cast cast_member : getCast(filmid)) {
                    if (cast_member.getID() == castid || getIndex(starCastIDs, cast_member.getID()) == -1) { 
                        continue; 
                    }
                    graph.addConnection(getIndex(starCastIDs, castid), getIndex(starCastIDs, cast_member.getID()));
                }
            }
        }
    
        int[] indexs = graph.findNodesWithMultipleConnections();
        int[] answer = new int[indexs.length];
        int i = 0;
        for (Integer index : indexs) {
            answer[i] = starCastIDs[index];
            i++;
        }
    
        return answer;
    }
    

    /**
     * Finds the distance between cast members A and B, by looking at common cast
     * members in films. For example, if A and B were in different movies, but both
     * started in a movie with cast member C, then there distance would be 1.
     * 
     * @param castIDA The starting cast member
     * @param castIDB The finishing cast member
     * @return If there is no connection, then return an empty array. If castIDA ==
     *         castIDB, then return an array containing ONLY castIDB. If there is a
     *         path from castIDA to castIDB, then all cast IDs in the path should be
     *         listed in order in the returned array, including castIDB. In the
     *         above example, the array should return {castIDC, castIDB}.
     */
    @Override
    public int[] findDistance(int castIDA, int castIDB) {
    
        if (size() == 0) {
            return new int[0];
        }
        //in the case where the same actor is selected twice then that actor is returned and the distance is 0
        int[] array = new int[1];
        if (castIDA == castIDB) {
            array[0] = castIDB;
            return array;
        }
        int[] ids = getUniqueCastIDs();
        AdjacencyMatrix graph = new AdjacencyMatrix(ids);
            /*
            *  implemented in a way such that a connection is added / edge is added between two nodes, each node representing
            *  a unique cast id, and each movie that joins two nodes is represented via an edge. A breadth first algroithmn is used,
            *  using a queue to find the shortest distance between two nodes in the graph, then returns the list of nodes.
            */
        for (Integer castid : ids) {
            for(Integer filmid : getFilmIDsFromCastID(castid)) {
                for (Cast cast_member : getCast(filmid)) {
                    if (cast_member.getID() == castid) { 
                        continue; 
                    }
                    graph.addConnection(getIndex(ids, castid), getIndex(ids, cast_member.getID()));
                }
            }
        }

        // BFS algorithm 
        CustomList<Integer> nodes = graph.findShortestPath(graph.getMatrix(), getIndex(ids, castIDA), getIndex(getUniqueCastIDs(), castIDB));
       
        if (nodes.isEmpty()) { 
            return new int[0]; 
        }
        //the list of nodes that are produced contains the castIDA as the head, and therefore this is removed before converting into an array 
        nodes.remove(nodes.getHead());

        int[] answer = new int[nodes.size()];
        int i = 0;
        for (Integer index : nodes ) {
            answer[i] = getUniqueCastIDs()[index];
            i++;
        }
        
        return answer;
    }

}

