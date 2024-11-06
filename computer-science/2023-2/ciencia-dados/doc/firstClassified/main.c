/**
 * ____________________________________________
 *      --- Simple binary classifier ---
 * Using a neural network with a single neuron
 * --------------------------------------------
 * Author: Rafael Passos Domingues
 * ____________________________________________
*/

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <math.h>

#include "main.h"

#define MAX_LINE_LENGTH 1024

void readCSV(const char *filename, Instance **instances, int *numInstances) {
    FILE *file = fopen(filename, "r");
    if (file == NULL) {
        perror("\n\tFile not found.\n");
        exit(EXIT_FAILURE);
    }

    char line[MAX_LINE_LENGTH];

    // Counts the number of instances in the file
    *numInstances = 0;
    while (fgets(line, sizeof(line), file) != NULL) {
        (*numInstances)++;
    }

    // Go back to the beginning of the file
    fseek(file, 0, SEEK_SET);

    // Allocates memory to store instances
    *instances = (Instance *)malloc(*numInstances * sizeof(Instance));
    if (*instances == NULL) {
        perror("\n\tMemory Allocation Error.\n");
        exit(EXIT_FAILURE);
    }

    // Read file instances
    for (int i = 0; i < *numInstances; i++) {
        fgets(line, sizeof(line), file);

        // Tokenize the line based on commas
        char *token = strtok(line, ",");
        
        // Read target
        sscanf(token, "%d", &(*instances)[i].target);

        // Read features
        for (int j = 0; j < 30; j++) {
            token = strtok(NULL, ",");
            sscanf(token, "%f", &(*instances)[i].features[j]);
        }
    }

    fclose(file);
}

/**
 * ---------------------------------------------
 * Sigmoid Activation Function
 * ---------------------------------------------
 * Narayan, S. (1997).
 * 
 * The generalized sigmoid activation function:
 * Competitive supervised learning.
 * 
 * Information sciences, 99(1-2), 69-82.
 * ---------------------------------------------
*/
double sigmoid(double x) {
    return 1.0 / (1.0 + exp(-x));
}

/**
 * ---------------------------------------------
 * Binary Cross Entropy Loss Function
 * ---------------------------------------------
 * Ruby, U., & Yendapalli, V. (2020).
 * 
 * Binary cross entropy with deep learning
 * technique for image classification.
 * 
 * Int. J. Adv. Trends Comput. Sci. Eng, 9(10).
 * ---------------------------------------------
*/
double binary_cross_entropy(double y_true, double y_pred) {
    return -((y_true * log(y_pred)) + ((1 - y_true) * log(1 - y_pred)));
}

// Initialize the binary classifier
void initialize_classifier(BinaryClassifier* model) {
    model->weight = ((double)rand() / RAND_MAX) * 2.0 - 1.0;  // Random initialization between -1 and 1
    model->bias = 0.0;
}

// Perform the forward pass
double forward_pass(BinaryClassifier* model, double x) {
    return sigmoid(model->weight * x + model->bias);
}

/**
 * -------------------------------------------------------
 * Binary classifier using gradient descent
 * -------------------------------------------------------
 * Pendharkar, P. C. (2007).
 * 
 * A comparison of gradient ascent, gradient descent
 * and genetic‐algorithm‐based artificial neural networks
 * for the binary classification problem.
 * 
 * Expert Systems, 24(2), 65-86.
 * -------------------------------------------------------
*/
void train_classifier(BinaryClassifier* model, double x, double y_true, double learning_rate, int epochs, FILE *outputFile) {
    printf("\n=========================");
    printf("\n Loss depending season");
    printf("\n-------------------------\n");
    for (int epoch = 0; epoch < epochs; ++epoch) {
        // Forward pass
        double y_pred = forward_pass(model, x);

        // Calculate the loss
        double loss = binary_cross_entropy(y_true, y_pred);

        // Calculate gradients
        double gradient_w = (y_pred - y_true) * x;
        double gradient_b = y_pred - y_true;

        // Update parameters using gradient descent
        model->weight -= learning_rate * gradient_w;
        model->bias -= learning_rate * gradient_b;

        // Salvar época e perda no arquivo CSV
        fprintf(outputFile, "%d,%.4f\n", epoch, loss);

        // Display loss every 100 epochs
        if (epoch % 100 == 0) {
            printf("Epoch %d, Loss: %.4f\n", epoch, loss);
        }
    }
    printf("\n=========================");
}

// Calculates accuracy and precision and prints the confusion matrix elements (TP, TN, FP, FN)
void evaluate_classifier(BinaryClassifier* model, Instance* instances, int numInstances) {
    int true_positive = 0, true_negative = 0, false_positive = 0, false_negative = 0;

    for (int i = 0; i < numInstances; ++i) {
        double x_test = instances[i].features[0];
        double y_true_test = instances[i].target;

        // Forward pass to get the predicted label
        double y_pred_test = forward_pass(model, x_test);

        // Convert predicted probability to binary prediction (0 or 1)
        int predicted_label = (y_pred_test >= 0.5) ? 1 : 0;

        // Calculate metrics
        if (predicted_label == 1 && y_true_test == 1) {
            true_positive++;
        } else if (predicted_label == 0 && y_true_test == 0) {
            true_negative++;
        } else if (predicted_label == 1 && y_true_test == 0) {
            false_positive++;
        } else if (predicted_label == 0 && y_true_test == 1) {
            false_negative++;
        }
    }

    // Calculate metrics
    double accuracy = (double)(true_positive + true_negative) / numInstances;
    double precision = (double)true_positive / (true_positive + false_positive);

    printf("\n=========================");
    printf("\nConfusion matrix");
    printf("\n-------------------------");
    printf("\nAccuracy: %.4f", accuracy);
    printf("\nPrecision: %.4f", precision);
    printf("\nTrue Positives: %d", true_positive);
    printf("\nTrue Negatives: %d", true_negative);
    printf("\nFalse Positives: %d", false_positive);
    printf("\nFalse Negatives: %d", false_negative);
    printf("\n=========================\n\n");
}

int main() {
    const char *filename = "../data/input.csv";
    int numInstances;
    Instance *instances;

    readCSV(filename, &instances, &numInstances);

    // Create a binary classifier model
    BinaryClassifier model;
    initialize_classifier(&model);

    // Example training data
    double x_train = 1.0;
    double y_true_train = 0.0;

    // Hyperparameters
    double learning_rate = 0.01;
    int epochs = 1000;

    // Open csv file for write results
    FILE *outputFile = fopen("../data/epochs_losses.csv", "w");
    if (outputFile == NULL) {
        perror("\n\tError opening output file.\n");
        exit(EXIT_FAILURE);
    }

    fprintf(outputFile, "Epoch,Loss\n");

    // Train model
    train_classifier(&model, x_train, y_true_train, learning_rate, epochs, outputFile);

    // Test the trained model
    double y_pred = forward_pass(&model, x_train);
    printf("\n\n >> Result: %.4f\n", y_pred);

    // Test the trained model on the entire dataset
    evaluate_classifier(&model, instances, numInstances);

    fclose(outputFile);

    free(instances);

    return 0;
}
