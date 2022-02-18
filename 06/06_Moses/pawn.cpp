#include "pawn.h"

pawn::pawn(const std::string& displayString, Color color): Piece(displayString,color)
{
}

bool pawn::isMovePossible(const Board& b, const Board::Move& mov){
    if(b.getPiece(mov.from.row,mov.from.col)->isWhite()){
        if (mov.to.row < mov.from.row){
            return false;
        }
        if (mov.to.row - mov.from.row > 1){
            return false;
        }
        if(!b.getPiece(mov.from.row+1,mov.from.col)->isEmpty()){
            return false;
        }
        if (mov.from.col != mov.to.col){
            if (!b.getPiece(mov.to.row,mov.to.col)->isEmpty()){
                return true;
                }
            else{
                return false;
            }
        }
    }
    if(b.getPiece(mov.from.row,mov.from.col)->isBlack()){
        if (mov.to.row > mov.from.row){
            return false;
        }
        if (mov.from.row - mov.to.row > 1){
            return false;
        }
        if(!b.getPiece(mov.from.row-1,mov.from.col)->isEmpty()){
            return false;
        }
        if (mov.from.col != mov.to.col){
            if (!b.getPiece(mov.from.row-1,mov.to.col)->isEmpty()){
                return true;
                }
            else{
                return false;
            }
        }
    }
    return true;

}
