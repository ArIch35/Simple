#include <iostream>
#include <figures.h>
#include <array>
#include <movement.h>
#include <string>
using namespace std;
const int laenge = 8;
const int hoehe = 8;
array <array<string,laenge+2>,hoehe+2> board;
const string leer = "------";

void Bewegung(int reihe_von,int spalte_von,int reihe_nach,int spalte_nach){
    string stuck = board.at(reihe_von).at(spalte_von);
    board.at(reihe_von).at(spalte_von)= leer;
    board.at(reihe_nach).at(spalte_nach) = stuck;
}
bool CollisionWhitePawn(int reihe_von,int spalte_von,int reihe_nach,int spalte_nach){
    if(reihe_nach < reihe_von){
        return true;
    }
    if (reihe_nach - reihe_von > 1){
        return true;
    }
    if (board.at(reihe_von+1).at(spalte_von) != leer){
        return true;
    }
    if (spalte_nach != spalte_von){
        if (board.at(reihe_nach).at(spalte_nach) != leer){
            return false;
            }
        else {
            return true;
            }
    }
    else{
        return false;
    }
}
bool CollisionBlackPawn(int reihe_von,int spalte_von,int reihe_nach,int spalte_nach){
    if(reihe_nach > reihe_von){
        return true;
    }
    if (reihe_von - reihe_nach > 1){
        return true;
    }
    if (board.at(reihe_von-1).at(spalte_von) != leer){
        return true;
    }
    if (spalte_nach != spalte_von){
        if (board.at(reihe_von-1).at(spalte_nach) != leer ){
            return false;
            }
        else {
            return true;
            }
    }
    else {
        return false;
    }
}
bool CollisionRook(int reihe_von,int spalte_von,int reihe_nach,int spalte_nach){
    int xIncrement = (spalte_nach - spalte_von) / (abs(spalte_nach - spalte_von));
    int yIncrement = (reihe_nach - reihe_von) / (abs(reihe_nach - reihe_von));
                    if (spalte_nach==spalte_von || reihe_nach==reihe_von){
                        if(spalte_nach==spalte_von){
                            if(abs(reihe_nach - reihe_von)==1){
                                return false;
                            }
                            for(int i= reihe_von+yIncrement;i!=reihe_nach;i+=yIncrement){
                                if(i > 0 && i < 9){
                                    if(board.at(i).at(spalte_nach)!=leer){
                                        return true;
                                    }
                                }
                            }
                            return false;
                        }
                        if(reihe_von==reihe_nach){
                            if(abs(spalte_nach-spalte_von)==1){
                                return false;
                            }
                            for(int i = spalte_von+xIncrement;i!=spalte_nach;i+=xIncrement){
                                if (i > 0 && i < 9){
                                    if(board.at(reihe_nach).at(i)!=leer){
                                        return true;
                                    }
                                }
                            }
                                return false;
                        }
                        }
                    else{
                        return true;
                    }
                        }
bool CollisionBshp(int reihe_von,int spalte_von,int reihe_nach,int spalte_nach){
    int xIncrement = (spalte_nach - spalte_von) / (abs(spalte_nach - spalte_von));
    int yIncrement = (reihe_nach - reihe_von) / (abs(reihe_nach - reihe_von));
    if(abs(spalte_nach-spalte_von)==abs(reihe_nach-reihe_von)){
    if(abs(reihe_nach - reihe_von)==1){
        return false;
}
    for(int i=1;i<abs(reihe_nach-reihe_von);i++){
        if(board.at(reihe_nach - yIncrement*i).at(spalte_nach - xIncrement*i)!=leer){
            return true;
        }
        else {
            return false;
        }
    }
}
else {
        return true;
}
}
bool CollisionKght(int reihe_von,int spalte_von,int reihe_nach,int spalte_nach){
    if((abs(spalte_von - spalte_nach) == 2 && abs(reihe_von - reihe_nach) == 1) || (abs(spalte_von - spalte_nach) == 1 && abs(reihe_von - reihe_nach) == 2)){
        return false;
   }
   else{
       return true;
   }
}
bool CollisionKing(int reihe_von,int spalte_von,int reihe_nach,int spalte_nach){
    if (abs(reihe_nach - reihe_von) > 1 || abs(spalte_nach - spalte_von) > 1){
        return true;
    }
    else {
        return false;
    }
}
bool CollisionQuen(int reihe_von,int spalte_von,int reihe_nach,int spalte_nach){
    int xIncrement = (spalte_nach - spalte_von) / (abs(spalte_nach - spalte_von));
    int yIncrement = (reihe_nach - reihe_von) / (abs(reihe_nach - reihe_von));
                    if (spalte_nach==spalte_von || reihe_nach==reihe_von){
                        if(spalte_nach==spalte_von){
                            if(abs(reihe_nach - reihe_von)==1){
                                return false;
                            }
                            for(int i= reihe_von+yIncrement;i!=reihe_nach;i+=yIncrement){
                                if(i > 0 && i < 9){
                                    if(board.at(i).at(spalte_nach)!=leer){
                                        return true;
                                    }
                                }
                            }
                            return false;
                        }
                        if(reihe_von==reihe_nach){
                            if(abs(spalte_nach-spalte_von)==1){
                                return false;
                            }
                            for(int i = spalte_von+xIncrement;i!=spalte_nach;i+=xIncrement){
                                if (i > 0 && i < 9){
                                    if(board.at(reihe_nach).at(i)!=leer){
                                        return true;
                                    }
                                }
                            }
                                return false;
                        }
                        }
                    if(abs(spalte_nach-spalte_von)==abs(reihe_nach-reihe_von)){
                    if(abs(reihe_nach - reihe_von)==1){
                        return false;
                }
                    for(int i=1;i<abs(reihe_nach-reihe_von);i++){
                        if(board.at(reihe_nach - yIncrement*i).at(spalte_nach - xIncrement*i)!=leer){
                            return true;
                        }
                        else {
                            continue;
                            return false;
                        }
                    }
                }
                    else {
                        return true;
                    }
}

void platte(){
    int posx_bKing,posy_bKing,posx_wKing,posy_wKing;
    Figur fig;
    Color col;
    for(int l=laenge;l>0;l--){
        for(int h=hoehe;h>0;h--){
            board.at(l).at(h) = leer;
            if (l==8 && (h==1||h==8)){
                board.at(l).at(h) = col.black+fig.rook;
            }
            if (l==8 && (h==2||h==7)){
                board.at(l).at(h) = col.black+fig.kght;
            }
            if (l==8 && (h==3||h==6)){
                board.at(l).at(h) = col.black+fig.bshp;
            }
            if (l==8 && h==5){
                board.at(l).at(h) = col.black+fig.quen;
            }
            if (l==8 && h==4){
                board.at(l).at(h) = col.black+fig.king;
            }
            if (l==7){
                board.at(l).at(h) = col.black+fig.pawn;
            }
            if (l==1 && (h==1||h==8)){
                board.at(l).at(h) = col.white+fig.rook;
            }
            if (l==1 && (h==2||h==7)){
                board.at(l).at(h) = col.white+fig.kght;
            }
            if (l==1 && (h==3||h==6)){
                board.at(l).at(h) = col.white+fig.bshp;
            }
            if (l==1 && h==5){
                board.at(l).at(h) = col.white+fig.quen;
            }
            if (l==1 && h==4){
                board.at(l).at(h) = col.white+fig.king;
            }
            if (l==2){
                board.at(l).at(h) = col.white+fig.pawn;
            }
        }
    }
//board.at(3).at(8)=col.white+fig.rook;
//board.at(2).at(8)=col.black+fig.pawn;
//board.at(2).at(6)=col.black+fig.king;
bool GameOver = false;
    while (GameOver==false) {
        cout << "     a      b      c      d      e      f      g      h" << endl;
        for(int l=laenge;l>0;l--){
            cout << l << " ";
            for(int h=hoehe;h>0;h--){
                cout << board.at(l).at(h) << " ";
                if (board.at(l).at(h) == col.black+fig.king) {
                    posy_bKing=l;
                    posx_bKing=h;
                }
                if (board.at(l).at(h) == col.white+fig.king) {
                    posy_wKing=l;
                    posx_wKing=h;
                }
            }
            cout << l;
            cout << endl;
        }
        cout << "     a      b      c      d      e      f      g      h" << endl;
        if(board.at(posy_bKing).at(posx_bKing)!=col.black+fig.king){
                    cout << "Game Over,Spieler 1 hat gewonnen! " << endl;
                    break;
                }
        if(board.at(posy_wKing).at(posx_wKing)!=col.white+fig.king){
                    cout << "Game Over,Spieler 2 hat gewonnen! " << endl;
                    break;
                }
        Move mov;
        static bool player1= true;

        if(player1){
            cout << "Spieler 1! " << endl;
        }
        else{
            cout << "Spieler 2! " << endl;
        }

        cout << "Bewegung: ";
        int index_from;
        cin >> mov.from.col >> mov.from.row;
        switch (mov.from.col) {
        case 'q':
        case 'Q':
            cout << "Danke fürs Spielen! " << endl;
            break;
        case 'a':
        case 'A':
            index_from = 8;
            break;
        case 'b':
        case 'B':
            index_from = 7;
            break;
        case 'c':
        case 'C':
            index_from = 6;
            break;
        case 'd':
        case 'D':
            index_from = 5;
            break;
        case 'e':
        case 'E':
            index_from = 4;
            break;
        case 'f':
        case 'F':
            index_from = 3;
            break;
        case 'g':
        case 'G':
            index_from = 2;
            break;
        case 'h':
        case 'H':
            index_from = 1;
            break;
        default:
            cout << "Sie haben falsche Position gegeben. \nBitte versuchen Sie es nochmal!" << endl;
            continue;
        }
        if(mov.from.row <=8 && mov.from.row >= 1){
            if(player1){
                if (board.at(mov.from.row).at(index_from) == leer){
                    cout << "Das ist leer..... \nBitte versuchen Sie es nochmal! " << endl;
                    continue;
                }
                if (board.at(mov.from.row).at(index_from) != col.white+fig.pawn && board.at(mov.from.row).at(index_from) != col.white+fig.rook && board.at(mov.from.row).at(index_from) != col.white+fig.kght && board.at(mov.from.row).at(index_from) != col.white+fig.bshp && board.at(mov.from.row).at(index_from) != col.white+fig.quen && board.at(mov.from.row).at(index_from) != col.white+fig.king){
                    cout << "Sie haben die Figur von Ihrem Gegner genommen! \nBitte versuchen Sie es nochmal! " << endl;
                    continue;
                }
            }
            else{
                if (board.at(mov.from.row).at(index_from) == leer){
                    cout << "Das ist leer..... \nBitte versuchen Sie es nochmal! " << endl;
                    continue;
                }
                if (board.at(mov.from.row).at(index_from) != col.black+fig.pawn && board.at(mov.from.row).at(index_from) != col.black+fig.rook && board.at(mov.from.row).at(index_from) != col.black+fig.kght && board.at(mov.from.row).at(index_from) != col.black+fig.bshp && board.at(mov.from.row).at(index_from) != col.black+fig.quen && board.at(mov.from.row).at(index_from) != col.black+fig.king){
                    cout << "Sie haben die Figur von Ihrem Gegner genommen! \nBitte versuchen Sie es nochmal! " << endl;
                    continue;
                }
            }
        }
        else {
            cout << "Das liegt nicht an dem Board! \nBitte versuchen Sie es nochmal!" << endl;
            continue;
        }
        cin >> mov.to.col >> mov.to.row;
        int index_to;
        switch (mov.to.col) {
        case 'q':
        case 'Q':
            cout << "Danke fürs Spielen! " << endl;
            break;
        case 'a':
        case 'A':
            index_to = 8;
            break;
        case 'b':
        case 'B':
            index_to = 7;
            break;
        case 'c':
        case 'C':
            index_to = 6;
            break;
        case 'd':
        case 'D':
            index_to = 5;
            break;
        case 'e':
        case 'E':
            index_to = 4;
            break;
        case 'f':
        case 'F':
            index_to = 3;
            break;
        case 'g':
        case 'G':
            index_to = 2;
            break;
        case 'h':
        case 'H':
            index_to = 1;
            break;
        default:
            cout << "Sie haben falsche Position gegeben. Bitte versuchen Sie nochmal!" << endl;
            continue;
        }
        if(mov.to.row > 8 || mov.to.row < 1){
            cout << "Das liegt nicht an dem Board! \nBitte versuchen Sie es nochmal!" << endl;
            continue;
        }
        if(player1){
            if (board.at(mov.to.row).at(index_to) == col.white+fig.pawn || board.at(mov.to.row).at(index_to) == col.white+fig.rook || board.at(mov.to.row).at(index_to) == col.white+fig.kght || board.at(mov.to.row).at(index_to) == col.white+fig.bshp || board.at(mov.to.row).at(index_to) == col.white+fig.quen || board.at(mov.to.row).at(index_to) == col.white+fig.king){
                cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal! " << endl;
                continue;
            }
            if (board.at(mov.from.row).at(index_from)==col.white+fig.pawn){
                if(CollisionWhitePawn(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = false;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }

            }
            if (board.at(mov.from.row).at(index_from)==col.white+fig.rook){
                if(CollisionRook(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = false;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }
                  }
            if (board.at(mov.from.row).at(index_from)==col.white+fig.bshp){
                if(CollisionBshp(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = false;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }
                  }
            if (board.at(mov.from.row).at(index_from)==col.white+fig.kght){
                if(CollisionKght(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = false;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }
            }
            if (board.at(mov.from.row).at(index_from)==col.white+fig.king){
                if(CollisionKing(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = false;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }
                                    }
            if (board.at(mov.from.row).at(index_from)==col.white+fig.quen){
                if(CollisionQuen(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = false;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }
            }
        }

       else {
            if (board.at(mov.to.row).at(index_to) == col.black+fig.pawn || board.at(mov.to.row).at(index_to) == col.black+fig.rook || board.at(mov.to.row).at(index_to) == col.black+fig.kght || board.at(mov.to.row).at(index_to) == col.black+fig.bshp || board.at(mov.to.row).at(index_to) == col.black+fig.quen || board.at(mov.to.row).at(index_to) == col.black+fig.king){
                cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal! " << endl;
                continue;
            }
            if (board.at(mov.from.row).at(index_from)==col.black+fig.pawn){
                if(CollisionBlackPawn(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = true;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }
            }
            if (board.at(mov.from.row).at(index_from)==col.black+fig.rook){
                if(CollisionRook(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = true;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }
                                        }
            if (board.at(mov.from.row).at(index_from)==col.black+fig.bshp){
            if(abs(index_to-index_from)==abs(mov.to.row-mov.from.row)){
                if(CollisionBshp(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = true;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }
            }
            }
            if (board.at(mov.from.row).at(index_from)==col.black+fig.kght){
                if(CollisionKght(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = true;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }
            }
            if (board.at(mov.from.row).at(index_from)==col.black+fig.king){
                if(CollisionKing(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = true;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }
              }
            if (board.at(mov.from.row).at(index_from)==col.black+fig.quen){
                if(CollisionQuen(mov.from.row,index_from,mov.to.row,index_to) != true){
                    Bewegung(mov.from.row,index_from,mov.to.row,index_to);
                    player1 = true;
                }
                else {
                    cout << "Sie haben die falsche Bewegung versucht! \nBitte versuchen Sie es nochmal!" << endl;
                    continue;
                }
            }
        }
}
}

int main()
{
    platte();
    return 0;
}
