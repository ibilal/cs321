

#include<cstring>
#include<iostream>
using namespace std;



int main(){
    char input[100];
    cin.get(input, 100, '\n');
    char * name = new char[strlen(input) +1];
    strcpy(name, input);
    
    cout << name << endl;
}



