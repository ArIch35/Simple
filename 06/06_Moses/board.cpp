#include "board.h"

#include "rook.h"
#include "bishop.h"
#include "knight.h"
#include "pawn.h"
#include "king.h"
#include "queen.h"
#include "empty.h"

Piece* piece;

Board::Board(){
    board = new Piece**[8];
    for (int i = 0;i<8;i++){
        board[i] = new Piece*[8];
    }
    for(int l=7;l>=0;l--){
        for(int h=7;h>=0;h--){
            if(l<6 && l>1){
            board[l][h] = new empty();
            }
            if (l==7 && (h==0||h==7)){
                board[l][h] = new rook("b_Rook",Color::Black);
            }
            if (l==7 && (h==1||h==6)){
                board[l][h] = new knight("b_Kght",Color::Black);
            }
            if (l==7 && (h==2||h==5)){
                board[l][h] = new bishop("b_Bshp",Color::Black);
            }
            if (l==7 && h==4){
                board[l][h] = new queen("b_Quen",Color::Black);
            }
            if (l==7 && h==3){
                board[l][h] = new king("b_King",Color::Black);
            }
            if (l==6){
                board[l][h] = new pawn("b_Pawn",Color::Black);
            }
            if (l==0 && (h==0||h==7)){
                board[l][h] = new rook("w_Rook",Color::White);
            }
            if (l==0 && (h==1||h==6)){
                board[l][h] = new knight("w_Kght",Color::White);
            }
            if (l==0 && (h==2||h==5)){
                board[l][h] = new bishop("w_Bshp",Color::White);
            }
            if (l==0 && h==4){
                board[l][h] = new queen("w_Quen",Color::White);
            }
            if (l==0 && h==3){
                board[l][h] = new king("w_King",Color::White);
            }
            if (l==1){
                board[l][h] = new pawn("w_Pawn",Color::White);
            }

        }
    }
    //board[2][7] = new queen("w_Quen",Color::White);
    //board[2][5] = new pawn("b_Pawn",Color::Black);
}

Board::~Board(){
    for(int i=0;i<8;i++){
        for(int j=0;j<8;j++){
            delete board[i][j];
        }
        delete [] board[i];
    }
    delete [] board;
    cout << "Board is deleted" << endl;
}

Board::Board(const Board& b) {
    board = new Piece**[8];
    for (int i = 0;i<8;i++){
        board[i] = new Piece*[8];
    }
    for(int l=7;l>=0;l--){
        for(int h=7;h>=0;h--){
            if(b.board[l][h]->toString()=="------"){
            board[l][h] = new empty();
            }
            if(b.board[l][h]->toString()=="b_Rook"){
                board[l][h] = new rook("b_Rook",Color::Black);
            }
            if(b.board[l][h]->toString()=="b_Kght"){
                board[l][h] = new knight("b_Kght",Color::Black);
            }
            if (b.board[l][h]->toString()=="b_Bshp"){
                board[l][h] = new bishop("b_Bshp",Color::Black);
            }
            if (b.board[l][h]->toString()=="b_Quen"){
                board[l][h] = new queen("b_Quen",Color::Black);
            }
            if (b.board[l][h]->toString()=="b_King"){
                board[l][h] = new king("b_King",Color::Black);
            }
            if (b.board[l][h]->toString()=="b_Pawn"){
                board[l][h] = new pawn("b_Pawn",Color::Black);
            }
            if (b.board[l][h]->toString()=="w_Rook"){
                board[l][h] = new rook("w_Rook",Color::White);
            }
            if (b.board[l][h]->toString()=="w_Kght"){
                board[l][h] = new knight("w_Kght",Color::White);
            }
            if (b.board[l][h]->toString()=="w_Bshp"){
                board[l][h] = new bishop("w_Bshp",Color::White);
            }
            if (b.board[l][h]->toString()=="w_Quen"){
                board[l][h] = new queen("w_Quen",Color::White);
            }
            if (b.board[l][h]->toString()=="w_King"){
                board[l][h] = new king("w_King",Color::White);
            }
            if (b.board[l][h]->toString()=="w_Pawn"){
                board[l][h] = new pawn("w_Pawn",Color::White);
            }
        }
    }
}

Piece* Board::getPiece(int row,int col) const{
    return board[row][col];
}


bool Board::getInput(Board &b, Move& m){
        cout << "Bewegung: ";
        int row;
        char col;
        int end = 0;
        cin >> col;
        int index_from;
        switch (col) {
        case 'q':
        case 'Q':
            end = 1;
            index_from = 7;
            break;
        case 'a':
        case 'A':
            index_from = 7;
            break;
        case 'b':
        case 'B':
            index_from = 6;
            break;
        case 'c':
        case 'C':
            index_from = 5;
            break;
        case 'd':
        case 'D':
            index_from = 4;
            break;
        case 'e':
        case 'E':
            index_from = 3;
            break;
        case 'f':
        case 'F':
            index_from = 2;
            break;
        case 'g':
        case 'G':
            index_from = 1;
            break;
        case 'h':
        case 'H':
            index_from = 0;
            break;
        default:
            cout << "Sie haben falsche Position gegeben. \nBitte versuchen Sie es nochmal!" << endl;
        }
        cin >> row;
        if (end == 0 && (index_from < 0 || index_from > 7)){
            cout << "Wrong Coloumn! " << endl;
            return false;
        }
        if (end == 0 && (row - 1 < 0 || row - 1 > 7)){
            cout << "Wrong Row! " << endl;
            return false;
        }
        Board::Pos from{index_from,row-1,end};
        cin >> col >> row;
        int index_to;
        switch (col) {
        case 'q':
        case 'Q':
            end = 1;
            index_from = 7;
            break;
        case 'a':
        case 'A':
            index_to = 7;
            break;
        case 'b':
        case 'B':
            index_to = 6;
            break;
        case 'c':
        case 'C':
            index_to = 5;
            break;
        case 'd':
        case 'D':
            index_to = 4;
            break;
        case 'e':
        case 'E':
            index_to = 3;
            break;
        case 'f':
        case 'F':
            index_to = 2;
            break;
        case 'g':
        case 'G':
            index_to = 1;
            break;
        case 'h':
        case 'H':
            index_to = 0;
            break;
        default:
            cout << "Sie haben falsche Position gegeben. Bitte versuchen Sie nochmal!" << endl;
        }
        if (end == 0 && (index_to < 0 || index_to > 7)){
            cout << "Wrong Coloumn! " << endl;
            return false;
        }
        if (end == 0 && (row - 1 < 0 || row - 1  > 7)){
            cout << "Wrong Row! " << endl;
            return false;
        }
        Board::Pos to{index_to,row-1,end};
        m.from = from;
        m.to = to;
        if (end == 0 && b.getPiece(m.from.row,m.from.col)->isEmpty()){
                cout << "Das ist leer...." << endl;
            }
        return true;
    }

void Board::Movement(const Move& mov){
    delete board [mov.to.row][mov.to.col];
    Piece* stuck = board[mov.from.row][mov.from.col];
    board[mov.from.row][mov.from.col]= new empty();
    board[mov.to.row][mov.to.col] = stuck;
}

bool Board::WhiteMove(Board& b,const Move& mov){
    if (b.getPiece(mov.to.row,mov.to.col)->isWhite()){
        cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal! " << endl;
        return false;
    }
    else{
        if(getPiece(mov.from.row,mov.from.col)->isMovePossible(b,mov)){
        Movement(mov);
        return true;
        }
        else if (b.getPiece(mov.from.row,mov.from.col)->isBlack()) {
            cout << "Sie haben die Figur von Ihrem Gegner genommen! \nBitte versuchen Sie es nochmal! " << endl;
            return false;
        }
        else {
        cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
        return false;
        }
    }
    }

bool Board::BlackMove(Board& b,const Move& mov){
    if (b.getPiece(mov.to.row,mov.to.col)->isBlack()){
        cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal! " << endl;
        return false;
    }
    else{
        if(getPiece(mov.from.row,mov.from.col)->isMovePossible(b,mov)){
        Movement(mov);
        return true;
        }
        else if (b.getPiece(mov.from.row,mov.from.col)->isWhite()) {
            cout << "Sie haben die Figur von Ihrem Gegner genommen! \nBitte versuchen Sie es nochmal! " << endl;
            return false;
        }
        else {
        cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
        return false;
        }
    }
    }


void Board::printBoard(){
    cout << "     a      b      c      d      e      f      g      h" << endl;
    for(int l=7;l>=0;l--){
        cout << l+1 << " ";
        for(int h=7;h>=0;h--){
            cout << board[l][h]->toString()<< " ";
        }
        cout << l+1;
        cout << endl;
    }
    cout << "     a      b      c      d      e      f      g      h" << endl;
}

void Board::play(){
    Move m;
    while (true) {
        printBoard();
        static bool player1= true;
        if(player1){
            cout << "Spieler 1! " << endl;
        }
        else{
            cout << "Spieler 2! " << endl;
        }
        if (player1){
            if(getInput(*this,m)){
            if (m.from.end == 1 || m.to.end == 1){
                    cout << "Danke fürs Spielen! "<< endl;
                    break;
               }
            else {
                if (WhiteMove(*this,m)){
                player1 = false;
                }
                continue;
            }
            }
            continue;
        }
        else {
            if(getInput(*this,m)){
            if (m.from.end == 1 || m.to.end == 1){
                    cout << "Danke fürs Spielen! "<< endl;
                    break;
             }
            else if(BlackMove(*this,m)){
                player1 = true;
            }
            continue;
            }
            continue;
        }
    }

}
