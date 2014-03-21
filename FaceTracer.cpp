#include <stdio.h>
#include <stdlib.h>
#include<dirent.h>
#include<string.h>
#include<ctype.h>
#include "opencv/highgui.h"
#include "stasm_lib.h"
#define PATH "/home/wangyan/FaceTracer/misMatchPic/"
#define N 1000
#define LANDMARK_NUMBER 154
typedef struct img_path{
	char img_path[100];
}IMG_PATH;
IMG_PATH path_array[N];
IMG_PATH GfileID[N];
static int img_numbers;
FILE *fp;

void save_landMark(float array[],char *fileID,int faceCount){
	//fistly, we need to limit the float length
	char temp[20];
	//fisrtly ,we write the fileID 
	fputs(fileID,fp);
	fputs("  ",fp);
	//then,we write the face id to it
	sprintf(temp,"%d",faceCount);
	fputs(temp,fp);
	fputs("  ",fp);
	//finally,we write the result to the file
	int i=0;
	for(i;i<2*stasm_NLANDMARKS;i++){
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
			strcat(GfileID[img_numbers].img_path,ent->d_name);
			img_numbers++;
		}
		closedir (dir);
	} else {
		/* could not open directory */
		printf("could not open the %s\n",PATH);
		return 0;
	}
	/*then we load this img path to the files*/
	if ((fp=fopen("/home/wangyan/FaceTracer/LandMarks2.txt","w+"))==NULL) 
	{
		printf("cannot open file!");
	}
    if (!stasm_init("../data", 0 /*trace*/)){
		//holy shit, this time it add the init function here
		printf("stasm init failed\n");
	}
	//now we read all the img path,let's write the result to the file
	int j=0;
	for(j=0;j<img_numbers;j++){
		cv::Mat_<unsigned char> img(cv::imread(path_array[j].img_path, CV_LOAD_IMAGE_GRAYSCALE));
		if (!img.data){
			printf("could not load%s\n",path_array[j].img_path);
		}
		if (!stasm_open_image((const char*)img.data, img.cols, img.rows, path_array[j].img_path,
			1, 10)){
			printf("could not open the img \n");
		}
		int foundface;
		float landmarks[2 * stasm_NLANDMARKS]; // x,y coords (note the 2)
		int nfaces = 0;
		printf("processing img %s\n",GfileID[j].img_path);
		while(1){
			if (!stasm_search_auto(&foundface, landmarks)){
				printf("the stasm_search_auto failed\n");
			}
			if (!foundface){ 
				//yep ,we get the foundface from previous procedure,then we check the result,break or not
				break;      
			}
			save_landMark(landmarks,&(GfileID[j].img_path[0]),nfaces);
			nfaces++;
		}
	}
	fclose(fp); 
	return 0;
}
