#include <iostream>
#include<math.h>
#include"figures.h"
#include<vector>
#include<string>
#include<array>
#include<iomanip>

using std::string;
using std::array;
using std::cin;
using std::cout;
using std::endl;

const string leer = ".";

const int rows = 8;
const int cols = 8;

int r1,r2,c1,c2;

bool HasWon = false;
bool WhiteTurn = true;



struct Pos{
    char col;
    int row;
    int c1,r1;  //coordinates from
    int c2,r2;  //coordinates to
}pos_from , pos_to;


array<array<string, cols>, rows> board;


void col_name(){
    cout<<"   ";
    for(int n = 97;n<=104;n++){

        cout<<static_cast<char>(n)<<"  ";
    }
    cout<<endl<<endl;
}
void ChessBoardInit(){
    col_name();

    for(int r = 0; r<rows; r++){

        for(int c =0; c<cols;c++){
            board[r][c]= leer;
            if (r==0){//while
                if((c==0)||(c==7)){
                    board[r][c]= black_Rook;
                }

                if((c==1)||(c==6)){
                    board[r][c]= black_Knight;
                }

                if((c==2)||(c==5)){
                    board[r][c]= black_Bishop;
                }

                if(c==3){
                    board[r][c]=black_Queen;
                }

                if(c==4){
                    board[r][c]=black_King;
                }


            }

            if (r==1) board[r][c]= black_Pawn;

            if (r==6) board[r][c]= white_Pawn;

            if (r==7){
                if((c==0)||(c==7)){
                    board[r][c]= white_Rook;
                }

                if((c==1)||(c==6)){
                    board[r][c]= white_Knight;
                }

                if((c==2)||(c==5)){
                    board[r][c]= white_Bishop;
                }

                if(c==3){
                    board[r][c]=white_Queen;
                }

                if(c==4){
                    board[r][c]=white_King;
                }


            }
            if(c == 0) cout<<8-r<<"  ";
            cout<<board[r][c]<<"  ";
            if (c == 7) cout<<8 - r;
        }
        cout<<endl<<endl;
    }
    col_name();
}

void ChessGame(){

    while(!HasWon){

        int count_white_king = 0;
        int count_black_king = 0;

        bool flag_Bishop = false;
        bool flag_Rook = false;

        if(WhiteTurn == true) cout<<"WHITE Turn"<<endl;
        else cout<<"BLACK Turn"<<endl;

        cout<<"Enter start and destiny...";

        cin>>pos_from.col>>pos_from.row;
        int c1=static_cast<int>(pos_from.col)-97;

        int r1 = 8 - pos_from.row;


        cin>>pos_to.col>>pos_to.row;
        int c2=static_cast<int>(pos_to.col)-97;

        int r2 = 8 - pos_to.row;
        cout<<endl;

        //Check if input is correct
        if ((board[r1][c1]==leer)||(!cin.good())){
            cout<<"Invalid input. Please enter again... "<<endl<<endl;
            cin.clear();
            cin.ignore(1345,'\n');
            continue;
        }

        if (board[r2][c2]==board[r1][c1]){
            cout<<"Why are you not moving ???"<<endl;
            continue;
        }

        if(WhiteTurn){
            if ((board[r1][c1]!=white_Pawn) && (board[r1][c1]!=white_Rook) && (board[r1][c1]!=white_Knight)
             && (board[r1][c1]!=white_Bishop) && (board[r1][c1]!=white_Queen) && (board[r1][c1]!=white_King)){
                    cout<<"That's not yours!"<<endl;
                    continue;
            }
            if ((board[r2][c2]==white_Pawn) || (board[r2][c2]==white_Rook) || (board[r2][c2]==white_Knight)
             || (board[r2][c2]==white_Bishop) || (board[r2][c2]==white_Queen) || (board[r2][c2]==white_King)){
                    cout<<"You can't kill your own figure!!!"<<endl;
                    continue;
            }

        }

        if(!WhiteTurn){
            if ((board[r1][c1]!=black_Pawn) && (board[r1][c1]!=black_Rook) && (board[r1][c1]!=black_Knight)
             && (board[r1][c1]!=black_Bishop) && (board[r1][c1]!=black_Queen) && (board[r1][c1]!=black_King)){
                    cout<<"That's not yours!"<<endl;
                    continue;
            }
            if ((board[r2][c2]==black_Pawn) || (board[r2][c2]==black_Rook) || (board[r2][c2]==black_Knight)
             || (board[r2][c2]==black_Bishop) || (board[r2][c2]==black_Queen) || (board[r2][c2]==black_King)){
                    cout<<"You can't kill your own figure!!!"<<endl;
                    continue;
            }

        }
        //////////////Moving Rules////////////////////////////////////////


            ///Rook///
            if((board[r1][c1]==white_Rook)||(board[r1][c1]==black_Rook)){
                if ((c2!= c1) && (r2!=r1)){
                    cout<<"Your Rook can't move there!"<<endl;
                    continue;
                }

                if (c2 == c1){
                    if (r2<r1){
                        for(int i = 1; i<r1-r2;i++){
                            if (board[r1-i][c1] != leer){
                                cout<<"Your Rook will colide with another piece!"<<endl;
                                flag_Rook = true;
                                break;
                            }
                        }
                        if (flag_Rook == true) continue;
                    }

                    if (r2>r1){
                        for(int i = 1; i<r2-r1;i++){
                            if (board[r1+i][c1] != leer){
                                cout<<"Your Rook will colide with another piece!"<<endl;
                                flag_Rook = true;
                                break;
                            }
                        }
                        if (flag_Rook == true) continue;
                    }
                }

                if (r2 == r1){
                    if (c2>c1){
                        for(int i = 1; i<c2-c1;i++){
                            if (board[r1][c1+i] != leer){
                                cout<<"Your Rook will colide with another piece!"<<endl;
                                flag_Rook = true;
                                break;
                            }
                        }
                        if (flag_Rook == true) continue;
                    }

                    if(c2<c1){
                        for(int i = 1; i<c1-c2;i++){
                            if (board[r1][c1-i] != leer){
                                cout<<"Your Rook will colide with another piece!"<<endl;
                                flag_Rook = true;
                                break;
                            }
                        }
                        if (flag_Rook == true) continue;
                    }
                }
            }

            ///Knight///
            if((board[r1][c1]==white_Knight)||(board[r1][c1]==black_Knight)){
                if (  !((r2 == r1-2)&&(c2==c1+1)) && !((r2 == r1-1)&&(c2==c1+2)) && !((r2 == r1+1)&&(c2==c1+2)) && !((r2 == r1+2)&&(c2==c1+1))
                    &&!((r2 == r1+2)&&(c2==c1-1)) && !((r2 == r1+1)&&(c2==c1-2)) && !((r2 == r1-1)&&(c2==c1-2)) && !((r2 == r1-2)&&(c2==c1-1))){
                    cout<<"Your Knight can't move there!"<<endl;
                    continue;
                }

            }

            ///Bishop///
            if((board[r1][c1]==white_Bishop)||(board[r1][c1]==black_Bishop)){
                if((r2<r1)&&(c2>c1)){
                    if (!((c2==(r1-r2)+c1))){
                        cout<<"Your Bishop can't move there!"<<endl;
                        continue;}
                    for(int i = 1; i<r1-r2;i++){
                        if (board[r1-i][c1+i] != leer){
                            cout<<"Your Bishop will colide with another piece!"<<endl;
                            flag_Bishop = true;
                            break;
                        }
                    }
                    if (flag_Bishop == true) continue;
                }
                if((r2>r1)&&(c2>c1)){
                    if (c2!=(r2-r1)+c1){
                        cout<<"Your Bishop can't move there!"<<endl;
                        continue;}
                    for(int i = 1; i<r2-r1;i++){
                        if (board[r1+i][c1+i] != leer){
                            cout<<"Your Bishop will colide with another piece!"<<endl;
                            flag_Bishop = true;
                            break;
                        }
                    }
                    if (flag_Bishop == true) continue;
                }
                if((r2>r1)&&(c2<c1)){
                    if (c2!=(c1-(r2-r1))){
                        cout<<"Your Bishop can't move there!"<<endl;
                        continue;}
                     for(int i =1; i<r2-r1;i++){
                         if (board[r1+i][c1-i] != leer){
                             cout<<"Your Bishop will colide with another piece!"<<endl;
                             flag_Bishop = true;
                             break;
                         }
                     }
                     if (flag_Bishop == true) continue;
                }
                if((r2<r1)&&(c2<c1)){
                    if (c2!=(c1-(r1-r2))){
                        cout<<"Your Bishop can't move there!"<<endl;
                        continue;}
                    for(int i = 1; i<r1-r2;i++){
                        if (board[r1-i][c1-i] != leer){
                            cout<<"Your Bishop will colide with another piece!"<<endl;
                            flag_Bishop = true;
                            break;
                        }
                    }
                    if (flag_Bishop == true) continue;
                }
                if((c2==c1)||(r2==r1)){
                    cout<<"Your Bishop can't move there!"<<endl;
                    continue;
                }

            }

        ///////////////////////////////////////////////////////////////

        board[r2][c2] = board[r1][c1];
        board[r1][c1] = leer;

    //print new board
        col_name();

        for(int r = 0; r<rows; r++){

            for(int c =0; c<cols;c++){

                //TODO: conditions of victory
                if (board[r][c] == white_King){
                    count_white_king +=1;
                }

                if (board[r][c] == black_King){
                    count_black_king +=1;
                }
                if(c == 0) cout<<8-r<<"  ";
                cout<<board[r][c]<<"  ";
                if (c == 7) cout<<8 - r;

            }

            cout<<endl<<endl;
        }
        col_name();

        WhiteTurn =!WhiteTurn;

        if (count_white_king == 0){
            cout<<"Black has won!!!"<<endl;
            HasWon = true;
        }

        if (count_black_king == 0){
            cout<<"White has won!!!"<<endl;
            HasWon = true;
        }
        continue;

        }

}

int main()
{

    ChessBoardInit();
    ChessGame();

}
