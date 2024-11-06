/*
 * Comparative Performance Analysis of Sorting Algorithms
 *
 * Author: Rafael Passos Domingues
 *     RA: 2023.1.08.036
 * 
 * The code at hand performs a performance analysis to compare non-recursive sorting methods
 * and understand their differences. To do so, it builds vectors of varying dimensions, filled
 * with non-repeated random numbers, and sorts them in three ways: in ascending, random and
 * descending order.
 * 
 * During sorting, the uses of each vector are counted, allowing a comparison between the methods.
 * The comparison is performed between the three implemented methods, using vector sizes ranging
 * from 100 to 10'000 units at regular intervals of 100 units.
 * 
 * The code generates an output file called "sort_usages.csv", containing the data obtained.
 * These values can be used to create graphs that make up the report "comparison_sort_algorithmis.pdf",
 * which is part of this repository.
*/

#include <iostream>
#include <fstream>
#include <ctime>

using namespace std;

// Function to perform Bubble Sort
int bubbleSort(int array[], int length) {
  int bubbleUsage = 0; // Counter for Bubble Sort usage
  int i, j;
  for (i = 0; i < length - 1; i++) {
    bubbleUsage += 2; // Increment counter for Bubble Sort usage
    for (j = 0; j < length - i - 1; j++) {
      if (array[j] > array[j + 1]) {
        int tmp = array[j];
        array[j] = array[j + 1];
        array[j + 1] = tmp;
      }
      bubbleUsage += 4;
    }
  }
  return bubbleUsage;
}

// Function to perform Insertion Sort
int insertionSort(int array[], int length) {
  int insertionUsage = 0; // Counter for Insertion Sort usage
  int i, j;
  for (i = 1; i < length; i++) {
    int handle = array[i];
    insertionUsage += 2; // Increment counter for Insertion Sort usage
    for (j = i - 1; j >= 0 && array[j] > handle; j--) {
      array[j + 1] = array[j];
      insertionUsage += 3;
    }
    array[j + 1] = handle;
  }
  return insertionUsage;
}

// Function to perform Selection Sort
int selectionSort(int array[], int length) {
  int selectionUsage = 0; // Counter for Selection Sort usage
  int i, j;
  for (i = 0; i < length - 1; i++) {
    int minIndex = i;
    selectionUsage += 2; // Increment counter for Selection Sort usage
    for (j = i + 1; j < length; j++) {
      if (array[j] < array[minIndex]) {
        minIndex = j;
      }
    }
    int swap = array[i];
    array[i] = array[minIndex];
    array[minIndex] = swap;
    selectionUsage += 4;
  }
  return selectionUsage;
}

// Function to generate an ordered array and shuffle it
void randomArrayGenerator(int start, int length, int randomArray[]) {
    for (int i = 0; i < length; i++) {
        randomArray[i] = i;
    }

    // shuffle array
    for (int i = length - 1; i > 0; i--) {
        int j = rand() % (i + 1);
        int temp = randomArray[i];
        randomArray[i] = randomArray[j];
        randomArray[j] = temp;
    }

    for (int i = 0; i < length; i++) {
        randomArray[i] += start;
    }
}

// Function to generate increasing array
void increasingArrayGenerator(int array[], int start, int length) {
  for (int i = 0; i < length; i++) {
    array[i] = start + i;
  }
}

// Function to generate decreasing array
void decreasingArrayGenerator(int array[], int start, int length) {
  for (int i = 0; i < length; i++) {
    array[i] = start + length - i - 1;
  }
}
 
/*
 * This function copy the elements of one array to another ensures that each sorting
 * algorithm operates on a separate copy of the original matrix, avoiding interference
 * between algorithm runs.
*/
void copyArray(int source[], int destination[], int length) {
  for (int i = 0; i < length; i++) {
    destination[i] = source[i];
  }
}

// Function to save results to a CSV file
void saveResultsToFile(const string& filename, const int sizes[], const int randomBubbleUsages[],
                       const int increasingBubbleUsages[], const int decreasingBubbleUsages[],
                       const int randomInsertionUsages[], const int increasingInsertionUsages[],
                       const int decreasingInsertionUsages[], const int randomSelectionUsages[],
                       const int increasingSelectionUsages[], const int decreasingSelectionUsages[],
                       int size) {
  ofstream file(filename);
  if (!file.is_open()) {
    cout << "Failed to create the file." << endl;
    return;
  }

  // Write header to the file
  file << "Array Size" << "," <<
    "Random Bubble Sort Usage" << "," << "Increasing Bubble Sort Usage" << "," << "Decreasing Bubble Sort Usage" << "," <<
    "Random Insertion Sort Usage" << "," << "Increasing Insertion Sort Usage" << "," << "Decreasing Insertion Sort Usage" << "," <<
    "Random Selection Sort Usage" << "," << "Increasing Selection Sort Usage" << "," << "Decreasing Selection Sort Usage" << endl;

  // Write data to the file
  for (int i = 0; i < size; i++) {
    file << sizes[i] << "," <<
      randomBubbleUsages[i] << "," << increasingBubbleUsages[i] << "," << decreasingBubbleUsages[i] << "," <<
      randomInsertionUsages[i] << "," << increasingInsertionUsages[i] << "," << decreasingInsertionUsages[i] << "," <<
      randomSelectionUsages[i] << "," << increasingSelectionUsages[i] << "," << decreasingSelectionUsages[i] << endl;
  }

  file.close();
}

int main(void) {
  const int start = 100;
  const int end = 10000;
  const int step = 100;
  const int numSizes = (end - start) / step + 1;

  int sizes[numSizes];

  int randomBubbleUsages[numSizes];
  int increasingBubbleUsages[numSizes];
  int decreasingBubbleUsages[numSizes];

  int randomInsertionUsages[numSizes];
  int increasingInsertionUsages[numSizes];
  int decreasingInsertionUsages[numSizes];

  int randomSelectionUsages[numSizes];
  int increasingSelectionUsages[numSizes];
  int decreasingSelectionUsages[numSizes];

  int currentSize = start;
  for (int i = 0; i < numSizes; i++) {
    sizes[i] = currentSize;
    currentSize += step;
  }

  for (int i = 0; i < numSizes; i++) {
    int length = sizes[i];

    // Random Array
    int randomArray[length];
    randomArrayGenerator(start, length, randomArray);

    int sortArray[length];

    // Bubble Sort
    copyArray(randomArray, sortArray, length);
    randomBubbleUsages[i] = bubbleSort(sortArray, length);

    // Insertion Sort
    copyArray(randomArray, sortArray, length);
    randomInsertionUsages[i] = insertionSort(sortArray, length);

    // Selection Sort
    copyArray(randomArray, sortArray, length);
    randomSelectionUsages[i] = selectionSort(sortArray, length);

    // Increasing Array
    increasingArrayGenerator(sortArray, start, length);
    increasingBubbleUsages[i] = bubbleSort(sortArray, length);
    copyArray(randomArray, sortArray, length);
    increasingInsertionUsages[i] = insertionSort(sortArray, length);
    copyArray(randomArray, sortArray, length);
    increasingSelectionUsages[i] = selectionSort(sortArray, length);

    // Decreasing Array
    decreasingArrayGenerator(sortArray, start, length);
    decreasingBubbleUsages[i] = bubbleSort(sortArray, length);
    copyArray(randomArray, sortArray, length);
    decreasingInsertionUsages[i] = insertionSort(sortArray, length);
    copyArray(randomArray, sortArray, length);
    decreasingSelectionUsages[i] = selectionSort(sortArray, length);
  }

  // Save results to a file
  saveResultsToFile("sort_usages.csv", sizes,
    randomBubbleUsages, increasingBubbleUsages, decreasingBubbleUsages,
    randomInsertionUsages, increasingInsertionUsages, decreasingInsertionUsages,
    randomSelectionUsages, increasingSelectionUsages, decreasingSelectionUsages,
    numSizes);

  cout << "File 'sort_usages.csv' created successfully." << endl;

  return 0;
}
