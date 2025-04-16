package org.example;

import weka.core.Attribute;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.CSVSaver;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        // Đường dẫn đến file CSV của bạn
        String inputFilePath = "/Users/hungtran/Downloads/Projects/data-mining/modelEvaluation/data/cleaned_data.csv";
        String outputFilePath = "/Users/hungtran/Downloads/Projects/data-mining/modelEvaluation/data/output.csv";

        System.out.println("Bắt đầu xử lý dữ liệu...");

        // Load CSV file
        CSVLoader loader = new CSVLoader();
        loader.setSource(new File(inputFilePath));
        Instances dataset = loader.getDataSet();

        // In thông tin tổng quan về dataset ban đầu
        System.out.println("Thông tin tổng quan dataset ban đầu:");
        System.out.println(dataset.toSummaryString());

        // Danh sách các cột cần loại bỏ
        String[] columnsToDrop = {
                "homepage", "tagline", "backdrop_path", "production_companies",
                "production_countries", "spoken_languages", "poster_path", "overview"
        };

        // Lấy chỉ số các thuộc tính cần loại bỏ
        List<Integer> indicesToDrop = new ArrayList<>();
        for (String columnName : columnsToDrop) {
            Attribute attribute = dataset.attribute(columnName);
            if (attribute != null) {
                indicesToDrop.add(attribute.index());
            } else {
                System.out.println("Không tìm thấy cột: " + columnName);
            }
        }

        // Sắp xếp chỉ số theo thứ tự giảm dần để an toàn khi xóa thuộc tính
        indicesToDrop.sort((a, b) -> b - a);

        // Xóa các cột
        for (int index : indicesToDrop) {
            dataset.deleteAttributeAt(index);
        }

        // Đặt "vote_average" làm thuộc tính mục tiêu (class)
        Attribute targetAttribute = dataset.attribute("vote_average");
        if (targetAttribute != null) {
            dataset.setClass(targetAttribute);
        } else {
            System.err.println("Lỗi: Không tìm thấy cột 'vote_average'!");
            return;
        }

        // In thông tin tổng quan về dataset sau khi cập nhật
        System.out.println("Thông tin tổng quan dataset sau khi cập nhật:");
        System.out.println(dataset.toSummaryString());

        // Lưu dataset đã cập nhật dưới dạng file CSV mới
        File outputDir = new File("data");
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }

        CSVSaver saver = new CSVSaver();
        saver.setInstances(dataset);
        saver.setFile(new File(outputFilePath));
        saver.writeBatch();

        System.out.println("Đã xử lý dữ liệu xong và lưu vào: " + outputFilePath);
    }
}