#include "bishop.h"

bishop::bishop(const std::string& displayString, Color color): Piece(displayString,color)
{
}

bool bishop::isMovePossible(const Board& b, const Board::Move& mov){
    int xIncrement = (mov.to.col - mov.from.col) / (abs(mov.to.col - mov.from.col));
    int yIncrement = (mov.to.row - mov.from.row) / (abs(mov.to.row - mov.from.row));
    if(abs(mov.to.col-mov.from.col)==abs(mov.to.row-mov.from.row)){
    if(abs(mov.to.col - mov.from.col)==1){
        return true;
}
    for(int i=1;i<abs(mov.to.row-mov.from.row);i++){
        if(!b.getPiece(mov.to.row-yIncrement*i,mov.to.col- xIncrement*i)->isEmpty()){
            return false;
        }
    }
    return true;
}
        return false;
}
