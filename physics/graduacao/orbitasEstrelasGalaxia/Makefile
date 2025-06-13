CXX = g++
CXXFLAGS = -std=c++17 -Wall -O2
LDFLAGS = 
TARGET = galaxy_sim
SRC = orbitasEstrelasGalaxia.cpp

.PHONY: all clean run plot

all: $(TARGET)

$(TARGET): $(SRC)
	$(CXX) $(CXXFLAGS) -o $@ $^ $(LDFLAGS)

run: $(TARGET)
	./$(TARGET)

plot:
	gnuplot plot.gp
	display galaxy_plot.png &

clean:
	rm -f $(TARGET) bulge.dat disk.dat halo.dat arms.dat plot.gp galaxy_plot.png
