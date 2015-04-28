#pragma once

#include "KeyMap.h"

class KeyPathFinder
{
public:
	KeyPathFinder(const char* a_ToneMapFileName);
	~KeyPathFinder();

    void find();

	const KeyMap& getKeyMap() const;

private:
	KeyMap m_KeyMap;
};