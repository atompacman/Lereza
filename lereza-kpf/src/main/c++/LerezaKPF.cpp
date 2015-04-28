#include "easylogging++.h"
#include "KeyPathFinder.h"

INITIALIZE_EASYLOGGINGPP

// The path to the easylogging++ configuration file
#define LOGGER_CONFIG_FILE "log/log_config.conf"

void initLoggingLibrary();

int main(int argc, char** argv)
{
    // Initialize easylogging++ library
    initLoggingLibrary();

    // Welcome message !
    LOG(INFO) << "====-===-==-=- Lereza - Key Path Finder -=-==-===-====";

    KeyPathFinder("frere_jacques.ltm").getKeyMap().print();
}

void initLoggingLibrary()
{
    el::Configurations conf;
    if (!conf.parseFromFile(LOGGER_CONFIG_FILE)) {
        LOG(WARNING) << "Easylogging++ configuration file "<< LOGGER_CONFIG_FILE
            << " was not found. Using default parameters.";
    }
    el::Loggers::reconfigureAllLoggers(conf);
}