package stores;

import java.util.Calendar;
import interfaces.IRatings;
import structures.*;

public class Ratings implements IRatings {
 
 
    //-----------------------------------------------nested rating class 
    public class Rating {
 
        private int userId;
        private int movieId;
        private float rating;
        private Calendar timestamp;
 
        public Rating(int userId, int movieId, float rating, Calendar timestamp) {
            this.userId = userId;
            this.movieId = movieId;
            this.rating = rating;
            this.timestamp = timestamp;
        }
 
        public float getRating() {
            return rating;
        }
 
        public Calendar getTimestamp() {
            return timestamp;
        }
 
        public String toString() {
          return "{" + getRating() + " : " + getTimestamp() + "}";
        }
 
    }
    //------------------------------------------------end of nested ratings class 
 
 
    private static final int CAPACITY = 10000;
    private int size;

    /*
     * I chose against the implementation of having two hashmaps for a nested hashmap that had the best memory efficiency to time ratio, in contrast to concatting both
     * ids to oneanother that would decrease the time efficiency and require more memory. With this implementation, each rating can be get in found in O(1) since they are indexed.
     */
    private HashMap<Integer, HashMap<Integer, Rating>> outer_hashmap;
 
    public Ratings(int capacity) {
        outer_hashmap = new HashMap<>(capacity);
        this.size = 0;
    } 
 
    public Ratings() {
        this(CAPACITY);
        this.size = 0;
    } 
 
    //method that i found myself using alot therefore taken out for increased readability for efficiency
    public boolean containsMovie(int movieID) {
        return outer_hashmap.containsKey(movieID);
    }

    public boolean add(int userID, int movieID, float rating, Calendar timestamp) {
        Rating r = new Rating(userID, movieID, rating, timestamp);
        if (!containsMovie(movieID)) {
            outer_hashmap.put(movieID, new HashMap<>(CAPACITY));
        } 
        boolean added = outer_hashmap.get(movieID).put(userID, r);
        if (added) {
            size++;
        }
        return added;
    }
 
    public boolean remove(int userID, int movieID) {
        if (!containsMovie(movieID)) {
            return false;
        }
        boolean removed = outer_hashmap.get(movieID).remove(userID);
        if (removed) {
            size--;
        }
        return removed;
    }
 

    public boolean set(int userID, int movieID, float rating, Calendar timestamp) {
        Rating r = new Rating(userID, movieID, rating, timestamp);
        if (containsMovie(movieID)) {
            return outer_hashmap.get(movieID).replace(userID, r);
        } 
        return add(userID, movieID, rating, timestamp);
    }
 

    public float[] getRatingsBetween(Calendar start, Calendar end) {
        
        if (size() == 0) {
            return new float[0];
        }

        CustomList<Float> ratings = new CustomList<>();
        for (Integer movieId : outer_hashmap.getKeySet()) {
            for (Float r : getMovieRatingsBetween(movieId, start, end)) {
                ratings.add(r);
            }
        }

        float[] answer = new float[ratings.size()]; 
        int i = 0;
        for (Float r : ratings ) {
            answer[i] = r;
            i++;
        }

        return answer;
    }

    public float[] getMovieRatingsBetween(int movieID, Calendar start, Calendar end) {

        if (!containsMovie(movieID) || size() == 0) {
            return new float[0];
        }
        
        if (containsMovie(movieID)) {
            CustomList<Integer> userids = outer_hashmap.get(movieID).getKeySet();
            CustomList<Float> rs = new CustomList<>();
            for (Integer key : userids) {
                Rating r = outer_hashmap.get(movieID).get(key);
                if (r.getTimestamp().after(start) && r.getTimestamp().before(end)) {
                    rs.add(r.getRating());
                }
                
            }
            int j = 0;
            float[] array = new float[rs.size()];
            for (Float r : rs ) {
                array[j] = r;
            }

            return array;
        }

        return new float[0];
    }
 
    public float[] getUserRatingsBetween(int userID, Calendar start, Calendar end) {
        
        if (size() == 0) {
            return new float[0];
        }

        float[] ratings = getUserRatings(userID);
        if (ratings == null || ratings.length == 0) {
            return new float[0];
        }
        
        CustomList<Float> ratings_list = new CustomList<>();

        CustomList<Integer> movieIds = outer_hashmap.getKeySet();
        for (Integer moviekey : movieIds) {
            CustomList<Integer> userIds = outer_hashmap.get(moviekey).getKeySet();
            for (Integer userkey : userIds) {
                if (userkey == userID) {
                    Rating r = outer_hashmap.get(moviekey).get(userkey);
                    if (r.getTimestamp().after(start) && r.getTimestamp().before(end)) {
                        ratings_list.add(r.getRating());
                    }
                }
            }
        }

        int j = 0;
        float[] array = new float[ratings_list.size()];
        for (Float r : ratings_list ) {
            array[j] = r;
        }

        return array;
    }

    public float[] getMovieRatings(int movieID) {
        if (!containsMovie(movieID)) {
            return new float[0];
        }
        
        if (containsMovie(movieID)) {
            CustomList<Integer> userids = outer_hashmap.get(movieID).getKeySet();
            float[] ratings = new float[outer_hashmap.get(movieID).size()];
            int i = 0;
            for (Integer key : userids) {
                float r = outer_hashmap.get(movieID).get(key).getRating();
                ratings[i] = r;
                i++;
            }

            return ratings;
        }

        return new float[0];
    }
 
    public float[] getUserRatings(int userID) {
        
        if (size() == 0) {
            return new float[0];
        }

        //linked list 
        CustomList<Float> ratings_list = new CustomList<>();

        CustomList<Integer> movieIds = outer_hashmap.getKeySet();
        for (Integer moviekey : movieIds) {
            CustomList<Integer> userIds = outer_hashmap.get(moviekey).getKeySet();
            for (Integer userkey : userIds) {
                if (userkey == userID ) {
                    Float r = outer_hashmap.get(moviekey).get(userkey).getRating();
                    ratings_list.add(r);
                }
            }
        }

        int i = 0;
        float [] array = new float[ratings_list.size()];
        for (Float element : ratings_list ) {
            array[i] = element;
            i++;
        }

        if (array == null || array.length == 0) {
            return new float[0];
        }
        
        return array;
    }

    public float getMovieAverageRatings(int movieID) {
        if (!containsMovie(movieID)) {
            return 0;
        }
 
        float[] array = getMovieRatings(movieID);
        float sum = 0.0f;
        for (int i = 0; i < array.length; i++ ) {
            sum += array[i];
        } 
        
        return sum / (float)array.length;
    }

    public float getUserAverageRatings(int userID) {

        if (size() == 0) {
            return 0;
        }
 
        float[] array = getUserRatings(userID);
        float sum = 0.0f;
        for (int i = 0; i < array.length; i++ ) {
            sum += array[i];
        } 

        if (array == null || array.length == 0) {
            return 0;
        }
        
        return sum / (float) array.length;
    }

    public int[] getTopMovies(int num) {
        
        if (size() == 0 || num == 0) {
            return new int[0];
        } 

        int[][] movies = new int[size()][2];
        CustomList<Integer> movieIds = outer_hashmap.getKeySet();
        
        int i = 0; 
        for (Integer key : movieIds) {
            CustomList<Integer> userIds = outer_hashmap.get(key).getKeySet();
            movies[i][0] = userIds.size();
            movies[i][1] = key;
            i++;
        }
        if (movies.length == 0) { return new int[0]; }
        sortDescending(movies);

        int[] answer = new int[num];
        if (i < num) {

            answer = new int[i];
        } 

        for (int j = 0; j < num; j++) {
            answer[j] = movies[j][1];
        }

        return answer;
    }
    
    public static void sortDescending(int[][] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = i + 1; j < arr.length; j++) {
                if (arr[j][0] > arr[i][0]) {
                    int[] temp = arr[i];
                    arr[i] = arr[j];
                    arr[j] = temp;
                }
            }
        }
    }
    

    public int[] getMostRatedUsers(int num) {
        
        if (size() == 0 || num == 0) {
            return new int[0];
        } 

        int[][] users = new int[size()][2];
        int i = 0; 
        CustomList<Integer> movieIds = outer_hashmap.getKeySet();
        for (Integer moviekey : movieIds) {
            CustomList<Integer> userIds = outer_hashmap.get(moviekey).getKeySet();
            for (Integer userkey : userIds) {
                int count = 0;
                for (int index = 0; index < i; index++ ) {
                    if (users[index][1] == userkey) {
                        users[index][0] += getUserRatings(userkey).length;
                        count++;
                    }
                }
                if (count == 0) {
                    users[i][0] = getUserRatings(userkey).length;
                    users[i][1] = userkey;
                }
                i++;
            }
        }

        if (users.length == 0) { return new int[0]; }
        sortDescending(users);

        int[] answer = new int[num];
        if (i < num) {
            answer = new int[i];
        } 

        for (int j = 0; j < num; j++) {
            answer[j] = users[j][1];
        }

        return answer;
    }
 
    public int size() {
        return size;
    }

    public CustomList<Integer> getAllMovies() {
        return outer_hashmap.getKeySet();
    }
 
}
 
