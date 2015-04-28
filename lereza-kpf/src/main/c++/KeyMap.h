#pragma once

#include <easylogging++.h>
#include <fstream>
#include <iomanip>
#include <string>
#include <vector>
#include "Utils.h"

#define FILE_TYPE "LerezaToneMap"

class KeyMap
{
public:
    KeyMap(const char* i_ToneMapFile);
	~KeyMap();

	uint32_t endTU() const;

	void print() const;

private:
    // The keys occurances at every timeunit
    double** m_KeyAssonance;

    // End timeunit
    uint32_t m_EndTU;

	void openFile(std::ifstream& io_File, const char* i_ToneMapFile);
};

