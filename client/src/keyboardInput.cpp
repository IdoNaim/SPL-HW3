#include "../include/keyboardInput.h"
#include <string>
#include <vector>
void keyboardInput::split_str(std::string message, char divider, std::vector<std::string>& vector){
    std::string slot1 ="";
    vector.clear();
    for(int i=0; i<message.size();i++){
        if(message[i]==divider){
            vector.push_back(slot1);
            if(!(i+1>=message.size())){
                vector.push_back(message.substr(i+1, message.size()));
            }
            return;
        }
        else{
            slot1 = slot1 + message[i];
        }
    }
    vector.push_back(slot1);    
}
