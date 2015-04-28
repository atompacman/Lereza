#pragma once

#include <assert.h>
#include <stdint.h>

typedef uint8_t Key;

#define NUM_KEYS 21

static const double KEY_ASSON_WIN[NUM_KEYS] = {
    0.07, // 0  Fb
    0.12, // 1  Cb
    0.16, // 2  Gb
    0.81, //*3  Db
    0.83, //*4  Ab
    0.87, //*5  Eb
    0.90, //*6  Bb
    0.97, //*7  F
    1.00, //*8  C
    0.93, //*9  G
    0.14, // 10 D
    0.10, // 11 A
    0.08, // 12 E
    0.05, // 13 B
    0.02, // 14 F#
    0.01, // 15 C#
    0.00, // 16 G#
    0.00, // 17 D#
    0.00, // 18 A#
    0.00, // 19 E#
    0.03  // 20 B#
};

static const char* keyToString(Key i_Key)
{
    assert(i_Key < NUM_KEYS);

    switch (i_Key) {
    case 0:  return "Fb";
    case 1:  return "Cb";
    case 2:  return "Gb";
    case 3:  return "Db";
    case 4:  return "Ab";
    case 5:  return "Eb";
    case 6:  return "Bb";
    case 7:  return "F";
    case 8:  return "C";
    case 9:  return "G";
    case 10: return "D";
    case 11: return "A";
    case 12: return "E";
    case 13: return "B";
    case 14: return "F#";
    case 15: return "C#";
    case 16: return "G#";
    case 17: return "D#";
    case 18: return "A#";
    case 19: return "E#";
    case 20: return "B#";
    default: return "?";
    }
}