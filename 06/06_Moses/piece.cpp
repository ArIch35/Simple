#include "piece.h"

Piece::Piece(const std::string& displayString, Color color): displayString(displayString),color(color)
{
}

bool Piece::isWhite() const{
    if (color==Color::White){
        return true;
    }
    return false;
}

bool Piece::isBlack() const{
    if (color==Color::Black){
        return true;
    }
    return false;
}

bool Piece::isEmpty() const{
    if (color==Color::Empty){
        return true;
    }
    return false;
}

std::string Piece::toString() const{
    return displayString;
}


