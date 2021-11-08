//
// Created by DP on 04/11/2021.
//

#include <fstream>
#include <vector>
#include <iostream>
#include <string>
#include <algorithm>
#include <cstring>
//Imported libjpeg for image processing
#include "jpeg-9d/jpeglib.h"

typedef unsigned char BYTE;

struct CIFF {
    uint64_t size;
    uint64_t width;
    uint64_t height;
    std::string caption;
    std::vector<std::string> tags;
    std::vector<BYTE> pixels;
    CIFF(uint64_t s, uint64_t w, uint64_t h ,std::string c) : size(s), width(w),
        height(h), caption(std::move(c)) { }
    std::string toString();
};

std::string CIFF::toString() {
    std::string t;
    for (int i = 0; i < tags.size()-1; i++)
        t += "\"" + tags.at(i) + "\", ";
    t += "\"" + tags.at(tags.size()-1) + "\"";

    return "\n\t\t\"size\":" + std::to_string(size) + ",\n\t\t\"width\":" + std::to_string(width) + ",\n\t\t\"height\":"
            + std::to_string(height) + ",\n\t\t\"caption\":\"" + caption + "\",\n\t\t\"tags\":[" + t + "]";
}

struct DateTime {
    uint16_t year;
    uint16_t month;
    uint16_t day;
    uint16_t hour;
    uint16_t minute;
    DateTime(uint16_t y, uint16_t m, uint16_t d, uint16_t h, uint16_t min)
        : year(y), month(m), day(d), hour(h), minute(min) {}
    std::string toString() const;
};

std::string DateTime::toString() const {
    return "\"" + std::to_string(year) + "-" + std::to_string(month) + "-" + std::to_string(day) + " "
        + std::to_string(hour) + ":" + std::to_string(minute) + "\"";
}

struct Animation {
    uint64_t id;
    uint64_t duration;
    CIFF image;
    Animation(uint64_t i, uint64_t d, CIFF  im) : id(i), duration(d), image(std::move(im)) {}
    std::string toString();
};

std::string Animation::toString() {
    return "\n\t\t\"id\":" + std::to_string(id) + ",\n\t\t\"duration\":" + std::to_string(duration)
        + image.toString();
}

struct CAFF {
    DateTime creation;
    std::string  creator;
    std::vector<Animation> images;
    CAFF(DateTime ct, std::string c) : creation(ct), creator(std::move(c)) {}
    std::string toString();
};

std::string CAFF::toString() {
    std::string ims;
    for (int i = 0; i < images.size()-1; i++)
        ims += images.at(i).toString() + ",\n";
    ims += images.at(images.size()-1).toString();

    return "{\n\t\"creation\":" + creation.toString() + ",\n\t\"creator\":\"" + creator
        + "\",\n\t\"ciffs\":\n\t[" + ims + "\n\t]\n}";
}

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

int parseCAFF(const std::vector<BYTE> &fileData, const std::string& file_out, const std::string& txt_out) {
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

    //header block length
    bytes64 h_length{};
    for(int i = 0; i < 8; i++)
        h_length.c[i] = fileData[read++];
    std::cout << "Header length: " << h_length.ll << std::endl;

    //***CAFF HEADER***//

    //magic
    std::string magic;
    for(int i = 0; i < 4; i++)
       magic += fileData[read++];
    if(magic != "CAFF") {
        std::cout << "Error: not a CAFF file" << std::endl;
        return 51;
    }

    //header size
    bytes64 h_size{};
    for(int i = 0; i < 8; i++)
        h_size.c[i] = fileData[read++];
    std::cout << "Header size: " << h_size.ll << std::endl;

    //number of CIFFs
    bytes64 ciff_num{};
    for(int i = 0; i < 8; i++)
        ciff_num.c[i] = fileData[read++];
    std::cout << "Number of CIFFs: " << ciff_num.ll << std::endl;

    //***CAFF CREDITS***//
    if(fileData[read] != 2) {
        std::cout << "Error: second block is not the credits" << std::endl;
        return 52;
    }
    read++;

    //credits block length
    bytes64 c_length{};
    for(int i = 0; i < 8; i++)
        c_length.c[i] = fileData[read++];
    std::cout << "Credits length: " << c_length.ll << std::endl;

    //creation date and time
    bytes16 year{};
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
    bytes64 creator_len{};
    for(int i = 0; i < 8; i++)
        creator_len.c[i] = fileData[read++];
    std::cout << "Creator length: " << creator_len.ll << std::endl;

    //creator
    std::string creator;
    for(int i = 0; i < creator_len.ll; i++)
        creator += fileData[read++];
    std::cout << "Creator: " << creator << std::endl;

    CAFF caff_file = CAFF(creation, creator);

    //***CAFF ANIMATION***//

    for(int i = 0; i < ciff_num.ll; i++) {

        if(fileData[read] != 3) {
            std::cout << "Error: not an animation block" << std::endl;
            return 53;
        }
        read++;

        //animation block length
        bytes64 a_length{};
        for(int j = 0; j < 8; j++)
            a_length.c[j] = fileData[read++];
        std::cout << "Animation length: " << a_length.ll << std::endl;

        //*duration*//
        bytes64 duration{};
        for(int j = 0; j < 8; j++)
            duration.c[j] = fileData[read++];
        std::cout << "CIFF" << i << " duration: " << duration.ll << std::endl;

        //*CIFF data*//

        //magic
        std::string ciff_magic;
        for(int j = 0; j < 4; j++)
            ciff_magic += fileData[read++];
        if(ciff_magic != "CIFF") {
            std::cout << "Error: not a CIFF file" << std::endl;
            std::cout << ciff_magic << std::endl;
            return 54;
        }

        //header size
        bytes64 ciff_header_size{};
        for(int j = 0; j < 8; j++)
            ciff_header_size.c[j] = fileData[read++];
        std::cout << "CIFF" << i << " header size:" << ciff_header_size.ll << std::endl;

        //content size
        bytes64 ciff_content_size{};
        for(int j = 0; j < 8; j++)
            ciff_content_size.c[j] = fileData[read++];
        std::cout << "CIFF" << i << " content size:" << ciff_content_size.ll << std::endl;

        //width
        bytes64 width{};
        for(int j = 0; j < 8; j++)
            width.c[j] = fileData[read++];
        std::cout << "CIFF" << i << " width:" << width.ll << std::endl;

        //height
        bytes64 height{};
        for(int j = 0; j < 8; j++)
            height.c[j] = fileData[read++];
        std::cout << "CIFF" << i << " height:" << height.ll << std::endl;

        if(ciff_content_size.ll != (3*width.ll*height.ll)) {
            std::cout << "Error: wrong content size" << std::endl;
            return 55;
        }

        //caption
        std::string caption;
        while (fileData[read] != '\n')
            caption += fileData[read++];
        std::cout << "CIFF" << i << " caption:" << caption << std::endl;
        read++; //the new line character

        CIFF ciff_file = CIFF(ciff_content_size.ll, width.ll, height.ll, caption);

        //tags
        int tag_len = ciff_header_size.ll - 36 - caption.size();
        std::cout << "Tags: ";
        for(int j = 0; j < tag_len; j++) {
            std::string tag;
            while (fileData[read] != '\0')
                tag += fileData[read++];
            j += (tag.length()+2);
            read++;
            ciff_file.tags.push_back(tag);
            std::cout << tag << " ";
        }
        std::cout << std::endl;

        //pixels
        for(int j = 0; j < ciff_content_size.ll; j++)
            ciff_file.pixels.push_back(fileData[read++]);

        Animation animation = Animation(i, duration.ll, ciff_file);
        caff_file.images.push_back(animation);
    }

    if (read != fileData.size()) {
        std::cout << "Error: parse bug" << std::endl;
        std::cout << "Read = " << read << ", Size = " << fileData.size() << std::endl;
        return 56;
    }

    if(!file_out.empty()){
        FILE *outfile;
        outfile = fopen(file_out.c_str(), "wb");
        if ( outfile == nullptr) {
            std::cout << "Can't open output file" << std::endl;
            return 57;
        }
        struct jpeg_compress_struct cinfo{};
        struct jpeg_error_mgr jerr{};

        JSAMPROW row_pointer[1];

        cinfo.err = jpeg_std_error(&jerr);

        jpeg_create_compress(&cinfo);
        jpeg_stdio_dest(&cinfo, outfile);

        cinfo.image_width = caff_file.images.at(0).image.width;
        cinfo.image_height = caff_file.images.at(0).image.height;
        cinfo.input_components = 3;
        cinfo.in_color_space = JCS_RGB;

        jpeg_set_defaults(&cinfo);

        jpeg_start_compress(&cinfo, TRUE);

        uint64_t row_stride = caff_file.images.at(0).image.width * 3;

        while (cinfo.next_scanline < cinfo.image_height) {
            row_pointer[0] = & caff_file.images.at(0).image.pixels[cinfo.next_scanline * row_stride];
            (void) jpeg_write_scanlines(&cinfo, row_pointer, 1);
        }

        jpeg_finish_compress(&cinfo);

        fclose(outfile);

        jpeg_destroy_compress(&cinfo);
    }

    if(!txt_out.empty()) {
        std::ofstream ofs;
        ofs.open(txt_out);

        if (ofs.is_open()) {
            ofs << caff_file.toString();
            ofs.close();
        }

        else {
            std::cout << "Can't open text file" << std::endl;
            return 58;
        }
    }

    return 0;
}

bool checkFileAvailability(const std::string& filename){
    std::ifstream file(filename);
    if(!file.is_open()){
        return false;
    }
    else{
        file.close();
    }
    return true;
}

int checkOutputValidity(std::string& out, const std::string& expected_extension){
    std::string extension;
    int name_start_idx = out.find_last_of('/');
    int extension_start_idx = out.find_last_of('.');
    if(extension_start_idx < name_start_idx){
        extension_start_idx = -1;
    }
    if(extension_start_idx == -1){
        if(expected_extension == "jpeg"){
            std::cout << "Using " << expected_extension << " as default image extension" << std::endl;
        }
        if(expected_extension == "txt"){
            std::cout << "Using " << expected_extension << " as default description extension" << std::endl;
        }
        out += ".";
        out += expected_extension;
    }
    else if( extension_start_idx == 0 || name_start_idx+1 == extension_start_idx){
        std::cout << "Output name is mandatory" << std::endl;
        return 8;
    }
    else{
        extension = out.substr(extension_start_idx+1);
    }

    if(extension != expected_extension && extension_start_idx != -1){
        std::cout << "Not supported extension expected " << expected_extension << std::endl;
        return 9;
    }
    return 0;
}


int main(int argc, char* argv []){
    std::vector<BYTE> caffFile;
    std::string image_out;
    std::string txt_out;

    int ia =0;
    int ofa =0;
    int ota = 0;
    for (int i = 0; i < argc; i++) {
        if (std::strcmp(argv[i], "-i\0") == 0) {
            ia++;
        }
        if (std::strcmp(argv[i], "-of\0") == 0) {
            ofa++;
        }
        if (std::strcmp(argv[i], "-ot\0") == 0) {
            ota++;
        }
    }
    if(ia == 0){
        std::cout << "No -i was found" << std::endl;
        return 1;
    }
    if(argc !=3 && argc != 5 && argc != 7){
        std::cout << "The number of arguments given are not 3, 5 or 7" << std::endl;
        return 2;
    }
    // No output, just caff validation
    else if(argc == 3){
        if(std::strcmp(argv[1],"-i\0") == 0){
            if(checkFileAvailability(argv[2])) {
                caffFile = readFile(argv[2]);
            }
            else{
                return 3;
            }
        }
        else{
            std::cout << "Wrong argument format" << std::endl;
            return 4;
        }
    }
    else {
        if (ia > 1 || ofa > 1 || ota > 1) {
            std::cout << "Wrong argument format" << std::endl;
            return 4;
        }
        for (int i = 0; i < argc; i++) {
            if (std::strcmp(argv[i], "-i\0") == 0) {
                if (i < argc - 1) {
                    if (checkFileAvailability(argv[i + 1])) {
                        caffFile = readFile(argv[i + 1]);
                    } else {
                        std::cout << "File not found" << std::endl;
                        return 5;
                    }
                } else {
                    std::cout << "Not expected -i as last argument" << std::endl;
                    return 6;
                }
            }
            if (std::strcmp(argv[i], "-of\0") == 0) {
                if (i < argc - 1) {
                    std::string out(argv[i + 1]);
                    std::replace(out.begin(), out.end(), '\\', '/');
                    int out_check = checkOutputValidity(out,"jpeg");
                    if (out_check == 0) {
                        image_out = out;
                    } else {
                        return out_check;
                    }
                } else {
                    std::cout << "Not expected -of as last argument" << std::endl;
                    return 10;
                }
            }
            if (std::strcmp(argv[i], "-ot\0") == 0) {
                if (i < argc - 1) {
                    std::string out(argv[i + 1]);
                    std::replace(out.begin(), out.end(), '\\', '/');
                    int out_check = checkOutputValidity(out,"txt");
                    if (out_check == 0) {
                        txt_out = out;
                    } else {
                        return out_check;
                    }
                } else {
                    std::cout << "Not expected -ot as last argument" << std::endl;
                    return 11;
                }
            }

        }
    }
    int ret = parseCAFF(caffFile, image_out, txt_out);

    return ret;
}
