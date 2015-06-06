#编译opencv程序
g++ rgb.cpp `pkg-config --cflags opencv `  `pkg-config --libs opencv` -o myRGB
