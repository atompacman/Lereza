#include "KeyMap.h"

KeyMap::KeyMap(const char* i_ToneMapFile)
{
    // Open file
	std::ifstream file;
	openFile(file, i_ToneMapFile);

    // Skip the space after piece name
    file.seekg(1, file.cur);

    // Read the 4-bytes end timeunit
    //file >> m_EndTU;
    file.read((char*)&m_EndTU, 4);
    LOG(DEBUG) << "End timeunit: " << m_EndTU;

    // Allocate key assonance vector
    m_KeyAssonance = new double*[m_EndTU];
    for (uint32_t i(0); i < m_EndTU; ++i) {
        m_KeyAssonance[i] = new double[NUM_KEYS];
        // Everything is perfectly assonnant at start
        std::fill(m_KeyAssonance[i], m_KeyAssonance[i] + NUM_KEYS, 1.0);
    }

    // Read file main content
    uint32_t tu(0);
    bool addedTones[NUM_KEYS];
    Key key;

    //////////////
    while (!file.eof()) {
        file >> key;
        LOG(DEBUG) << (int) key;
    }
    ///////////

	while (!file.eof()) {
        // Check that next timeunit does not exceed declared end
        if (tu >= m_EndTU) {
            LOG(ERROR) << "A note is ending after declared ending timeunit";
            break;
        }

        std::fill(addedTones, addedTones + NUM_KEYS, false);

        file >> key;

        // Add tones until end of timeunit (value is higher than maximum tone)
        while (key < NUM_KEYS) {
            // Check if tone was already added for this timeunit
            if (addedTones[key]) {
                LOG(ERROR) << "Tone \"" << keyToString(key) <<
                    "\" is specified multiple time at timeunit " << tu;
            }
            addedTones[key] = true;

            // Apply key assonnance window for that key for that timeunit
            for (uint8_t i(0); i < NUM_KEYS; ++i) {
                m_KeyAssonance[tu][i] *= KEY_ASSON_WIN[
                    (NUM_KEYS - key + i + 8) % NUM_KEYS];
            }

            // Read next byte
            file >> key;
        }

        // Find highest assonance value for that timeunit
        double highest(-1);
        for (uint8_t i(0); i < NUM_KEYS; ++i) {
            highest = max(m_KeyAssonance[tu][i], highest);
        }

        if (highest != 1.0) {
            // Divide all assonance values for that timeunit by highest value
            for (uint8_t i(0); i < NUM_KEYS; ++i) {
                m_KeyAssonance[tu][i] /= highest;
            }
        }

        // Next timeunit
        ++tu;
	}

    if (tu != m_EndTU) {
        LOG(ERROR) << "Missing data between timunit " << tu <<" and "<< m_EndTU;
        m_EndTU = tu;
    }

	file.close();
}

void KeyMap::openFile(std::ifstream& io_File, const char* i_ToneMapFile)
{
    LOG(DEBUG) << "Opening tone map file \"" << i_ToneMapFile << "\"";

	io_File.open(i_ToneMapFile, std::ios::binary);

	if (io_File.bad()) {
        LOG(FATAL) << "File not found";
	}

	uint32_t fileSize = (uint32_t)io_File.seekg(0, io_File.end).tellg();
	io_File.seekg(0, io_File.beg);

	std::string fileType;
	io_File >> fileType;

	if (fileType != FILE_TYPE) {
        LOG(FATAL) << "Invalid file type (missing " << FILE_TYPE 
            << " tag at the beginning of the file";
	}

	std::string pieceName;
	io_File >> pieceName;

    LOG(DEBUG) << "Reading tone map of piece \"" <<
        pieceName << "\" (" << fileSize << " bytes)";
}

KeyMap::~KeyMap() 
{
    for (uint32_t i(0); i < m_EndTU; ++i) {
        delete[] m_KeyAssonance[i];
    }
    delete[] m_KeyAssonance;
}

uint32_t KeyMap::endTU() const
{
	return m_EndTU;
}

char assonanceToString(double value)
{
    if (value < 0.6) {
        return (char) 32;
    }
    else if (value < 0.90) {
        return (char) 176;
    }
    else if (value < 0.95) {
        return (char) 177;
    }
    else if (value < 1) {
        return (char) 178;
    }
    return (char) 219;
}

void KeyMap::print() const
{
    std::stringstream ss;
    ss << '|' << std::setw(3);
    for (uint8_t i(0); i < NUM_KEYS; ++i) {
        ss << keyToString(i) << '|' << std::setw(3);
    }
    LOG(TRACE) << ss.str();

    for (uint32_t tu(0); tu < m_EndTU; ++tu) {
        std::stringstream ss;
        ss << '|';
        for (uint8_t i(0); i < NUM_KEYS; ++i) {
            char c(assonanceToString(m_KeyAssonance[tu][i]));
            for (int i(0); i < 3; ++i) {
                ss << c;
            }
            ss << '|';
        }
        if (tu % 64 == 0) {
            LOG(TRACE) << "";
            ss << ' ' << tu;
        }
        LOG(TRACE) << ss.str();
    }
}