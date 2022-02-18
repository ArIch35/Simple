#ifndef ROOK_H
#define ROOK_H
#include<string>
#include "piece.h"

class rook : public Piece {
public:
    rook(const std::string& displayString, Color color);

    bool isMovePossible(const Board &b, const Board::Move &mov) override;

};


#endif // ROOK_H
