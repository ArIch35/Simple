#include "king.h"

king::king(const std::string& displayString, Color color) : Piece(displayString,color)
{

}

bool king::isMovePossible(const Board& b, const Board::Move& mov){
    if (abs(mov.to.row - mov.from.row) > 1 || abs(mov.to.col - mov.from.col) > 1){
        return false;
    }
        return true;
}
