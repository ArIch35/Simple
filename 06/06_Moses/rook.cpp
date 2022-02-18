#include "rook.h"

rook::rook(const std::string& displayString,Color color):Piece(displayString,color)
{
}

bool rook::isMovePossible(const Board& b, const Board::Move &mov){
    int xIncrement = (mov.to.col - mov.from.col) / (abs(mov.to.col - mov.from.col));
    int yIncrement = (mov.to.row - mov.from.row) / (abs(mov.to.row - mov.from.row));
                    if (mov.to.col==mov.from.col || mov.to.row==mov.from.row){
                        if(mov.to.col==mov.from.col){
                            if(abs(mov.from.row - mov.to.row)==1){
                                return true;
                            }
                            for(int i= mov.from.row+yIncrement;i!=mov.to.row;i+=yIncrement){
                                if(i > 0 && i < 9){
                                    if(!b.getPiece(i,mov.to.col)->isEmpty()){
                                        return false;
                                    }
                                }
                            }
                            return true;
                        }
                        if(mov.from.row==mov.to.row){
                            if(abs(mov.from.col-mov.to.col)==1){
                                return true;
                            }
                            for(int i = mov.from.col+xIncrement;i!=mov.to.col;i+=xIncrement){
                                if (i > 0 && i < 9){
                                    if(!b.getPiece(mov.to.row,i)->isEmpty()){
                                        return false;
                                    }
                                }
                            }
                                return true;
                        }
                        }
                        return false;
}

