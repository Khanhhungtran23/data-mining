package org.example.dto;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class MovieRequestDTO {
    // Required fields (based on Image 2)
    @NotNull(message = "ID is required")
    private Long id;

    @NotNull(message = "Vote count is required")
    @Min(value = 0, message = "Vote count must be non-negative")
    private Integer voteCount;

    @NotNull(message = "Revenue is required")
    @Min(value = 0, message = "Revenue must be non-negative")
    private Long revenue;

    @NotNull(message = "Runtime is required")
    @Min(value = 0, message = "Runtime must be non-negative")
    private Integer runtime;

    @NotNull(message = "Budget is required")
    @Min(value = 0, message = "Budget must be non-negative")
    private Long budget;

    @NotNull(message = "Popularity is required")
    @Min(value = 0, message = "Popularity must be non-negative")
    private Double popularity;

    @NotNull(message = "Average rating is required")
    @Min(value = 0, message = "Average rating must be non-negative")
    private Double averageRating;

    @NotNull(message = "Number of votes is required")
    @Min(value = 0, message = "Number of votes must be non-negative")
    private Integer numVotes;

    @NotNull(message = "Title is required")
    private String title;

    @NotNull(message = "Status is required")
    private String status;

    @NotNull(message = "Release date is required")
    private String releaseDate;

    @NotNull(message = "Adult content flag is required")
    private Boolean adult;

    @NotNull(message = "tconst is required")
    private String tconst;

    private String originalLanguage;
    private String originalTitle;
    private String genres;
    private String keywords;
    private String directors;
    private String writers;
    private String cast;

    private String backdropPath;
    private String homepage;
    private String overview;
    private String posterPath;
    private String tagline;
    private String productionCompanies;
    private String productionCountries;
    private String spokenLanguages;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Long getRevenue() {
        return revenue;
    }

    public void setRevenue(Long revenue) {
        this.revenue = revenue;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public Long getBudget() {
        return budget;
    }

    public void setBudget(Long budget) {
        this.budget = budget;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Double getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Double averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getNumVotes() {
        return numVotes;
    }

    public void setNumVotes(Integer numVotes) {
        this.numVotes = numVotes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public String getTconst() {
        return tconst;
    }

    public void setTconst(String tconst) {
        this.tconst = tconst;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public String getWriters() {
        return writers;
    }

    public void setWriters(String writers) {
        this.writers = writers;
    }

    public String getCast() {
        return cast;
    }

    public void setCast(String cast) {
        this.cast = cast;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(String productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public String getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(String productionCountries) {
        this.productionCountries = productionCountries;
    }

    public String getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(String spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }
}