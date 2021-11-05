//
// Created by DP on 04/11/2021.
//

#include <fstream>
#include <vector>
#include <iostream>
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

bool parseCAFF(const std::vector<BYTE>& fileData) {
    int length = 0;
    if(fileData[0] != 1){
        printf("Error first block is not the header");
        return false;
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
        return false;
    }

    return true;
}


int main(int argc, char** argv){
    std::vector<BYTE> caffFile = readFile(R"(C:\Users\DP\CLionProjects\SUDOku\NativeComponent\CAFFTest\1.caff)");
    bool ret = parseCAFF(caffFile);
    if(ret){
        return 0;
    }
    else{
        return 1;
    }

}
