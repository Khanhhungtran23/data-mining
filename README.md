# Movie Rating Prediction Project

This project uses machine learning models to predict movie ratings based on attributes like budget, revenue, runtime, genre, etc.

## Project Structure

```
movie-rating-prediction/
├── data/
│   ├── cleaned_data.csv (original data file)
│   └── output.csv (processed data file)
├── results/
│   ├── model_comparison.txt (model comparison results)
│   ├── predictions.csv (model predictions)
│   └── model_comparison.png (comparison chart)
├── src/
│   └── main/
│       └── java/
│           └── org/
│               └── example/
│                   ├── Main.java (data preprocessing)
│                   ├── ModelEvaluation.java (evaluates Random Forest and Linear Regression)
│                   ├── ModelComparison.java (compares all models)
│                   ├── ModelVisualization.java (visualizes results)
│                   ├── DecisionStumpEvaluation.java
│                   ├── GaussianProcessesEvaluation.java
│                   ├── KNNEvaluation.java
│                   ├── M5PEvaluation.java
│                   ├── MultilayerPerceptronEvaluation.java
│                   ├── REPTreeEvaluation.java
│                   └── SMOregEvaluation.java
└── pom.xml (Maven configuration file)
```

## Installation

1. Install Java Development Kit (JDK) 11 or higher
2. Install Maven
3. Clone or download this project

## Project Setup

1. Place the `cleaned_data.csv` file in the `data/` directory
2. Open a terminal and navigate to the project's root directory
3. Run the command: `mvn clean install`

## Running the Program Classes

You can run each class individually to evaluate each model, or run `ModelComparison` to compare all models at once.

### Data Preprocessing

```bash
mvn exec:java -Dexec.mainClass="org.example.Main"
```

### General Model Evaluation

```bash
mvn exec:java -Dexec.mainClass="org.example.ModelEvaluation"
```

### Compare All Models

```bash
mvn exec:java -Dexec.mainClass="org.example.ModelComparison"
```

### Visualize Results

```bash
mvn exec:java -Dexec.mainClass="org.example.ModelVisualization"
```

### Evaluate Individual Models

```bash
# Decision Stump
mvn exec:java -Dexec.mainClass="org.example.DecisionStumpEvaluation"

# Gaussian Processes
mvn exec:java -Dexec.mainClass="org.example.GaussianProcessesEvaluation"

# KNN
mvn exec:java -Dexec.mainClass="org.example.KNNEvaluation"

# M5P
mvn exec:java -Dexec.mainClass="org.example.M5PEvaluation"

# Multilayer Perceptron
mvn exec:java -Dexec.mainClass="org.example.MultilayerPerceptronEvaluation"

# REPTree
mvn exec:java -Dexec.mainClass="org.example.REPTreeEvaluation"

# SMOreg
mvn exec:java -Dexec.mainClass="org.example.SMOregEvaluation"
```

## JVM Parameters

If you encounter memory issues when running models with large datasets, you can increase the heap size:

```bash
export MAVEN_OPTS="-Xmx4g"
```

This will allocate 4GB of heap memory to the JVM. You can adjust this value according to your computer's configuration.

## Visualizing Results with Python

After running `ModelVisualization`, the `predictions.csv` file will be created in the `results/` directory. You can use Python to visualize:

1. Install the necessary libraries:
```bash
pip install pandas matplotlib seaborn
```

2. Create a `visualize.py` file with the content as suggested in the output of `ModelVisualization.java`

3. Run the Python script:
```bash
python visualize.py
```

## Implemented Models

- Linear Regression
- Decision Stump
- REPTree
- M5P
- Random Forest
- K-Nearest Neighbors (K=5)
- Multilayer Perceptron
- Support Vector Regression (SMOreg)
- Gaussian Processes

## Evaluation Metrics

- Mean Absolute Error (MAE)
- Root Mean Squared Error (RMSE)
- Correlation Coefficient
- Runtime