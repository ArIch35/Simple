#include <iostream>
using std::cin;
using std::cout;
using std::endl;

int main()
{
  int x;
  int heute=2021;

  cout << "Wie alt sind Sie? ";
  cin >> x;
  cout << "Sie sind " << heute - x << " geboren." << endl;

  return 0;
}
