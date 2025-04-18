package org.example.util;

import org.example.dto.MovieRequestDTO;
import org.example.exception.ModelPredictionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

/**
 * Utility class for data preparation tasks
 */
public class DataPreparationUtil {

    private static final Logger logger = LoggerFactory.getLogger(DataPreparationUtil.class);

    /**
     * Prepares a Weka Instance for prediction based on movie data
     *
     * @param movieRequestDTO The movie data
     * @param datasetStructure The structure of the dataset used for prediction
     * @return A Weka Instance ready for prediction
     */
    public static Instances prepareInstanceForPrediction(MovieRequestDTO movieRequestDTO, Instances datasetStructure) {
        try {
            // Create a new instances with the structure of our dataset
            Instances predictionDataset = new Instances(datasetStructure, 0);
            double[] values = new double[predictionDataset.numAttributes()];

            // Fill the instance with values from the request
            for (int i = 0; i < predictionDataset.numAttributes(); i++) {
                Attribute att = predictionDataset.attribute(i);
                String attName = att.name();

                // Skip the class attribute (averageRating) - we're trying to predict this
                if (attName.equals("averageRating")) {
                    values[i] = weka.core.Utils.missingValue();
                    continue;
                }

                // Map the DTO fields to dataset attributes based on attribute name
                switch (attName) {
                    case "id":
                        values[i] = movieRequestDTO.getId();
                        break;
                    case "vote_count":
                        values[i] = movieRequestDTO.getVoteCount();
                        break;
                    case "revenue":
                        values[i] = movieRequestDTO.getRevenue();
                        break;
                    case "runtime":
                        values[i] = movieRequestDTO.getRuntime();
                        break;
                    case "budget":
                        values[i] = movieRequestDTO.getBudget();
                        break;
                    case "popularity":
                        values[i] = movieRequestDTO.getPopularity();
                        break;
                    case "vote_average":
                        values[i] = movieRequestDTO.getVoteAverage();
                        break;
                    case "numVotes":
                        values[i] = movieRequestDTO.getNumVotes();
                        break;
                    case "title":
                        values[i] = att.addStringValue(movieRequestDTO.getTitle());
                        break;
                    case "status":
                        values[i] = att.addStringValue(movieRequestDTO.getStatus());
                        break;
                    case "release_date":
                        values[i] = att.addStringValue(movieRequestDTO.getReleaseDate());
                        break;
                    case "adult":
                        values[i] = movieRequestDTO.getAdult() ? 1.0 : 0.0;
                        break;
                    case "tconst":
                        values[i] = att.addStringValue(movieRequestDTO.getTconst());
                        break;
                    // Optional fields
                    case "original_language":
                        values[i] = movieRequestDTO.getOriginalLanguage() != null ?
                                att.addStringValue(movieRequestDTO.getOriginalLanguage()) :
                                att.addStringValue("");
                        break;
                    case "original_title":
                        values[i] = movieRequestDTO.getOriginalTitle() != null ?
                                att.addStringValue(movieRequestDTO.getOriginalTitle()) :
                                att.addStringValue("");
                        break;
                    case "genres":
                        values[i] = movieRequestDTO.getGenres() != null ?
                                att.addStringValue(movieRequestDTO.getGenres()) :
                                att.addStringValue("");
                        break;
                    case "keywords":
                        values[i] = movieRequestDTO.getKeywords() != null ?
                                att.addStringValue(movieRequestDTO.getKeywords()) :
                                att.addStringValue("");
                        break;
                    case "directors":
                        values[i] = movieRequestDTO.getDirectors() != null ?
                                att.addStringValue(movieRequestDTO.getDirectors()) :
                                att.addStringValue("");
                        break;
                    case "writers":
                        values[i] = movieRequestDTO.getWriters() != null ?
                                att.addStringValue(movieRequestDTO.getWriters()) :
                                att.addStringValue("");
                        break;
                    case "cast":
                        values[i] = movieRequestDTO.getCast() != null ?
                                att.addStringValue(movieRequestDTO.getCast()) :
                                att.addStringValue("");
                        break;
                    case "backdrop_path":
                        values[i] = movieRequestDTO.getBackdropPath() != null ?
                                att.addStringValue(movieRequestDTO.getBackdropPath()) :
                                att.addStringValue("");
                        break;
                    case "homepage":
                        values[i] = movieRequestDTO.getHomepage() != null ?
                                att.addStringValue(movieRequestDTO.getHomepage()) :
                                att.addStringValue("");
                        break;
                    case "overview":
                        values[i] = movieRequestDTO.getOverview() != null ?
                                att.addStringValue(movieRequestDTO.getOverview()) :
                                att.addStringValue("");
                        break;
                    case "poster_path":
                        values[i] = movieRequestDTO.getPosterPath() != null ?
                                att.addStringValue(movieRequestDTO.getPosterPath()) :
                                att.addStringValue("");
                        break;
                    case "tagline":
                        values[i] = movieRequestDTO.getTagline() != null ?
                                att.addStringValue(movieRequestDTO.getTagline()) :
                                att.addStringValue("");
                        break;
                    case "production_companies":
                        values[i] = movieRequestDTO.getProductionCompanies() != null ?
                                att.addStringValue(movieRequestDTO.getProductionCompanies()) :
                                att.addStringValue("");
                        break;
                    case "production_countries":
                        values[i] = movieRequestDTO.getProductionCountries() != null ?
                                att.addStringValue(movieRequestDTO.getProductionCountries()) :
                                att.addStringValue("");
                        break;
                    case "spoken_languages":
                        values[i] = movieRequestDTO.getSpokenLanguages() != null ?
                                att.addStringValue(movieRequestDTO.getSpokenLanguages()) :
                                att.addStringValue("");
                        break;
                    default:
                        // For any attributes not in our DTO, set to missing
                        values[i] = weka.core.Utils.missingValue();
                        break;
                }
            }

            // Create a new instance and add it to the dataset
            DenseInstance instance = new DenseInstance(1.0, values);
            predictionDataset.add(instance);

            // Set class index
            predictionDataset.setClassIndex(datasetStructure.classIndex());

            return predictionDataset;
        } catch (Exception e) {
            logger.error("Error preparing instance for prediction", e);
            throw new ModelPredictionException("Failed to prepare data for prediction", e);
        }
    }
}