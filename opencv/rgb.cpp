#include "highgui.h"
#include <stdio.h>
#include <stdlib.h>
#include <iostream>  
#include <vector>  
#include <string.h>  
#include <dirent.h>

using namespace cv;
//void rgb(char * argv);
void rgb(char * argv){
    //printf("da:wq");
    CvScalar s;
    IplImage * img = cvLoadImage( argv  );
    namedWindow( "RGB", WINDOW_AUTOSIZE );
    cvShowImage("RGB", img);
    cvWaitKey(0);
    //s = cvGet2D(img,i,j);
    //string a = argv;
    //printf("start rgb");
    /*FILE *fp;
    if((fp=fopen( strcat(argv,"_RGB") ,"a"))==NULL)
    {
        printf("不能打开文件\n");
        exit(0);
    }*/
    printf("%d,%d",img->height,img->width);
    for(int i = 0; i<img->height; i++)
        for( int j = 0; j<img->width; j++)
        {
            s = cvGet2D(img,i,j);
            //printf("H= %d,W= %d\n",img->height,img->width);
            printf("\n%f,%f,%f",s.val[0], s.val[1],s.val[2]);
            //fprintf(fp,"\nB = %f,G=%f, R= %f,",s.val[0], s.val[1],s.val[2]);
            s.val[0]=111;
            s.val[1]=111;
            s.val[2]=111;
            cvSet2D( img,i,j,s);
        }
    cvReleaseImage(&img);
    cvDestroyWindow("RGB");
    //printf("end rgb");
    //fclose(fp);
}

int main( int argc, char ** argv)
{
   /* struct dirent *ptr;      
    DIR *dir;  
    dir=opendir(argv[1]);  
    printf("文件列表:\n");  
    while((ptr=readdir(dir))!=NULL)  
    {  
   
        //跳过'.'和'..'两个目录  
        if(ptr->d_name[0] == '.')  
            continue;  
        printf("%s\n",ptr->d_name);
	rgb(ptr->d_name);
    }  
    closedir(dir);*/
    rgb(argv[1]);
    return 0;
}
