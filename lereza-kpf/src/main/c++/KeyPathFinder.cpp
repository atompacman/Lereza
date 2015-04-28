#include "KeyPathFinder.h"

KeyPathFinder::KeyPathFinder(const char* a_ToneMapFileName) :
m_KeyMap(a_ToneMapFileName)
{}

KeyPathFinder::~KeyPathFinder() 
{}

void KeyPathFinder::find()
{

}

const KeyMap& KeyPathFinder::getKeyMap() const
{
	return m_KeyMap;
}