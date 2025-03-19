package org.example.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.model.MovieData;
import org.springframework.stereotype.Component;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Component
public class DataPreprocessor {
    private final Map<String, Object> preprocessingParams;
    private final Instances datasetStructure;

    public DataPreprocessor(String paramsFilePath) throws Exception {
        // Đọc tham số tiền xử lý từ file JSON
        ObjectMapper mapper = new ObjectMapper();
        this.preprocessingParams = mapper.readValue(new File(paramsFilePath), Map.class);

        // Tạo cấu trúc dataset rỗng để sử dụng cho việc tạo instances mới
        this.datasetStructure = createDatasetStructure();
    }

    public Instance preprocessMovieData(MovieData movieData) {
        // Tạo một instance mới với cấu trúc của dataset
        Instance instance = new DenseInstance(datasetStructure.numAttributes());
        instance.setDataset(datasetStructure);

        // Áp dụng các bước tiền xử lý dựa trên tham số đã lưu
        // Đây là ví dụ - bạn cần thay thế bằng logic thực tế

        // Chuẩn hóa ngân sách
        Map<String, Double> budgetParams = (Map<String, Double>)
                ((Map<String, Object>) preprocessingParams.get("numerical_features")).get("budget");
        double minBudget = budgetParams.get("min");
        double maxBudget = budgetParams.get("max");
        double normalizedBudget = (movieData.getBudget() - minBudget) / (maxBudget - minBudget);
        instance.setValue(datasetStructure.attribute("budget"), normalizedBudget);

        // Tương tự cho các đặc trưng khác
        // ...

        return instance;
    }

    private Instances createDatasetStructure() {
        // Tạo cấu trúc dataset với các thuộc tính tương ứng
        // Đây là ví dụ - bạn cần điều chỉnh dựa trên dữ liệu thực tế
        ArrayList<Attribute> attributes = new ArrayList<>();

        // Thêm các thuộc tính số
        attributes.add(new Attribute("budget"));
        attributes.add(new Attribute("revenue"));
        attributes.add(new Attribute("runtime"));
        attributes.add(new Attribute("popularity"));

        // Thêm các thuộc tính phân loại
        // Tùy thuộc vào cách bạn xử lý, đây có thể là string hoặc nominal

        // Thuộc tính mục tiêu
        attributes.add(new Attribute("vote_average"));

        // Tạo dataset trống với các thuộc tính đã xác định
        Instances datasetStructure = new Instances("MovieDataset", attributes, 0);
        datasetStructure.setClassIndex(attributes.size() - 1); // vote_average là mục tiêu

        return datasetStructure;
    }
}