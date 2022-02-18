#ifndef KING_H
#define KING_H
#include "piece.h"

class king : public Piece
{
public:
    king(const std::string& displayString, Color color);

    bool isMovePossible(const Board& b, const Board::Move& mov) override;

};

#endif // KING_H
