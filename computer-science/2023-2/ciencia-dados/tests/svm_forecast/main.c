/**
 * ____________________________________________________________
 *             --- Time series forecasting ---
 * ------------------------------------------------------------
 * Using a combination of methods including feature extraction,
 * training a support vector machine (SVM) model,
 * and forecasting with a sliding window.
 * ------------------------------------------------------------
 * Author: Rafael Passos Domingues
 * ____________________________________________________________
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>
#include <libsvm/svm.h>

#include "main.h"

#define WINDOW_SIZE 4

// Function to read data from CSV file
void readData(const char *filename, TimeSeries **data, int *numEntries) {
    FILE *file = fopen(filename, "r");
    if (file == NULL) {
        perror("\n\tFile not found.\n\n");
        exit(EXIT_FAILURE);
    }

    // Counts the number of lines in the file
    *numEntries = 0;
    char buffer[256];
    // Skip the header line
    if (fgets(buffer, sizeof(buffer), file) == NULL) {
        fprintf(stderr, "\n\tError reading header from file.\n\n");
        exit(EXIT_FAILURE);
    }

    while (fgets(buffer, sizeof(buffer), file) != NULL) {
        (*numEntries)++;
    }

    // Back to start of the file
    fseek(file, 0, SEEK_SET);

    // Skip the header line again
    if (fgets(buffer, sizeof(buffer), file) == NULL) {
        fprintf(stderr, "\n\tError reading header from file.\n\n");
        exit(EXIT_FAILURE);
    }

    // Allocating memory space to store the data
    *data = (TimeSeries *)malloc(*numEntries * sizeof(TimeSeries));
    if (*data == NULL) {
        perror("\n\tMemory allocation error.\n\n");
        exit(EXIT_FAILURE);
    }

    // Read Data from file to fill struct
    for (int i = 0; i < *numEntries; i++) {
        if (fscanf(file, "%19[^,],%lf\n", (*data)[i].date, &(*data)[i].price) != 2) {
            fprintf(stderr, "\n\tError reading data from file.\n\n");
            exit(EXIT_FAILURE);
        }
    }

    fclose(file);
}

// Helper function to configure the svm_problem structure
void configureSVMProblem(const TimeSeries *data, int numEntries, struct svm_problem *prob) {
    prob->l = numEntries;
    prob->y = (double *)malloc(numEntries * sizeof(double));
    prob->x = (struct svm_node **)malloc(numEntries * sizeof(struct svm_node *));

    for (int i = 0; i < numEntries; i++) {
        prob->x[i] = (struct svm_node *)malloc(4 * sizeof(struct svm_node));
        for (int j = 0; j < 4; j++) {
            prob->x[i][j].index = j + 1;
            prob->x[i][j].value = data[i].constant1 + j;
        }
        prob->y[i] = data[i].price;
    }
}

// Function to train and evaluate SVM model
struct svm_model* trainAndEvaluateSVM(const TimeSeries *data, int numEntries, struct svm_model **modelPtr) {
    // Set SVM parameters
    struct svm_parameter param;
    param.svm_type = EPSILON_SVR;
    param.kernel_type = RBF;
    param.eps = 0.001;
    param.C = 1.0;

    // Configure the svm_problem structure
    struct svm_problem prob;
    configureSVMProblem(data, numEntries, &prob);

    // Train SVM model
    struct svm_model *local_model = svm_train(&prob, &param);
    *modelPtr = local_model;

    // Evaluate the SVM model
    double predictValue = svm_predict(*modelPtr, prob.x[0]);
    printf("SVM Prediction: %f\n", predictValue);

    // Free allocated memory
    for (int i = 0; i < numEntries; i++) {
        free(prob.x[i]);
    }
    free(prob.x);
    free(prob.y);

    return *modelPtr;
}

// Function to convert univariate time series to tabular format
TabularData univariate_to_tabular(TimeSeries *series, int n_samples) {
    TabularData tabular_data;
    tabular_data.n_samples = n_samples - WINDOW_SIZE;
    tabular_data.n_features = WINDOW_SIZE;
    tabular_data.X = (double **)malloc(tabular_data.n_samples * sizeof(double *));
    tabular_data.y = (double *)malloc(tabular_data.n_samples * sizeof(double));

    for (int i = 0; i < tabular_data.n_samples; ++i) {
        tabular_data.X[i] = (double *)malloc(WINDOW_SIZE * sizeof(double));
        tabular_data.y[i] = series[i + WINDOW_SIZE].price;
        for (int j = 0; j < WINDOW_SIZE; ++j) {
            tabular_data.X[i][j] = series[i + j].price;
        }
    }

    return tabular_data;
}

// Function to predict future values using sliding window with SVM model
Forecast* forecast(const double **data, int numEntries, int numSteps, struct svm_model *model) {
    // Convert univariate time series to tabular format
    TabularData tabular_data = univariate_to_tabular(data, numEntries);

    // Allocate space to store predicted data
    Forecast *forecastData = (Forecast *)malloc(numSteps * sizeof(Forecast));
    if (forecastData == NULL) {
        perror("\n\tMemory allocation error.\n\n");
        exit(EXIT_FAILURE);
    }

    // Loop to predict each future step
    for (int i = 0; i < numSteps; i++) {
        // Prepare input vector for prediction
        struct svm_node x[windowSize + 1]; // +1 for the end-of-data indicator
        for (int j = 0; j < windowSize; ++j) {
            x[j].index = j + 1;
            x[j].value = tabular_data.X[i][j];
        }
        x[windowSize].index = -1; // Indicates the end of data

        // Predict using the SVM model
        double prediction = svm_predict(model, x);

        // Store the predicted value along with the date
        strcpy(forecastData[i].date, data[numEntries - windowSize + i].date);
        forecastData[i].predictedPrice = prediction;
    }

    // Free allocated memory for tabular data
    free_tabular_data(&tabular_data);

    return forecastData;
}

// Function to free the memory allocated for tabular data
void free_tabular_data(TabularData *tabular_data) {
    for (int i = 0; i < tabular_data->n_samples; ++i) {
        free(tabular_data->X[i]);
    }
    free(tabular_data->X);
    free(tabular_data->y);
}

// Function to save results to a file
void saveResultsToFile(const char *filename, Forecast *observedResults, Forecast *svmResults, Forecast *mlpResults, Forecast *rfResults, int length) {
    FILE *outputFile = fopen(filename, "w");
    if (outputFile == NULL) {
        perror("\n\tError opening output file.\n\n");
        exit(EXIT_FAILURE);
    }

    // Write header
    fprintf(outputFile, "Date,Observed,SVM,MLP,RandomForest\n");

    // Write data
    for (int i = 0; i < length; ++i) {
        fprintf(outputFile, "%s,%f,%f,%f,%f\n", observedResults[i].date, observedResults[i].predictedPrice, svmResults[i].predictedPrice, mlpResults[i].predictedPrice, rfResults[i].predictedPrice);
    }

    // Close the output file
    fclose(outputFile);

    printf("\nResults saved to %s.\n", filename);
}

// Function to show the menu
void show_menu() {
    printf("\n=========================================");
    printf("\n\tMenu:\n");
    printf("\n=========================================");
    printf("0. Exit\n");
    printf("1. Print observed results\n");
    printf("2. Print SVM forecast results\n");
    printf("3. Print MLP forecast results\n");
    printf("4. Print Random Forest forecast results\n");
    printf("5. Save forecast results to file\n");
    printf("\n=========================================");
}

int main() {
    const char *filename = "../data/input.csv";
    TimeSeries *data;
    int numEntries;

    // Load data from file
    readData(filename, &data, &numEntries);

    // Train SVM model
    struct svm_model *svm_model;
    trainAndEvaluateSVM(data, numEntries, &svm_model);

    // Convert univariate time series to tabular format
    TabularData tabular_data = univariate_to_tabular(&data[0], numEntries);

    // Split training and test sets
    double **X_train, **X_test, *y_train, *y_test;
    double test_size = 0.3;
    train_test_split(tabular_data, &X_train, &X_test, &y_train, &y_test, test_size);

    // Train models
    struct svm_model *svm_model_trained, *mlp_model, *rf_model;
    train_models(tabular_data, &svm_model_trained, &mlp_model, &rf_model);

    // Predict future values with sliding window
    int forecast_steps = 5;

    // Use X_test[0] instead of X_test to pass a single array
    Forecast *svm_forecast = forecast((const double **)X_test, tabular_data.n_samples, forecast_steps, svm_model_trained);
    Forecast *mlp_forecast = forecast((const double **)X_test, tabular_data.n_samples, forecast_steps, mlp_model);
    Forecast *rf_forecast = forecast((const double **)X_test, tabular_data.n_samples, forecast_steps, rf_model);

    int choice;
    do {
        // Show the menu
        show_menu();
        printf("Choose an option: ");
        scanf("%d", &choice);

        switch (choice) {
            case 0:
                printf("\nExiting...\n");
                // Free allocated memory
                free_tabular_data(&tabular_data);
                free(data);
                svm_free_and_destroy_model(&svm_model_trained);
                return 0;
            case 1:
                printf("\nObserved Results:\n");
                for (int i = 0; i < forecast_steps; ++i) {
                    printf("%f ", y_test[i]);
                }
                break;
            case 2:
                printf("\nSVM Forecast Results:\n");
                for (int i = 0; i < forecast_steps; ++i) {
                    printf("%f ", svm_forecast[i]);
                }
                break;
            case 3:
                printf("\nMLP Forecast Results:\n");
                for (int i = 0; i < forecast_steps; ++i) {
                    printf("%f ", mlp_forecast[i]);
                }
                break;
            case 4:
                printf("\nRandom Forest Forecast Results:\n");
                for (int i = 0; i < forecast_steps; ++i) {
                    printf("%f ", rf_forecast[i]);
                }
                break;
            case 5:
                saveResultsToFile("../data/output.csv", (Forecast *)y_test, svm_forecast, mlp_forecast, rf_forecast, forecast_steps);
                break;
            default:
                printf("\nInvalid option.\n");
        }
    } while (choice != 0);

}
