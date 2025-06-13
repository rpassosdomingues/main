
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
