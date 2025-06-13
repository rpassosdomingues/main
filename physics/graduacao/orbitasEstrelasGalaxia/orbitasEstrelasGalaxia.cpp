#include <iostream>
#include <fstream>
#include <cmath>
#include <vector>

const double PI = 3.14159265358979323846;

struct StarData {
    std::vector<double> x, y, z;
};

void simulate_orbits() {
    // Simulation parameters
    const int n_steps = 100000;
    const double dt = 0.001;
    
    // Initialize star data
    StarData bulge, disk, halo;
    
    // Initial conditions
    double sb0 = 1.0, phib0 = 0.0, zb0 = 0.0;
    double vsb0 = 0.5, vpb0 = 10.0, vzb0 = 50.0;
    
    double sd0 = 7.5, phid0 = 0.0, zd0 = 0.0;
    double vsd0 = 0.5, vpd0 = 100.0, vzd0 = 50.0;
    
    double sh0 = 15.2, phih0 = 0.0, zh0 = 2*15.2*sqrt(1-0.99*0.99);
    double vsh0 = 0.5, vph0 = 50.0, vzh0 = 50.0;
    
    // Angular momentum
    double lb = sb0 * vpb0;
    double ld = sd0 * vpd0;
    double lh = sh0 * vph0;
    
    // Simulation loop
    for (int i = 0; i < n_steps; ++i) {
        // Update positions (simplified)
        phib0 += dt * (lb / (sb0*sb0));
        phid0 += dt * (ld / (sd0*sd0));
        phih0 += dt * (lh / (sh0*sh0));
        
        zb0 += dt * vzb0;
        zd0 += dt * vzd0;
        zh0 += dt * vzh0;
        
        // Convert to Cartesian coordinates
        double xb = sb0 * cos(phib0);
        double yb = sb0 * sin(phib0);
        
        double xd = sd0 * cos(phid0);
        double yd = sd0 * sin(phid0);
        
        double xh = sh0 * cos(phih0);
        double yh = sh0 * sin(phih0);
        
        // Store positions
        bulge.x.push_back(xb);
        bulge.y.push_back(yb);
        bulge.z.push_back(zb0);
        
        disk.x.push_back(xd);
        disk.y.push_back(yd);
        disk.z.push_back(zd0);
        
        halo.x.push_back(xh);
        halo.y.push_back(yh);
        halo.z.push_back(zh0);
    }
    
    // Write data to files
    std::ofstream bulge_file("bulge.dat");
    std::ofstream disk_file("disk.dat");
    std::ofstream halo_file("halo.dat");
    
    for (size_t i = 0; i < bulge.x.size(); ++i) {
        bulge_file << bulge.x[i] << " " << bulge.y[i] << " " << bulge.z[i] << "\n";
        disk_file << disk.x[i] << " " << disk.y[i] << " " << disk.z[i] << "\n";
        halo_file << halo.x[i] << " " << halo.y[i] << " " << halo.z[i] << "\n";
    }
    
    // Create spiral arms data
    std::ofstream arms_file("arms.dat");
    double a0 = 2.8;
    double m = 0.8;
    double i_angle = 14.0 / 57.3;
    double psi0 = 0.0;
    
    for (double s = 0.1; s < 18.0; s += 0.1) {
        double psi = (m / tan(i_angle)) * log(s / a0) + psi0;
        arms_file << s * cos(psi) << " " << s * sin(psi) << " 0\n";
        arms_file << -s * cos(psi) << " " << -s * sin(psi) << " 0\n";
    }
    
    // Create gnuplot script
    std::ofstream plot_file("plot.gp");
    plot_file << R"(
set term pngcairo size 1000,800 enhanced font 'Verdana,10'
set output 'galaxy_plot.png'
set xlabel 'X [kpc]'
set ylabel 'Y [kpc]'
set zlabel 'Z [kpc]'
set xrange [-15:15]
set yrange [-15:15]
set zrange [-7:7]
set title 'Orbitas Estelares na Galaxia'
set view 60, 30

splot 'arms.dat' with points pt 7 ps 0.5 lc rgb 'steelblue' title 'Bracos Espirais', \
      'bulge.dat' with points pt 7 ps 1 lc rgb 'black' title 'Estrelas do Bojo', \
      'disk.dat' with points pt 7 ps 1 lc rgb 'navy' title 'Estrelas do Disco', \
      'halo.dat' with points pt 7 ps 1 lc rgb 'red' title 'Estrelas do Halo'
)";
    
    std::cout << "Simulation complete. Run 'gnuplot plot.gp' to generate the plot.\n";
}

int main() {
    simulate_orbits();
    return 0;
}
