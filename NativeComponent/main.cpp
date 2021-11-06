//
// Created by DP on 04/11/2021.
//

#include <fstream>
#include <vector>
#include <iostream>
#include <string>
#include <algorithm>
#include <cstring>

typedef unsigned char BYTE;

struct CIFF {
    uint64_t size;
    std::string caption;
    std::vector<std::string> tags;
    BYTE* pixels;
    CIFF(uint64_t s, std::string c) : size(s), caption(c) { pixels = new BYTE[size]; }
};

struct DateTime {
    uint16_t year;
    BYTE month;
    BYTE day;
    BYTE hour;
    BYTE minute;
    DateTime(uint16_t y, BYTE m, BYTE d, BYTE h, BYTE min)
        : year(y), month(m), day(d), hour(h), minute(min) {}
};

struct Animation {
    uint64_t duration;
    CIFF image;
    Animation(uint64_t d, CIFF i) : duration(d), image(i) {}
};

struct CAFF {
    DateTime creation;
    std::string  creator;
    std::vector<Animation> images;
    CAFF(DateTime ct, std::string c) : creation(ct), creator(c) {}
};

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
    long read = 0; //the number of bytes already read

    if(fileData[read] != 1) {
        std::cout << "Error: first block is not the header" << std::endl;
        return 50;
    }
    read++;

    union bytes64 {
        BYTE c[8];
        uint64_t ll;
    };

    union bytes16 {
        BYTE c[2];
        uint16_t sh;
    };

    union bytes8 {
        BYTE c;
        uint8_t sh;
    };

    //header block length
    bytes64 h_length;
    for(int i = 0; i < 8; i++)
        h_length.c[i] = fileData[read++];
    std::cout << "Header length: " << h_length.ll << std::endl;

    //***CAFF HEADER***//

    //magic
    std::string magic = "";
    for(int i = 0; i < 4; i++)
       magic += fileData[read++];
    if(magic.compare("CAFF") != 0) {
        std::cout << "Error: not a CAFF file" << std::endl;
        return 51;
    }

    //header size
    bytes64 h_size;
    for(int i = 0; i < 8; i++)
        h_size.c[i] = fileData[read++];
    std::cout << "Header size: " << h_size.ll << std::endl;

    //number of CIFFs
    bytes64 ciff_num;
    for(int i = 0; i < 8; i++)
        ciff_num.c[i] = fileData[read++];
    std::cout << "Number of CIFFs: " << ciff_num.ll << std::endl;

    //***CAFF CREDITS***//
    if(fileData[read] != 2) {
        std::cout << "Error: second block is not the credits" << std::endl;
        return 50;
    }
    read++;

    //credits block length
    bytes64 c_length;
    for(int i = 0; i < 8; i++)
        c_length.c[i] = fileData[read++];
    std::cout << "Credits length: " << c_length.ll << std::endl;

    //creation date and time
    bytes16 year;
    for(int i = 0; i < 2; i++)
        year.c[i] = fileData[read++];
    char month = fileData[read++];
    char day = fileData[read++];
    char hour = fileData[read++];
    char minute = fileData[read++];
    DateTime creation = DateTime(year.sh, month, day, hour, minute);
    std::cout << "Creation: " << creation.year << "-" << creation.month << "-" << creation.day
        << ", " << creation.hour << ":" << creation.minute << std::endl;

    //creator length
    bytes64 creator_len;
    for(int i = 0; i < 8; i++)
        creator_len.c[i] = fileData[read++];
    std::cout << "Creator length: " << creator_len.ll << std::endl;

    //creator
    std::string creator = "";
    for(int i = 0; i < creator_len.ll; i++)
        creator += fileData[read++];
    std::cout << "Creator: " << creator << std::endl;

    CAFF caff_file = CAFF(creation, creator);

    //***CAFF ANIMATION***//

    for(int i = 0; i < ciff_num.ll; i++) {

        if(fileData[read] != 3) {
            std::cout << "Error: not an animation block" << std::endl;
            return 50;
        }
        read++;

        //animation block length
        bytes64 a_length;
        for(int i = 0; i < 8; i++)
            a_length.c[i] = fileData[read++];
        std::cout << "Animation length: " << a_length.ll << std::endl;

        //*duration*//
        bytes64 duration;
        for(int j = 0; j < 8; j++)
            duration.c[j] = fileData[read++];
        std::cout << "CIFF" << i << " duration: " << duration.ll << std::endl;

        //*CIFF data*//

        //magic
        std::string ciff_magic = "";
        for(int j = 0; j < 4; j++)
            ciff_magic += fileData[read++];
        if(ciff_magic.compare("CIFF") != 0) {
            std::cout << "Error: not a CIFF file" << std::endl;
            std::cout << ciff_magic << std::endl;
            return 51;
        }

        //header size
        bytes64 ciff_header_size;
        for(int j = 0; j < 8; j++)
            ciff_header_size.c[j] = fileData[read++];
        std::cout << "CIFF" << i << " header size:" << ciff_header_size.ll << std::endl;

        //content size
        bytes64 ciff_content_size;
        for(int j = 0; j < 8; j++)
            ciff_content_size.c[j] = fileData[read++];
        std::cout << "CIFF" << i << " content size:" << ciff_content_size.ll << std::endl;

        //width
        bytes64 width;
        for(int j = 0; j < 8; j++)
            width.c[j] = fileData[read++];
        std::cout << "CIFF" << i << " width:" << width.ll << std::endl;

        //height
        bytes64 height;
        for(int j = 0; j < 8; j++)
            height.c[j] = fileData[read++];
        std::cout << "CIFF" << i << " height:" << height.ll << std::endl;

        if(ciff_content_size.ll != (3*width.ll*height.ll)) {
            std::cout << "Error: wrong content size" << std::endl;
            return 51;
        }

        //caption
        std::string caption = "";
        while (fileData[read] != '\n')
            caption += fileData[read++];
        std::cout << "CIFF" << i << " caption:" << caption << std::endl;
        read++; //the new line character

        CIFF ciff_file = CIFF(ciff_content_size.ll, caption);

        //tags
        int tag_len = ciff_header_size.ll - 36 - caption.size();
        for(int j = 0; j < tag_len; j++) {
            std::string tag = "";
            while (fileData[read] != '\0')
                tag += fileData[read++];
            j += (tag.length()+2);
            read++;
            ciff_file.tags.push_back(tag);
            std::cout << tag << std::endl;
        }

        //pixels
        for(int j = 0; j < ciff_content_size.ll; j++)
            ciff_file.pixels[j] = fileData[read++];

        Animation animation = Animation(duration.ll, ciff_file);
        caff_file.images.push_back(animation);
    }

    if (read != fileData.size()) {
        std::cout << "Error: parse bug" << std::endl;
        std::cout << "Read = " << read << ", Size = " << fileData.size() << std::endl;
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
    /*std::vector<BYTE> caffFile;
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
    }*/

    //int ret = parseCAFF(caffFile, out_path);

    //return ret;

    std::vector<BYTE> caffFile = readFile(R"(C:\Users\Tibor\Desktop\caff_files\1.caff)");
    return parseCAFF(caffFile, "semmi");
}
