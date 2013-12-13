// minimal.cpp: Display the landmarks of a face in an image.
//              This demonstrates stasm_search_single.
#include <stdio.h>
#include <stdlib.h>
#include<dirent.h>
#include<string.h>
#include<ctype.h>
#include "opencv/highgui.h"
#include "stasm_lib.h"
#define PATH "/home/onfire/FaceTracer/stasm/data/pic/"
#define N 1000
#define LANDMARK_NUMBER 154
typedef struct img_path{
	char img_path[100];
}IMG_PATH;
IMG_PATH path_array[N];
static int img_numbers;
FILE *fp;

void save_landMark(float array[]){
	int i=0;
	for(i;i<LANDMARK_NUMBER-1;i++){
		char temp[10];
		sprintf(temp,"%f",array[i]);
		fputs(temp,fp); /*写入串*/
		fputs("  ",fp); /*写入串*/
	}
	fputs("\n",fp); /*写入回车符*/
}
int main()
{
	/* firstly ,we need to get all the image path from*/
	DIR *dir;
	struct dirent *ent;
	if ((dir = opendir (PATH)) != NULL) {
		/* print all the files and directories within directory */
		img_numbers=0;
		while ((ent = readdir (dir)) != NULL) {
			strcat(path_array[img_numbers].img_path,PATH);
			strcat(path_array[img_numbers].img_path,ent->d_name);
			img_numbers++;
		}
		closedir (dir);
	} else {
		/* could not open directory */
		printf("could not open the %s\n",PATH);
		return 0;
	}
	printf("%d\n",img_numbers);
	/*then we load this img path to the files*/
	if ((fp=fopen("LandMarks.txt","w"))==NULL) /*打开只写的文本文件*/
	{
		printf("cannot open file!");
	}
	int j=0;
	for(j=0;j<img_numbers;j++){
		cv::Mat_<unsigned char> img(cv::imread(path_array[j].img_path, CV_LOAD_IMAGE_GRAYSCALE));

		if (!img.data)
		{
			printf("Cannot load %s\n", path_array[j].img_path);
		}
		else{
			printf("print the %d img info\n",j);
			int foundface;
			float landmarks[2 * stasm_NLANDMARKS]; // x,y coords (note the 2)

			if (!stasm_search_single(&foundface, landmarks,
						(const char*)img.data, img.cols, img.rows, path_array[j].img_path, "../data"))
			{
				printf("Error in stasm_search_single: %s\n", stasm_lasterr());
			}

			if (!foundface)
				printf("No face found in %s\n", path_array[j].img_path);
			else
			{
				save_landMark(landmarks);
			}

		}
	}
	fclose(fp); /*关文件*/
	return 0;
}
