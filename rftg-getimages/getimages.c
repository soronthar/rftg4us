/*
 * Race for the Galaxy AI
 * 
 * Copyright (C) 2009-2011 Keldon Jones
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

#include <stdlib.h>
#include <gtk/gtk.h>
#include <gdk/gdkkeysyms.h>
#include <stdio.h>
#include <string.h>

/*
 * Number of icon images.
 */
#define MAX_ICON 19

/*
 * Number of action card images.
 */
#define MAX_ACT_CARD   11

/*
 * Number of card designs.
 */
#define MAX_DESIGN 191

/*
 * Number of intermediate goals.
 */
#define MAX_GOAL 20

/*
 * Card images.
 */
static GdkPixbuf *image_cache[MAX_DESIGN];

/*
 * Goal card images.
 */
static GdkPixbuf *goal_cache[MAX_GOAL];

/*
 * Icon images.
 */
static GdkPixbuf *icon_cache[MAX_ICON];

/*
 * Action card images.
 */
static GdkPixbuf *action_cache[MAX_ACT_CARD];

/*
 * Card back image.
 */
static GdkPixbuf *card_back;

/*
 * Load pixbufs with card images from image bundle.
 *
 * The image bundle format is nothing special -- it exists mainly to make
 * it difficult for people to get at the images directly.
 *
 * This was requested by Tom Lehmann.
 */
static void load_image_bundle(void)
{
	GFile *bundle;
	GInputStream *fs, *ms;
	GdkPixbuf **pix_ptr;
	char buf[1024], *data_buf;
	int count, x;
	int type = 0;
	int order = 0;

	g_type_init();

	printf("creating bundle file handle...\n");
	/* Create bundle file handle */
	bundle = g_file_new_for_path("images.data");

	printf("opening file...\n");
	/* Open file for reading */
	fs = G_INPUT_STREAM(g_file_read(bundle, NULL, NULL));

	/* Check for error */
	if (!fs)
	{
		/* Error */
		printf("Can't open raw images or image bundle!\n");
		return;
	}

	printf("reading header...\n");
	/* Read header */
	count = g_input_stream_read(fs, buf, 4, NULL, NULL);

	printf("checking header...");
	/* Check header */
	if (strncmp(buf, "RFTG", 4))
	{
		/* Error */
		printf("Image bundle missing header!\n");
		return;
	} else {
		printf("OK\n");
	}

	printf("looping until end of file...\n");
	/* Loop until end of file */
	while (1)
	{
		printf(".");
		/* Get next type of image */
		count = g_input_stream_read(fs, buf, 1, NULL, NULL);

		/* Check for end of file */
		if (buf[0] == 0) break;

		/* Check for card image */
		if (buf[0] == 1)
		{
			type = 1;
			/* Read card number */
			count = g_input_stream_read(fs, buf, 4, NULL, NULL);

			/* Convert to integer */
			x = strtol(buf, NULL, 10);

			/* Get pointer to pixbuf holder */
			pix_ptr = &image_cache[x];
		}

		/* Check for card back image */
		else if (buf[0] == 2)
		{
			type = 2;
			/* Get pointer to pixbuf */
			pix_ptr = &card_back;
		}

		/* Check for goal image */
		else if (buf[0] == 3)
		{
			type = 3;
			/* Read card number */
			count = g_input_stream_read(fs, buf, 3, NULL, NULL);

			/* Convert to integer */
			x = strtol(buf, NULL, 10);

			/* Get pointer to pixbuf holder */
			pix_ptr = &goal_cache[x];
		}

		/* Check for icon image */
		else if (buf[0] == 4)
		{
			type = 4;
			/* Read card number */
			count = g_input_stream_read(fs, buf, 3, NULL, NULL);

			/* Convert to integer */
			x = strtol(buf, NULL, 10);

			/* Get pointer to pixbuf holder */
			pix_ptr = &icon_cache[x];
		}

		/* Check for action card image */
		else if (buf[0] == 5)
		{
			type = 5;
			/* Read card number */
			count = g_input_stream_read(fs, buf, 3, NULL, NULL);

			/* Convert to integer */
			x = strtol(buf, NULL, 10);

			/* Get pointer to pixbuf holder */
			pix_ptr = &action_cache[x];
		}

		/* Check for something else */
		else
		{
			/* Error */
			printf("Bad image type!\n");
			break;
		}

		/* Read file size */
		count = g_input_stream_read(fs, buf, 8, NULL, NULL);

		/* Convert to integer */
		x = strtol(buf, NULL, 10);

		/* Create buffer for image data */
		data_buf = (char *)malloc(x);

		/* Read into buffer */
		count = g_input_stream_read(fs, data_buf, x, NULL, NULL);

		/* Check for not enough read */
		if (count < x)
		{
			/* Error */
			printf("Did not read enough image data!\n");
			break;
		}

		/* Create memory stream from image data */
		ms = g_memory_input_stream_new_from_data(data_buf, x, NULL);

		/* Read image from file stream */
		*pix_ptr = gdk_pixbuf_new_from_stream(ms, NULL, NULL);

		/* Close memory stream */
		g_input_stream_close(ms, NULL, NULL);

		/* Free memory */
		free(data_buf);

		/* Check for error */
		if (!(*pix_ptr))
		{
			/* Print error */
			printf("Error reading image from bundle!\n");
			break;
		} else {
			//printf("Saving image %d \n", x);
			char aux[100];
			char type_name[10];
			switch (type) {
			case 1: sprintf(&type_name,"%s", "images/cards/"); break;
			case 2: sprintf(&type_name,"%s", "images/cards/back_"); break;
			case 3: sprintf(&type_name,"%s", "images/goals/"); break;
			case 4: sprintf(&type_name,"%s", "images/icons/"); break;
			case 5: sprintf(&type_name,"%s", "images/actions/"); break;
			}
			
			sprintf(&aux[0], "%s%d%s", &type_name, order, ".jpeg");
			gdk_pixbuf_save (*pix_ptr, &aux, "jpeg", NULL, "quality", "100", NULL);
		}
	order++;
	}

	/* Close stream */
	g_input_stream_close(fs, NULL, NULL);
}

int main () {
	printf("iniciando...\n");
	load_image_bundle();
	printf("\n\n");
	return 0;
}


