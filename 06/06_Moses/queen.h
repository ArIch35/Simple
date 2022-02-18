#ifndef QUEEN_H
#define QUEEN_H
#include "piece.h"

class queen : public Piece
{
public:
    queen(const std::string& displayString, Color color);

    bool isMovePossible(const Board& b, const Board::Move& mov) override;


};

#endif // QUEEN_H
