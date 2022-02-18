#include "knight.h"

knight::knight(const std::string& displayString, Color color) : Piece(displayString,color)
{
}

bool knight::isMovePossible(const Board &b, const Board::Move &mov){
    if((abs(mov.from.col - mov.to.col) == 2 && abs(mov.from.row - mov.to.row) == 1) || (abs(mov.from.col - mov.to.col) == 1 && abs(mov.from.row - mov.to.row) == 2)){
        return true;
   }
       return false;
}
