#include <iostream>

using namespace std;
using std::cout;
using std::cin;
using std::endl;
using std::string;
using std::to_string;

int bagi(int x){
    int jumlah_bagi=1;
    if (x/16<16){
        return jumlah_bagi+=1;
    }
    else{
        return bagi(x/16)+jumlah_bagi;
    }
}

string hexa(int num){
    int mod=num/16;
    string m = to_string(mod);
    string simpen[bagi(num)-1];
    if ((num/16)<1){
        switch (num%16) {
        case 1:
            return "1";
        case 2:
            return "2";
        case 3:
            return "3";
        case 4:
            return "4";
        case 5:
            return "5";
        case 6:
            return "6";
        case 7:
            return "7";
        case 8:
            return "8";
        case 9:
            return "9";
        case 10:
            return "A";
        case 11:
            return "B";
        case 12:
            return "C";
        case 13:
            return "D";
        case 14:
            return "E";
        case 15:
            return "F";
        }
        }
    else{
        for (int i=0;i<=bagi(num);i++){
            if(num/16<16){
               simpen[i]= hexa(num/16);
            }
            else{
               simpen[i]=hexa(num%16);
            }

        }
        return simpen[bagi(num)-1];
    }
}



int main()
{
   cout<<hexa(16)<<endl;
}
