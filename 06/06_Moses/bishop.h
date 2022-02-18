#ifndef BISHOP_H
#define BISHOP_H
#include<string>
#include "piece.h"

class bishop : public Piece {
public:
    bishop(const std::string& displayString, Color color);

    bool isMovePossible(const Board &b, const Board::Move &mov) override;

};

#endif // BISHOP_H
