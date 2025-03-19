package org.example.service.prediction;

import org.example.model.*;
import org.example.util.DataPreprocessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.core.Instance;
import weka.core.SerializationHelper;

import jakarta.annotation.PostConstruct;
import java.util.*;

@Service
public class WekaMoviePredictionService implements PredictionService {

    private final String modelPath;
    private final String preprocessingParamsPath;
    private Classifier model;
    private DataPreprocessor preprocessor;

    @Autowired
    public WekaMoviePredictionService(
            @Value("${model.path}${model.filename}") String modelPath,
            @Value("${preprocessing.params.path}") String preprocessingParamsPath) {
        this.modelPath = modelPath;
        this.preprocessingParamsPath = preprocessingParamsPath;
    }

    @PostConstruct
    public void init() throws Exception {
        // Tải mô hình khi khởi động service
        loadModel();

        // Khởi tạo preprocessor với các tham số đã lưu
        this.preprocessor = new DataPreprocessor(preprocessingParamsPath);
    }

    private void loadModel() throws Exception {
        try {
            this.model = (Classifier) SerializationHelper.read(modelPath);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load model from " + modelPath, e);
        }
    }

    @Override
    public PredictionResponse predictRating(MovieData movieData) throws Exception {
        // Tiền xử lý dữ liệu phim
        Instance processedData = preprocessor.preprocessMovieData(movieData);

        // Dự đoán xếp hạng
        double predictedRating = model.classifyInstance(processedData);

        // Tạo response với thông tin dự đoán và phân tích
        return PredictionResponse.builder()
                .movieTitle(movieData.getTitle())
                .predictedRating(predictedRating)
                .confidenceLevel(0.95) // Giả định - có thể tính toán từ mô hình
                .featureImportance(getFeatureImportance(movieData))
                .similarMovies(findSimilarMovies(movieData))
                .build();
    }

    @Override
    public TrendData getMovieTrends() throws Exception {
        // Trong ví dụ này, tôi tạo dữ liệu mẫu
        // Trong thực tế, bạn có thể truy xuất từ database hoặc phân tích dữ liệu

        TrendData trendData = new TrendData();

        // Tạo dữ liệu xu hướng thể loại
        List<TrendData.GenreTrend> genreTrends = new ArrayList<>();
        // ... code tạo dữ liệu xu hướng ...

        // Tạo dữ liệu phân phối xếp hạng
        List<TrendData.RatingDistribution> ratingDistribution = new ArrayList<>();
        // ... code tạo dữ liệu phân phối ...

        trendData.setGenreTrends(genreTrends);
        trendData.setRatingDistribution(ratingDistribution);

        return trendData;
    }

    private Map<String, Double> getFeatureImportance(MovieData movieData) {
        // Ví dụ: Trả về giá trị quan trọng của các đặc trưng
        // Trong thực tế, bạn có thể trích xuất từ mô hình hoặc tính toán
        Map<String, Double> importance = new HashMap<>();
        importance.put("runtime", 0.35);
        importance.put("budget", 0.25);
        importance.put("genre", 0.20);
        importance.put("release_date", 0.10);
        importance.put("other", 0.10);
        return importance;
    }

    private List<SimilarMovie> findSimilarMovies(MovieData movieData) {
        // Ví dụ: Tìm các phim tương tự
        // Trong thực tế, bạn có thể sử dụng database hoặc thuật toán tìm kiếm
        List<SimilarMovie> similarMovies = new ArrayList<>();
        similarMovies.add(new SimilarMovie("tt0111161", "The Shawshank Redemption", 9.3, 0.92));
        similarMovies.add(new SimilarMovie("tt0068646", "The Godfather", 9.2, 0.85));
        similarMovies.add(new SimilarMovie("tt0071562", "The Godfather: Part II", 9.0, 0.82));
        return similarMovies;
    }
}