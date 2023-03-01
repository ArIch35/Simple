import time

import mouse
import random

#var
num = 1
prev_pos = mouse.get_position()

def random_pos():
    pos_x = random.randint(961,1000)
    pos_y = random.randint(540,600)
    return pos_x,pos_y

def print_log():
    print(f"Log Num {num}: {prev_pos[0],prev_pos[1]} -----> {new_pos[0],new_pos[1]}")


#main
while True:
    new_pos = random_pos()
    mouse.drag(prev_pos[0],prev_pos[1],new_pos[0],new_pos[1],absolute=False,duration=0.1)
    print_log()
    num += 1
    time.sleep(4)
    prev_pos = new_pos
