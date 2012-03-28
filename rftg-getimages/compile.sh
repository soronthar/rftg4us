gcc getimages.c -I/usr/include/gtk-2.0/ -I/usr/include/glib-2.0/ -I/usr/include/cairo/ -I/usr/include/pango-1.0/ -Wall -g -o getimages `pkg-config --cflags --libs glib-2.0  gtk+-2.0`
