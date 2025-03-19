package org.example.service.prediction;

import org.example.model.MovieData;
import org.example.model.PredictionResponse;
import org.example.model.TrendData;

public interface PredictionService {
    /**
     * Dự đoán xếp hạng cho một bộ phim
     *
     * @param movieData Dữ liệu phim cần dự đoán
     * @return Kết quả dự đoán
     * @throws Exception Nếu có lỗi trong quá trình dự đoán
     */
    PredictionResponse predictRating(MovieData movieData) throws Exception;

    /**
     * Lấy dữ liệu xu hướng phim
     *
     * @return Dữ liệu xu hướng
     * @throws Exception Nếu có lỗi khi lấy dữ liệu
     */
    TrendData getMovieTrends() throws Exception;
}