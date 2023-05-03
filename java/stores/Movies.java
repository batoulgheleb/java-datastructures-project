package stores;

import java.util.Calendar;

import interfaces.IMovies;
import structures.*;

public class Movies implements IMovies {

    //-----------------------------------------------nested movie object 

    private class Movie {

        private int id;
        private int collectionID;
        private String IMDb;
        private double popularity;
        
        //data stores an array of the movie data that is represented by strings, same amount of memory and time efficiency, but neater implemention
        private String[] data;
        
        private Genre[] genres;
        private Calendar release;
        private long budget;
        private long revenue;
        private String[] languages;
        private double runtime;
        private boolean adult;
        private boolean video;

        //votes
        private double voteAverage;
        private int voteCount;

        //country + company
        private CustomList<Company> companies;
        private CustomList<String> countries;

        public Movie(int id, String title, String originalTitle, String overview, String tagline, String status,
                        Genre[] genres, Calendar release, long budget, long revenue, String[] languages, String originalLanguage,
                        double runtime, String homepage, boolean adult, boolean video, String poster) 
        {
            this.id = id;
            this.data = new String[]{title, originalTitle, overview, tagline, status, originalLanguage, homepage, poster};
            this.genres = genres;
            this.release = release;
            this.budget = budget;
            this.revenue = revenue;
            this.languages = languages;
            this.runtime = runtime;
            this.adult = adult;
            this.video = video;
            
            this.companies = new CustomList<>();
            this.countries = new CustomList<>();
        }

        public int getId() {
            return id;
        }

        public Calendar getRelease() {
            return release;
        }

        public long getBudget() {
            return budget;
        }

        //since all the string data feilds are represented by an array, does not require 6 different getter methods, but rather just one since an array is indexed.
        public String getData(int num) {
            return data[num];
        }   

        public Genre[] getGenre() {
            return genres;
        }

        public long getRevenue() {
            return revenue;
        }

        public String[] getLanguages() {
            return languages;
        }

        public double getRuntime() {
            return runtime;
        }

        public boolean getAdult() {
            return adult;
        }

        public boolean getVideo() {
            return video;
        }

        public void setCollectionID(int collectionID) {
            this.collectionID = collectionID;
        }

        public int getCollectionID() {
            return collectionID;
        }

        public void setIMDb(String imdb) {
            this.IMDb = imdb;
        }

        public String getIMDb() {
            return IMDb;
        }

        public double getPopularity() {
            return popularity;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public void setVote(double voteAverage, int voteCount) {
            this.voteAverage = voteAverage;
            this.voteCount = voteCount;
        }

        public double getVoteAverage() {
            return voteAverage;
        }

        public int getVoteCount() {
            return voteCount;
        }

        public void addProductionCompany(Company company) {
            companies.add(company);
        }

        public void addProductionCountry(String country) {
            countries.add(country);
        }

        //converts the list into an array; this is more memory effient than predicting the size of an array, 
        // and more time efficent than having to loop through the array in searching 
        
        public Company[] getProductionCompanies() {
            Company[] array = new Company[companies.size()];
            int i = 0;
            for (Company c : companies) {
                array[i] = c;
                i++;
            }
            return array;
        }

        public String[] getProductionCountries() {
            String[] array = new String[countries.size()];
            int i = 0;
            for (String c : countries) {
                array[i] = c;
                i++;
            }
            return array;
        }


    }
    //-----------------------------------------------end of nested movie object 
    

    private int size;
    private static final int CAPACITY = 9999; 

    /* two constructors required: since could be done in one hashmap for the best memory efficiency, but the time complexity may hinder the app performance, 
     * therefore this implementation was the best memeory to time effiencity trade off. the hashmap are initialised within the constructor, with the capacity given above.
     */
    HashMap<Integer, Movie> hashmap;
    HashMap<Integer, Collection> collection_map;
    
    public Movies(int capacity) {
        hashmap = new HashMap<>(capacity);
        collection_map = new HashMap<>(capacity);
    }

    public Movies() {
        this(CAPACITY);
    }

    public boolean contains(int id) {
        return hashmap.containsKey(id);
    }

    @Override
    public boolean add(int id, String title, String originalTitle, String overview, String tagline, String status, Genre[] genres, Calendar release, 
            long budget, long revenue, String[] languages, String originalLanguage, double runtime, String homepage, boolean adult, boolean video, String poster) {
        
            Movie movie = new Movie(id, title, originalTitle, overview, tagline, status, genres, release, budget, revenue, languages, originalLanguage,
                                         runtime, homepage, adult, video, poster);
            
            boolean added = hashmap.put(movie.getId(), movie);    
            if (added) {
                size++;
            }
            return added;
    }

    /**
     * Removes a film from the data structure, and any data
     * added through this class related to the film
     * 
     * @param id The film ID
     * @return TRUE if the film has been removed successfully, FALSE otherwise
     */
    @Override
    public boolean remove(int id) {
        boolean removed = hashmap.remove(id);
        if (removed) {
            size--;
        }
        return removed;
    }

    /**
     * Finds the film IDs of all films released within a given range. If a film is
     * released either on the start or end dates, then that film should not be
     * included
     * 
     * @param start The start point of the range of dates
     * @param end   The end point of the range of dates
     * @return An array of film IDs that were released between start and end
     */
    @Override
    public int[] getAllIDsReleasedInRange(Calendar start, Calendar end) {
        if (hashmap.isEmpty()) {
            return new int[0];
        } 
        
        CustomList<Integer> ids = new CustomList<>();
        for (Integer id : hashmap.getKeySet()) {
            Calendar release = hashmap.get(id).getRelease();
            if (release == null) { continue; }
            if (release.after(start) && release.before(end)) {
                ids.add(hashmap.get(id).getId());
            }
        }

        int[] array = new int[ids.size()]; int i = 0;
        for (Integer element : ids) {
            array[i] = element; 
            i++;
        }

        return array;
    }

    /**
     * Finds the film IDs of all films released within a given range and within a
     * given range of budget. If a film is
     * released either on the start or end dates, then that film should not be
     * included. If a film has a budgets exactly the same as the lower or upper
     * bounds, then this film should not be included
     * 
     * @param start       The start point of the range of dates
     * @param end         The end point of the range of dates
     * @param lowerBudget The lowest bound of the range for budgets
     * @param upperBudget The upper bound of the range of budgets
     * @return An array of film IDs that were released between start and end, and
     *         had a budget between lowerBudget and upperBudget
     */
    @Override
    public int[] getAllIDsReleasedInRangeAndBudget(Calendar start, Calendar end, long lowerBudget, long upperBudget) {
        if (hashmap.isEmpty()) {
            return new int[0];
        } 
        
        CustomList<Integer> ids = new CustomList<>();
        for (Integer id : hashmap.getKeySet()) {
            Calendar release = hashmap.get(id).getRelease();
            Long budget = hashmap.get(id).getBudget();
            if (release.after(start) && release.before(end) && budget > lowerBudget && budget < upperBudget) {
                ids.add(hashmap.get(id).getId());
            }
        }

        int[] array = new int[ids.size()]; int i = 0;
        for (Integer element : ids) {
            array[i] = element; 
            i++;
        }

        return array;
    }

    /**
     * Gets the title of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The title of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getTitle(int id) {
        if (!contains(id)) {return null;}
        return hashmap.get(id).getData(0);
    }

    /**
     * Gets the original title of a particular film, given the ID number of that
     * film
     * 
     * @param id The movie ID
     * @return The original title of the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public String getOriginalTitle(int id) {
        if (!contains(id)) {return null;}
        return hashmap.get(id).getData(1);
    }

    /**
     * Gets the overview of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The overview of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getOverview(int id) {
        if (!contains(id)) {return null;}
        return hashmap.get(id).getData(2);
    }

    /**
     * Gets the tagline of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The tagline of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getTagline(int id) {
        if (!contains(id)) {return null;}
        return hashmap.get(id).getData(3);
    }

    /**
     * Gets the status of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The status of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getStatus(int id) {
        if (!contains(id)) {return null;}
        return hashmap.get(id).getData(4);
    }

    /**
     * Gets the genres of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The genres of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public Genre[] getGenres(int id) {
        if (!contains(id)) {return null;}
        return hashmap.get(id).getGenre();
    }

    /**
     * Gets the release date of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The release date of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public Calendar getRelease(int id) {
        if (!contains(id)) {return null;}
        return hashmap.get(id).getRelease();
    }

    /**
     * Gets the budget of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The budget of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public long getBudget(int id) {
        if (!contains(id)) {return -1;}
        return hashmap.get(id).getBudget();
    }

    /**
     * Gets the revenue of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The revenue of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public long getRevenue(int id) {
        if (!contains(id)) {return -1;}
        return hashmap.get(id).getRevenue();
    }

    /**
     * Gets the languages of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The languages of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public String[] getLanguages(int id) {
        if (!contains(id)) {return null;}
        return hashmap.get(id).getLanguages();
    }

    /**
     * Gets the original language of a particular film, given the ID number of that
     * film
     * 
     * @param id The movie ID
     * @return The original language of the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public String getOriginalLanguage(int id) {
        if (!contains(id)) {return null;}
        return hashmap.get(id).getData(5);
    }

    /**
     * Gets the runtime of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The runtime of the requested film. If the film cannot be found, then
     *         return -1
     */
    @Override
    public double getRuntime(int id) {
        if (!contains(id)) {return -1;}
        return hashmap.get(id).getRuntime();
    }

    /**
     * Gets the homepage of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The homepage of the requested film. If the film cannot be found, then
     *         return null
     */
    @Override
    public String getHomepage(int id) {
        if (!contains(id)) {return null;}
        return hashmap.get(id).getData(6);
    }

    /**
     * Gets weather a particular film is classed as "adult", given the ID number of
     * that film
     * 
     * @param id The movie ID
     * @return The "adult" status of the requested film. If the film cannot be
     *         found, then return false
     */
    @Override
    public boolean getAdult(int id) {
        if (!contains(id)) {return false;}
        return hashmap.get(id).getAdult();
    }

    /**
     * Gets weather a particular film is classed as "direct-to-video", given the ID
     * number of that film
     * 
     * @param id The movie ID
     * @return The "direct-to-video" status of the requested film. If the film
     *         cannot be found, then return false
     */
    @Override
    public boolean getVideo(int id) {
        if (!contains(id)) {return false;}
        return hashmap.get(id).getVideo();
    }

    /**
     * Gets the poster URL of a particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The poster URL of the requested film. If the film cannot be found,
     *         then return null
     */
    @Override
    public String getPoster(int id) {
        if (!contains(id)) {return null;}
        return hashmap.get(id).getData(7);
    }


        //-----------------------------------nested collections class
        private class Collection {

            private int filmid;
            private int collectionId;
            private String collectionName;
            private String collectionPosterPath;
            private String collectionBackdropPath;
    
            public Collection(int filmid, int collectionId, String collectionName, String collectionPosterPath,
                                     String collectionBackdropPath) 
            {
                this.filmid = filmid;
                this.collectionId = collectionId;
                this.collectionName = collectionName;
                this.collectionPosterPath = collectionPosterPath;
                this.collectionBackdropPath = collectionBackdropPath;
            }
        
            public String getCollectionPosterPath() {
                return collectionPosterPath;
            }
    
            public String getCollectionBackdropPath() {
                return collectionBackdropPath;
            }

            public String getCollectionName() {
                return collectionName;
            }
    
        }
        
        //-----------------------------------end of nested class 

    /**
     * Sets the average IMDb score and the number of reviews used to generate this
     * score, for a particular film
     * 
     * @param id          The movie ID
     * @param voteAverage The average score on IMDb for the film
     * @param voteCount   The number of reviews on IMDb that were used to generate
     *                    the average score for the film
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean setVote(int id, double voteAverage, int voteCount) {
        if (!contains(id)) { return false; }
        hashmap.get(id).setVote(voteAverage, voteCount);
        return true;
    }

    /**
     * Gets the average score for IMDb reviews of a particular film, given the ID
     * number of that film
     * 
     * @param id The movie ID
     * @return The average score for IMDb reviews of the requested film. If the film
     *         cannot be found, then return -1
     */
    @Override
    public double getVoteAverage(int id) {
        if (!contains(id)) { return -1; }
        return hashmap.get(id).getVoteAverage();
    }

    /**
     * Gets the amount of IMDb reviews used to generate the average score of a
     * particular film, given the ID number of that film
     * 
     * @param id The movie ID
     * @return The amount of IMDb reviews used to generate the average score of the
     *         requested film. If the film cannot be found, then return -1
     */
    @Override
    public int getVoteCount(int id) {
        if (!contains(id)) {return -1;}
        return hashmap.get(id).getVoteCount();
    }



    /**
     * Adds a given film to a collection. The collection is required to have an ID
     * number, a name, and a URL to a poster for the collection
     * 
     * @param filmID                 The movie ID
     * @param collectionID           The collection ID
     * @param collectionName         The name of the collection
     * @param collectionPosterPath   The URL where the poster can
     *                               be found
     * @param collectionBackdropPath The URL where the backdrop can
     *                               be found
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addToCollection(int filmID, int collectionID, String collectionName, String collectionPosterPath,
            String collectionBackdropPath) {
        
        if (!contains(filmID)) { return false; }

        Collection collection = new Collection(filmID, collectionID, collectionName, collectionPosterPath, collectionBackdropPath);
        hashmap.get(filmID).setCollectionID(collectionID);
        return collection_map.put(collectionID, collection);
    }

    /**
     * Gets the name of a given collection
     *  
     * @param collectionID The collection ID
     * @return The name of the collection. If the collection cannot be found, then
     *         return null
     */
    @Override
    public String getCollectionName(int collectionID) {
        if (!collection_map.containsKey(collectionID)) { return null; }
        return collection_map.get(collectionID).getCollectionName();
    }

    /**
     * Gets the poster URL for a given collection
     * 
     * @param collectionID The collection ID
     * @return The poster URL of the collection. If the collection cannot be found,
     *         then return null
     */
    @Override
    public String getCollectionPoster(int collectionID) {
        if (!collection_map.containsKey(collectionID)) { return null; }
        return collection_map.get(collectionID).getCollectionPosterPath();
    }

    /**
     * Gets the backdrop URL for a given collection
     * 
     * @param collectionID The collection ID
     * @return The backdrop URL of the collection. If the collection cannot be
     *         found, then return null
     */
    @Override
    public String getCollectionBackdrop(int collectionID) {
        if (!collection_map.containsKey(collectionID)) { return null; }
        return collection_map.get(collectionID).getCollectionBackdropPath();
    }

    /**
     * Gets the collection ID of a given film
     * 
     * @param filmID The movie ID
     * @return The collection ID for the requested film. If the film cannot be
     *         found, then return -1
     */
    @Override
    public int getCollectionID(int filmID) {
        if (!contains(filmID)) {
            return -1;
        }
        int collection_id = hashmap.get(filmID).getCollectionID();
        if (collection_id == 0) {
            return -1;
        }
        return collection_id;
    }

    /**
     * Sets the IMDb ID for a given film
     * 
     * @param filmID The movie ID
     * @param imdbID The IMDb ID
     * @return TRUE if the data able to be set, FALSE otherwise
     */
    @Override
    public boolean setIMDB(int filmID, String imdbID) {
        if (!contains(filmID)) {
            return false;
        }
        hashmap.get(filmID).setIMDb(imdbID);
        return true;
    }

    /**
     * Gets the IMDb ID for a given film
     * 
     * @param filmID The movie ID
     * @return The IMDb ID for the requested film. If the film cannot be found,
     *         return null
     */
    @Override
    public String getIMDB(int filmID) {
        if (!contains(filmID)) {
            return null;
        }
        return hashmap.get(filmID).getIMDb();
    }

    /**
     * Sets the popularity of a given film
     * 
     * @param id         The movie ID
     * @param popularity The popularity of the film
     * @return TRUE if the data able to be set, FALSE otherwise
     */
    @Override
    public boolean setPopularity(int id, double popularity) {
        if (!contains(id)) { return false; }
        hashmap.get(id).setPopularity(popularity);
        return true;
    }

    /**
     * Gets the popularity of a given film
     * 
     * @param id The movie ID
     * @return The popularity value of the requested film. If the film cannot be
     *         found, then return -1
     */
    @Override
    public double getPopularity(int id) {
        if (!contains(id)) { return -1; }
        return hashmap.get(id).getPopularity();
    }

    /**
     * Adds a production company to a given film
     * 
     * @param id      The movie ID
     * @param company A Company object that represents the details on a production
     *                company
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addProductionCompany(int id, Company company) {
        if (!contains(id)) { return false; }
        hashmap.get(id).addProductionCompany(company);
        return true;
    }

    /**
     * Adds a production country to a given film
     * 
     * @param id      The movie ID
     * @param country A ISO 3166 string containing the 2-character country code
     * @return TRUE if the data able to be added, FALSE otherwise
     */
    @Override
    public boolean addProductionCountry(int id, String country) {
        if (!contains(id)) { return false; }
        hashmap.get(id).addProductionCountry(country);
        return true;
    }

    /**
     * Gets all the production companies for a given film
     * 
     * @param id The movie ID
     * @return An array of Company objects that represent all the production
     *         companies that worked on the requested film. If the film cannot be
     *         found, then return null
     */
    @Override
    public Company[] getProductionCompanies(int id) {
        if (!contains(id)) { return null; }
        return hashmap.get(id).getProductionCompanies();
    }

    /**
     * Gets all the production companies for a given film
     * 
     * @param id The movie ID
     * @return An array of Strings that represent all the production countries (in
     *         ISO 3166 format) that worked on the requested film. If the film
     *         cannot be found, then return null
     */
    @Override
    public String[] getProductionCountries(int id) {
        if (!contains(id)) { return null; }
        return hashmap.get(id).getProductionCountries();
    }

    /**
     * States the number of movies stored in the data structure
     * 
     * @return The number of movies stored in the data structure
     */
    @Override
    public int size() {
        return size;
    }

    /**
     * Produces a list of movie IDs that have the search term in their title,
     * original title or their overview
     * 
     * @param searchTerm The term that needs to be checked
     * @return An array of movie IDs that have the search term in their title,
     *         original title or their overview. If no movies have this search term,
     *         then an empty array should be returned
     */
    @Override
    public int[] findFilms(String searchTerm) {
        if (size() == 0) { return new int[0]; }

        CustomList<Integer> movieids = new CustomList<>();
        for (Integer movieid : hashmap.getKeySet()) {
            for (int i = 0; i < 3; i++) {
                if (hashmap.get(movieid).getData(i).contains(searchTerm) && !movieids.contains(movieid)) {
                    movieids.add(movieid);
                }
            }
        }

        if (movieids.isEmpty()) { return new int[0]; }

        int[] array = new int[movieids.size()];
        int i = 0;
        for (Integer id : movieids ) {
            array[i] = id;
            i++;
        }

        return array;
    }
}
