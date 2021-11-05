//
// Created by DP on 04/11/2021.
//

#include <fstream>
#include <vector>
#include <iostream>
#include <string>
#include <algorithm>
typedef unsigned char BYTE;

std::vector<BYTE> readFile(const char* filename)
{
    // open the file:
    std::streampos fileSize;
    std::ifstream file(filename, std::ios::binary);

    // get its size:
    file.seekg(0, std::ios::end);
    fileSize = file.tellg();
    file.seekg(0, std::ios::beg);

    // read the data:
    std::vector<BYTE> fileData(fileSize);
    file.read((char*) &fileData[0], fileSize);
    return fileData;
}

int parseCAFF(const std::vector<BYTE> &fileData, const std::string& string) {
    int length = 0;
    if(fileData[0] != 1){
        printf("Error first block is not the header");
        return 50;
    }
    printf("First block id %i \n",fileData[0]);
    for(int i=1;i<9;i++){
        length += fileData[i + 1];
    }

    printf("The length of the first block is %i \n",length);
    unsigned char magic[5];
    for(int i=9;i<13;i++){
       magic[i-9] = fileData[i];
    }
    magic[4] = '\0';
    printf("%s",magic);
    if(std::strcmp(reinterpret_cast<const char *>(magic), "CAFF\0") != 0){
        return 51;
    }



    return 0;
}

bool checkFileAvailability(const char * filename){
    std::ifstream file(filename);
    if(!file.is_open()){
        return false;
    }
    else{
        file.close();
    }
    return true;
}


int main(int argc, char* argv []){
    std::vector<BYTE> caffFile;
    std::string out_path;
    if(argc !=2 && argc !=3 && argc != 5){
        std::cout << "The number of arguments given are not 2, 3 or 5" << std::endl;
        return 1;
    }
    // No output, just caff validation
    else if(argc == 3){
        if(std::strcmp(argv[1],"-i\0") == 0){
            if(checkFileAvailability(argv[2])) {
                caffFile = readFile(argv[2]);
            }
            else{
                return 2;
            }
        }
        else{
            std::cout << "Wrong argument format, expected -i \"filepath\"" << std::endl;
            return 3;
        }
    }
    //No -i arg just the input path
    else if(argc == 2){
        if(checkFileAvailability(argv[1])) {
            caffFile = readFile(argv[1]);
        }
        else{
            return 2;
        }
    }
    else {
        //-i and -o args
        int ia =0;
        int oa =0;

        for (int i = 0; i < argc; i++) {
            if (std::strcmp(argv[i], "-i\0") == 0) {
                ia++;
                if (ia > 1) {
                    std::cout << "More than one -i arg is not allowed" << std::endl;
                    return 4;
                }
                if (i < argc - 1) {
                    if (checkFileAvailability(argv[i + 1])) {
                        caffFile = readFile(argv[i + 1]);
                    }
                    else{
                        std::cout << "File not found" << std::endl;
                        return 5;
                    }
                } else {
                    std::cout << "Not expected -i as last argument" << std::endl;
                    return 6;
                }
            }
            if (std::strcmp(argv[i], "-o\0") == 0) {
                oa++;
                if (oa > 1) {
                    std::cout << "More than one -o arg is not allowed" << std::endl;
                    return 7;
                }
                if (i < argc - 1) {
                    unsigned int name_len = 0;
                    std::string out(argv[i+1]);
                    std::string filename;
                    std::string extension;
                    std::replace(out.begin(),out.end(),'/','\\');
                    int name_start_idx = out.find_last_of('\\');
                    int extension_start_idx = out.find_last_of('.');
                    if(extension_start_idx < name_start_idx){
                        extension_start_idx = -1;
                    }
                    if(extension_start_idx == -1){
                        std::cout << "Using jpg as default extension" << std::endl;
                        filename = out.substr(name_start_idx+1);
                    }
                    else if( extension_start_idx == 0 || name_start_idx+1 == extension_start_idx){
                        std::cout << "Output name is mandatory" << std::endl;
                        return 8;
                    }
                    else{
                        filename = out.substr(name_start_idx+1,extension_start_idx-1-name_start_idx);
                        extension = out.substr(extension_start_idx+1);
                    }
                    //std::cout << filename << std::endl;

                    //std::cout << extension << std::endl;
                    if(extension != "jpg" && extension_start_idx != -1){
                        std::cout << "Only jpg is supported as output" << std::endl;
                        return 9;
                    }

                    out_path = out;

                } else {
                    std::cout << "Not expected -o as last argument" << std::endl;
                    return 10;
                }
            }
        }
    }

    int ret = parseCAFF(caffFile, out_path);

    return ret;

}
