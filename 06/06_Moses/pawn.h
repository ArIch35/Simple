#ifndef PAWN_H
#define PAWN_H
#include "piece.h"

class pawn: public Piece
{
public:
    pawn(const std::string& displayString, Color color);

    bool isMovePossible(const Board& b, const Board::Move& mov) override;

};

#endif // PAWN_H
