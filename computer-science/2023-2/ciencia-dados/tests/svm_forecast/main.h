#ifndef _MAIN_H_
#define _MAIN_H_

typedef struct {
    char date[20];
    double price;
    double subjectiveFeature1;
    double subjectiveFeature2;
    double subjectiveFeature3;
    double subjectiveFeature4;
    // Add features as needed
    double constant1;
    double constant2;
    double constant3;
    double constant4;
} TimeSeries;

typedef struct {
    double **X;
    double *y;
    int n_samples;
    int n_features;
} TabularData;

typedef struct {
    char date[20];
    double predictedPrice;
} Forecast;

void readData(const char *filename, TimeSeries **data, int *numEntries);

void configureSVMProblem(const TimeSeries *data, int numEntries, struct svm_problem *prob);
struct svm_model* trainAndEvaluateSVM(const TimeSeries *data, int numEntries, struct svm_model **modelPtr);

TabularData univariate_to_tabular(TimeSeries *series, int n_samples);
Forecast* forecast(const double **data, int numEntries, int numSteps, struct svm_model *model);

void free_tabular_data(TabularData *tabular_data);

void saveResultsToFile(const char *filename, Forecast *observedResults, Forecast *svmResults, Forecast *mlpResults, Forecast *rfResults, int length);

void show_menu();

#endif // _MAIN_H_