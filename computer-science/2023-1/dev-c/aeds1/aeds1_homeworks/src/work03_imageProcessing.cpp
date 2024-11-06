/*
* Images Processing
* Author: Rafael Passos Domingues
*     RA: 2023.1.08.036
*
  * This C++ application reads an image in PGM format as input.
  * It then creates an interactive menu where the user can choose different operations to be performed
  * on the input image. Each operation is implemented as a separate C++ function.
 
* The available operations are as follows:
  * 1. Darken or lighten the image by a factor entered by the user.
  * 2. Rotate the image to the right.
  * 3. Rotate the image to the left.
  * 4. Binarize the image based on a threshold entered by the user.
  * 5. Iconize the image, i.e. reduce the input image to a 64x64 pixel image.
  * 6. Apply a low-pass filter to smooth the image.

* After performing each operation, the application writes the resulting image to a separate text file.
* In addition, all operations ensure that the pixel gray values are within the range of 0 to a
* specified maximum value.
*/

#include <cstdlib>
#include <iostream>
#include <fstream>

using namespace std;

typedef int tImage[1000][1000]; // Image data type

string error; // Global variable to store error messages

// Load the image in pgm format and checks if the image is in p2 format
int uploadPGM(string name, tImage image, int* lines, int* columns, int* tones) {
  string type;
  ifstream file(name);
  
  if (!file.is_open()) {
    error = "Error: File not found.";
    return 1;
  }

  file >> type;
  if (type != "P2") {
    error = "Error: The file does not have the P2 format.";
    return 2;
  }

  file >> *columns >> *lines >> *tones;
  for (int i = 0; i < *lines; i++) {
    for (int j = 0; j < *columns; j++) {
      file >> image[i][j];
    }
  }
  file.close();
  return 0;
}

// Save the image after it has been processed
int savePGM(string name, tImage image, int lines, int columns, int tones) {
  ofstream file(name);
  
  if (!file.is_open()) {
    error = "Error: File not found.";
    return 1;
  }

  file << "P2" << endl << columns << " " << lines << endl << tones << endl;
  for (int i = 0; i < lines; i++) {
    for (int j = 0; j < columns; j++) {
      file << image[i][j] << " ";
    }
    file << endl;
  }
  file.close();
  return 0;
}

/*
 * Adjusts the brightness of an image represented by an array of pixels.
 * The brightness is changed by adding a value called "brightnessFactor" to each pixel.
 * Then the pixel values are checked to make sure they are within the allowed limits (0 to "maxTones").
 * The result is stored in another array called "outputImage".
*/
void adjustBrightness(tImage inputImage, tImage outputImage, int lines, int columns, int brightnessFactor, int maxTones) {
  for (int i = 0; i < lines; i++) {
    for (int j = 0; j < columns; j++) {
      outputImage[i][j] = inputImage[i][j] + brightnessFactor;

      if (outputImage[i][j] < 0) {
        outputImage[i][j] = 0;
      } else if (outputImage[i][j] > maxTones) {
        outputImage[i][j] = maxTones;
      }
    }
  }
}

/*
 * It rotates 90 degrees to the right creating an output image represented by the
 * array "outputImage".
 * This array has its dimensions swapped: the number of rows becomes equal to the number
 * of columns of the original image and the number of columns will be equal to the number of rows
 * of the original image. 
*/
void rotateRight(tImage inputImage, tImage outputImage, int lines, int columns) {
  for (int i = 0; i < lines; i++) {
    for (int j = 0; j < columns; j++) {
      outputImage[j][lines - 1 - i] = inputImage[i][j];
    }
  }
}

/*
 * It rotates 90 degrees to the left creating an output image
 * represented by the array "outputImage".
*/
void rotateLeft(tImage inputImage, tImage outputImage, int lines, int columns) {
  for (int i = 0; i < lines; i++) {
    for (int j = 0; j < columns; j++) {
      outputImage[columns - 1 - j][i] = inputImage[i][j];
    }
  }
}

/*
 * Transforms the image into black and white,
 * where pixels with intensities above a threshold
 * are defined as white, and pixels with intensities
 * at or below the threshold are defined as black.
*/
void binarize(tImage inputImage, tImage outputImage, int lines, int columns, int tones, int threshold) {
  for (int i = 0; i < lines; i++) {
    for (int j = 0; j < columns; j++) {
      if (inputImage[i][j] <= threshold) {
        outputImage[i][j] = 0; // Set pixel to black
      } else {
        outputImage[i][j] = tones; // Set pixel to white
      }
    }
  }
}

/*
 * Creates a thumbnail version (icon) of an image represented by
 * an array keeping the essence, but with lower resolution.
*/
void iconize(tImage inputImage, tImage outputImage, int lines, int columns) {
  int iconSize = 64;

  for (int i = 0; i < iconSize; i++) {
    for (int j = 0; j < iconSize; j++) {
      int row = i * lines / iconSize;
      int col = j * columns / iconSize;
      outputImage[i][j] = inputImage[row][col];
    }
  }
}

/*
 * It applies a filter that helps to smooth the image, reducing detail and removing noise.
*/
void applyLowPassFilter(tImage inputImage, tImage outputImage, int lines, int columns) {
  for (int i = 1; i < lines - 1; i++) {
    for (int j = 1; j < columns - 1; j++) {
      // Calculate the average of the surrounding pixels
      outputImage[i][j] = (inputImage[i - 1][j - 1] + inputImage[i - 1][j] + inputImage[i - 1][j + 1] +
                          inputImage[i][j - 1] + inputImage[i][j] + inputImage[i][j + 1] +
                          inputImage[i + 1][j - 1] + inputImage[i + 1][j] + inputImage[i + 1][j + 1]) / 9;
    }
  }
}

/*
 * Interactive menu to apply the operations on an image represented by a array (PGM image).
 * The user can choose one of the available options to manipulate the image,
 * such as adjusting the brightness, rotating the image, binarizing, creating an image icon
 * and applying a low-pass filter.
*/
void menu(tImage inputImage, int lines, int columns, int tones) {
  int choice;
  string outputFileName;
  
  cout << "---------------------------\n";
  cout << "\t Menu \t \n";
  cout << "---------------------------\n";
  cout << "1. Adjust brightness\n";
  cout << "2. Rotate the image right\n";
  cout << "3. Rotate the image left\n";
  cout << "4. Binarize the image\n";
  cout << "5. Iconize the image\n";
  cout << "6. Apply a low-pass filter\n";
  cout << "---------------------------\n";
  cout << "\u2713 Enter your choice (1-6): ";
  cin >> choice;
  while (choice < 1 || choice > 6) {
    cout << "Sorry, try again: ";
    cin >> choice;
  }
  
  tImage outputImage; // New matrix to store the result
  
  switch (choice) {
    case 1:
      // Adjust brightness of the image
      int brightnessFactor;
      cout << "\n\t \u2600 Enter the brightness adjustment factor: ";
      cin >> brightnessFactor;

      adjustBrightness(inputImage, outputImage, lines, columns, brightnessFactor, tones);
      cout << "\n Brightness adjustment applied.\n";
      break;
    case 2:
      // Rotate the image to the right
      rotateRight(inputImage, outputImage, lines, columns);
      cout << "\n Image rotated to the right.\n";
      break;
    case 3:
      // Rotate the image left
      rotateLeft(inputImage, outputImage, lines, columns);
      cout << "\n Image rotated to the left.\n";
      break;
    case 4:
      // Binarize the image
      int threshold;
      cout << "\n\t \u25CF Enter the binarization threshold: ";
      cin >> threshold;
      
      binarize(inputImage, outputImage, lines, columns, tones, threshold);
      cout << "\n Image binarized.\n";
      break;
    case 5:
      // Iconize the image
      iconize(inputImage, outputImage, lines, columns);
      cout << "\n Image iconized.\n";
      break;
    case 6:
      // Apply low-pass filter to the image
      applyLowPassFilter(inputImage, outputImage, lines, columns);
      cout << "\n Low-pass filter applied.\n";
      break;
    default:
      cout << "Invalid choice. Please try again.\n";
      return;
  }
  
  // Write the output image file
  cout << "\n \u2193 \U0001F304 Enter the output image name: ";
  cin >> outputFileName;
  outputFileName = outputFileName + ".pgm";
  if (savePGM(outputFileName, outputImage, columns, lines, tones) != 0) {
    cout << "\n" + error + "\n";
    return;
  }
}

/*
 * Processes a PGM format image read from a file and then calls the
 * "menu" function to allow the user to make adjustments to the image.
*/
int main() {
  tImage inputImage;
  int columns = 10, lines = 10, tones;
  string inputFile;

  // Read input image file
  cout << "\n \u2191 \U0001F304 Enter the input image name: ";
  cin >> inputFile;
  inputFile = inputFile + ".pgm";
  if (uploadPGM(inputFile, inputImage, &lines, &columns, &tones) != 0) {
    cout << "\n" + error + "\n";
    return 1;
  }

  // Call the menu function to process user's choice
  menu(inputImage, lines, columns, tones);
  
  return 0;
}
