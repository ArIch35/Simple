#ifndef BOARD_H
#define BOARD_H

#include<iostream>
#include<array>
#include "pos.h"
#include <stdexcept>

using std::cout;
using std::endl;
using std::cin;
using std::copy;

class Piece;
class Board
{
public:
    struct Pos
    {
    int col;
    int row;
    int end;
    };
    struct Move
    {
    Pos from;
    Pos to;
    };

    //Board Constructor
    Board();

    //Board Destructor
    ~Board();

    //Board Copy Constructor
    Board(const Board& b);

    bool getInput (Board& b, Move& m);

    Piece* getPiece(int row,int col) const;

    void Movement(const Move& mov);

    bool move (const Move& mov);

    bool WhiteMove(Board& b,const Move& mov);

    bool BlackMove(Board& b,const Move& mov);

    void printBoard();

    void play();

private:
    Piece*** board;

};

#endif // BOARD_H
