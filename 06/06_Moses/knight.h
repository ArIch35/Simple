#ifndef KNIGHT_H
#define KNIGHT_H
#include "piece.h"

class knight : public Piece
{
public:
    knight(const std::string& displayString, Color color);

    bool isMovePossible(const Board& b, const Board::Move& mov) override;

};

#endif // KNIGHT_H
